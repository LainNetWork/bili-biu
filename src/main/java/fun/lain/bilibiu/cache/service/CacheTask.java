
package fun.lain.bilibiu.cache.service;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.BeanUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 缓存任务
 */

public class CacheTask implements Runnable{
    private static final String BASE_URL = "https://www.bilibili.com/video/av";
    private CachePartTask cachePartTask;
    private CacheInfoService cacheInfoService;
//    private ApiService apiService;
    private String cookie;
    private String savePath;


    public CacheTask(CachePartTask cachePartTask){
        this.cachePartTask = cachePartTask;
        this.cacheInfoService = BeanUtil.getBean(CacheInfoService.class);
//        this.apiService = BeanUtil.getBean(ApiService.class);
        this.savePath = BeanUtil.getBean(Environment.class).getProperty("lain.save-path");
    }
//    public CacheTask(CachePartTask cachePartTask,String cookie){
//        this.cachePartTask = cachePartTask;
//        this.cacheInfoService = BeanUtil.getBean(CacheInfoService.class);
////        this.apiService = BeanUtil.getBean(ApiService.class);
//        this.savePath = BeanUtil.getBean(Environment.class).getProperty("lain.save-path");
//        this.cookie = cookie;
//
//    }



    @Override
    public void run() {
        //缓存，获取到下载链接之后，根据返回的size，以8M为一个下载块，每次循环都记录当前的下载进度


        String fileDir = String.format(savePath+"/%s/%s",this.cachePartTask.getAvid(),this.cachePartTask.getCid());
        //创建缓存目录
        File cacheFile = new File(fileDir);
        if(!cacheFile.exists()){
            cacheFile.mkdirs();
        }else if(!cacheFile.isDirectory()){
            cacheFile.delete();
            cacheFile.mkdirs();
        }
        File  videoFile = new File(String.format(fileDir+"/%s%d-%d%s",File.separator,this.cachePartTask.getCid(),this.cachePartTask.getQuality(),".temp"));
        long cacheSize = 0;
        if(videoFile.exists()){
             cacheSize = videoFile.length();
        }
        long size = this.cachePartTask.getSize();
        int block = 8 * 1024 * 1024;
        int count = 0;//下载的块个数
        try (RandomAccessFile file = new RandomAccessFile(videoFile,"rw")){
            URL downUrl  = new URL(this.cachePartTask.getDownloadUrl());
            file.seek(cacheSize);
            boolean flag = true;
            //终止条件，当块数*count>=size时
            long length = 0;
            while (flag){
                //校验任务是否暂停或取消

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
                URLConnection connection = downUrl.openConnection();
                connection.setRequestProperty(HttpHeaders.REFERER,BASE_URL+this.cachePartTask.getAvid());
                connection.setRequestProperty(HttpHeaders.RANGE,String.format("bytes=%d-%d",start,end));

                try(InputStream inputStream = connection.getInputStream()) {
                    byte[] buffer = new byte[1024*4];
                    while (true){
                        int len =inputStream.read(buffer);
                        if(len == -1){
                            break;
                        }
                        length+=len;
                        file.write(buffer,0,len);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //记录长度
                System.out.println(String.format("size:%d now: %d start:%d end:%d",this.cachePartTask.getSize(),length,start,end));
            }
            //改名
            file.close();
            if(cachePartTask.getDownloadUrl().contains(".mp4")){
                File targetFile = new File(FilenameUtils.removeExtension(videoFile.getPath())+".mp4");
                videoFile.renameTo(targetFile);
            }else{
                File targetFile = new File(FilenameUtils.removeExtension(videoFile.getPath())+".flv");
                videoFile.renameTo(targetFile);
            }
            System.out.println("over");
        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
