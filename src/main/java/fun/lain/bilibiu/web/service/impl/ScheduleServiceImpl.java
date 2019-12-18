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
    public void start(Long taskId) {
        SaveTask task = saveTaskMapper.selectById(taskId);
        createAndStart(task);
    }

    private void createAndStart(SaveTask task){
        if(task==null){
            throw new LainException("任务不存在！");
        }
        //判断调度中是否存在
        try {
            if(scheduler.checkExists(getJobKey(task.getId()))){
                resume(task.getId());
            }else{
                create(task);
            }
        } catch (SchedulerException e) {
            throwException("定时任务调度错误！",e);
        }
    }
    @Override
    public void create(SaveTask task) {
        JobDetail job = JobBuilder.newJob().ofType(MonitorTask.class).withIdentity(getJobKey(task.getId())).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(task.getCron()).withMisfireHandlingInstructionDoNothing();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(task.getId()))
                .withSchedule(cronScheduleBuilder)
                .build();
        job.getJobDataMap().put("task",task);

        try {
            scheduler.scheduleJob(job,trigger);
            saveTaskMapper.updateById(SaveTask.builder()
                    .id(task.getId())
                    .status(SaveTask.Status.RUNNING)
                    .build());
        } catch (SchedulerException e) {
            if(e instanceof ObjectAlreadyExistsException){
                throwException("任务正在运行中!",e);
            }
            throwException("启动定时任务失败!",e);
        }
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
            throwException("恢复定时任务失败!",e);
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
            throwException("暂停定时任务失败！",e);
        }
    }

    @Override
    public void delete(Long taskId) {
        try {
            scheduler.deleteJob(getJobKey(taskId));
            saveTaskMapper.deleteById(taskId);
        } catch (SchedulerException e) {
            throwException("删除定时任务失败!",e);
        }
    }

    @Override
    public void initTask() {
        List<SaveTask> taskList = saveTaskMapper.selectList(new QueryWrapper<SaveTask>()
                .ne("status",SaveTask.Status.CREATE)
                .ne("status",SaveTask.Status.PAUSE)
        );
        for(SaveTask task:taskList){
            createAndStart(task);
        }
    }

    private void throwException(String message,Exception e){
        log.error(message,e);
        throw new LainException(message);
    }
}
