package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Reservation {
    private Long id;
    private Long hotelId;
    private String reservationNo;
    private Long roomTypeId;
    private Long roomId;
    private Long channelId;
    private Long statusId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestName;
    private String guestPhone;
    private Long guestIdTypeId;
    private String guestIdNo;
    private BigDecimal roomRate;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联查询字段（非数据库字段）
    private String roomTypeName;
    private String roomNo;
    private String channelName;
    private String statusName;
}
