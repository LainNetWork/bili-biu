package fun.lain.bilibiu.collection.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BiliUserInfo {
    private String uname;
    private String face;
    private Long mid;
}
