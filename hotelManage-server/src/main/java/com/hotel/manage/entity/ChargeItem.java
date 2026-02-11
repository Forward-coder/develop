package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ChargeItem {
    private Long id;
    private Long hotelId;
    private String code;
    private String name;
    private BigDecimal unitPrice;
    private String chargeMode;
    private Boolean allowRoomCharge;
    private Integer sortOrder;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
