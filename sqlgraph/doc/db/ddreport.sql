/*
Navicat MySQL Data Transfer

Source Server         : 172.20.10.97
Source Server Version : 50714
Source Host           : 172.20.10.97:30062
Source Database       : ddreport

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2020-01-16 16:09:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for db
-- ----------------------------
DROP TABLE IF EXISTS `db`;
CREATE TABLE `db` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id 主键',
  `host` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据库地址',
  `port` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据库端口',
  `user` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号密码',
  `database` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据库',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建此db的用户的id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `job_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务名',
  `job_log` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '任务运行的log',
  `job_exec_month` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务执行日期 月',
  `job_exec_day` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务执行日期 天',
  `job_exec_hour` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务执行日期 小时',
  `job_exec_minute` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务执行日期 分钟',
  `job_exec_second` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任务执行日期 秒',
  `dd_robot` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '钉钉机器人地址',
  `status` smallint(5) DEFAULT NULL COMMENT '任务状态-1-错误终止 0-就绪 1-正常运行 2-暂停 3-异常重试 4-移除',
  `db_id` int(11) DEFAULT NULL COMMENT '数据库id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建此job的用户id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `project_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for job_sql
-- ----------------------------
DROP TABLE IF EXISTS `job_sql`;
CREATE TABLE `job_sql` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `job_id` int(255) DEFAULT NULL COMMENT '对应的job id',
  `job_sql` mediumtext COLLATE utf8mb4_unicode_ci COMMENT '需要执行的sql 语句',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=335 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '项目描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目表';

-- ----------------------------
-- Table structure for r_user_project
-- ----------------------------
DROP TABLE IF EXISTS `r_user_project`;
CREATE TABLE `r_user_project` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `project_id` bigint(255) NOT NULL DEFAULT '1' COMMENT '项目id',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-项目权限管理表';

-- ----------------------------
-- Table structure for scheduler_info
-- ----------------------------
DROP TABLE IF EXISTS `scheduler_info`;
CREATE TABLE `scheduler_info` (
  `host` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '定时任务的ip地址',
  `port` int(6) DEFAULT NULL COMMENT '定时任务的端口号',
  `status` tinyint(4) DEFAULT '0' COMMENT '0-正常 1-出错',
  `alert_times` tinyint(4) DEFAULT '0' COMMENT '告警次数',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`host`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for time_table
-- ----------------------------
DROP TABLE IF EXISTS `time_table`;
CREATE TABLE `time_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_type` date DEFAULT NULL,
  `datetime_type` datetime DEFAULT NULL,
  `timestamp_type` timestamp NULL DEFAULT NULL,
  `time_type` time DEFAULT NULL,
  `year_type` year(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for user_api_token
-- ----------------------------
DROP TABLE IF EXISTS `user_api_token`;
CREATE TABLE `user_api_token` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `token` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'api token 32位',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for user_base
-- ----------------------------
DROP TABLE IF EXISTS `user_base`;
CREATE TABLE `user_base` (
  `id` bigint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `level` tinyint(1) DEFAULT NULL COMMENT '用户等级 0-超级管理员 1-普通管理员 2-用户',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '0-没删 1-删',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for user_token
-- ----------------------------
DROP TABLE IF EXISTS `user_token`;
CREATE TABLE `user_token` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'token',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
