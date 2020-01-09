package fun.lain.bilibiu.cache.task.frag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

/**
 * 分片
 */
@Builder
@Data
@AllArgsConstructor
@Slf4j
public class CachePartFrag implements Runnable {
    /**
     * 顺序编号
     */
    private int order;
    private String referer;
    private String path;
    private long start;
    private long end;
    private String url;
    private CacheFragCallBack cacheFragCallBack;


    @Override
    public void run(){
        try {
            RandomAccessFile file = new RandomAccessFile(path,"rw");
            file.seek(start);
            URL downUrl = new URL(url);
            URLConnection connection = downUrl.openConnection();
            connection.setRequestProperty(HttpHeaders.REFERER,this.referer);
            connection.setRequestProperty(HttpHeaders.RANGE,String.format("bytes=%d-%d",start,end));
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024*4];
            while (true){
                int len =inputStream.read(buffer);
                if(len == -1){
                    break;
                }
                file.write(buffer,0,len);
            }
            inputStream.close();
            file.close();
            cacheFragCallBack.log(end - start);
            writeProgress();
        } catch (Exception e) {
            log.error("视频：",path,"分片序号：",order,"下载出错！");
        }

    }

    private void writeProgress() throws IOException {
        RandomAccessFile file = new RandomAccessFile(path+CacheTask.DPC_FILE_EXTENSION,"rw");
        file.seek(order);
        file.writeByte(1);
        file.close();
    }
}
