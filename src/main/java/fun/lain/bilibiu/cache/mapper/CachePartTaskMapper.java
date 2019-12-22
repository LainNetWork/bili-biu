package fun.lain.bilibiu.cache.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import fun.lain.bilibiu.cache.entity.MediaDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface CachePartTaskMapper extends BaseMapper<CachePartTask> {

    /**
     * 获取任务状态
     * @param taskId
     * @return
     */
    @Select("select status from cache_part_task where id = #{taskId}")
    Integer getTaskStatus(@Param("taskId") long taskId);

    @Update("update cache_part_task set status = #{status} where id = #{taskId}")
    int updateStatus(@Param("taskId") long taskId,@Param("status")int status);

    int saveOrUpdateBatch(@Param("list") List<CachePartTask> list);

    @Update("update cache_part_task set cacheSize = #{cacheSize} where id = #{taskId}")
    int updateCacheSize(@Param("taskId") long taskId,@Param("cacheSize")long cacheSize);

    List<MediaDTO> getMediaList(Page page);

    List<CachePartTask> getPartsByMediaId(long id);

}
