package fun.lain.bilibiu.web.service;

import org.quartz.SchedulerException;

public interface ScheduleService {
    void createAndStart(Long taskId) throws SchedulerException;
    void resume(Long taskId);
    void pause(Long taskId);
    void delete(Long taskId);
    void initTask();
}
