package fun.lain.bilibiu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.BeanUtil;
import fun.lain.bilibiu.web.entity.SaveCollection;
import fun.lain.bilibiu.web.entity.SaveTask;
import fun.lain.bilibiu.web.mapper.SaveCollectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;


@Slf4j
public class MonitorTask implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取需要的SpringBean
        SaveCollectionMapper saveCollectionDao = BeanUtil.getBean(SaveCollectionMapper.class);
        SaveTask task = (SaveTask) context.getMergedJobDataMap().get("task");
        log.info("进入定时任务！");
        //获取此任务中用户要监控的收藏夹信息
        List<SaveCollection> list = saveCollectionDao.selectList(new QueryWrapper<SaveCollection>().eq("taskId",task.getId()));
        System.out.println(list);
    }
}
