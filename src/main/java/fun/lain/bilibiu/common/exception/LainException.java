package fun.lain.bilibiu.common.exception;

import fun.lain.bilibiu.common.Echo;
import lombok.Data;

@Data
public class LainException extends RuntimeException{
    private String  msg;
    private Integer code;
    private Object data;
    public LainException(){
        super();
        this.code = Echo.Code.ERROR.getCode();
        this.msg = "error";
    }

    public LainException(String message){
        super(message);
        this.code = Echo.Code.ERROR.getCode();
        this.msg = message;
    }

    public LainException(int code,String message){
        super(message);
        this.msg = message;
        this.code = code;
    }

    public LainException(int code,String message,Object data){
        super(message);
        this.msg = message;
        this.code = code;
        this.data = data;
    }
}
