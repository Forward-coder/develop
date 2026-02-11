package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomType {
    private Long id;
    private Long hotelId;
    private String code;
    private String name;
    private BigDecimal area;
    private Long bedTypeId;
    private Integer maxGuests;
    private BigDecimal basePrice;
    private Integer sortOrder;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联查询字段（非数据库字段）
    private String bedTypeName;
}
