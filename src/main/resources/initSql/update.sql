alter table SAVE_COLLECT  add column `collectionName` varchar(255) NOT NULL DEFAULT '' COMMENT '收藏夹名字' after `collectionId`;
UPDATE app_info SET version = 'Ver_0.02';