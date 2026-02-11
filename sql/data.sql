-- ============================================================
-- 酒店客房管理系统 - 初始数据
-- 执行顺序: 先 schema.sql 再 data.sql
-- ============================================================

SET NAMES utf8mb4;

-- ------------------------------------------------------------
-- 一、字典数据
-- ------------------------------------------------------------

INSERT INTO `dict_room_status` (`code`, `name`, `sort_order`, `is_enabled`) VALUES
('vacant', '空房', 1, 1),
('occupied', '入住', 2, 1),
('reserved', '预订', 3, 1),
('cleaning', '清洁中', 4, 1),
('repair', '维修', 5, 1);

INSERT INTO `dict_bed_type` (`code`, `name`, `sort_order`, `is_enabled`) VALUES
('single', '单床', 1, 1),
('double', '双床', 2, 1),
('king', '大床', 3, 1),
('twin', '双人床', 4, 1),
('tatami', '榻榻米', 5, 1);

INSERT INTO `dict_id_type` (`code`, `name`, `sort_order`, `is_enabled`) VALUES
('id_card', '身份证', 1, 1),
('passport', '护照', 2, 1),
('hk_macau', '港澳通行证', 3, 1),
('taiwan', '台湾通行证', 4, 1),
('other', '其他', 99, 1);

INSERT INTO `dict_reservation_status` (`code`, `name`, `sort_order`, `is_enabled`) VALUES
('pending', '待入住', 1, 1),
('checked_in', '已入住', 2, 1),
('cancelled', '已取消', 3, 1),
('completed', '已完成', 4, 1),
('no_show', '未到店', 5, 1);

INSERT INTO `dict_reservation_channel` (`code`, `name`, `sort_order`, `is_enabled`) VALUES
('front_desk', '前台', 1, 1),
('phone', '电话', 2, 1),
('ota', 'OTA', 3, 1),
('official', '官网', 4, 1),
('other', '其他', 99, 1);

INSERT INTO `dict_payment_method` (`code`, `name`, `sort_order`, `allow_change`, `is_enabled`) VALUES
('cash', '现金', 1, 1, 1),
('card', '刷卡', 2, 1, 1),
('wechat', '微信', 3, 1, 1),
('alipay', '支付宝', 4, 1, 1),
('room_charge', '挂账', 5, 0, 1);


-- ------------------------------------------------------------
-- 二、酒店与楼层(示例)
-- ------------------------------------------------------------

INSERT INTO `hotel` (`code`, `name`, `address`, `phone`, `is_enabled`) VALUES
('H001', '示例酒店', '某某市某某路1号', '400-888-8888', 1);

-- 假设 hotel id=1
INSERT INTO `floor` (`hotel_id`, `code`, `name`, `sort_order`, `is_enabled`) VALUES
(1, 'F1', '1楼', 1, 1),
(1, 'F2', '2楼', 2, 1),
(1, 'F3', '3楼', 3, 1);


-- ------------------------------------------------------------
-- 三、房型与床型关联(需先有 dict_bed_type 的 id)
-- 标准间->双床 id=2, 大床房->大床 id=3, 套房->大床 id=3
-- ------------------------------------------------------------

INSERT INTO `room_type` (`hotel_id`, `code`, `name`, `area`, `bed_type_id`, `max_guests`, `base_price`, `sort_order`, `is_enabled`) VALUES
(1, 'STD', '标准间', 28.00, 2, 2, 298.00, 1, 1),
(1, 'KNG', '大床房', 32.00, 3, 2, 358.00, 2, 1),
(1, 'SUITE', '套房', 55.00, 3, 4, 688.00, 3, 1);

-- 房型价格(默认价)
INSERT INTO `room_type_price` (`room_type_id`, `price_type`, `price`, `is_default`) VALUES
(1, 'default', 298.00, 1),
(2, 'default', 358.00, 1),
(3, 'default', 688.00, 1);


-- ------------------------------------------------------------
-- 四、房间(示例: 每层若干间, 需 dict_room_status vacant=1, floor id 1,2,3, room_type 1,2,3)
-- ------------------------------------------------------------

-- 1楼: 101-104 标准间, 105-106 大床, 107 套房
INSERT INTO `room` (`hotel_id`, `floor_id`, `room_type_id`, `room_no`, `room_status_id`, `is_enabled`) VALUES
(1, 1, 1, '101', 1, 1), (1, 1, 1, '102', 1, 1), (1, 1, 1, '103', 1, 1), (1, 1, 1, '104', 1, 1),
(1, 1, 2, '105', 1, 1), (1, 1, 2, '106', 1, 1),
(1, 1, 3, '107', 1, 1);
-- 2楼
INSERT INTO `room` (`hotel_id`, `floor_id`, `room_type_id`, `room_no`, `room_status_id`, `is_enabled`) VALUES
(1, 2, 1, '201', 1, 1), (1, 2, 1, '202', 1, 1), (1, 2, 1, '203', 1, 1), (1, 2, 1, '204', 1, 1),
(1, 2, 2, '205', 1, 1), (1, 2, 2, '206', 1, 1),
(1, 2, 3, '207', 1, 1);
-- 3楼
INSERT INTO `room` (`hotel_id`, `floor_id`, `room_type_id`, `room_no`, `room_status_id`, `is_enabled`) VALUES
(1, 3, 1, '301', 1, 1), (1, 3, 1, '302', 1, 1), (1, 3, 2, '303', 1, 1), (1, 3, 2, '304', 1, 1),
(1, 3, 3, '305', 1, 1);


-- ------------------------------------------------------------
-- 五、杂项消费项目
-- ------------------------------------------------------------

INSERT INTO `charge_item` (`hotel_id`, `code`, `name`, `unit_price`, `charge_mode`, `allow_room_charge`, `sort_order`, `is_enabled`) VALUES
(1, 'MINIBAR', '迷你吧', 15.00, 'quantity', 1, 1, 1),
(1, 'LAUNDRY', '洗衣', 30.00, 'quantity', 1, 2, 1),
(1, 'DINING', '餐饮挂房账', NULL, 'fixed', 1, 3, 1),
(1, 'OTHER', '其他', NULL, 'fixed', 1, 99, 1);


-- ------------------------------------------------------------
-- 六、系统角色与用户
-- 密码建议: 生产环境请改为 BCrypt 等加密, 此处示例 admin/123456 的 BCrypt
-- ------------------------------------------------------------

INSERT INTO `sys_role` (`code`, `name`, `is_enabled`) VALUES
('admin', '管理员', 1),
('reception', '前台', 1),
('finance', '财务', 1);

INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `is_enabled`) VALUES
-- 密码: 123456 (BCrypt $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi)
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', NULL, 1);

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1);


-- ------------------------------------------------------------
-- 七、完善业务初始数据
-- ------------------------------------------------------------

-- 会员等级
INSERT INTO `member_level` (`name`, `discount_rate`, `min_points`, `is_enabled`) VALUES
('普卡', 1.00, 0, 1),
('银卡', 0.95, 2000, 1),
('金卡', 0.90, 5000, 1),
('白金卡', 0.85, 10000, 1);

-- 系统菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `title`, `name`, `path`, `component`, `icon`, `sort_order`) VALUES
(1, 0, '首页', 'Home', '/home', 'Home', 'House', 1),
(2, 0, '客房管理', NULL, 'room-menu', NULL, 'OfficeBuilding', 2),
(3, 2, '房型管理', 'RoomType', '/room-type', 'room/RoomTypeList', NULL, 1),
(4, 2, '房间管理', 'Room', '/room', 'room/RoomList', NULL, 2),
(5, 0, '预订管理', 'Reservation', '/reservation', 'reservation/ReservationList', 'Calendar', 3),
(6, 0, '入住退房', NULL, 'stay-menu', NULL, 'Key', 4),
(7, 6, '入住管理', 'CheckIn', '/check-in', 'checkin/CheckInList', NULL, 1),
(8, 6, '退房结账', 'CheckOut', '/check-out', 'checkin/CheckOutList', NULL, 2),
(9, 0, '用户管理', NULL, 'user-menu', NULL, 'UserFilled', 5),
(10, 9, '普通客人', 'Guest', '/guest', 'user/GuestList', NULL, 1),
(11, 9, '会员档案', 'Member', '/member', 'user/MemberList', NULL, 2),
(12, 0, '系统管理', NULL, 'system-menu', NULL, 'Setting', 6),
(13, 12, '角色管理', 'Role', '/role', 'system/RoleList', NULL, 1),
(14, 12, '系统用户', 'SystemUser', '/system-user', 'system/SystemUserList', NULL, 2);

-- 管理员角色菜单全量授权
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) SELECT 1, id FROM `sys_menu`;
