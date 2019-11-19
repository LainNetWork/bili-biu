package fun.lain.bilibiu.cache.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("cache_part_task")
@Builder
//分段视频缓存任务
public class CachePartTask {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long avid;
    private Long cid;
    private String title;
    private Integer quality;
    private Long size;
    private Long cacheSize;//已缓存的大小
    private Integer status;
    private String message;
}
