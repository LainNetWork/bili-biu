package fun.lain.bilibiu.web.service.impl;

import fun.lain.bilibiu.web.entity.SaveTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class MonitorTask implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SaveTask task = (SaveTask) context.getMergedJobDataMap().get("task");
        log.info("进入定时任务！");
        log.info("定时任务内容：");
        System.out.println(task);
    }
}
