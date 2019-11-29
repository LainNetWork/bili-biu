package fun.lain.bilibiu.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.service.BackApiService;

public class BackApiServiceImpl implements BackApiService {
    @Override
    public Echo getUserCollection(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json) ;
        Integer type = Integer.parseInt(jsonObject.getString("type"));
        if(type==null){
            return Echo.error("参数错误");
        }
        if(type.equals(0)){//0为uid

        }else if(type.equals(1)){//1为cookie

        }
        return null;
    }
}
