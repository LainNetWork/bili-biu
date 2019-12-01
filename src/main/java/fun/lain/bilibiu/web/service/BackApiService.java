package fun.lain.bilibiu.web.service;

import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.web.entity.SaveTaskParam;

import java.util.List;

public interface BackApiService {
    Echo getUserCollection(String json);
    Echo saveCollection(SaveTaskParam param);

}
