package fun.lain.bilibiu.cache.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.entity.CachePartTaskParam;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import fun.lain.bilibiu.cache.tunnel.CacheTunnel;
import fun.lain.bilibiu.cache.var.CachePartTaskVar;
import fun.lain.bilibiu.collection.entity.BiliUserInfo;
import fun.lain.bilibiu.web.entity.SaveTask;
import fun.lain.bilibiu.web.mapper.SaveTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component("cachePushTask")
public class CachePushTask implements LainTask{
    @Autowired
    private CacheInfoService cacheInfoService;
    @Resource
    private SaveTaskMapper saveTaskMapper;
    @Override
    public void execute(String param) throws Exception {
        //查询处于等待状态的5条任务
        List<CachePartTask> list = cacheInfoService.list(new QueryWrapper<CachePartTask>()
                .eq("status", CachePartTaskVar.WAIT.getCode())
                .last("limit 5")
        );
        List<SaveTask> errorList = new ArrayList<>();
        for(CachePartTask task:list){
            //获取这些任务的cookie信息
            SaveTask saveTask= saveTaskMapper.selectById(task.getTaskId());
            if(saveTask == null){
                errorList.add(saveTask);
            }
            BiliUserInfo info = JSONObject.toJavaObject(JSONObject.parseObject(saveTask.getParam()).getJSONObject("userInfo"), BiliUserInfo.class);
            CacheTask cacheTask = new CacheTask(CachePartTaskParam.builder()
                    .cookie(info.getCookie())
                    .taskId(task.getId())
                    .build());
            CacheTunnel.submit(cacheTask);
        }
    }
}
