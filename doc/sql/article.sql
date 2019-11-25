CREATE TABLE `article` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userid` bigint(20) unsigned DEFAULT NULL COMMENT '用户id',
  `title` varchar(655) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文章标题',
  `summary` varchar(655) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '摘要',
  `content` mediumtext CHARACTER SET utf8mb4 COMMENT '内容',
  `status` smallint(4) DEFAULT NULL COMMENT '文章状态，为0表示公开，-1表示私密',
  `ctime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='文章表';
