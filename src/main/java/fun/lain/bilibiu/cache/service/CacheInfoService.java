package fun.lain.bilibiu.cache.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.lain.bilibiu.cache.entity.CachePartTask;

import java.util.List;

public interface CacheInfoService extends IService<CachePartTask> {


    Integer getTaskStatus(long taskId);

    int saveOrUpdateBatch( List<CachePartTask> list);

    int run(long taskId);

    int pause(long taskId);

    int finish(long taskId);

    int updateCacheSize(long taskId,long cacheSize);

}
