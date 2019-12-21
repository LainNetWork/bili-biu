package fun.lain.bilibiu.cache.tunnel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CachePartResult {
    private String message;
    private Exception exception;
}
