package fun.lain.bilibiu.collection.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaPart {
    private Long id;
    private Long cid;
    @JSONField(name="part")
    private String partName;
    //最高允许的质量
    @JSONField(name = "quality")
    private Integer acceptQuality;
    @JSONField(name = "accept_quality")
    private List<Integer> qualityLevel;
    @JSONField(name = "accept_description")
    private List<String> qualityName;
    @JSONField(name = "durl")
    private List<DownLoadInfo> downLoadInfos;
}
