-- ============================================================
-- 酒店客房管理系统 - MySQL 数据库初始化脚本
-- 字符集: utf8mb4, 引擎: InnoDB
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------
-- 一、字典表
-- ------------------------------------------------------------

-- 房间状态
DROP TABLE IF EXISTS `dict_room_status`;
CREATE TABLE `dict_room_status` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '状态编码',
  `name` varchar(64) NOT NULL COMMENT '状态名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间状态字典';

-- 床型
DROP TABLE IF EXISTS `dict_bed_type`;
CREATE TABLE `dict_bed_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '床型编码',
  `name` varchar(64) NOT NULL COMMENT '床型名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='床型字典';

-- 证件类型
DROP TABLE IF EXISTS `dict_id_type`;
CREATE TABLE `dict_id_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '证件类型编码',
  `name` varchar(64) NOT NULL COMMENT '证件类型名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证件类型字典';

-- 预订状态
DROP TABLE IF EXISTS `dict_reservation_status`;
CREATE TABLE `dict_reservation_status` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '状态编码',
  `name` varchar(64) NOT NULL COMMENT '状态名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预订状态字典';

-- 预订渠道
DROP TABLE IF EXISTS `dict_reservation_channel`;
CREATE TABLE `dict_reservation_channel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '渠道编码',
  `name` varchar(64) NOT NULL COMMENT '渠道名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预订渠道字典';

-- 支付方式
DROP TABLE IF EXISTS `dict_payment_method`;
CREATE TABLE `dict_payment_method` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '支付方式编码',
  `name` varchar(64) NOT NULL COMMENT '支付方式名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `allow_change` tinyint(1) DEFAULT 1 COMMENT '是否参与找零/退款 0否 1是',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付方式字典';


-- ------------------------------------------------------------
-- 二、酒店与物理结构
-- ------------------------------------------------------------

-- 酒店
DROP TABLE IF EXISTS `hotel`;
CREATE TABLE `hotel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '酒店编码',
  `name` varchar(128) NOT NULL COMMENT '酒店名称',
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `phone` varchar(64) DEFAULT NULL COMMENT '电话',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='酒店';

-- 楼层
DROP TABLE IF EXISTS `floor`;
CREATE TABLE `floor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hotel_id` bigint NOT NULL COMMENT '酒店ID',
  `code` varchar(32) NOT NULL COMMENT '楼层编码',
  `name` varchar(64) NOT NULL COMMENT '楼层名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hotel_code` (`hotel_id`,`code`),
  KEY `idx_hotel_id` (`hotel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='楼层';


-- ------------------------------------------------------------
-- 三、客房主数据
-- ------------------------------------------------------------

-- 房型
DROP TABLE IF EXISTS `room_type`;
CREATE TABLE `room_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hotel_id` bigint NOT NULL COMMENT '酒店ID',
  `code` varchar(32) NOT NULL COMMENT '房型编码',
  `name` varchar(64) NOT NULL COMMENT '房型名称',
  `area` decimal(10,2) DEFAULT NULL COMMENT '面积(平方米)',
  `bed_type_id` bigint DEFAULT NULL COMMENT '床型ID',
  `max_guests` int DEFAULT 2 COMMENT '可住人数',
  `base_price` decimal(12,2) DEFAULT NULL COMMENT '基础价格(元)',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hotel_code` (`hotel_id`,`code`),
  KEY `idx_hotel_id` (`hotel_id`),
  KEY `idx_bed_type_id` (`bed_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房型';

-- 房间
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hotel_id` bigint NOT NULL COMMENT '酒店ID',
  `floor_id` bigint NOT NULL COMMENT '楼层ID',
  `room_type_id` bigint NOT NULL COMMENT '房型ID',
  `room_no` varchar(32) NOT NULL COMMENT '房号',
  `room_status_id` bigint NOT NULL COMMENT '房间状态ID',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hotel_room_no` (`hotel_id`,`room_no`),
  KEY `idx_hotel_id` (`hotel_id`),
  KEY `idx_floor_id` (`floor_id`),
  KEY `idx_room_type_id` (`room_type_id`),
  KEY `idx_room_status_id` (`room_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间';

-- 房型价格(房价表)
DROP TABLE IF EXISTS `room_type_price`;
CREATE TABLE `room_type_price` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_type_id` bigint NOT NULL COMMENT '房型ID',
  `price_type` varchar(32) NOT NULL DEFAULT 'default' COMMENT '价格类型 default平日 weekend周末 holiday节假日 hourly钟点',
  `price` decimal(12,2) NOT NULL COMMENT '价格(元)',
  `effective_date` date DEFAULT NULL COMMENT '生效日期 NULL表示长期',
  `expire_date` date DEFAULT NULL COMMENT '失效日期 NULL表示长期',
  `is_default` tinyint(1) DEFAULT 0 COMMENT '是否默认价 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_room_type_id` (`room_type_id`),
  KEY `idx_effective` (`effective_date`,`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房型价格';

-- 杂项消费项目
DROP TABLE IF EXISTS `charge_item`;
CREATE TABLE `charge_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hotel_id` bigint NOT NULL COMMENT '酒店ID',
  `code` varchar(32) NOT NULL COMMENT '项目编码',
  `name` varchar(64) NOT NULL COMMENT '项目名称',
  `unit_price` decimal(12,2) DEFAULT NULL COMMENT '单价(元) 按数量计费时使用',
  `charge_mode` varchar(32) DEFAULT 'quantity' COMMENT '计费方式 quantity数量 fixed固定金额',
  `allow_room_charge` tinyint(1) DEFAULT 1 COMMENT '是否可挂房账 0否 1是',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hotel_code` (`hotel_id`,`code`),
  KEY `idx_hotel_id` (`hotel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='杂项消费项目';


-- ------------------------------------------------------------
-- 四、客人(可选档案)
-- ------------------------------------------------------------

DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `id_type_id` bigint DEFAULT NULL COMMENT '证件类型ID',
  `id_no` varchar(64) DEFAULT NULL COMMENT '证件号',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_id_no` (`id_no`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客人档案';

-- 会员等级
DROP TABLE IF EXISTS `member_level`;
CREATE TABLE `member_level` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '等级名称',
  `discount_rate` decimal(5,2) DEFAULT 1.00 COMMENT '折扣率(如0.90表示9折)',
  `min_points` int DEFAULT 0 COMMENT '达到该等级所需最低积分',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员等级';

-- 会员信息
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `guest_id` bigint NOT NULL COMMENT '关联客人ID',
  `member_no` varchar(64) NOT NULL COMMENT '会员号',
  `level_id` bigint DEFAULT NULL COMMENT '会员等级ID',
  `points` int DEFAULT 0 COMMENT '当前积分',
  `total_points` int DEFAULT 0 COMMENT '累计积分',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态 1正常 2冻结 3黑名单',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_member_no` (`member_no`),
  UNIQUE KEY `uk_guest_id` (`guest_id`),
  KEY `idx_level_id` (`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员信息';

-- 黑名单
DROP TABLE IF EXISTS `blacklist`;
CREATE TABLE `blacklist` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `guest_id` bigint NOT NULL COMMENT '客人ID',
  `reason` varchar(256) DEFAULT NULL COMMENT '拉黑原因',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_guest_id` (`guest_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='黑名单';


-- ------------------------------------------------------------
-- 五、预订与入住业务
-- ------------------------------------------------------------

-- 预订
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hotel_id` bigint NOT NULL COMMENT '酒店ID',
  `reservation_no` varchar(64) NOT NULL COMMENT '预订单号',
  `room_type_id` bigint NOT NULL COMMENT '房型ID',
  `room_id` bigint DEFAULT NULL COMMENT '房间ID 可选具体房号',
  `channel_id` bigint DEFAULT NULL COMMENT '预订渠道ID',
  `status_id` bigint NOT NULL COMMENT '预订状态ID',
  `check_in_date` date NOT NULL COMMENT '预计入住日期',
  `check_out_date` date NOT NULL COMMENT '预计退房日期',
  `guest_name` varchar(64) NOT NULL COMMENT '客人姓名',
  `guest_phone` varchar(32) DEFAULT NULL COMMENT '客人电话',
  `guest_id_type_id` bigint DEFAULT NULL COMMENT '客人证件类型ID',
  `guest_id_no` varchar(64) DEFAULT NULL COMMENT '客人证件号',
  `room_rate` decimal(12,2) DEFAULT NULL COMMENT '预订房价',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_reservation_no` (`reservation_no`),
  KEY `idx_hotel_id` (`hotel_id`),
  KEY `idx_check_in_date` (`check_in_date`),
  KEY `idx_status_id` (`status_id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预订';

-- 入住单(在住记录)
DROP TABLE IF EXISTS `check_in`;
CREATE TABLE `check_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hotel_id` bigint NOT NULL COMMENT '酒店ID',
  `check_in_no` varchar(64) NOT NULL COMMENT '入住单号',
  `reservation_id` bigint DEFAULT NULL COMMENT '关联预订ID',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `room_type_id` bigint NOT NULL COMMENT '房型ID',
  `actual_check_in_time` datetime NOT NULL COMMENT '实际入住时间',
  `planned_check_out_date` date NOT NULL COMMENT '预计退房日期',
  `actual_check_out_time` datetime DEFAULT NULL COMMENT '实际退房时间',
  `room_rate` decimal(12,2) NOT NULL COMMENT '房费(元/间夜)',
  `guest_name` varchar(64) NOT NULL COMMENT '客人姓名',
  `guest_phone` varchar(32) DEFAULT NULL COMMENT '客人电话',
  `guest_id_type_id` bigint DEFAULT NULL COMMENT '客人证件类型ID',
  `guest_id_no` varchar(64) DEFAULT NULL COMMENT '客人证件号',
  `status` varchar(32) NOT NULL DEFAULT 'staying' COMMENT '状态 staying在住 checked_out已退房',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_check_in_no` (`check_in_no`),
  KEY `idx_hotel_id` (`hotel_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_status` (`status`),
  KEY `idx_planned_check_out` (`planned_check_out_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入住单';

-- 房间消费(杂项挂房账)
DROP TABLE IF EXISTS `room_consumption`;
CREATE TABLE `room_consumption` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `check_in_id` bigint NOT NULL COMMENT '入住单ID',
  `charge_item_id` bigint NOT NULL COMMENT '消费项目ID',
  `quantity` decimal(12,2) DEFAULT 1.00 COMMENT '数量',
  `unit_price` decimal(12,2) NOT NULL COMMENT '单价',
  `amount` decimal(12,2) NOT NULL COMMENT '金额',
  `occurred_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_check_in_id` (`check_in_id`),
  KEY `idx_charge_item_id` (`charge_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间消费';

-- 押金
DROP TABLE IF EXISTS `deposit`;
CREATE TABLE `deposit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `check_in_id` bigint NOT NULL COMMENT '入住单ID',
  `amount` decimal(12,2) NOT NULL COMMENT '押金金额',
  `payment_method_id` bigint NOT NULL COMMENT '支付方式ID',
  `occurred_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_check_in_id` (`check_in_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='押金';

-- 账单(退房结账生成)
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `check_in_id` bigint NOT NULL COMMENT '入住单ID',
  `bill_no` varchar(64) NOT NULL COMMENT '账单号',
  `room_fee` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '房费合计',
  `consumption_fee` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '杂费合计',
  `total_amount` decimal(12,2) NOT NULL COMMENT '应收总额',
  `deposit_used` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '押金抵扣',
  `actual_paid` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '实收金额',
  `change_amount` decimal(12,2) DEFAULT 0.00 COMMENT '找零/退款',
  `bill_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '结账时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`),
  KEY `idx_check_in_id` (`check_in_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账单';

-- 账单支付明细
DROP TABLE IF EXISTS `bill_payment`;
CREATE TABLE `bill_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bill_id` bigint NOT NULL COMMENT '账单ID',
  `payment_method_id` bigint NOT NULL COMMENT '支付方式ID',
  `amount` decimal(12,2) NOT NULL COMMENT '支付金额',
  PRIMARY KEY (`id`),
  KEY `idx_bill_id` (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账单支付明细';


-- ------------------------------------------------------------
-- 六、房间清洁与维修
-- ------------------------------------------------------------

-- 清洁记录
DROP TABLE IF EXISTS `room_clean_record`;
CREATE TABLE `room_clean_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `clean_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '清洁时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间清洁记录';

-- 维修记录
DROP TABLE IF EXISTS `room_repair_record`;
CREATE TABLE `room_repair_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `start_time` datetime NOT NULL COMMENT '开始维修时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束维修时间',
  `reason` varchar(256) DEFAULT NULL COMMENT '维修原因',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间维修记录';

-- 房间状态变更日志
DROP TABLE IF EXISTS `room_status_log`;
CREATE TABLE `room_status_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` bigint NOT NULL COMMENT '房间ID',
  `old_status_id` bigint NOT NULL COMMENT '原状态ID',
  `new_status_id` bigint NOT NULL COMMENT '新状态ID',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '变更时间',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间状态变更日志';


-- ------------------------------------------------------------
-- 七、系统用户与权限
-- ------------------------------------------------------------

-- 角色
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '角色编码',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色';

-- 用户
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(64) NOT NULL COMMENT '登录名',
  `password` varchar(128) NOT NULL COMMENT '密码(加密)',
  `real_name` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  `is_enabled` tinyint(1) DEFAULT 1 COMMENT '是否启用 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

-- 用户角色关联
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联';

-- 菜单管理
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint DEFAULT 0 COMMENT '父菜单ID',
  `title` varchar(64) NOT NULL COMMENT '菜单标题',
  `name` varchar(64) DEFAULT NULL COMMENT '路由名称',
  `path` varchar(256) DEFAULT NULL COMMENT '路由路径',
  `component` varchar(256) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `is_visible` tinyint(1) DEFAULT 1 COMMENT '是否显示 0否 1是',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单';

-- 角色菜单关联
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联';

-- 操作日志
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `username` varchar(64) DEFAULT NULL COMMENT '登录名',
  `module` varchar(64) DEFAULT NULL COMMENT '模块',
  `operation` varchar(64) DEFAULT NULL COMMENT '操作',
  `request_uri` varchar(256) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(16) DEFAULT NULL COMMENT '请求方法',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志';

SET FOREIGN_KEY_CHECKS = 1;
