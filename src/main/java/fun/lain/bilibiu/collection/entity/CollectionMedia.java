package fun.lain.bilibiu.collection.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CollectionMedia {
    private Long id;
    private String title;
    //cid,partName
    private List<MediaPart> parts;
}
