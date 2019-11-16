package fun.lain.bilibiu.collection.service;

import fun.lain.bilibiu.collection.entity.BiliUserInfo;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.UserCollection;

import java.util.List;

/**
 * 调用B站接口，获取数据
 */
public interface ApiService {
    //获取用户数据
    BiliUserInfo getUserInfo(String cookies);
    BiliUserInfo getUserInfo(Long uid);
    //通过用户id，获取用户收藏夹列表
    List<UserCollection> getUserCollections(Long uid);
    //通过收藏夹id，获取收藏夹内所有视频以及下载信息（api限制，一次只能获取20条，因此需根据收藏夹中的视频总数来多次取出）
    List<CollectionMedia> getAllMediaInCollection(Long id,String cookies);
    List<CollectionMedia> getAllMediaInCollection(Long id);
    CollectionMedia getMediaInfo(Long id,String cookies);
//    //根据视频分段信息
//    void getCidInfo(List<CollectionMedia> medias);
//
//    void getCidInfo(CollectionMedia media);
//
//    //获取视频清晰度信息及下载地址（可以传入用户cookie，以获取更高画质）
//    void getDownloadInfo(CollectionMedia media,String cookie);



}
