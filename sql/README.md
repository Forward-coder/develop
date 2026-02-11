# 数据库初始化说明

## 执行顺序

1. **schema.sql** - 建表（含删除已存在表，注意备份）
2. **data.sql** - 插入字典与示例数据

## 方式一：命令行

```bash
mysql -u root -p your_database < sql/schema.sql
mysql -u root -p your_database < sql/data.sql
```

## 方式二：先创建库再执行

```sql
CREATE DATABASE IF NOT EXISTS hotel_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_manage;
SOURCE d:/develop/workspace/HotelManage/sql/schema.sql;
SOURCE d:/develop/workspace/HotelManage/sql/data.sql;
```

## 默认账号

| 用户名 | 密码   | 说明     |
|--------|--------|----------|
| admin  | 123456 | 管理员   |

密码为 BCrypt 加密。若使用其他加密方式，需在应用层重新生成并更新 `sys_user.password`。

## 表结构概览

- **字典**: `dict_room_status`, `dict_bed_type`, `dict_id_type`, `dict_reservation_status`, `dict_reservation_channel`, `dict_payment_method`
- **主数据**: `hotel`, `floor`, `room_type`, `room`, `room_type_price`, `charge_item`, `guest`
- **业务**: `reservation`, `check_in`, `room_consumption`, `deposit`, `bill`, `bill_payment`, `room_clean_record`, `room_repair_record`
- **系统**: `sys_role`, `sys_user`, `sys_user_role`, `sys_operation_log`
