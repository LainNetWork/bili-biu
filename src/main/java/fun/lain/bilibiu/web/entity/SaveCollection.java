package fun.lain.bilibiu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * 任务关联收藏实体类
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "save_collect")
public class SaveCollection {
    @TableId(type = IdType.AUTO)
    private Long id;

    //任务id
    private Long  taskId;

    //收藏夹id
    private Long collectionId;
}
