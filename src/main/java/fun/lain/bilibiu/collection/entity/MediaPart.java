package fun.lain.bilibiu.collection.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MediaPart {
    private Long id;
    private Long cid;
    @JSONField(name="part")
    private String partName;
    @JSONField(name = "accept_quality")
    private Integer[] quality;
    @JSONField(name = "accept_description")
    private String [] qualityName;

    private String[] url;

    private String[] backURL;
}
