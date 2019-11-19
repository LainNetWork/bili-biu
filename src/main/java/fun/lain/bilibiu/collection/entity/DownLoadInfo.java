package fun.lain.bilibiu.collection.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownLoadInfo {
    private Integer order;
    private Long length;
    private Long size;
    private String url;
    @JSONField(name = "backup_url")
    private List<String> backupUrl;
}
