package fun.lain.bilibiu.web.controller;

import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.dto.SaveTaskParam;
import fun.lain.bilibiu.web.service.BackApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private BackApiService backApiService;



//    @PostMapping("/user/save")
//    public Echo saveUserInfo(String userIdentical,String identicalType,String tag)
//    {
//
//        return Echo.success();
//    }


    @PostMapping("/cache/reLoad")
    public Echo reDownload(Long cid){
        backApiService.reload(cid);
        return Echo.success();
    }

    @GetMapping("/cache/mediaList")
    public Echo getMediaCacheList(Integer page,Integer size,String keyword){
        return Echo.success().data(backApiService.getMediaList(page,size,keyword));
    }

    //根据cookie获取用户收藏夹
    @PostMapping("/getCollection")
    public Echo getUserCollection(@RequestBody String json){
        return  backApiService.getUserCollection(json);
    }


    //保存任务,一个用户只能建立一个任务
    @PostMapping("/saveTask")
    public Echo saveTask(@Valid @RequestBody SaveTaskParam saveTaskParam){
        return backApiService.saveCollection(saveTaskParam);
    }

    //获取所有创建的任务列表
    @GetMapping("/getTaskList")
    public Echo getTaskList(Integer index,Integer size){
        return backApiService.getTaskList(index,size);
    }

    @PostMapping("/task/start")
    public Echo start(@RequestParam("taskId") Long taskId){
        backApiService.start(taskId);
        return Echo.success();
    }

    @PostMapping("/task/pause")
    public Echo pause(Long taskId){
        backApiService.pause(taskId);
        return Echo.success();
    }

    @PostMapping("/task/delete")
    public Echo delete(Long taskId){
        backApiService.delete(taskId);
        return Echo.success();
    }


}
