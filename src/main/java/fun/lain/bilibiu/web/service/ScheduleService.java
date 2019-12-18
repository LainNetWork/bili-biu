package fun.lain.bilibiu.web.service;

import fun.lain.bilibiu.web.entity.SaveTask;
import org.quartz.SchedulerException;

public interface ScheduleService {
    void create(SaveTask task);
    void start(Long taskId);
    void resume(Long taskId);
    void pause(Long taskId);
    void delete(Long taskId);
    void initTask();
}
