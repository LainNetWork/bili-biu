
package fun.lain.bilibiu.cache.service;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.common.BeanUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class CacheTask implements Runnable{
    private static final String BASE_URL = "https://www.bilibili.com/video/av";
    private CachePartTask cachePartTask;
    private CacheInfoService cacheInfoService;
    private String savePath;
    public CacheTask(CachePartTask cachePartTask){
        this.cachePartTask = cachePartTask;
        this.cacheInfoService = BeanUtil.getBean(CacheInfoService.class);
        this.savePath = BeanUtil.getBean(Environment.class).getProperty("lain.savePath");
    }


    @Override
    public void run() {
        //缓存，获取到下载链接之后，根据返回的size，以8M为一个下载块，每次循环都记录当前的下载进度
        int size = 0;
        String url = "";
        int block = 8 * 1024 * 1024;
        int count = 0;//下载的块个数
        long cacheSize= this.cachePartTask.getCacheSize();

        String fileDir = String.format(savePath+"/%s/%s",this.cachePartTask.getAvid(),this.cachePartTask.getCid());
        //创建缓存目录
        File cacheFile = new File(fileDir);
        if(!cacheFile.exists()){
            cacheFile.mkdirs();
        }else if(!cacheFile.isDirectory()){
            cacheFile.delete();
            cacheFile.mkdirs();
        }

        URL downUrl = null;
        try (RandomAccessFile file = new RandomAccessFile(String.format(fileDir+"%s%d",File.pathSeparator,this.cachePartTask.getCid(),".flv"),"rw")){
            ;
            downUrl = new URL(url);

            boolean flag = true;
            //终止条件，当块数*count>=size时
            while (flag){
                int start = count * block;
                count++;
                int end = count*block;
                if(end>=size){
                    end = size;
                    flag = false;
                }
                URLConnection connection = downUrl.openConnection();
                connection.setRequestProperty(HttpHeaders.REFERER,BASE_URL+this.cachePartTask.getAvid());
                connection.setRequestProperty(HttpHeaders.RANGE,String.format("bytes=%d-%d",start,end));

                try(InputStream inputStream = connection.getInputStream()) {
                    byte[] buffer = new byte[1024*1024*4];//
                    while (true){
                        int len =inputStream.read(buffer);
                        if(len == -1){
                            break;
                        }
                        file.write(buffer,0,len);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
