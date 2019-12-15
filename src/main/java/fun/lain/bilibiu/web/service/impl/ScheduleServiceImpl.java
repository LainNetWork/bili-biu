package fun.lain.bilibiu.web.service.impl;

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
        Trigger.TriggerState triggerState = scheduler.getTriggerState(getTriggerKey(taskId));
        SaveTask task = saveTaskMapper.selectById(taskId);
        if(task==null){
            throw new LainException("任务不存在！");
        }
        if(task.getStatus().equals(SaveTask.Status.PAUSE)){
            resume(taskId);
        }
        JobDetail job = JobBuilder.newJob().ofType(MonitorTask.class).withIdentity(getJobKey(taskId)).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
            .cronSchedule(task.getCron()).withMisfireHandlingInstructionDoNothing();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(taskId))
                .withSchedule(cronScheduleBuilder)

                .build();
        job.getJobDataMap().put("task",task);
        scheduler.scheduleJob(job,trigger);
        saveTaskMapper.updateById(SaveTask.builder()
                .id(taskId)
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
}
