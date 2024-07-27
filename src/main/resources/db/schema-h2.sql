-- galigeigei.a_data_json definition

CREATE TABLE `a_data_json` (
  `id` int NOT NULL AUTO_INCREMENT,
  `json` json DEFAULT NULL COMMENT '数据',
  `today` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据日期',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `a_data_json_today_IDX` (`today`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='每日数据';


-- galigeigei.a_info definition

CREATE TABLE `a_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '股票代码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `bk_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '板块id',
  `bk_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '板块名称',
  `ej_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二级板块',
  `ej_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二级板块名称',
  `exchange` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属交易所',
  PRIMARY KEY (`id`),
  KEY `a_info_code_IDX` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5625 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='股票基础信息板块';


-- galigeigei.a_sw definition

CREATE TABLE `a_sw` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stock_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '股票代码',
  `sw_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '申万板块代码',
  `industry_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '板块名称',
  `industry_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '申万类型',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源',
  PRIMARY KEY (`id`),
  KEY `a_sw_stock_code_IDX` (`stock_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10855 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='申万板块';


-- galigeigei.a_sw_dict definition

CREATE TABLE `a_sw_dict` (
  `code` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代码',
  `name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型',
  KEY `a_sw_dict_code_IDX` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='申万字典';


-- galigeigei.a_today definition

CREATE TABLE `a_today` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '股票名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '股票代码',
  `total` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '总市值',
  `price` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前价',
  `increase` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '涨跌幅',
  `arr_value` json DEFAULT NULL COMMENT '数据数组',
  `turnover` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '换手率',
  `into_date` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上市日期',
  `today` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据日期',
  `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `a_today_code_IDX` (`code`) USING BTREE,
  KEY `a_today_today_IDX` (`today`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5625 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='今日数据';