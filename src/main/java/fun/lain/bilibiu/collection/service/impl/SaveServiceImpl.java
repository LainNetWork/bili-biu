package fun.lain.bilibiu.collection.service.impl;

import fun.lain.bilibiu.collection.service.SaveService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("saveService")
@Scope("prototype")
public class SaveServiceImpl implements SaveService {

    @Value("${lain.save-path}")
    private String path;


    @Override
    public void saveToLocal(String url, Long taskId) {

    }
}
