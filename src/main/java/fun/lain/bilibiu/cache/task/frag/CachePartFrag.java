package fun.lain.bilibiu.cache.task.frag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;

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
public class CachePartFrag implements Callable<String> {
    private int order;
    private String referer;
    private String path;
    private long start;
    private long end;
    private String url;


    @Override
    public String call() throws Exception {
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
        return "success";
    }
}
