package fun.lain.bilibiu.cache.service.impl;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import org.springframework.stereotype.Service;

@Service("cacheInfoService")
public class CacheInfoServiceImpl implements CacheInfoService {



    @Override
    public void createTask(CachePartTask task) {

    }

    @Override
    public CachePartTask getPartTaskById(Long id) {
        return null;
    }

    @Override
    public void pauseTask(Long id) {

    }

    @Override
    public void startTask(Long id) {

    }

    @Override
    public void updateTaskInfo(CachePartTask task) {

    }
}
