> 由于B站av号机制改变，下载视频的功能受到了影响，由于最近在找工作，暂停维护
### BILI-BIU B站收藏夹视频定时备份工具
一个部署在服务器上的监控BiliBili收藏夹并自动下载备份视频的工具，自带一个webUI
初版长这样：
![Snipaste_2020-04-22_01-55-15.png](https://i.loli.net/2020/04/22/5u9ZDRhF8EszGat.png)

下载页面，带进度条：
![Snipaste_2020-04-22_01-54-48.png](https://i.loli.net/2020/04/22/Xh9f1p7aFYzTukr.png)

使用Java语言，基于SpringBoot框架开发。
#### 功能：

- 定时监控指定收藏夹，并自动下载保存视频（已完成）
- WEB UI(简陋版)
- WEB UI 登录验证(目前还是初版，暂时没加)
- 单视频多线程分片下载（已完成
- 命令行模式（未开发
- 弹幕备份（未开发
- 定时备份上传谷歌云盘（遥远的未来
- 通用化任务，添加P站定时备份收藏夹模块（地球毁灭之时

clone代码后，需要在IDE里安装lombok插件

通过maven打包后执行(或者点[这里(可能不是最新的版本)](https://github.com/LainNetWork/bili-biu/releases/tag/0.0.1-SNAPSHOT)下载打包好的预览版)：

``` shell
java -jar bili-biu-0.0.1-SNAPSHOT.jar
```  

如需指定视频缓存存放路径，启动时加上参数即可

``` shell
java -jar bili-biu-0.0.1-SNAPSHOT.jar --lain.save-path=D:/cache
```

启动后，访问localhost:8080即可进入后台管理。

由于还没开发完，在第一个发布版前不会维护数据库随版本升级的功能= =

请勿用于违法用途，使用本公开源码造成的任何后果均与本人无关




