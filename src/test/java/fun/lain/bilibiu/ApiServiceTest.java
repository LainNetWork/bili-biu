package fun.lain.bilibiu;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.entity.UserCollection;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.app.mapper.AppInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ApiServiceTest {
    @Autowired
    ApiService apiService;
    @Test
    public void test1(){
        System.out.println(apiService.getUserInfo(""));
        System.out.println(apiService.getUserInfo(114514L));
    }
    @Test
    public void test2(){
        UserCollection collection = apiService.getUserCollections(1631252L).get(2);
        System.out.println(collection.getId());
        System.out.println(apiService.getAllMediaInCollection(collection.getId(),null));
    }

    @Test
    public void test3(){
//        apiService.
        List<CollectionMedia> list = apiService.getAllMediaInCollection(207520352L,null);

        System.out.println(list);
    }

    @Test
    public void test4(){
//        apiService.
        CollectionMedia medias = apiService.getMediaInfo(75408133L,null);
        MediaPart part = medias.getParts().get(0);
        CachePartTask task = CachePartTask.builder()
                .avid(part.getId())
                .cid(part.getCid())
                .size(part.getDownLoadInfos().get(0).getSize())
                .build();

////        Thread t = new Thread(new CacheTask(task));
//        t.start();
    }

    @Test
    public void test5(){
//        apiService.
        CollectionMedia media = apiService.getMediaInfo(75408133L,null);

        System.out.println(media);
    }
    @Resource
    AppInfoMapper appInfoMapper;

    @Test
    public void test6(){
        System.out.println(appInfoMapper.ifTableInit());
    }

    @Test
    public void test7(){
        CronSequenceGenerator generator = new CronSequenceGenerator("0/30 * * * * ?");
        Date date = new Date();
        Date date1 = generator.next(date);
        Date date2 = generator.next(date1);
        System.out.println(date2.getTime() - date1.getTime());
    }
}
