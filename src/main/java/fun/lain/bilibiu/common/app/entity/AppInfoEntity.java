package fun.lain.bilibiu.common.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("app_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppInfoEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String version;
    private Integer initState;
}
