package fun.lain.bilibiu.cache.task.frag;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回调，用于处理分片下载的进度记录.对于同一个视频下载任务来说，应当为单例
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheFragCallBack {
    private Long taskId;
    private CacheInfoService cacheInfoService;

    public synchronized void log(long size){
        CachePartTask task = cacheInfoService.getById(taskId);
        cacheInfoService.updateCacheSize(taskId,task.getCacheSize()+size);
        if((task.getCacheSize()+size)==task.getSize()){
            cacheInfoService.finish(taskId);
        }

    }
}
