package fun.lain.bilibiu.cache.service.impl;

import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.mapper.CachePartTaskMapper;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import org.springframework.stereotype.Service;

@Service("cacheInfoService")
public class CacheInfoServiceImpl implements CacheInfoService {

    CachePartTaskMapper cachePartTaskMapper;

    @Override
    public void createTask(CachePartTask task) {
        cachePartTaskMapper.insert(task);
    }

    @Override
    public CachePartTask getPartTaskById(Long id) {
       return cachePartTaskMapper.selectById(id);
    }

    @Override
    public void pauseTask(Long id) {
        CachePartTask task = cachePartTaskMapper.selectById(id);
        if(task==null){

        }
    }

    @Override
    public void startTask(Long id) {

    }

    @Override
    public void updateTaskInfo(CachePartTask task) {

    }
}
