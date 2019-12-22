package fun.lain.bilibiu.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    private Long id;
    private String title;
    private List<CachePartTask> parts;
}
