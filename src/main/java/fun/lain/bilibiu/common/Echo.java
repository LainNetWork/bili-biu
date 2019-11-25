package fun.lain.bilibiu.common;

import lombok.Data;

/**
 * 通用响应封装
 * @author Lain
 * @date 2019-10-26
 */

@Data
public class Echo {


    private int code;
    private Object data;
    private String message;

    public static Echo success(){
        Echo echo = new Echo();
        echo.setMessage("success");
        echo.setCode(Code.SUCCESS.getCode());
        return echo;
    }

    public static Echo success(String message){
        Echo echo = new Echo();
        echo.setMessage(message);
        echo.setCode(Code.SUCCESS.getCode());
        return echo;
    }

    public static Echo error(){
        Echo echo = new Echo();
        echo.setMessage("error");
        echo.setCode(Code.ERROR.getCode());
        return echo;
    }

    public static Echo error(String message){
        Echo echo = new Echo();
        echo.setMessage(message);
        echo.setCode(Code.ERROR.getCode());
        return echo;
    }



    public static Echo error(int code,String message){
        Echo echo = new Echo();
        echo.setCode(code);
        echo.setMessage(message);
        echo.setCode(Code.SUCCESS.getCode());
        return echo;
    }

    public static Echo error(int code,String message,Object data){
        Echo echo = new Echo();
        echo.setCode(code);
        echo.setMessage(message);
        echo.setCode(Code.SUCCESS.getCode());
        echo.setData(data);
        return echo;
    }

    public Echo data(Object data){
        this.data = data;
        return this;
    }

    public Echo meaage(String message){
        this.message = message;
        return this;
    }

    public enum Code{
        SUCCESS(200),
        ERROR(500),
        ACCESS_DENIED(403)//拒绝访问
        ;
        private int code;
        Code(int code){
            this.code = code;
        }
        public int getCode(){
            return this.code;
        }
    }


}
