CREATE TABLE  `save_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，无意义',
  `userId` int(11) NULL DEFAULT NULL COMMENT '用户UID',
  `cookie` varchar(255)   NULL DEFAULT NULL COMMENT '用户cookie',
  `cron` varchar(255)  NULL DEFAULT NULL COMMENT 'cron表达式',
  `status` int(1) NULL DEFAULT NULL COMMENT '任务状态',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
);

CREATE TABLE `save_collect`  (
  `id` int(11) NOT NULL  AUTO_INCREMENT COMMENT '唯一主键',
  `taskId` int(11) NULL DEFAULT NULL COMMENT '任务id',
  `collectionId` int(11) NULL DEFAULT NULL COMMENT '收藏夹id',
  PRIMARY KEY (`id`),
  CONSTRAINT `save_collect_ibfk_1` FOREIGN KEY (`taskId`) REFERENCES `save_task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE `cache_part_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avid` int(11) DEFAULT NULL COMMENT '视频av号',
  `avTitle` varchar(255) DEFAULT NULL COMMENT 'av号标题',
  `cid` int(11) DEFAULT NULL COMMENT '分段号',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `quality` int(255) DEFAULT NULL COMMENT '质量',
  `size` int(11) DEFAULT NULL COMMENT '视频字节数',
  `cacheSize` int(11) DEFAULT NULL COMMENT '已缓存的大小',
  `status` int(1) DEFAULT NULL COMMENT '状态',
  `message` varchar(255) DEFAULT NULL COMMENT '状态信息',
  PRIMARY KEY (`id`)
);

CREATE TABLE `app_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，无意义',
  `initState` int(1) DEFAULT NULL COMMENT '初始化状态',
  `version` varchar(10) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`)
);



INSERT  INTO `app_Info` (`initState`,`version`) VALUES (1,'Ver_0.01');