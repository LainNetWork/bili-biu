package fun.lain.bilibiu.cache.task;

import com.alibaba.fastjson.JSONObject;
import fun.lain.bilibiu.common.BeanUtil;
import fun.lain.bilibiu.web.entity.SaveTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class LainJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Object task = context.getMergedJobDataMap().get("task");
        if(!(task instanceof SaveTask)){
            throw new JobExecutionException("参数有误！");
        }
        SaveTask saveTask = (SaveTask) task;
        Object object = BeanUtil.getBean(saveTask.getBeanName());
        if(!(object instanceof LainTask)){
            throw new JobExecutionException("任务必须实现LainTask接口！");
        }
        LainTask lainTask = (LainTask)object;
        JSONObject param = JSONObject.parseObject(saveTask.getParam());
        param.put("taskId",saveTask.getId());
        try {
            lainTask.execute(param.toJSONString());
        } catch (Exception e) {
            log.error("任务执行异常！",e);
            //TODO 记录日志
            e.printStackTrace();
        }
    }
}
