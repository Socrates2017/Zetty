CREATE TABLE `reply` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `article_id` int(20) unsigned DEFAULT NULL COMMENT '文章id',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `content` varchar(16000) DEFAULT NULL COMMENT '内容',
  `ctime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`) USING BTREE
) ENGINE=InnoDB  COMMENT='文章回复评论表';
