package fun.lain.bilibiu.cache.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@TableName("cache_part_task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//分段视频缓存任务
public class CachePartTask {
    //id主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //视频av号
    private Long avid;
    //关联的主任务id
    private Long taskId;

    private String avTitle;
    //分段号
    private Long cid;
    //标题
    private String title;
    //不做入库，每次任务执行前都重新获取，后续计划改为下载地址列表，启用备用地址多次尝试
    @TableField(exist = false)
    private String downloadUrl;
    //质量
    private Integer quality;
    //视频字节数
    private Long size;
    //已缓存的大小
    private long cacheSize;//初始值为0
    //状态
    private int status;
    //状态信息
    private String message;

    private Date createTime;
}
