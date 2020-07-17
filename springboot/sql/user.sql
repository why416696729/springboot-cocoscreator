/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : mygame

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2020-07-17 09:03:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `telephone` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `score` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
