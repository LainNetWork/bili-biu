package fun.lain.bilibiu.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.lain.bilibiu.collection.entity.BiliUserInfo;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.Echo;
import fun.lain.bilibiu.common.exception.LainException;
import fun.lain.bilibiu.web.entity.SaveCollection;
import fun.lain.bilibiu.web.entity.SaveTask;
import fun.lain.bilibiu.web.entity.SaveTaskParam;
import fun.lain.bilibiu.web.entity.dto.SaveTaskDTO;
import fun.lain.bilibiu.web.mapper.SaveCollectionMapper;
import fun.lain.bilibiu.web.mapper.SaveTaskMapper;
import fun.lain.bilibiu.web.service.BackApiService;
import fun.lain.bilibiu.web.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("backApiService")
@Slf4j
public class BackApiServiceImpl implements BackApiService {

    @Resource
    private ApiService apiService;

    @Autowired
    ScheduleService scheduleService;

    @Resource
    SaveTaskMapper saveTaskMapper;
    @Resource
    SaveCollectionMapper saveCollectionMapper;
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
            if(StringUtils.isBlank(cookie)){
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
    @Transactional
    public Echo saveCollection(SaveTaskParam param) {
        //校验Cron表达式
        if(!CronSequenceGenerator.isValidExpression(param.getCron())){
            return Echo.error("不合法的Cron表达式！");
        }
//        //TODO 判断周期，不得小于限制时间
//        CronSequenceGenerator generator = new CronSequenceGenerator("0/30 * * * * ?");
//        Date date = new Date();
//        Date date1 = generator.next(date);
//        Date date2 = generator.next(date1);
//
//        if((date2.getTime() - date1.getTime())<5*60*1000){//周期不得小于5min
//            return Echo.error("时间周期的最小间隔不得小于5分钟！");
//        }
        BiliUserInfo info = null;
        String cookie = "";
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
                cookie = param.getUserIdentical();
            default:return Echo.error("参数异常！");
        }

        //保存任务
        SaveTask task = SaveTask.builder()
                .cookie(cookie)
                .cron(param.getCron())
                .userId(info.getMid())
                //获取头像
                .status(SaveTask.Status.CREATE)
                .build();
        saveTaskMapper.insert(task);
        if(CollectionUtils.isNotEmpty(param.getIds())){
            for(Long id:param.getIds()){
                SaveCollection saveCollection = SaveCollection.builder()
                        .taskId(task.getId())
                        .collectionId(id)
                        .build();
                saveCollectionMapper.insert(saveCollection);
            }
        }

        return Echo.success("保存成功！");
    }

    @Override
    public Echo getTaskList(Integer index,Integer size) {
        Page<SaveTask> page = new Page<>(index,size);
        IPage<SaveTask> list = saveTaskMapper.selectPage(page,new QueryWrapper<SaveTask>());
        List<SaveTaskDTO> dtoList = new ArrayList<>();
        list.getRecords().forEach(task ->{
            SaveTaskDTO dto = SaveTaskDTO.builder()
                    .cron(task.getCron())
                    .id(task.getId())
                    .uid(task.getUserId())
                    .status(getStatus(task.getStatus()))
                    .statusCode(task.getStatus())
                    .build();
            dtoList.add(dto);

        });
        Map reMap = new HashMap();
        reMap.put("data",dtoList);
        reMap.put("size",list.getSize());
        reMap.put("total",list.getTotal());
        reMap.put("now",list.getCurrent());
        return Echo.success().data(reMap);
    }

    @Override
    public void start(Long taskId) {
        scheduleService.start(taskId);
    }

    @Override
    public void pause(Long taskId) {
        scheduleService.pause(taskId);
    }

    @Override
    public void delete(Long taskId) {
        scheduleService.delete(taskId);
    }

    private String getStatus(int code){
        switch (code){
            case 0 : return "创建";
            case 1 : return "运行";
            case 2 : return "暂停";
            case 3 : return "异常";
        }
        return "状态异常";
    }
}
