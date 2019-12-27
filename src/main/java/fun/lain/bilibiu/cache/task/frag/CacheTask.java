
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
    private final int block = 4 * 1024 * 1024;
    private static final String BASE_URL = "https://www.bilibili.com/video/av";
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
        String videoFileDPCPath = videoFilePath + ".dpc";


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

            int cachedBlockNum = 0;
            List<CachePartFrag> frags = new ArrayList<>();
            if (fragNum == 0) {
                frags.add(CachePartFrag.builder().start(0).end(endFrag).order(0).path(videoFilePath)
                        .url(cachePartTask.getDownloadUrl())
                        .referer(BASE_URL + cachePartTask.getAvid())
                        .build());
            } else {
                for (int i = 0; i < fragNum; i++) {
                    videoFileDPCFile.seek(i);
                    byte b = videoFileDPCFile.readByte();
                    if (b == 0) {
                        frags.add(CachePartFrag.builder().start(i * block).end((i + 1) * block).order(i).path(videoFilePath)
                                .url(cachePartTask.getDownloadUrl())
                                .referer(BASE_URL + cachePartTask.getAvid())
                                .build());
                    }else{
                        cachedBlockNum++;
                    }
                }
            }
            //创建线程池
            ExecutorService fragExecutor = new ThreadPoolExecutor(2,2,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1));
            List<Future> futureList = new ArrayList<>();
            for(CachePartFrag frag:frags){
                //构建线程，提交执行
                //提交不上就抛异常阻塞，然后等待线程执行完
                try {
                    Future future = fragExecutor.submit(frag);
                    futureList.add(future);
                }catch (RejectedExecutionException e){
                    //等待其中一个完成，并从队列中移除
                }
            }
            fragExecutor.shutdown();
        } catch (IOException e) {
            log.error("IO错误！", e);
            //TODO 记录下载失败原因,并更新下载状态为失败
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
