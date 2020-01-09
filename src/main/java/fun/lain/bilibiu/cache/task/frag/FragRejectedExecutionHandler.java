package fun.lain.bilibiu.cache.task.frag;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
@Slf4j
public class FragRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            executor.getQueue().put(r);//阻塞
        } catch (InterruptedException e) {
            log.error("分片任务执行异常！",e);
        }
    }
}
