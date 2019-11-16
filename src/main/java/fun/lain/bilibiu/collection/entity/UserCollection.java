package fun.lain.bilibiu.collection.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCollection implements Serializable {
    private Long id;
    private String title;
    @JSONField(name = "media_count")
    private Integer mediaCount;
}
