package fun.lain.bilibiu.web.controller;

import com.alibaba.fastjson.JSONObject;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.service.BackApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private BackApiService backApiService;

    //根据cookie获取用户收藏夹
    @PostMapping("/getCollection")
    public Echo getUserCollection(@RequestBody String json){
        return  backApiService.getUserCollection(json);
    }

    //保存任务
    @PostMapping("/saveTask")
    public Echo saveTask(){
        return null;
    }
}
