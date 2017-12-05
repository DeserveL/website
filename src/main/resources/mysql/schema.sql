-- 创建数据库
DROP DATABASE IF EXISTS `website`;
create database `website` default character set utf8 collate utf8_general_ci;
use website;

-- 日志系统
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `action` varchar(100) DEFAULT NULL COMMENT '产生的动作',
  `data` varchar(2000) DEFAULT NULL COMMENT '产生的数据',
  `author_id` int(10) DEFAULT NULL COMMENT '发生人id',
  `ip` varchar(20) DEFAULT NULL COMMENT '日志产生的ip',
  `created` int(10) DEFAULT NULL COMMENT '日志创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志系统';

-- 附件管理
DROP TABLE IF EXISTS `attach`;
CREATE TABLE `attach` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '附件主键',
  `fname` varchar(100) NOT NULL DEFAULT '' COMMENT '附件名称',
  `ftype` varchar(50) DEFAULT '' COMMENT '附件类型',
  `fkey` varchar(100) NOT NULL DEFAULT '' COMMENT '附件key',
  `author_id` int(10) DEFAULT NULL COMMENT '创建时间',
  `created` int(10) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件管理';

-- 评论
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `coid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'comment表主键',
  `cid` int(10) unsigned DEFAULT '0' COMMENT 'post表主键,关联字段',
  `created` int(10) unsigned DEFAULT '0' COMMENT '评论生成时的GMT unix时间戳',
  `author` varchar(200) DEFAULT NULL COMMENT '评论作者',
  `author_id` int(10) unsigned DEFAULT '0'COMMENT '评论所属用户id',
  `owner_id` int(10) unsigned DEFAULT '0' COMMENT '评论所属内容作者id',
  `mail` varchar(200) DEFAULT NULL COMMENT '评论者邮件',
  `url` varchar(200) DEFAULT NULL COMMENT '评论者网址',
  `ip` varchar(64) DEFAULT NULL COMMENT '评论者ip地址',
  `agent` varchar(200) DEFAULT NULL COMMENT '评论者客户端',
  `content` text COMMENT '评论内容',
  `type` varchar(16) DEFAULT 'comment' COMMENT '评论类型',
  `status` varchar(16) DEFAULT 'approved' COMMENT '评论状态',
  `parent` int(10) unsigned DEFAULT '0' COMMENT '父级评论',
  PRIMARY KEY (`coid`),
  KEY `cid` (`cid`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论';

-- 内容表
DROP TABLE IF EXISTS `contents`;
CREATE TABLE `contents` (
  `cid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'post表主键',
  `title` varchar(200) DEFAULT NULL COMMENT '内容标题',
  `slug` varchar(200) DEFAULT NULL COMMENT '内容缩略名',
  `created` int(10) unsigned DEFAULT '0' COMMENT '内容生成时的GMT unix时间戳',
  `modified` int(10) unsigned DEFAULT '0' COMMENT '内容更改时的GMT unix时间戳',
  `content` text COMMENT '内容文字' COMMENT '内容文字',
  `author_id` int(10) unsigned DEFAULT '0' COMMENT '内容所属用户id',
  `type` varchar(16) DEFAULT 'post' COMMENT '内容类别',
  `status` varchar(16) DEFAULT 'publish' COMMENT '内容状态',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签列表',
  `categories` varchar(200) DEFAULT NULL COMMENT '分类列表',
  `hits` int(10) unsigned DEFAULT '0' COMMENT '点击次数',
  `comments_num` int(10) unsigned DEFAULT '0' COMMENT '内容所属评论数',
  `allow_comment` tinyint(1) DEFAULT '1' COMMENT '是否允许评论',
  `allow_ping` tinyint(1) DEFAULT '1' COMMENT '是否允许ping',
  `allow_feed` tinyint(1) DEFAULT '1' COMMENT '允许出现在聚合中',
  PRIMARY KEY (`cid`),
  UNIQUE KEY `slug` (`slug`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容表';

-- 内容表初始数据
LOCK TABLES `contents` WRITE;
INSERT INTO `contents` (`cid`, `title`, `slug`, `created`, `modified`, `content`, `author_id`, `type`, `status`, `tags`, `categories`, `hits`, `comments_num`, `allow_comment`, `allow_ping`, `allow_feed`)
VALUES
	(1,'about my blog','about',1487853610,1487872488,'### Hello World\r\n\r\nabout me\r\n\r\n### ...\r\n\r\n...',1,'page','publish',NULL,NULL,0,0,1,1,1),
	(2,'Hello My Blog',NULL,1487861184,1487872798,'## Hello  World.\r\n\r\n> ...\r\n\r\n----------\r\n\r\n\r\n<!--more-->\r\n\r\n```java\r\npublic static void main(String[] args){\r\n    System.out.println(\"Hello 13 Blog.\");\r\n}\r\n```',1,'post','publish','','default',10,0,1,1,1);
UNLOCK TABLES;
-- 内容表初始数据结束

-- 标签/分类/各种
DROP TABLE IF EXISTS `metas`;
CREATE TABLE `metas` (
  `mid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '项目主键',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `slug` varchar(200) DEFAULT NULL COMMENT '项目缩略名',
  `type` varchar(32) NOT NULL DEFAULT '' COMMENT '项目类型',
  `description` varchar(200) DEFAULT NULL COMMENT '选项描述',
  `sort` int(10) unsigned DEFAULT '0' COMMENT '项目排序',
  `parent` int(10) unsigned DEFAULT '0' COMMENT '父项目',
  PRIMARY KEY (`mid`),
  KEY `slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签/分类/各种';

-- 标签/分类/各种 初始数据
LOCK TABLES `metas` WRITE;
INSERT INTO `metas` (`mid`, `name`, `slug`, `type`, `description`, `sort`, `parent`)
VALUES
	(1,'default',NULL,'category',NULL,0,0),
	(6,'my github','https://github.com/deservel','link',NULL,0,0);
UNLOCK TABLES;
-- 标签/分类/各种 初始数据结束

-- 内容项目对应表
DROP TABLE IF EXISTS `relationships`;
CREATE TABLE `relationships` (
  `cid` int(10) unsigned NOT NULL COMMENT '内容主键',
  `mid` int(10) unsigned NOT NULL COMMENT '项目主键',
  PRIMARY KEY (`cid`,`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='内容项目对应表';

-- 内容项目初始数据
LOCK TABLES `relationships` WRITE;
INSERT INTO `relationships` (`cid`, `mid`)
VALUES
  (2,1);
UNLOCK TABLES;
-- 内容项目初始数据结束

-- 网站配置表
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '配置名称',
  `value` varchar(1000) DEFAULT '' COMMENT '配置值',
  `description` varchar(200) DEFAULT NULL COMMENT '配置描述',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网站配置表';

-- 网站配置表初始数据
LOCK TABLES `options` WRITE;
INSERT INTO `options` (`name`, `value`, `description`)
VALUES
	('site_title','DeserveL',''),
	('social_weibo','https://weibo.com/714395012',NULL),
	('social_zhihu','',NULL),
	('social_github','https://github.com/deservel',NULL),
	('social_twitter','',NULL),
	('site_theme','default',NULL),
	('site_keywords','DeserveL',NULL),
	('site_description','DeserveL的个人网站',NULL),
	('site_record','鄂ICP备17026132号-1','备案号');
UNLOCK TABLES;
-- 网站配置表初始数据结束

-- 用户表
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'user表主键',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `email` varchar(200) DEFAULT NULL COMMENT '用户的邮箱',
  `home_url` varchar(200) DEFAULT NULL COMMENT '用户的主页',
  `screen_name` varchar(32) DEFAULT NULL COMMENT '用户显示的名称',
  `created` int(10) unsigned DEFAULT '0' COMMENT '用户注册时的GMT unix时间戳',
  `activated` int(10) unsigned DEFAULT '0' COMMENT '最后活动时间',
  `logged` int(10) unsigned DEFAULT '0' COMMENT '上次登录最后活跃时间',
  `group_name` varchar(16) DEFAULT 'visitor' COMMENT '用户组',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `name` (`username`),
  UNIQUE KEY `mail` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 用户表初始数据
LOCK TABLES `users` WRITE;
INSERT INTO `users` (`uid`, `username`, `password`, `email`, `home_url`, `screen_name`, `created`, `activated`, `logged`, `group_name`)
VALUES
	(1, 'admin', 'f6fdffe48c908deb0f4c3bd36c032e72', '714395012@qq.com', NULL, 'admin', 1490756162, 0, 0, 'visitor');
UNLOCK TABLES;
-- 用户表初始数据结束