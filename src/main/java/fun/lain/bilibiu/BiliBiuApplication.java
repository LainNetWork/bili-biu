package fun.lain.bilibiu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.service.CacheTask;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.app.service.AppService;
import fun.lain.bilibiu.web.entity.SaveTask;
import fun.lain.bilibiu.web.mapper.SaveTaskMapper;
import fun.lain.bilibiu.web.service.ScheduleService;
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
import java.util.List;

@SpringBootApplication
@MapperScan(basePackages = "fun.lain.**.mapper")
@Slf4j
public class BiliBiuApplication{


    public static void main(String[] args) {
        SpringApplication.run(BiliBiuApplication.class, args);

    }



    @Resource
    private AppService appService;
    @Autowired
    private ScheduleService scheduleService;
    @Resource
    private SaveTaskMapper saveTaskMapper;

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
    public void init() throws Exception {

        if(!appService.ifTableInit()){
            appService.InitTable();
            log.info("数据库初始化完毕！");
        }
        //数据库版本升级逻辑判断
        if(appService.ifNeedUpdate()){
            appService.updateTable();
        }
        //初始化调度器
        //在启动后初始化调度器，构建触发器和任务
        scheduleService.initTask();

    }



}
