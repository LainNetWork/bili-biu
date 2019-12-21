package fun.lain.bilibiu.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CachePartTaskParam {
    private String cookie;
    private long taskId;
}
