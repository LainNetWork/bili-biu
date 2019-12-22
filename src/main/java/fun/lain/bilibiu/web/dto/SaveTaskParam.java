package fun.lain.bilibiu.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SaveTaskParam {
    @NotNull(message = "用户类型不得为空！")
    private Integer userInfoType;
    @NotNull(message = "用户标识不得为空！")
    private String userIdentical;
    @NotEmpty(message = "收藏夹id不能为空！")
    private List<Long> ids;
    /**
     * 检测周期，使用cron表达式
     */
    private String cron;
}
