package fun.lain.bilibiu.web.entity.dto;

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
    private Long uid;
    private String cron;
    private String status;
}
