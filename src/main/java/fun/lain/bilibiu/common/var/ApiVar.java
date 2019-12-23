package fun.lain.bilibiu.common.var;

public interface ApiVar {
    //获取用户信息
    String USER_INFO= "https://api.bilibili.com/x/space/acc/info?mid={1}&jsonp=jsonp";

    String USER_INFO_BY_COOKIES= "https://api.bilibili.com/x/web-interface/nav";

    //获取用户的收藏夹(参数：用户uid)
    String USER_COLLECT_LIST = "https://api.bilibili.com/medialist/gateway/base/created?pn=1&ps=100&up_mid={1}&is_space=0&jsonp=jsonp";

    String COLLECTION_DETAIL = "http://api.bilibili.com/medialist/gateway/base/spaceDetail?media_id={1}&pn=1&ps=1";

    //可以请求到收藏夹内的视频信息
    String MEDIA_COLLECT_MEDIA = "https://api.bilibili.com/medialist/gateway/base/spaceDetail?media_id={1}&pn={2}&ps=20&order=mtime&type=0&tid=0&jsonp=jsonp";

    //根据av号获取playList
    String MEDIA_PLAY_LIST = "https://api.bilibili.com/x/player/pagelist?aid={1}";

    //根据av号获取详情
    String  MEDIA_INFO = "https://api.bilibili.com/x/web-interface/view?aid={1}";
    //获取下载地址
    String MEDIA_DOWNLOAD_INFO = "https://api.bilibili.com/x/player/playurl?avid={1}&cid={2}&qn={3}&otype=json";
}
