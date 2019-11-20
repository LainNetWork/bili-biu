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
    //任务id主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //视频av号
    private Long avid;
    //分段号
    private Long cid;
    //标题
    private String title;
    //质量
    private Integer quality;
    //视频字节数
    private Long size;
    //已缓存的大小
    private Long cacheSize;
    //状态
    private Integer status;
    //状态信息
    private String message;
}
