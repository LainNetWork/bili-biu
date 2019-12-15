package fun.lain.bilibiu;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.service.CacheTask;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.app.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;

@SpringBootApplication
@MapperScan(basePackages = "fun.lain.**.mapper")
@Slf4j
public class BiliBiuApplication{
    @Resource
    private AppService appService;
    @Autowired
    private Scheduler scheduler;

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

    /**
     * 初始化逻辑
     */
    @PostConstruct
    public void init(){
        //TODO 之后可以添加数据库版本升级逻辑判断
        if(!appService.ifTableInit()){
            appService.InitTable();
            log.info("数据库初始化完毕！");
        }
        //初始化调度器
        //TODO 在启动后初始化调度器，构建触发器和任务
    }

    public static void main(String[] args) {
        SpringApplication.run(BiliBiuApplication.class, args);

    }

}
