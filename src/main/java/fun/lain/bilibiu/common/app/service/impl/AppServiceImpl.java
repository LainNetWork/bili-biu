package fun.lain.bilibiu.common.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.lain.bilibiu.common.app.entity.AppInfoEntity;
import fun.lain.bilibiu.common.app.mapper.AppInfoMapper;
import fun.lain.bilibiu.common.app.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

@Service("appService")
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppInfoMapper, AppInfoEntity> implements AppService {
    @Resource
    private DataSource dataSource;

    @Override
    public Boolean ifTableInit() {
        return baseMapper.ifTableInit();
    }

    @Override
    public void InitTable() {
        try {
            Connection connection = dataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);
            runner.setFullLineDelimiter(false);
            runner.setDelimiter(";");
            runner.setSendFullScript(false);
            runner.setStopOnError(true);//遇到错误直接退出
            runner.setThrowWarning(true);
            InputStream initSql = getClass().getClassLoader().getResourceAsStream("initSql/init.sql");
            runner.runScript(new InputStreamReader(initSql));
            runner.closeConnection();
        } catch (Exception e) {
            log.error("初始化失败！",e);
            e.printStackTrace();
        }
    }
}
