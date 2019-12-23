package fun.lain.bilibiu.collection.service;

import fun.lain.bilibiu.collection.entity.BiliUserInfo;
import fun.lain.bilibiu.collection.entity.CollectionMedia;
import fun.lain.bilibiu.collection.entity.MediaPart;
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
    //通过收藏夹id，获取收藏夹内所有视频信息（api限制，一次只能获取20条，因此需根据收藏夹中的视频总数来多次取出）
    List<CollectionMedia> getAllMediaInCollection(Long id,String cookies);

    /**
     * 根据AV号获取视频信息
     * @param id
     * @param cookies
     * @return
     */
    CollectionMedia getMediaInfo(Long id,String cookies);

    /**
     * 获取下载信息
     * @param id
     * @param cid
     * @param cookie
     * @return
     */
    MediaPart getDownloadInfo(Long id, Long cid, String cookie);

    String getTitleByCollectionId(Long collectionId);

}
