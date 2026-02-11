package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Room {
    private Long id;
    private Long hotelId;
    private Long floorId;
    private Long roomTypeId;
    private String roomNo;
    private Long roomStatusId;
    private String remark;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联查询字段（非数据库字段）
    private String floorName;
    private String roomTypeName;
    private String roomStatusName;
}
