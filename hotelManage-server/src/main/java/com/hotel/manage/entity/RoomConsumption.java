package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomConsumption {
    private Long id;
    private Long checkInId;
    private Long chargeItemId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private LocalDateTime occurredAt;
    private String remark;
}
