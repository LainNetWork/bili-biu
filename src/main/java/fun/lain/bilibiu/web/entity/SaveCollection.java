package fun.lain.bilibiu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;


/**
 * 任务关联收藏实体类
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("save_collect")
public class SaveCollection {
    @TableId(type = IdType.AUTO)
    private Long id;

    //任务id
    private Long  taskId;

    //收藏夹id
    private Long collectionId;

    private String collectionName;
}
