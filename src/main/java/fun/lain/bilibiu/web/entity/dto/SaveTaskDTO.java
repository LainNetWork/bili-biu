package fun.lain.bilibiu.web.entity.dto;

import fun.lain.bilibiu.collection.entity.BiliUserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveTaskDTO {
    private Long id;
    private BiliUserInfo info;
    private String cron;
    private String status;
    private Integer statusCode;
}
