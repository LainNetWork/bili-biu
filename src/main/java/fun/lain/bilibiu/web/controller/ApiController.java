package fun.lain.bilibiu.web.controller;

import com.alibaba.fastjson.JSONObject;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.service.BackApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private BackApiService backApiService;

    //根据cookie获取用户收藏夹
    @GetMapping("/getCollection")
    public Echo getUserCollection(@RequestBody String json){

        return  Echo.success();
    }
}
