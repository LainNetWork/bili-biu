package fun.lain.bilibiu.cache.service;

import fun.lain.bilibiu.cache.entity.CachePartTask;

public interface CacheInfoService {

    void createTask(CachePartTask task);
    /**
     * 根据id获取任务
     * @param id
     * @return
     */
    CachePartTask getPartTaskById(Long id);

    /**
     * 暂停缓存任务
     * @param id
     */
    void pauseTask(Long id);

    /**
     * 开始缓存任务
     * @param id
     */
    void startTask(Long id);

    void updateTaskInfo(CachePartTask task);

}
