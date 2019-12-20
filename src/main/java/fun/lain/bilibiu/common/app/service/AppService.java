package fun.lain.bilibiu.common.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.lain.bilibiu.common.app.entity.AppInfoEntity;

public interface AppService extends IService<AppInfoEntity> {
    boolean ifTableInit();
    boolean ifNeedUpdate();
    void InitTable() throws Exception;
    void updateTable() throws Exception;
}
