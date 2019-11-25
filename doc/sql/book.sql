CREATE TABLE `book` (
  `id` bigint(11) unsigned NOT NULL COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '书名',
  `description` varchar(255) DEFAULT NULL COMMENT '书籍简介',
  `creator` bigint(255) unsigned DEFAULT NULL COMMENT '书籍创建人',
  `ctime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='书籍表';
