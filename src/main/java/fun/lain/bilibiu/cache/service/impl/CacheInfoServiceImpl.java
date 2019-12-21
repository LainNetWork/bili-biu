package fun.lain.bilibiu.cache.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.mapper.CachePartTaskMapper;
import fun.lain.bilibiu.cache.service.CacheInfoService;
import fun.lain.bilibiu.cache.var.CachePartTaskVar;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("cacheInfoService")
public class CacheInfoServiceImpl extends ServiceImpl<CachePartTaskMapper,CachePartTask> implements CacheInfoService {
    @Override
    public Integer getTaskStatus(long taskId) {
        return baseMapper.getTaskStatus(taskId);
    }

    @Override
    public int saveOrUpdateBatch(List<CachePartTask> list) {
        return baseMapper.saveOrUpdateBatch(list);
    }

    @Override
    public int run(long taskId) {
        return baseMapper.updateStatus(taskId, CachePartTaskVar.RUNNING.getCode());
    }

    @Override
    public int pause(long taskId) {
        return baseMapper.updateStatus(taskId, CachePartTaskVar.WAIT.getCode());
    }

    @Override
    public int finish(long taskId) {
        return baseMapper.updateStatus(taskId, CachePartTaskVar.FINNISH.getCode());
    }

    @Override
    public int updateCacheSize(long taskId, long cacheSize) {
        return baseMapper.updateCacheSize(taskId,cacheSize);
    }

}
