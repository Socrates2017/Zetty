CREATE TABLE `book_index` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent` int(255) unsigned DEFAULT '0' COMMENT '父章节id',
  `url` varchar(255) DEFAULT NULL COMMENT '链接',
  `name` varchar(255) DEFAULT NULL COMMENT '章节名',
  `ctime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `book` bigint(11) unsigned DEFAULT NULL COMMENT '所属书籍id',
  `index_order` float DEFAULT '0' COMMENT '排序',
  `is_leaf` tinyint(4) DEFAULT '0' COMMENT '是否是叶子章节，如果为0则表示下面没有目录',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='章节表';
