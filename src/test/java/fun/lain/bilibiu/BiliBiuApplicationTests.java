package fun.lain.bilibiu;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.mapper.CachePartTaskMapper;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import fun.lain.bilibiu.common.BeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@SpringBootTest
class BiliBiuApplicationTests {

    @Resource
    CachePartTaskMapper cachePartTaskMapper;
    @Test
    void contextLoads() {
        Page page = new Page(2,3);
        System.out.println(cachePartTaskMapper.getMediaList(page));
    }

}
