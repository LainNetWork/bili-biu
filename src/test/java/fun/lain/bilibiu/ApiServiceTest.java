package fun.lain.bilibiu;

import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.UserCollection;
import fun.lain.bilibiu.collection.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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
        CollectionMedia media = apiService.getMediaInfo(75408133L,null);

        System.out.println(media);
    }

    @Test
    public void test5(){
//        apiService.
        CollectionMedia media = apiService.getMediaInfo(75408133L,null);

        System.out.println(media);
    }
}
