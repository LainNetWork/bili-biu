
package fun.lain.bilibiu.cache.task.frag;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.entity.CachePartTaskParam;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import fun.lain.bilibiu.cache.tunnel.CachePartResult;
import fun.lain.bilibiu.cache.var.CachePartTaskVar;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 缓存任务
 */
@Slf4j
public class CacheTask implements Runnable {
    private final int block = 3 * 1024 * 1024;
    private static final String BASE_URL = "https://www.bilibili.com/video/av";
    public static final String DPC_FILE_EXTENSION = ".dpc";
    private CachePartTaskParam cachePartTaskParam;
    private ApiService apiService;
    private CacheInfoService cacheInfoService;
    private String savePath;

    public CacheTask(CachePartTaskParam cachePartTaskParam) {
        this.cachePartTaskParam = cachePartTaskParam;
        this.apiService = BeanUtil.getBean(ApiService.class);
        this.cacheInfoService = BeanUtil.getBean(CacheInfoService.class);
        this.savePath = BeanUtil.getBean(Environment.class).getProperty("lain.save-path");
    }

    @Override
    public void run() {
        CachePartTask cachePartTask = cacheInfoService.getById(cachePartTaskParam.getTaskId());
        cacheInfoService.run(cachePartTask.getId());
        buildTaskInfo(cachePartTask);
        //缓存，获取到下载链接之后，根据返回的size，以block为一个下载块，每次循环都记录当前的下载进度
        String fileDir = String.format(savePath + "/%s/%s", cachePartTask.getAvid(), cachePartTask.getCid());
        //创建缓存目录
        File cacheFile = new File(fileDir);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        } else if (!cacheFile.isDirectory()) {
            cacheFile.delete();
            cacheFile.mkdirs();
        }
        String videoFilePath = String.format(fileDir + "/%s%d-%d%s", File.separator, cachePartTask.getCid(), cachePartTask.getQuality(), getSuffix(cachePartTask.getDownloadUrl()));
        String videoFileDPCPath = videoFilePath + DPC_FILE_EXTENSION;


        //计算块数
        long size = cachePartTask.getSize();
        long fragNum = size / block;
        long endFrag = size % block;
        //如果缓存临时文件存在，读取缓存临时文件
        //不存在则创建，并填充块数+1个0
        RandomAccessFile videoFileDPCFile = null;
        try {
            videoFileDPCFile = new RandomAccessFile(videoFileDPCPath, "rw");
            if (videoFileDPCFile.length() == 0) {
                videoFileDPCFile.write(new byte[(int) fragNum + 1]);
            }

            List<CachePartFrag> frags = new ArrayList<>();
            //创建回调
            CacheFragCallBack callBack = CacheFragCallBack.builder()
                    .cacheInfoService(cacheInfoService)
                    .taskId(cachePartTaskParam.getTaskId())
                    .build();
            if (fragNum == 0) {
                frags.add(CachePartFrag.builder().start(0).end(endFrag).order(0).path(videoFilePath)
                        .url(cachePartTask.getDownloadUrl())
                        .cacheFragCallBack(callBack)
                        .referer(BASE_URL + cachePartTask.getAvid())
                        .build());
            } else {
                for (int i = 0; i < fragNum; i++) {
                    videoFileDPCFile.seek(i);
                    byte b = videoFileDPCFile.readByte();
                    if (b == 0) {//如果当前块没有被缓存过，则加入任务列表
                        frags.add(CachePartFrag.builder().start(i * block).end((i + 1) * block).order(i).path(videoFilePath)
                                .url(cachePartTask.getDownloadUrl())
                                .cacheFragCallBack(callBack)
                                .referer(BASE_URL + cachePartTask.getAvid())
                                .build());
                    }
                }
                if(endFrag!=0){
                    frags.add(CachePartFrag.builder().start(fragNum * block).end(fragNum * block + endFrag).order((int)fragNum).path(videoFilePath)
                            .url(cachePartTask.getDownloadUrl())
                            .cacheFragCallBack(callBack)
                            .referer(BASE_URL + cachePartTask.getAvid())
                            .build());
                }
            }
            //创建线程池
            ExecutorService fragExecutor = new ThreadPoolExecutor(2,2,1, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1),new FragRejectedExecutionHandler());
            for(CachePartFrag frag:frags){
                //提交不上就阻塞，然后等待线程执行完
                fragExecutor.submit(frag);
            }
            //等待所有线程完成（按照现有机制，由于出错，分片下载不完整的视频会被重新拉进下载队列，因此即使在本次任务中没有下载完，总是会下载完成的
            fragExecutor.shutdown();
        } catch (IOException e) {
            log.error("IO错误！", e);
            return;
        }

    }

    private void buildTaskInfo(CachePartTask cachePartTask) {
        MediaPart part = apiService.getDownloadInfo(cachePartTask.getAvid(), cachePartTask.getCid(), cachePartTaskParam.getCookie());
        cachePartTask.setDownloadUrl(part.getDownLoadInfos().get(0).getUrl());//TODO 暂时不取备用url
        cachePartTask.setQuality(part.getAcceptQuality());
        cachePartTask.setSize(part.getDownLoadInfos().get(0).getSize());
        //更新大小信息
        cacheInfoService.update(new UpdateWrapper<CachePartTask>()
                .set("quality", part.getAcceptQuality())
                .set("size", part.getDownLoadInfos().get(0).getSize())
                .eq("id", cachePartTask.getId())
        );
    }

    private String getSuffix(String downloadUrl) {
        if (downloadUrl.contains(".flv")) {
            return ".flv";
        }
        if (downloadUrl.contains(".mp4")) {
            return ".mp4";
        }
        return ".flv";
    }


}
