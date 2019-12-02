package fun.lain.bilibiu.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import fun.lain.bilibiu.collection.entity.BiliUserInfo;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.entity.SaveTaskParam;
import fun.lain.bilibiu.web.service.BackApiService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service("backApiService")
public class BackApiServiceImpl implements BackApiService {

    @Resource
    private ApiService apiService;
    @Override
    public Echo getUserCollection(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json) ;
        Integer type = Integer.parseInt(jsonObject.getString("type"));
        if(type==null){
            return Echo.error("参数错误");
        }
        BiliUserInfo userInfo = null;
        if(type.equals(0)){//0为uid
            Long uid = jsonObject.getLong("value");
            if(uid == null){
                return Echo.error("参数错误喵~");
            }
            userInfo = apiService.getUserInfo(uid);
            if(userInfo == null){
                return Echo.error("抓取用户信息失败！");
            }

        }else if(type.equals(1)){//1为cookie
            String cookie = jsonObject.getString("value");
            if(cookie == null){
                return Echo.error("参数错误喵~");
            }
            userInfo = apiService.getUserInfo(cookie);
            if(userInfo == null){
                return Echo.error("抓取用户信息失败！");
            }
        }
        return Echo.success().data(apiService.getUserCollections(userInfo.getMid()));
    }

    @Override
    public Echo saveCollection(SaveTaskParam param) {
        BiliUserInfo info = null;
        switch (param.getUserInfoType())
        {
            case 0:
                if(param.getUserIdentical() == null||!NumberUtils.isParsable(param.getUserIdentical())){
                    return Echo.error("参数异常！");
                }
                info = apiService.getUserInfo(NumberUtils.createLong(param.getUserIdentical()));
                break;
            case 1:
                info = apiService.getUserInfo(param.getUserIdentical());
            default:return Echo.error("参数异常！");
        }

        if(CollectionUtils.isNotEmpty(param.getIds())){

        }
        return null;
    }

}
