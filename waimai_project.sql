/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50610
Source Host           : localhost:3306
Source Database       : waimai_project

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2021-09-12 17:52:03
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `address`
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `a_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `tel` varchar(255) NOT NULL,
  `area_code` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `province` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `county` varchar(255) NOT NULL,
  `u_id` int(11) NOT NULL,
  PRIMARY KEY (`a_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES ('1', 'lky1', '12345612345', '130105', '河北省石家庄市新华区文三路 138 号东方通信大厦 7 楼 501 室', '河北省', '石家庄市', '新华区', '1');
INSERT INTO `address` VALUES ('2', 'lky2', '12312312312', '110101', '北京市东城区666号', '北京市', '北京市', '东城区', '1');
INSERT INTO `address` VALUES ('3', 'lky3', '12312313212', '110101', '北京市东城区东城大道666号', '北京市', '北京市', '东城区', '1');
INSERT INTO `address` VALUES ('4', 'lky4', '12312313212', '140214', '山西省大同市云冈区云岗大道666号', '山西省', '大同市', '云冈区', '1');
INSERT INTO `address` VALUES ('5', 'lky5', '12312313212', '110101', '北京市东城区123', '北京市', '北京市', '东城区', '1');
INSERT INTO `address` VALUES ('6', 'lky6', '12312312312', '110102', '北京市西城区阳光大道666号', '北京市', '北京市', '西城区', '1');
INSERT INTO `address` VALUES ('7', 'lky666', '12312312312', '140105', '山西省小店区幸福大道666号', '山西省', '太原市', '小店区', '1');
INSERT INTO `address` VALUES ('8', 'lky777', '12312312312', '110101', '北京市东城区快乐大道777号', '北京市', '北京市', '东城区', '1');
INSERT INTO `address` VALUES ('9', 'lky333', '12312312312', '130102', '河北省长安区阳光大道333号', '河北省', '石家庄市', '长安区', '1');
INSERT INTO `address` VALUES ('10', 'lky111', '12312313212', '120102', '天津市河东区阳关大道111号', '天津市', '天津市', '河东区', '1');
INSERT INTO `address` VALUES ('11', 'lky000', '12312313212', '110101', '北京市东城区阳关大道000号', '北京市', '北京市', '东城区', '1');
INSERT INTO `address` VALUES ('12', '小黄鸭', '15179542036', '360902', '江西省袁州区幸福街666号', '江西省', '宜春市', '袁州区', '6');

-- ----------------------------
-- Table structure for `category`
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  `s_id` int(11) NOT NULL,
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `categoryName` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', '招牌炸鸡', '1');
INSERT INTO `category` VALUES ('2', '主食 卷/堡', '1');
INSERT INTO `category` VALUES ('3', '欢乐时光', '1');
INSERT INTO `category` VALUES ('4', '单品汉堡', '2');
INSERT INTO `category` VALUES ('5', '甜品小吃', '2');
INSERT INTO `category` VALUES ('6', '饮品系列', '2');
INSERT INTO `category` VALUES ('7', '现磨咖啡', '2');
INSERT INTO `category` VALUES ('8', '饮品', '1');
INSERT INTO `category` VALUES ('9', '特色主食', '3');
INSERT INTO `category` VALUES ('10', '套餐', '3');
INSERT INTO `category` VALUES ('11', '美味小食', '3');
INSERT INTO `category` VALUES ('12', '精选配餐', '3');
INSERT INTO `category` VALUES ('13', '甜点与饮料', '3');
INSERT INTO `category` VALUES ('14', '冰淇淋与茶', '4');
INSERT INTO `category` VALUES ('15', '奶茶&特饮', '4');
INSERT INTO `category` VALUES ('16', '奶盖&原味茶', '4');
INSERT INTO `category` VALUES ('17', '真果茶系列', '4');

-- ----------------------------
-- Table structure for `delivery`
-- ----------------------------
DROP TABLE IF EXISTS `delivery`;
CREATE TABLE `delivery` (
  `d_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `total_num` int(11) NOT NULL DEFAULT '0',
  `number` varchar(255) NOT NULL,
  `stat` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`d_id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of delivery
-- ----------------------------
INSERT INTO `delivery` VALUES ('1', 'lky6', '15179510163', '6', '123123123', '1');

-- ----------------------------
-- Table structure for `good`
-- ----------------------------
DROP TABLE IF EXISTS `good`;
CREATE TABLE `good` (
  `g_id` int(11) NOT NULL AUTO_INCREMENT,
  `good_name` varchar(255) NOT NULL,
  `good_pic` varchar(255) DEFAULT NULL,
  `price` varchar(255) NOT NULL,
  `c_id` int(255) NOT NULL,
  `sales` int(11) NOT NULL DEFAULT '0',
  `s_id` int(11) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`g_id`),
  UNIQUE KEY `goodName` (`good_name`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of good
-- ----------------------------
INSERT INTO `good` VALUES ('1', '双层鳕鱼堡', '/upload/华莱士-双层鳕鱼堡_20210901171704.jpg', '8', '2', '3', '1', '华莱士-双层鳕鱼堡');
INSERT INTO `good` VALUES ('2', '香辣鸡腿堡', '/upload/华莱士-香辣鸡腿堡_20210901171554.jpg', '8', '2', '1', '1', '香辣、香脆汉堡');
INSERT INTO `good` VALUES ('3', '牛肉汉堡', '/upload/华莱士-牛肉堡_20210901171624.jpg', '10', '2', '0', '1', '丰富多汁牛肉汉堡');
INSERT INTO `good` VALUES ('4', '香辣鸡翅', '/upload/华莱士-香辣鸡翅_20210901171744.jpg', '8', '3', '3', '1', '华莱士-香辣鸡翅');
INSERT INTO `good` VALUES ('5', '双层红云热辣堡', '/upload/华莱士-双层红云热辣堡_20210901171830.jpg', '10', '2', '0', '1', '华莱士-双层红云热辣堡');
INSERT INTO `good` VALUES ('6', '烤翅', '/upload/华莱士-烤翅_20210901171917.jpg', '8', '3', '2', '1', '华莱士-烤翅');
INSERT INTO `good` VALUES ('7', '薯条', '/upload/华莱士-薯条_20210901171946.jpg', '12', '3', '0', '1', '香脆薯条');
INSERT INTO `good` VALUES ('8', '芝芝安格斯', '/upload/芝芝安格斯_20210901144807.png', '32', '4', '1', '2', '重度芝士爱好者');
INSERT INTO `good` VALUES ('9', '大嘴安格斯', '/upload/大嘴安格斯_20210901145424.png', '40', '4', '2', '2', '满满肉感');
INSERT INTO `good` VALUES ('10', '安格斯厚牛堡', '/upload/安格斯厚牛堡-原味-天椒_20210901145603.png', '38', '4', '0', '2', '独家秘制');
INSERT INTO `good` VALUES ('11', '狠霸王鸡堡', '/upload/狠霸王鸡堡_20210901145652.png', '26', '4', '0', '2', '霸气鸡堡');
INSERT INTO `good` VALUES ('12', '狠霸王牛堡', '/upload/狠霸王牛堡_20210901145759.png', '26', '4', '0', '2', '霸气牛堡');
INSERT INTO `good` VALUES ('13', '狠霸王鱼堡', '/upload/狠霸王鱼堡_20210901145837.png', '26', '4', '0', '2', '霸气鱼堡');
INSERT INTO `good` VALUES ('14', '薯霸王(中)', '/upload/薯霸王_20210901145951.png', '14', '5', '0', '2', '挑战3/8黄金切割');
INSERT INTO `good` VALUES ('15', '王道川蜀鸡翅', '/upload/王道川蜀鸡翅_20210901150110.png', '13.5', '5', '0', '2', '椒麻鲜香');
INSERT INTO `good` VALUES ('16', '王道椒香鸡腿', '/upload/王道椒香鸡腿_20210901150258.png', '13.5', '5', '0', '2', '香味四溢');
INSERT INTO `good` VALUES ('17', '可口可乐', '/upload/可口可乐_20210901150358.png', '11', '6', '1', '2', '配直饮杯盖');
INSERT INTO `good` VALUES ('18', '柠檬风味红茶', '/upload/柠檬风味红茶_20210901150438.png', '12', '6', '1', '2', '柠檬风味红茶');
INSERT INTO `good` VALUES ('19', '卡布奇诺 (热)', '/upload/卡布奇诺 (热)_20210901150538.png', '19', '7', '1', '2', '甄选咖啡');
INSERT INTO `good` VALUES ('20', '美式 (热/冰)', '/upload/美式_20210901150611.png', '17', '7', '0', '2', '甄选咖啡');
INSERT INTO `good` VALUES ('21', '牛奶', '/upload/牛奶_20210901172142.jpg', '5', '8', '2', '1', '鲜香牛奶');
INSERT INTO `good` VALUES ('22', '密汁手扒鸡', '/upload/华莱士-密汁手扒鸡_20210905134524.jpg', '22', '1', '0', '1', '华莱士-密汁手扒鸡');
INSERT INTO `good` VALUES ('23', '辣辣辣条半鸡', '/upload/华莱士-辣辣辣条半鸡_20210905134603.jpg', '22', '1', '0', '1', '华莱士-辣辣辣条半鸡');
INSERT INTO `good` VALUES ('24', '脆鸡八分堡', '/upload/kfc-脆鸡八分堡_20210905135706.jpg', '12', '9', '0', '3', 'kfc-脆鸡八分堡');
INSERT INTO `good` VALUES ('25', '劲脆鸡腿堡', '/upload/kfc-劲脆鸡腿堡_20210905135730.jpg', '12', '9', '0', '3', 'kfc-劲脆鸡腿堡');
INSERT INTO `good` VALUES ('29', 'kfc-香辣鸡腿堡', '/upload/kfc-香辣鸡腿堡_20210905140138.jpg', '12', '9', '0', '3', 'kfc-香辣鸡腿堡');
INSERT INTO `good` VALUES ('30', 'kfc-香辣鸡翅', '/upload/kfc-香辣鸡翅_20210905140205.jpg', '8', '11', '0', '3', 'kfc-香辣鸡翅');
INSERT INTO `good` VALUES ('31', 'kfc-吮指原味鸡', '/upload/kfc-吮指原味鸡_20210905140231.jpg', '8', '11', '0', '3', 'kfc-吮指原味鸡');
INSERT INTO `good` VALUES ('32', 'kfc-芙蓉荟萃汤', '/upload/kfc-芙蓉荟萃汤_20210905140310.jpg', '15', '12', '0', '3', 'kfc-芙蓉荟萃汤');
INSERT INTO `good` VALUES ('33', 'kfc-菌菇四宝汤', '/upload/kfc-菌菇四宝汤_20210905140342.jpg', '15', '12', '0', '3', 'kfc-菌菇四宝汤');
INSERT INTO `good` VALUES ('34', 'kfc-香甜玉米棒', '/upload/kfc-香甜玉米棒_20210905140357.jpg', '5', '12', '0', '3', 'kfc-香甜玉米棒');
INSERT INTO `good` VALUES ('35', 'kfc-奥利奥花淇淋', '/upload/kfc-奥利奥花淇淋_20210905140431.jpg', '12', '13', '0', '3', 'kfc-奥利奥花淇淋');
INSERT INTO `good` VALUES ('36', 'kfc-葡式蛋挞(经典)', '/upload/kfc-葡式蛋挞(经典)_20210905140531.jpg', '5', '13', '0', '3', 'kfc-葡式蛋挞(经');
INSERT INTO `good` VALUES ('37', 'kfc-雪碧', '/upload/雪碧_20210905140918.png', '8', '13', '0', '3', 'kfc-雪碧');
INSERT INTO `good` VALUES ('38', 'kfc-脆鸡八分堡套餐', '/upload/kfc-脆鸡八分堡套餐_20210905170416.jpg', '25', '10', '0', '3', 'kfc-脆鸡八分堡套');
INSERT INTO `good` VALUES ('39', 'kfc-劲辣鸡腿堡套餐', '/upload/kfc-劲辣鸡腿堡套餐_20210905170434.jpg', '25', '10', '0', '3', 'kfc-劲辣鸡腿堡套');
INSERT INTO `good` VALUES ('40', '奥利奥味雪王大圣代', '/upload/蜜雪冰城-奥利奥味雪王大圣代_20210905193147.jpg', '6', '14', '1', '4', '蜜雪冰城-奥利奥味雪');
INSERT INTO `good` VALUES ('41', '草莓味雪王大圣代', '/upload/蜜雪冰城-草莓味雪王大圣代_20210905193404.jpg', '6', '14', '0', '4', '蜜雪冰城-草莓味雪王');
INSERT INTO `good` VALUES ('42', '蓝莓味雪王大圣代', '/upload/蓝莓味雪王大圣代_20210905193433.jpg', '6', '14', '0', '4', '蓝莓味雪王大圣代');
INSERT INTO `good` VALUES ('43', '棒打鲜橙', '/upload/蜜雪冰城-棒打鲜橙_20210905193506.jpg', '7', '17', '1', '4', '蜜雪冰城-棒打鲜橙');
INSERT INTO `good` VALUES ('44', '冰淇淋红茶', '/upload/蜜雪冰城-冰淇淋红茶_20210905193556.jpg', '6', '14', '0', '4', '蜜雪冰城-冰淇淋红茶');
INSERT INTO `good` VALUES ('45', '红豆奶茶', '/upload/蜜雪冰城-红豆奶茶_20210905193634.jpg', '7', '15', '0', '4', '蜜雪冰城-红豆奶茶');
INSERT INTO `good` VALUES ('46', '冰鲜柠檬水', '/upload/蜜雪冰城-冰鲜柠檬水_20210905193711.jpg', '5', '17', '1', '4', '蜜雪冰城-冰鲜柠檬水');
INSERT INTO `good` VALUES ('47', '吃土摇摇奶昔', '/upload/蜜雪冰城-吃土摇摇奶昔_20210905193738.jpg', '7', '14', '0', '4', '蜜雪冰城-吃土摇摇奶');
INSERT INTO `good` VALUES ('48', '黑糖珍珠奶茶', '/upload/蜜雪冰城-黑糖珍珠奶茶_20210905193802.jpg', '7', '15', '0', '4', '蜜雪冰城-黑糖珍珠奶');
INSERT INTO `good` VALUES ('49', '红茶', '/upload/蜜雪冰城-红茶_20210905193912.jpg', '6', '16', '1', '4', '蜜雪冰城-红茶');
INSERT INTO `good` VALUES ('50', '蓝莓摇摇奶昔', '/upload/蜜雪冰城-蓝莓摇摇奶昔_20210905193951.jpg', '7', '14', '0', '4', '蜜雪冰城-蓝莓摇摇奶');
INSERT INTO `good` VALUES ('51', '马来西亚白咖啡', '/upload/蜜雪冰城-马来西亚白咖啡_20210905194051.jpg', '7', '16', '0', '4', '马来西亚白咖啡');

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `o_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_Number` varchar(255) NOT NULL,
  `total_price` decimal(11,2) NOT NULL,
  `u_id` int(11) NOT NULL,
  `s_id` int(11) NOT NULL,
  `a_id` int(11) NOT NULL,
  `create_time` varchar(255) NOT NULL COMMENT '下单时间',
  `order_Stat` int(255) NOT NULL DEFAULT '1',
  `good_total_price` decimal(10,2) NOT NULL,
  `delivery_price` varchar(255) NOT NULL,
  `d_id` int(11) DEFAULT NULL,
  `order_time` varchar(255) DEFAULT NULL COMMENT '接单时间',
  `pick_time` varchar(255) DEFAULT NULL COMMENT '取货时间',
  `arrive_time` varchar(255) DEFAULT NULL COMMENT '送达时间',
  PRIMARY KEY (`o_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('1', 'DM202109031528564240001', '56.00', '1', '2', '1', '2021-09-03 15:28:56', '4', '51.00', '5', '1', '2021-09-03 15:30:39', '2021-09-03 15:31:02', '2021-09-03 15:33:08');
INSERT INTO `order` VALUES ('2', 'DM202109031610031250001', '34.00', '1', '1', '2', '2021-09-03 16:10:03', '4', '29.00', '5', '1', '2021-09-03 16:10:42', '2021-09-03 16:12:10', '2021-09-05 20:04:12');
INSERT INTO `order` VALUES ('3', 'DM202109031611239150001', '49.00', '1', '2', '3', '2021-09-03 16:11:23', '3', '44.00', '5', '1', '2021-09-03 16:11:45', '2021-09-03 16:24:54', null);
INSERT INTO `order` VALUES ('4', 'DM202109031638029290001', '62.00', '1', '2', '5', '2021-09-03 16:38:02', '3', '57.00', '5', '1', '2021-09-03 16:38:07', '2021-09-03 16:38:15', null);
INSERT INTO `order` VALUES ('10', 'DM202109052111398490006', '28.50', '6', '4', '12', '2021-09-05 21:11:39', '4', '24.00', '4.5', '1', '2021-09-05 21:12:06', '2021-09-05 21:12:31', '2021-09-05 21:12:54');
INSERT INTO `order` VALUES ('11', 'DM202109052114239670006', '113.00', '6', '2', '12', '2021-09-05 21:14:23', '4', '103.00', '10', '1', '2021-09-05 21:15:00', '2021-09-05 21:15:07', '2021-09-05 21:15:11');
INSERT INTO `order` VALUES ('12', 'DM202109060910560470006', '25.00', '6', '1', '12', '2021-09-06 09:10:56', '1', '21.00', '4', null, null, null, null);
INSERT INTO `order` VALUES ('13', 'DM202109061115411000006', '28.00', '6', '1', '12', '2021-09-06 11:15:41', '4', '24.00', '4', '1', '2021-09-06 11:17:13', '2021-09-06 11:18:06', '2021-09-06 11:18:36');
INSERT INTO `order` VALUES ('14', 'DM202109092214266660006', '33.00', '6', '1', '12', '2021-09-09 22:14:26', '4', '29.00', '4', '1', '2021-09-09 22:15:03', '2021-09-09 22:15:54', '2021-09-09 22:16:07');

-- ----------------------------
-- Table structure for `order_good`
-- ----------------------------
DROP TABLE IF EXISTS `order_good`;
CREATE TABLE `order_good` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `o_id` int(11) NOT NULL,
  `g_id` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_good
-- ----------------------------
INSERT INTO `order_good` VALUES ('1', '1', '9', '1');
INSERT INTO `order_good` VALUES ('2', '1', '17', '1');
INSERT INTO `order_good` VALUES ('3', '2', '1', '1');
INSERT INTO `order_good` VALUES ('4', '2', '4', '1');
INSERT INTO `order_good` VALUES ('5', '2', '6', '1');
INSERT INTO `order_good` VALUES ('6', '2', '21', '1');
INSERT INTO `order_good` VALUES ('7', '3', '8', '1');
INSERT INTO `order_good` VALUES ('8', '3', '18', '1');
INSERT INTO `order_good` VALUES ('9', '4', '11', '1');
INSERT INTO `order_good` VALUES ('10', '4', '14', '1');
INSERT INTO `order_good` VALUES ('11', '4', '20', '1');
INSERT INTO `order_good` VALUES ('12', '5', '22', '1');
INSERT INTO `order_good` VALUES ('13', '5', '2', '1');
INSERT INTO `order_good` VALUES ('14', '5', '7', '1');
INSERT INTO `order_good` VALUES ('15', '5', '21', '1');
INSERT INTO `order_good` VALUES ('16', '6', '40', '1');
INSERT INTO `order_good` VALUES ('17', '6', '45', '1');
INSERT INTO `order_good` VALUES ('18', '6', '49', '1');
INSERT INTO `order_good` VALUES ('19', '6', '46', '1');
INSERT INTO `order_good` VALUES ('20', '7', '40', '2');
INSERT INTO `order_good` VALUES ('21', '7', '41', '1');
INSERT INTO `order_good` VALUES ('22', '8', '41', '1');
INSERT INTO `order_good` VALUES ('23', '8', '47', '1');
INSERT INTO `order_good` VALUES ('24', '8', '45', '1');
INSERT INTO `order_good` VALUES ('25', '9', '42', '1');
INSERT INTO `order_good` VALUES ('26', '9', '47', '1');
INSERT INTO `order_good` VALUES ('27', '9', '50', '1');
INSERT INTO `order_good` VALUES ('28', '10', '40', '1');
INSERT INTO `order_good` VALUES ('29', '10', '49', '1');
INSERT INTO `order_good` VALUES ('30', '10', '43', '1');
INSERT INTO `order_good` VALUES ('31', '10', '46', '1');
INSERT INTO `order_good` VALUES ('32', '11', '8', '1');
INSERT INTO `order_good` VALUES ('33', '11', '9', '1');
INSERT INTO `order_good` VALUES ('34', '11', '18', '1');
INSERT INTO `order_good` VALUES ('35', '11', '19', '1');
INSERT INTO `order_good` VALUES ('36', '12', '1', '1');
INSERT INTO `order_good` VALUES ('37', '12', '4', '1');
INSERT INTO `order_good` VALUES ('38', '12', '21', '1');
INSERT INTO `order_good` VALUES ('39', '13', '1', '1');
INSERT INTO `order_good` VALUES ('40', '13', '4', '1');
INSERT INTO `order_good` VALUES ('41', '13', '6', '1');
INSERT INTO `order_good` VALUES ('42', '14', '1', '1');
INSERT INTO `order_good` VALUES ('43', '14', '2', '1');
INSERT INTO `order_good` VALUES ('44', '14', '4', '1');
INSERT INTO `order_good` VALUES ('45', '14', '21', '1');

-- ----------------------------
-- Table structure for `rider_order`
-- ----------------------------
DROP TABLE IF EXISTS `rider_order`;
CREATE TABLE `rider_order` (
  `r_o_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_id` int(11) NOT NULL,
  `o_id` int(11) NOT NULL,
  PRIMARY KEY (`r_o_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rider_order
-- ----------------------------

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `r_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `perms` varchar(255) NOT NULL,
  PRIMARY KEY (`r_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'user', 'user');
INSERT INTO `role` VALUES ('2', 'shop_owner', 'owner');
INSERT INTO `role` VALUES ('3', 'admin', 'admin');

-- ----------------------------
-- Table structure for `shop`
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `s_id` int(11) NOT NULL AUTO_INCREMENT,
  `logo_src` varchar(255) NOT NULL,
  `shop_name` varchar(255) NOT NULL,
  `u_id` int(11) NOT NULL,
  `license` varchar(255) NOT NULL,
  `stat` int(11) NOT NULL DEFAULT '0',
  `delivery_cost` varchar(255) NOT NULL,
  `min_cost` varchar(255) NOT NULL,
  `total_sales` varchar(255) NOT NULL DEFAULT '0',
  `phone` varchar(255) NOT NULL,
  `address_detail` varchar(255) NOT NULL,
  `delivery_time` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`s_id`),
  UNIQUE KEY `shopName` (`shop_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop
-- ----------------------------
INSERT INTO `shop` VALUES ('1', '/upload/hls_20210818164655.png', '华莱士·全鸡汉堡', '2', '88688', '1', '4', '20', '3', '12312345612', '中关村大道123号', '40', '炸鸡与汉堡');
INSERT INTO `shop` VALUES ('2', '/upload/汉堡王_20210901143053.jfif', '汉堡王', '4', '88687', '1', '10', '10', '2', '12312345612', '龙冠置业店19694', '40', '真正火烤，源于1954');
INSERT INTO `shop` VALUES ('3', '/upload/KFCLogo_20210812213306.jpg', 'KFC(中关村店)', '3', '3652148754', '1', '5', '20', '0', '15423652145', '中关村大道666号', '45', 'KFC');
INSERT INTO `shop` VALUES ('4', '/upload/蜜雪冰城logo (2)_20210905175244.png', '蜜雪冰城', '5', '88686', '1', '4.5', '15', '1', '15179854023', '北京市昌平区回龙观西大街35号院4号楼1层120', '40', '冰淇淋与茶');

-- ----------------------------
-- Table structure for `shop_manager`
-- ----------------------------
DROP TABLE IF EXISTS `shop_manager`;
CREATE TABLE `shop_manager` (
  `s_m_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `identity_number` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `stat` int(255) NOT NULL DEFAULT '1',
  `r_id` int(11) NOT NULL DEFAULT '2',
  PRIMARY KEY (`s_m_id`),
  UNIQUE KEY `username` (`user_name`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `identity_number` (`identity_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_manager
-- ----------------------------
INSERT INTO `shop_manager` VALUES ('1', 'boss', '123', '12312312313', '362202200002144052', 'lky', '1', '2');
INSERT INTO `shop_manager` VALUES ('2', 'boss1', '123', '15428754951', '362154215779845126', 'boss', '1', '2');
INSERT INTO `shop_manager` VALUES ('3', 'lky', '123', '12545632514', '362201254123652200', 'lky', '1', '2');
INSERT INTO `shop_manager` VALUES ('4', 'lky1', '123', '15421365213', '362154214779845126', 'lky1', '1', '2');
INSERT INTO `shop_manager` VALUES ('5', 'bob', '123', '15179548562', '362201254201254900', 'bob', '1', '2');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `r_id` int(11) NOT NULL DEFAULT '1',
  `picSrc` varchar(255) DEFAULT NULL,
  `score` int(11) NOT NULL DEFAULT '0',
  `stat` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'lky', '15123456789', 'lky', '123', '1', '/upload/小狐狸_20210905195612.png', '2', '1');
INSERT INTO `user` VALUES ('3', 'admin', '15179510122', 'admin', '123', '3', null, '0', '1');
INSERT INTO `user` VALUES ('5', 'lky1', '15179845215', 'lky1', '123', '1', '/upload/小狗_20210905195633.png', '0', '1');
INSERT INTO `user` VALUES ('6', 'xiaohuangya', '15176458295', '小黄鸭超爱吃', '123', '1', '/upload/小黄鸭_20210909221632.png', '4', '1');
