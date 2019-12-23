package fun.lain.bilibiu.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.entity.MediaDTO;
import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.dto.SaveTaskParam;

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



    IPage<MediaDTO> getMediaList(int index, int size);

    void start(Long taskId);

    void pause(Long taskId);

    void delete(Long taskId);

    /*
     * 重新下载 TODO 之后将用户信息独立出来后将重构此功能
     */
    void reload(Long cid);
}
