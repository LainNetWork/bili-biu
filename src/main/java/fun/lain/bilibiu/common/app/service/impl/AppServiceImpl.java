package fun.lain.bilibiu.common.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.lain.bilibiu.common.app.entity.AppInfoEntity;
import fun.lain.bilibiu.common.app.mapper.AppInfoMapper;
import fun.lain.bilibiu.common.app.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;

@Service("appService")
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppInfoMapper, AppInfoEntity> implements AppService {
    @Resource
    private DataSource dataSource;

    @Value("${app.version}")
    private String appVersion;

    @Override
    public boolean ifTableInit() {
        return baseMapper.ifTableInit();
    }

    /**
     * 判断是否需要更新
     * @return
     */
    @Override
    public boolean ifNeedUpdate() {
        String version = baseMapper.getAppVersion();
        if(!appVersion.equals(version)){
            return true;
        }
        return false;
    }

    @Override
    public void InitTable() throws Exception {
        executeSql("initSql/init.sql");
    }

    private void executeSql(String path) throws Exception {
        Connection connection=null;
        ScriptRunner runner = null;
        InputStreamReader sql = null;
        try {
            connection = dataSource.getConnection();
            runner = new ScriptRunner(connection);
            runner.setAutoCommit(false);
            runner.setFullLineDelimiter(false);
            runner.setDelimiter(";");
            runner.setSendFullScript(false);
            runner.setStopOnError(true);//遇到错误直接退出
            runner.setThrowWarning(true);
            sql = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path));
            runner.runScript(sql);
            connection.commit();
        } catch (Exception e) {
            log.error("初始化失败！",e);
            connection.rollback();
        }finally {
            sql.close();
            runner.closeConnection();
        }
    }

    @Override
    public void updateTable() throws Exception {
        executeSql("initSql/update.sql");
    }


}
