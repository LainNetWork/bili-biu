
package fun.lain.bilibiu.cache.task;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.entity.CachePartTaskParam;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import fun.lain.bilibiu.cache.tunnel.CachePartResult;
import fun.lain.bilibiu.cache.var.CachePartTaskVar;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 缓存任务
 */
@Slf4j
public class CacheTask implements Callable<CachePartResult> {
    private  final int block = 4 * 1024 * 1024;
    private static final String BASE_URL = "https://www.bilibili.com/video/av";
    private CachePartTaskParam cachePartTaskParam;
    private ApiService apiService;
    private CacheInfoService cacheInfoService;
    private String savePath;

    public CacheTask(CachePartTaskParam cachePartTaskParam){
        this.cachePartTaskParam = cachePartTaskParam;
        this.apiService = BeanUtil.getBean(ApiService.class);
        this.cacheInfoService = BeanUtil.getBean(CacheInfoService.class);
        this.savePath = BeanUtil.getBean(Environment.class).getProperty("lain.save-path");
    }
    @Override
    public CachePartResult call() {
        //获取下载链接

        CachePartTask cachePartTask = cacheInfoService.getById(cachePartTaskParam.getTaskId());
        cacheInfoService.run(cachePartTask.getId());
        buildTaskInfo(cachePartTask);
        //缓存，获取到下载链接之后，根据返回的size，以8M为一个下载块，每次循环都记录当前的下载进度
        String fileDir = String.format(savePath+"/%s/%s",cachePartTask.getAvid(),cachePartTask.getCid());
        //创建缓存目录
        File cacheFile = new File(fileDir);
        if(!cacheFile.exists()){
            cacheFile.mkdirs();
        }else if(!cacheFile.isDirectory()){
            cacheFile.delete();
            cacheFile.mkdirs();
        }
        File videoFile = new File(String.format(fileDir+"/%s%d-%d%s",File.separator,cachePartTask.getCid(),cachePartTask.getQuality(),".temp"));

        long cacheSize = 0;
        if(videoFile.exists()){
             cacheSize = cachePartTask.getCacheSize();
        }
        long size = cachePartTask.getSize();

        int count = 0;//下载的块个数
        try (RandomAccessFile file = new RandomAccessFile(videoFile,"rw")){
            URL downUrl  = new URL(cachePartTask.getDownloadUrl());//TODO 下载地址应该在进循环前通过API服务获取
            file.seek(cacheSize);
            boolean flag = true;
            //终止条件，当块数*count>=size时
            long length = cacheSize;//统计下载大小
            while (flag){
                //校验任务是否暂停或取消,取消则跳出循环
                if(!cacheInfoService.getTaskStatus(cachePartTask.getId()).equals(CachePartTaskVar.RUNNING.getCode())){
                    return CachePartResult.builder().message("下载暂停").build();
                }
                long start = cacheSize + count * block +1;//加上上次缓存的内容
                if(count==0){
                    start = cacheSize + 0;
                }
                count++;
                long end = cacheSize + count * block;
                if(end>=size){
                    end = size;
                    flag = false;
                }
                if(start == end){
                    break;
                }
                //TODO 低优先级，之后改为HttpClient，采用连接池（设计切片，开启多线程下载单文件
                URLConnection connection = downUrl.openConnection();
                connection.setRequestProperty(HttpHeaders.REFERER,BASE_URL+cachePartTask.getAvid());
                connection.setRequestProperty(HttpHeaders.RANGE,String.format("bytes=%d-%d",start,end));
                int fragLength = 0;//记录本次向文件写入了多少字节
                try(InputStream inputStream = connection.getInputStream()) {
                    byte[] buffer = new byte[1024*4];
                    while (true){
                        int len =inputStream.read(buffer);
                        if(len == -1){
                            break;
                        }
                        file.write(buffer,0,len);
                        fragLength+=len;
                    }

                } catch (IOException e) {
                    //IO错误，回退指针
                    log.error("下载出错！",e);
                    file.seek(file.getFilePointer()-fragLength);
                    count--;
                    continue;
                }
                length+=fragLength;//记录当前总字节大小
                //记录长度
                cacheInfoService.updateCacheSize(cachePartTask.getId(),length);
            }
            file.close();
            //改名
            if(cachePartTask.getDownloadUrl().contains(".mp4")){
                File targetFile = new File(FilenameUtils.removeExtension(videoFile.getPath())+".mp4");
                videoFile.renameTo(targetFile);
            }else{
                File targetFile = new File(FilenameUtils.removeExtension(videoFile.getPath())+".flv");
                videoFile.renameTo(targetFile);
            }
            cacheInfoService.finish(cachePartTask.getId());
        } catch (Exception e){
            e.printStackTrace();
            return CachePartResult.builder()
                    .exception(e)
                    .message("下载失败！")
                    .build();
        }
        return CachePartResult.builder()
                .message("下载成功！")
                .build();
    }

    private void buildTaskInfo(CachePartTask cachePartTask){
        MediaPart part= apiService.getDownloadInfo(cachePartTask.getAvid(),cachePartTask.getCid(),cachePartTaskParam.getCookie());
        cachePartTask.setDownloadUrl(part.getDownLoadInfos().get(0).getUrl());//TODO 暂时不取备用url
        cachePartTask.setQuality(part.getAcceptQuality());
        cachePartTask.setSize(part.getDownLoadInfos().get(0).getSize());
        //更新大小信息
        cacheInfoService.update(new UpdateWrapper<CachePartTask>()
                .set("quality",part.getAcceptQuality())
                .set("size",part.getDownLoadInfos().get(0).getSize())
                .eq("id",cachePartTask.getId())
        );
    }


}
