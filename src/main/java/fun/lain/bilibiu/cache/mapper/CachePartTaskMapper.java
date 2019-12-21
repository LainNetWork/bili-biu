package fun.lain.bilibiu.cache.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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



}
