/*
Navicat MySQL Data Transfer

Source Server         : mysql-localhost
Source Server Version : 50629
Source Host           : 127.0.0.1:3306
Source Database       : myalice

Target Server Type    : MYSQL
Target Server Version : 50629
File Encoding         : 65001

Date: 2017-04-17 10:33:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for assign_record
-- ----------------------------
DROP TABLE IF EXISTS `assign_record`;
CREATE TABLE `assign_record` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键',
  `customer_id` varchar(36) DEFAULT NULL COMMENT '客户ID',
  `customer_conn_id` varchar(36) DEFAULT NULL COMMENT '客户连接ID',
  `supporter_id` varchar(36) DEFAULT NULL COMMENT '客服ID',
  `supporter_conn_id` varchar(36) DEFAULT NULL COMMENT '客服连接ID',
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for connection_record
-- ----------------------------
DROP TABLE IF EXISTS `connection_record`;
CREATE TABLE `connection_record` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `client_address` varchar(128) NOT NULL COMMENT '客户端地址',
  `server_address` varchar(128) NOT NULL COMMENT '服务端地址',
  `type` char(255) NOT NULL COMMENT '连接类型(0:客户连接，1:客服连接)',
  `status` char(255) NOT NULL COMMENT '连接状态(0:连接中，1:已断开)',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户id',
  `open_time` datetime NOT NULL COMMENT '打开时间',
  `close_time` datetime DEFAULT NULL COMMENT '关闭时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for evaluation
-- ----------------------------
DROP TABLE IF EXISTS `evaluation`;
CREATE TABLE `evaluation` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键',
  `customer_id` varchar(36) DEFAULT NULL COMMENT '客户ID',
  `customer_conn_id` varchar(36) DEFAULT NULL COMMENT '客户连接ID',
  `score` int(1) DEFAULT NULL COMMENT '分数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for talk_record
-- ----------------------------
DROP TABLE IF EXISTS `talk_record`;
CREATE TABLE `talk_record` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `content` varchar(255) NOT NULL COMMENT '聊天内容',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `user_type` char(255) NOT NULL COMMENT '用户类型（0：客户，1：客服）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `connection_id` varchar(36) NOT NULL COMMENT '连接ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(36) NOT NULL,
  `user_name` varchar(32) NOT NULL,
  `password` varchar(64) NOT NULL,
  `name` varchar(32) NOT NULL,
  `email` varchar(64) NOT NULL,
  `mobile_phone` varchar(16) NOT NULL,
  `create_time` datetime NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `user_type` char(255) NOT NULL,
  `portrait_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
