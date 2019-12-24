package fun.lain.bilibiu.cache.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.mapper.CachePartTaskMapper;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.BeanUtil;
import fun.lain.bilibiu.web.entity.SaveCollection;
import fun.lain.bilibiu.web.entity.SaveTask;
import fun.lain.bilibiu.web.mapper.SaveCollectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component("monitorTask")
public class MonitorTask implements LainTask {


    @Resource
    private SaveCollectionMapper saveCollectionMapper;

    @Resource
    private CachePartTaskMapper cachePartTaskMapper;

    @Override
    public void execute(String param) throws Exception{
        JSONObject jsonParam = JSONObject.parseObject(param);
        Long taskId = jsonParam.getLong("taskId");
        if(taskId == null){
            throw new RuntimeException("任务Id不存在！");
        }
        String cookie = jsonParam.getString("cookie");
        ApiService apiService = BeanUtil.getBean(ApiService.class);

//        SaveTask task = (SaveTask) context.getMergedJobDataMap().get("task");
        log.info("进入定时任务！");
        //获取此任务中用户要监控的收藏夹信息
        List<SaveCollection> list = saveCollectionMapper.selectList(new QueryWrapper<SaveCollection>().eq("taskId",taskId));
        for(SaveCollection collection : list){
            List<CollectionMedia> medias = apiService.getAllMediaInCollection(collection.getCollectionId(),cookie);
            for (CollectionMedia media : medias){
                List<CachePartTask> parts = buildTasks(media);
                if(CollectionUtils.isNotEmpty(parts)){
                    List<CachePartTask> result = cachePartTaskMapper.selectList(
                            new QueryWrapper<CachePartTask>()
                                    .in("cid",parts.stream().map(CachePartTask::getCid).collect(Collectors.toList()))
                    );
                    //如果数据库中已有该片段Id，则将其筛选出去
                    List<Long> ids = result.stream().map(CachePartTask::getCid).collect(Collectors.toList());
                    List<CachePartTask> newParts  = parts.stream().filter(part->!ids.contains(part.getCid())).collect(Collectors.toList());
                    newParts.forEach(task->{
                        task.setTaskId(taskId);
                    });
                    if(CollectionUtils.isNotEmpty(newParts)){
                        cachePartTaskMapper.saveOrUpdateBatch(newParts);
                    }
                }
            }
        }
    }


    private List<CachePartTask> buildTasks(CollectionMedia media){
        List<CachePartTask> cachePartTasks = new ArrayList<>();
        if(CollectionUtils.isEmpty(media.getParts())){
            return cachePartTasks;
        }
        for(MediaPart part:media.getParts()){
            cachePartTasks.add(CachePartTask.builder()
                    .avid(media.getId())
                    .cid(part.getCid())
                    .title(part.getPartName())
                    .avTitle(media.getTitle())
                    .quality(part.getAcceptQuality())
                    .createTime(new Date())
                    .build());
        }
        return cachePartTasks;
    }
}
