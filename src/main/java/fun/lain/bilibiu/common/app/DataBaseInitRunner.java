package fun.lain.bilibiu.common.app;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Component
public class DataBaseInitRunner implements ApplicationRunner {
    @Resource
    DataSource dataSource;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //判断表是否存在

    }
}
