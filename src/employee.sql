/*
Navicat MySQL Data Transfer

Source Server         : 10.38.128.105
Source Server Version : 50524
Source Host           : 10.38.128.105:3306
Source Database       : basic

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2012-12-03 14:39:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `employee`
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `USERID` varchar(36) NOT NULL DEFAULT '' COMMENT '用户id',
  `USERNAME` varchar(20) DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(32) DEFAULT NULL COMMENT '密码',
  `TELNO` varchar(20) DEFAULT NULL COMMENT '电话',
  `CREATOR` varchar(60) DEFAULT NULL COMMENT '创建人',
  `CREATEDATE` date DEFAULT NULL COMMENT '创建日期',
  `MODIFIER` varchar(60) DEFAULT NULL COMMENT '修改人',
  `MODIFYDATE` date DEFAULT NULL COMMENT '修改日期',
  `REMARK` tinytext COMMENT '备注',
  `SEX` varchar(20) DEFAULT NULL COMMENT '性别',
  `EMAIL` varchar(30) DEFAULT NULL,
  `TYPE` varchar(1) DEFAULT NULL,
  `STATE` char(2) DEFAULT NULL,
  `ADDRESS` varchar(60) DEFAULT NULL,
  `AGE` decimal(8,0) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL,
  `status` char(255) DEFAULT NULL,
  `LOGINNAME` varchar(20) DEFAULT NULL,
  `problem` varchar(100) DEFAULT NULL,
  `answer` varchar(100) DEFAULT NULL,
  `problem2` varchar(100) DEFAULT NULL,
  `answer2` varchar(100) DEFAULT NULL,
  `problem3` varchar(100) DEFAULT NULL,
  `answer3` varchar(100) DEFAULT NULL,
  `JANITOR` varchar(1) DEFAULT NULL,
  `SCRAP` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`USERID`),
  KEY `USER_EMAIL_UNI` (`EMAIL`) USING BTREE,
  KEY `INDEX_EMPLOYEE1` (`USERNAME`) USING BTREE,
  KEY `CREATOR` (`CREATOR`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('7def0489-7e32-4614-bd6d-e43343d5cf63', '向铁沙', 'ff3fe2e028a2f1b5', null, '880da05d-1cf0-4526-a4ca-3f0f0fc751e7', '2012-09-05', '880da05d-1cf0-4526-a4ca-3f0f0fc751e7', '2012-09-05', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `employee` VALUES ('880da05d-1cf0-4526-a4ca-3f0f0fc751e7', 'system', 'ff3fe2e028a2f1b5', '13911773243', null, '2012-09-04', '880da05d-1cf0-4526-a4ca-3f0f0fc751e7', '2012-12-03', null, null, 'tarzon@21cn.com', null, null, null, null, null, '0', 'system', null, null, null, null, null, null, null, null);
