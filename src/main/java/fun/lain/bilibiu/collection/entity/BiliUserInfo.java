package fun.lain.bilibiu.collection.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BiliUserInfo {
    private String cookie;
    private String name;
    private String face;
    private Long mid;
}
