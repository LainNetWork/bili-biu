package fun.lain.bilibiu.web.service.impl;

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
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class MonitorTask implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取需要的SpringBean
        SaveCollectionMapper saveCollectionDao = BeanUtil.getBean(SaveCollectionMapper.class);
        CachePartTaskMapper cachePartTaskMapper = BeanUtil.getBean(CachePartTaskMapper.class);
        ApiService apiService = BeanUtil.getBean(ApiService.class);

        SaveTask task = (SaveTask) context.getMergedJobDataMap().get("task");
        log.info("进入定时任务！");
        //获取此任务中用户要监控的收藏夹信息
        List<SaveCollection> list = saveCollectionDao.selectList(new QueryWrapper<SaveCollection>().eq("taskId",task.getId()));
        List<CachePartTask> newPartList = new ArrayList<>();
        for(SaveCollection collection : list){
            List<CollectionMedia> medias = apiService.getAllMediaInCollection(collection.getCollectionId(),task.getCookie());
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
                    newPartList.addAll(newParts);
                }
            }
        }
        //TODO 入库(注意insert可能会和其他任务冲突，需要做锁)
        log.info(newPartList.toString());
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
                    .quality(part.getAcceptQuality())
                    .build());
        }
        return cachePartTasks;
    }
}
