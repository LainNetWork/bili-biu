package fun.lain.bilibiu.common.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.lain.bilibiu.common.app.entity.AppInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AppInfoMapper extends BaseMapper<AppInfoEntity> {


    @Select("select count(*) from INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME  = 'APP_INFO'")
    Boolean ifTableInit();

    @Select("select version from app_info where id = 1")
    String getAppVersion();
}
