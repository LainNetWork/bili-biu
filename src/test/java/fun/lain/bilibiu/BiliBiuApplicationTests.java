package fun.lain.bilibiu;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.common.BeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@SpringBootTest
class BiliBiuApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(BeanUtil.getBean(Environment.class).getProperty("lain.user-agent"));
    }

}
