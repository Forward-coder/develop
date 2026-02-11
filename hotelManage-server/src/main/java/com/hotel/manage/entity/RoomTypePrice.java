package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RoomTypePrice {
    private Long id;
    private Long roomTypeId;
    private String priceType;
    private BigDecimal price;
    private LocalDate effectiveDate;
    private LocalDate expireDate;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
