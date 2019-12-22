package fun.lain.bilibiu.web.var;

public enum SaveTaskType {
    SYSTEM_TASK(0),
    WORK_TASK(1)
    ;

    private int code;
    SaveTaskType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
