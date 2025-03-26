CREATE TABLE `t_keystore_info` (
  `pk_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wework_uid` varchar(128) NOT NULL DEFAULT '' COMMENT '用户ID',
  `public_key` varchar(512) NOT NULL DEFAULT '' COMMENT '公钥',
  `private_key` varchar(2048) NOT NULL DEFAULT '' COMMENT '私钥',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`pk_id`),
  UNIQUE KEY `wework_uid` (`wework_uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;