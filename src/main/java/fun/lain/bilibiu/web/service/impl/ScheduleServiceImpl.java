package fun.lain.bilibiu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fun.lain.bilibiu.common.exception.LainException;
import fun.lain.bilibiu.web.entity.SaveTask;
import fun.lain.bilibiu.web.mapper.SaveTaskMapper;
import fun.lain.bilibiu.web.service.BackApiService;
import fun.lain.bilibiu.web.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("scheduleService")
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
//    private final String JOB_KEY = "Lain_";
    private final String GROUP_KEY = "monitor_group";
    @Autowired
    private Scheduler scheduler;

    @Resource
    SaveTaskMapper saveTaskMapper;;

    private JobKey getJobKey(Long taskId){
        return new JobKey(taskId.toString(),GROUP_KEY);
    }
    private TriggerKey getTriggerKey(Long taskId){
        return new TriggerKey(taskId.toString(),GROUP_KEY);
    }

    @Override
    public void createAndStart(Long taskId) throws SchedulerException {
        SaveTask task = saveTaskMapper.selectById(taskId);
        createAndStart(task);
    }

    private void createAndStart(SaveTask task) throws SchedulerException {
        //        Trigger.TriggerState triggerState = scheduler.getTriggerState(getTriggerKey(taskId));

        if(task==null){
            throw new LainException("任务不存在！");
        }
        JobDetail job = JobBuilder.newJob().ofType(MonitorTask.class).withIdentity(getJobKey(task.getId())).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(task.getCron()).withMisfireHandlingInstructionDoNothing();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(task.getId()))
                .withSchedule(cronScheduleBuilder)

                .build();
        job.getJobDataMap().put("task",task);
        scheduler.scheduleJob(job,trigger);
        saveTaskMapper.updateById(SaveTask.builder()
                .id(task.getId())
                .status(SaveTask.Status.RUNNING)
                .build());

    }

    @Override
    public void resume(Long taskId) {
        try {
            scheduler.resumeJob(getJobKey(taskId));
            saveTaskMapper.updateById(SaveTask.builder()
                    .id(taskId)
                    .status(SaveTask.Status.RUNNING)
                    .build());
        } catch (SchedulerException e) {
            log.error("恢复定时任务失败!",e);
            throw new LainException("恢复定时任务失败!");
        }
    }


    @Override
    public void pause(Long taskId) {
        try {
            scheduler.pauseJob(getJobKey(taskId));
            saveTaskMapper.updateById(SaveTask.builder()
                    .id(taskId)
                    .status(SaveTask.Status.PAUSE)
                    .build());
        } catch (SchedulerException e) {
            log.info("暂停定时任务失败！");
            throw new LainException("暂停定时任务失败！");
        }
    }

    @Override
    public void delete(Long taskId) {
        try {
            scheduler.deleteJob(getJobKey(taskId));
            saveTaskMapper.deleteById(taskId);
        } catch (SchedulerException e) {
            log.error("删除定时任务失败!",e);
            throw new LainException("删除定时任务失败!");
        }
    }

    @Override
    public void initTask() {
        List<SaveTask> taskList = saveTaskMapper.selectList(new QueryWrapper<SaveTask>().ne("status",SaveTask.Status.CREATE));
        for(SaveTask task:taskList){
            try {
                createAndStart(task);
            } catch (SchedulerException e) {
                log.error("任务%d启动失败！",task.getId());
                saveTaskMapper.updateById(SaveTask.builder()
                        .id(task.getId())
                        .status(SaveTask.Status.ERROR)
                        .build());
            }
        }
    }
}
