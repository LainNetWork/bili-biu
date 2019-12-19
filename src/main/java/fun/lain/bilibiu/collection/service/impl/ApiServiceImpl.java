package fun.lain.bilibiu.collection.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import fun.lain.bilibiu.collection.entity.*;
import fun.lain.bilibiu.collection.service.ApiService;
import fun.lain.bilibiu.common.exception.LainException;
import fun.lain.bilibiu.common.var.ApiVar;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("apiService")
public class ApiServiceImpl implements ApiService {
    @Value("${lain.user-agent}")
    public String userAgent;

    @Autowired
    RestTemplate restTemplate;



    private  HttpEntity getRequestEntity(String cookie){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.COOKIE,cookie);
        httpHeaders.set(HttpHeaders.USER_AGENT,userAgent);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE,"application/json; charset=utf-8");
        httpHeaders.set(HttpHeaders.HOST,"api.bilibili.com");
        HttpEntity entity = new HttpEntity(null,httpHeaders);
        return entity;
    }

    @Override
    public BiliUserInfo getUserInfo(String cookie) {
        HttpEntity req = getRequestEntity(cookie);
        ResponseEntity<JSONObject> response = restTemplate.exchange(ApiVar.USER_INFO_BY_COOKIES, HttpMethod.GET,req,JSONObject.class);
        return handleUserInfo(response);
    }

    @Override
    public BiliUserInfo getUserInfo(Long uid) {
        HttpEntity req = getRequestEntity(null);
        ResponseEntity<JSONObject> response = restTemplate.exchange(ApiVar.USER_INFO,HttpMethod.GET,req,JSONObject.class,uid);
        return handleUserInfo(response);
    }


    private BiliUserInfo handleUserInfo(ResponseEntity<JSONObject> response){
        JSONObject data = response.getBody();
        if(response.getStatusCode().value()!=200||data==null||data.getInteger("code")!=0){
            throw new RuntimeException("接口请求异常！");
        }
        ArrayList<Object> userInfo = (ArrayList) JSONPath.eval(data,"$.data['uname','mid','face']");
        return BiliUserInfo.builder()
                .uname((String)userInfo.get(0))
                .mid(NumberUtils.createLong(userInfo.get(1).toString()))
                .face((String)userInfo.get(2))
                .build();
    }

    @Override
    public List<UserCollection> getUserCollections(Long uid) {
        HttpEntity req = getRequestEntity(null);
        ResponseEntity<JSONObject> response = restTemplate.exchange(ApiVar.USER_COLLECT_LIST,HttpMethod.GET,req,JSONObject.class,uid);
        JSONObject data  = response.getBody();
        if(response.getStatusCode().value()!=200||data==null||data.getInteger("code")!=0){
            throw new LainException("接口请求异常！");
        }
        data = data.getJSONObject("data");
        if(data==null){
            throw new LainException("数据为空！");
        }
        JSONArray array = data.getJSONArray("list");
        List<UserCollection> userCollections = array.toJavaList(UserCollection.class);
        return userCollections;
    }

    @Override
    public List<CollectionMedia> getAllMediaInCollection(Long id,String cookie) {
        //一次只能请求20个，因此第一次请求的时候需要获得视频总数
        List<CollectionMedia> reList = new ArrayList<>();
        int count = 1;
        for(int i = 1;i<=Math.ceil(count/20.0);i++){
            HttpEntity req = getRequestEntity(cookie);
            ResponseEntity<JSONObject> response = restTemplate.exchange(ApiVar.MEDIA_COLLECT_MEDIA,HttpMethod.GET,req,JSONObject.class,id,i);
            JSONObject data = response.getBody();
            if(response.getStatusCode().value()!=200||data==null||data.getInteger("code")!=0){
                throw new RuntimeException("接口请求异常！");
            }
            if(i == 1){
                count = (Integer) JSONPath.eval(data,"$.data.info.media_count");
            }
            JSONArray array = (JSONArray) JSONPath.read(data.toJSONString(),"$.data.medias");
            List<CollectionMedia> collectionMedia = array.toJavaList(CollectionMedia.class);
            reList.addAll(collectionMedia);
        }
        getCidInfo(reList,cookie);
        return reList;
    }

//    @Override
//    public List<CollectionMedia> getAllMediaInCollection(Long id) {
//        return getAllMediaInCollection(id,null);
//    }

    @Override
    public CollectionMedia getMediaInfo(Long id,String cookies) {
        CollectionMedia media = new CollectionMedia();
        media.setId(id);
        getCidInfo(media,cookies);
        for(MediaPart part: media.getParts()){
            getDownloadInfo(part,cookies);
        }
        return media;
    }


    private void getCidInfo(List<CollectionMedia> medias,String cookies) {
        //获取av号中的视频分段的cid
        if(CollectionUtils.isNotEmpty(medias)){
            for(CollectionMedia media :medias){
                getCidInfo(media,cookies);
            }
        }
    }

    /**
     * 获取Cid信息
     * @param media
     * @param cookies
     */
    private void getCidInfo(CollectionMedia media,String cookies) {
        HttpEntity req = getRequestEntity(cookies);
        ResponseEntity<JSONObject> cidInfoJson = restTemplate.exchange(ApiVar.MEDIA_PLAY_LIST,HttpMethod.GET,req,JSONObject.class,media.getId());
        //校验
        JSONObject data = cidInfoJson.getBody();
        if(cidInfoJson.getStatusCode().value()!=200||data==null||data.getInteger("code")!=0){
            if(data.getInteger("code")==-404){
                return;
            }
            throw new RuntimeException("接口请求异常！");
        }
        JSONArray array = (JSONArray) JSONPath.read(data.toJSONString(),"$.data");
        List<MediaPart> mediaParts = array.toJavaList(MediaPart.class);
        mediaParts.forEach(part->{
            part.setId(media.getId());
//            getDownloadInfo(part,cookies);
        });
        media.setParts(mediaParts);
    }

    private void getDownloadInfo(MediaPart part, String cookie) {
        if(part.getId()!=null &&part.getCid()!=null){
            HttpEntity req = getRequestEntity(cookie);
            ResponseEntity<JSONObject> res = restTemplate.exchange(ApiVar.MEDIA_DOWNLOAD_INFO,HttpMethod.GET,req,JSONObject.class,part.getId(),part.getCid(),112);
            JSONObject data = res.getBody();
            if(res.getStatusCode().value()!=200||data==null||data.getInteger("code")!=0){
                throw new RuntimeException(" ！");
            }
            MediaPart tempPart = JSONObject.toJavaObject(data.getJSONObject("data"),MediaPart.class);
            part.setAcceptQuality(tempPart.getAcceptQuality());
            part.setDownLoadInfos(tempPart.getDownLoadInfos());
            part.setPartName(tempPart.getPartName());
            part.setQualityLevel(tempPart.getQualityLevel());
            part.setQualityName(tempPart.getQualityName());

        }else{
            //TODO 抛出异常
        }
    }

    @Override
    public MediaPart getDownloadInfo(Long id,Long cid, String cookie) {
        if(id !=null &&cid!=null){
            HttpEntity req = getRequestEntity(cookie);
            ResponseEntity<JSONObject> res = restTemplate.exchange(ApiVar.MEDIA_DOWNLOAD_INFO,HttpMethod.GET,req,JSONObject.class,id,cid,112);
            JSONObject data = res.getBody();
            if(res.getStatusCode().value()!=200||data==null||data.getInteger("code")!=0){
                throw new RuntimeException(" ！");
            }
            MediaPart tempPart = JSONObject.toJavaObject(data.getJSONObject("data"),MediaPart.class);
            return  tempPart;
        }else{
            //TODO 抛出异常
        }
        return null;
    }
}
