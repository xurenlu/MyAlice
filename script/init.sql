



create database if not exists myalice ;

use myalice ;

DROP TABLE IF EXISTS `assign_record`;

CREATE TABLE `assign_record` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键',
  `customer_id` varchar(36) DEFAULT NULL COMMENT '客户ID',
  `customer_conn_id` varchar(36) DEFAULT NULL COMMENT '客户连接ID',
  `supporter_id` varchar(36) DEFAULT NULL COMMENT '客服ID',
  `supporter_conn_id` varchar(36) DEFAULT NULL COMMENT '客服连接ID',
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `assign_record` */

/*Table structure for table `authorities` */

DROP TABLE IF EXISTS `authorities`;

CREATE TABLE `authorities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `authority` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `authorities` */

insert  into `authorities`(`id`,`username`,`authority`) values (5,'admin','manager');
insert  into `authorities`(`id`,`username`,`authority`) values (6,'admin','op_createuser');
insert  into `authorities`(`id`,`username`,`authority`) values (7,'admin','admin');

/*Table structure for table `connection_record` */

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

/*Data for the table `connection_record` */

insert  into `connection_record`(`id`,`client_address`,`server_address`,`type`,`status`,`user_id`,`open_time`,`close_time`) values ('31F573E1AC634045B6D063F21C5B55E6','0:0:0:0:0:0:0:1','0:0:0:0:0:0:0:1','1','1',NULL,'2017-05-08 16:37:54','2017-05-08 16:38:36');
insert  into `connection_record`(`id`,`client_address`,`server_address`,`type`,`status`,`user_id`,`open_time`,`close_time`) values ('4433CA76213AE83DD9D6C6EAAC3B2D18','0:0:0:0:0:0:0:1','0:0:0:0:0:0:0:1','1','0',NULL,'2017-05-08 16:38:38',NULL);
insert  into `connection_record`(`id`,`client_address`,`server_address`,`type`,`status`,`user_id`,`open_time`,`close_time`) values ('50A83586998A30FBD9332D0FE8B9A0BC','0:0:0:0:0:0:0:1','0:0:0:0:0:0:0:1','1','1',NULL,'2017-05-08 16:37:55','2017-05-08 16:39:10');

/*Table structure for table `evaluation` */

DROP TABLE IF EXISTS `evaluation`;

CREATE TABLE `evaluation` (
  `id` varchar(36) DEFAULT NULL COMMENT '主键',
  `customer_id` varchar(36) DEFAULT NULL COMMENT '客户ID',
  `customer_conn_id` varchar(36) DEFAULT NULL COMMENT '客户连接ID',
  `score` int(1) DEFAULT NULL COMMENT '分数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `evaluation` */

/*Table structure for table `persistent_logins` */

DROP TABLE IF EXISTS `persistent_logins`;

CREATE TABLE `persistent_logins` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(50) DEFAULT '' COMMENT '用户名',
  `series` varchar(50) DEFAULT '' COMMENT 'series',
  `token` varchar(64) DEFAULT '' COMMENT 'tokenValue',
  `last_used` datetime DEFAULT NULL COMMENT 'last_used',
  KEY `id` (`id`),
  KEY `series` (`series`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `persistent_logins` */

insert  into `persistent_logins`(`id`,`username`,`series`,`token`,`last_used`) values (2,'admin2','Xq7MNv2JWg/1/t4Q2irznw==','NXjcqBVYEHfNQoQmy3INtw==','2017-03-22 13:59:26');
insert  into `persistent_logins`(`id`,`username`,`series`,`token`,`last_used`) values (3,'admin2','W5mmwWakwEYTffUVFHfTKg==','UuXpZ4xzILO/VSaq+pH28w==','2017-03-22 14:04:51');
insert  into `persistent_logins`(`id`,`username`,`series`,`token`,`last_used`) values (4,'admin2','qB1Tkyy9WVo4AcRLo5bnmQ==','d5dCjQ+Wj3r2A8FUuWOXyA==','2017-03-22 14:10:00');

/*Table structure for table `question_order` */

DROP TABLE IF EXISTS `question_order`;

CREATE TABLE `question_order` (
  `id` varchar(32) NOT NULL COMMENT '主键（UUID）',
  `create_time` datetime DEFAULT NULL COMMENT '创建日起',
  `create_user` varchar(32) DEFAULT NULL COMMENT '提交人',
  `solved_time` datetime DEFAULT NULL COMMENT '工单处理时间',
  `state` tinyint(2) DEFAULT NULL COMMENT '0:已创建；1:待处理；2：处理中；3：已处理；4：已解决；',
  `question_type` tinyint(2) DEFAULT NULL COMMENT '0:通用类型;1:其他类型',
  `question_summary` varchar(128) DEFAULT NULL COMMENT '问题概述',
  `question_content` varchar(2000) DEFAULT NULL COMMENT '问题内容',
  `email` varchar(64) DEFAULT NULL COMMENT '联系邮箱账号',
  `accept` varchar(32) DEFAULT NULL COMMENT '受理人',
  PRIMARY KEY (`id`),
  KEY `create_user` (`create_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单表';

/*Data for the table `question_order` */

insert  into `question_order`(`id`,`create_time`,`create_user`,`solved_time`,`state`,`question_type`,`question_summary`,`question_content`,`email`,`accept`) values ('ff31496635b54d65aed5e6824862ce96','2017-05-09 09:42:29','hf',NULL,2,1,'','ces ','ca',NULL);

/*Table structure for table `question_order_attachment` */

DROP TABLE IF EXISTS `question_order_attachment`;

CREATE TABLE `question_order_attachment` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `question_order_id` varchar(32) DEFAULT NULL COMMENT '问题工单ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态 0:已创建；1：已删除',
  `url` varchar(256) DEFAULT NULL COMMENT '工单地址',
  PRIMARY KEY (`id`),
  KEY `question_order_id` (`question_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单附件';

/*Data for the table `question_order_attachment` */

insert  into `question_order_attachment`(`id`,`question_order_id`,`create_time`,`status`,`url`) values ('60274aa638d648a892a6894d2598a1c1','ff31496635b54d65aed5e6824862ce96','2017-05-09 09:42:30',1,'201705/f695c71100774a40a04029a685b70be1.jpg');
insert  into `question_order_attachment`(`id`,`question_order_id`,`create_time`,`status`,`url`) values ('6d526fb65e774ee091414a7e3348f151','ff31496635b54d65aed5e6824862ce96','2017-05-09 09:42:30',1,'201705/94d2e05caf70444c985195199b584450.jpg');
insert  into `question_order_attachment`(`id`,`question_order_id`,`create_time`,`status`,`url`) values ('bcbe829059274181879033a7571fbd9c','ff31496635b54d65aed5e6824862ce96','2017-05-09 09:42:30',1,'201705/a5b95df1add74e3ab26ecbe6cb8b41c6.jpg');

/*Table structure for table `question_record` */

DROP TABLE IF EXISTS `question_record`;

CREATE TABLE `question_record` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `content` varchar(1000) DEFAULT NULL COMMENT '交谈内容',
  `commit_user` varchar(32) DEFAULT NULL COMMENT '提交人',
  `userType` tinyint(2) DEFAULT NULL COMMENT '0 用户 1管理员',
  `question_order_id` varchar(32) DEFAULT NULL COMMENT '工单id',
  PRIMARY KEY (`id`),
  KEY `commit_user` (`commit_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工单' ;    

/*Data for the table `question_record` */

insert  into `question_record`(`id`,`create_time`,`content`,`commit_user`,`userType`,`question_order_id`) values ('06cffd26cc4f4b618e26f894f270ac21','2017-05-09 09:54:26','fdsa','hf',0,'ff31496635b54d65aed5e6824862ce96');

/*Table structure for table `sys_dictionaries` */

DROP TABLE IF EXISTS `sys_dictionaries`;

CREATE TABLE `sys_dictionaries` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `tname` varchar(50) DEFAULT '' COMMENT '类型',
  `createTime` datetime DEFAULT NULL COMMENT '创建日期',
  `dtype` varchar(15) DEFAULT NULL COMMENT '字典Key',
  `tindex` int(11) DEFAULT NULL COMMENT '索引',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dtype` (`dtype`,`tindex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_dictionaries` */

insert  into `sys_dictionaries`(`id`,`tname`,`createTime`,`dtype`,`tindex`) values ('1','咨询类','2017-04-24 15:29:48','orderType',1);
insert  into `sys_dictionaries`(`id`,`tname`,`createTime`,`dtype`,`tindex`) values ('2','其他类','2017-04-24 15:31:33','orderType',2);
insert  into `sys_dictionaries`(`id`,`tname`,`createTime`,`dtype`,`tindex`) values ('3','待受理','2017-04-24 16:17:51','orderState',1);
insert  into `sys_dictionaries`(`id`,`tname`,`createTime`,`dtype`,`tindex`) values ('4','已受理','2017-04-24 16:15:34','orderState',2);
insert  into `sys_dictionaries`(`id`,`tname`,`createTime`,`dtype`,`tindex`) values ('5','已确认','2017-04-24 16:17:29','orderState',3);

/*Table structure for table `talk_record` */

DROP TABLE IF EXISTS `talk_record`;

CREATE TABLE `talk_record` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `content` varchar(500) NOT NULL COMMENT '提问内容',
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `user_type` char(255) NOT NULL COMMENT '用户类型（0：客户，1：客服）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `connection_id` varchar(36) NOT NULL COMMENT '连接ID',
  `reply` varchar(500) DEFAULT NULL COMMENT '回复内容',
  `replyType` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='沟通记录' ; 

/*Data for the table `talk_record` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` varchar(32) NOT NULL,
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '登录密码',
  `name` varchar(32) DEFAULT NULL COMMENT '昵称',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `mobile_phone` varchar(16) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `user_type` char(255) DEFAULT '0' COMMENT '用户类型',
  `portrait_url` varchar(255) DEFAULT '' COMMENT '图片url',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `users`(`id`,`username`,`password`,`name`,`email`,`mobile_phone`,`create_time`,`remarks`,`user_type`,`portrait_url`,`enabled`) values ('f985db77928b412e9f307547f0b1df56','admin','admin','admin1','admin1','','2017-05-05 14:47:24','','1','',1);


use mysql ;
delete from user where host != '127.0.0.1' ; 
update user set password=password('123456'),host='%' ; 
flush privileges ;
