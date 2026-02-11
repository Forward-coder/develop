package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Deposit {
    private Long id;
    private Long checkInId;
    private BigDecimal amount;
    private Long paymentMethodId;
    private LocalDateTime occurredAt;
    private String remark;
}
