package fun.lain.bilibiu.cache.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.lain.bilibiu.cache.entity.CachePartTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CachePartTaskMapper extends BaseMapper<CachePartTask> {
    int saveOrUpdateBatch(@Param("list") List<CachePartTask> list);
}
