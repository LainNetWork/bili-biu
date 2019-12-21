package fun.lain.bilibiu.cache.var;

import lombok.Data;

public enum  CachePartTaskVar {

    WAIT(0),//等待状态
    RUNNING(1),//运行状态
    FINNISH(2)//完成状态
    ;

    private int code;

    CachePartTaskVar(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }

}
