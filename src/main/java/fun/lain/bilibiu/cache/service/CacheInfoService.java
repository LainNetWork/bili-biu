package fun.lain.bilibiu.cache.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.entity.MediaDTO;

import java.util.List;

public interface CacheInfoService extends IService<CachePartTask> {


    Integer getTaskStatus(long taskId);

    int saveOrUpdateBatch( List<CachePartTask> list);

    int run(long taskId);

    int pause(long taskId);

    int finish(long taskId);

    int updateCacheSize(long taskId,long cacheSize);

    IPage<MediaDTO> getMediaList(int page, int size,String keyword);

}
