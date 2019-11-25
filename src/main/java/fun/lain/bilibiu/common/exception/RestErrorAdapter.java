package fun.lain.bilibiu.common.exception;

import fun.lain.bilibiu.common.Echo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestErrorAdapter {

    @ExceptionHandler(LainException.class)
    public Echo handleLainException(LainException ex){
        return Echo.error(ex.getCode(),ex.getMessage(),ex.getData());
    }

}
