CREATE TABLE if not exists `cache_media_info`  (
     `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增主键，无意义',
     `avId` int(0) NULL COMMENT '视频av号',
     `title` varchar(255) NULL COMMENT '标题',
     PRIMARY KEY (`id`)
);

ALTER TABLE cache_part_task ADD UNIQUE(cid);

UPDATE app_info SET version = 'Ver_0.04';