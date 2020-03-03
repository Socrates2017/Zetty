/*
 Navicat Premium Data Transfer

 Source Server         : cms_prod@129.28.182.220
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 129.28.182.220:3306
 Source Schema         : cms

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 03/03/2020 21:44:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_table` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`admin_table`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台管理可操作表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userid` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户id',
  `title` varchar(655) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章标题',
  `summary` varchar(655) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `status` smallint(4) NULL DEFAULT NULL COMMENT '文章状态，为0表示公开，-1表示私密',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `utime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 738 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `article` int(255) UNSIGNED NOT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `uuser` bigint(20) UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`article`, `tag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` bigint(11) UNSIGNED NOT NULL COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '书籍简介',
  `creator` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '书籍创建人',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `utime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '书籍表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book_index
-- ----------------------------
DROP TABLE IF EXISTS `book_index`;
CREATE TABLE `book_index`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent` int(255) UNSIGNED NULL DEFAULT 0 COMMENT '父章节id',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '章节名',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `utime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `book` bigint(11) UNSIGNED NULL DEFAULT NULL COMMENT '所属书籍id',
  `index_order` float NULL DEFAULT 0 COMMENT '排序',
  `is_leaf` tinyint(4) NULL DEFAULT 0 COMMENT '是否是叶子章节，如果为0则表示下面没有目录',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 826 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '章节表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for huiyan
-- ----------------------------
DROP TABLE IF EXISTS `huiyan`;
CREATE TABLE `huiyan`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cdate` datetime(0) NULL DEFAULT NULL,
  `username` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(655) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `status` smallint(4) NULL DEFAULT NULL,
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for openid
-- ----------------------------
DROP TABLE IF EXISTS `openid`;
CREATE TABLE `openid`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `num` int(11) NULL DEFAULT 0 COMMENT '数量',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `article_id` int(20) UNSIGNED NULL DEFAULT NULL COMMENT '文章id',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` varchar(16000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `utime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章回复评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `status` smallint(8) NULL DEFAULT 1 COMMENT '账户状态，0表示正常,1表示未激活，其他表示异常',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `utime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_email`(`email`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_ext
-- ----------------------------
DROP TABLE IF EXISTS `user_ext`;
CREATE TABLE `user_ext`  (
  `userid` bigint(20) UNSIGNED NOT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `token_exptime` datetime(0) NULL DEFAULT NULL COMMENT '激活码过期时间',
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 387 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_openid
-- ----------------------------
DROP TABLE IF EXISTS `user_openid`;
CREATE TABLE `user_openid`  (
  `sys_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `userid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `openid` bigint(255) UNSIGNED NULL DEFAULT NULL,
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `uuser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sys_code`, `userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_position_log
-- ----------------------------
DROP TABLE IF EXISTS `user_position_log`;
CREATE TABLE `user_position_log`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `session_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'ip地址',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '用户id，没有登录默认为0',
  `latitude` double(10, 6) NULL DEFAULT NULL COMMENT '十进制的纬度估值。值范围为 [-90.00, +90.00]。',
  `longitude` double(10, 6) NULL DEFAULT NULL COMMENT '十进制的经度固执。值范围为 [-180.00, +180.00]。',
  `altitude` double(255, 0) NULL DEFAULT NULL COMMENT 'WGS-84 球面以上的海拔高度固执，以米为单位计算。',
  `accuracy` double(255, 0) NULL DEFAULT NULL COMMENT '以米为单位的纬度和经度精确估值。',
  `altitudeAccuracy` double(255, 0) NULL DEFAULT NULL COMMENT '以米为单位的海拔高度精确估值。',
  `heading` double(255, 0) NULL DEFAULT NULL COMMENT '相对正北方向设备以顺时针方向运动计算的当前方向。',
  `speed` double(255, 0) NULL DEFAULT NULL COMMENT '米/每秒为单位的设备当前地面速度。',
  `p_timestamp` datetime(0) NULL DEFAULT NULL,
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3028 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for web_log
-- ----------------------------
DROP TABLE IF EXISTS `web_log`;
CREATE TABLE `web_log`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `session_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `duration` bigint(255) NULL DEFAULT NULL,
  `user_id` bigint(20) UNSIGNED NULL DEFAULT 0,
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_agent` varchar(655) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ctime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `utime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
