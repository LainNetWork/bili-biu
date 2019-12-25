package fun.lain.bilibiu;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import fun.lain.bilibiu.common.app.service.AppService;
import fun.lain.bilibiu.web.mapper.SaveTaskMapper;
import fun.lain.bilibiu.web.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = "fun.lain.**.mapper")
@Slf4j
public class BiliBiuApplication{

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        return paginationInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(BiliBiuApplication.class, args);

    }



    @Resource
    private AppService appService;
    @Autowired
    private ScheduleService scheduleService;

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
