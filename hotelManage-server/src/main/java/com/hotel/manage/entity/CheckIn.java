package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CheckIn {
    private Long id;
    private Long hotelId;
    private String checkInNo;
    private Long reservationId;
    private Long roomId;
    private Long roomTypeId;
    private LocalDateTime actualCheckInTime;
    private LocalDate plannedCheckOutDate;
    private LocalDateTime actualCheckOutTime;
    private BigDecimal roomRate;
    private String guestName;
    private String guestPhone;
    private Long guestIdTypeId;
    private String guestIdNo;
    private BigDecimal depositTotal;
    private String remark;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 关联查询字段
    private String roomNo;
    private String roomTypeName;
}
