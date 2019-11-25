package fun.lain.bilibiu;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.service.CacheTask;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.service.ApiService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@SpringBootApplication
@MapperScan(basePackages = "fun.lain.**.mapper")
public class BiliBiuApplication implements CommandLineRunner {

@Resource
ApiService apiService;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().forEach(converter->{
            if(converter instanceof StringHttpMessageConverter){
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
                return;
            }
        });

        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(BiliBiuApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        CollectionMedia medias = apiService.getMediaInfo(75408133L,"SESSDATA=99fc2115%2C1576742104%2Cba6373b1");
        MediaPart part = medias.getParts().get(2);
        CachePartTask task = CachePartTask.builder()
                .avid(part.getId())
                .cid(part.getCid())
                .downloadUrl(part.getDownLoadInfos().get(0).getUrl())
                .size(part.getDownLoadInfos().get(0).getSize())
                .build();
        System.out.println(part);
        Thread t = new Thread(new CacheTask(task));
        t.start();
    }
}
