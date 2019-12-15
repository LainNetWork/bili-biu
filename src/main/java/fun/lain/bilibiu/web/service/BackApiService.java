package fun.lain.bilibiu.web.service;

import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.entity.SaveTaskParam;

import java.util.List;

public interface BackApiService {
    Echo getUserCollection(String json);
    Echo saveCollection(SaveTaskParam param);

    /**
     * 获取任务列表
     * @param index 页码
     * @param size 大小
     * @return
     */
    Echo getTaskList(Integer index,Integer size);

    void start(Long taskId);
}
