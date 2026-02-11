package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Bill {
    private Long id;
    private Long checkInId;
    private String billNo;
    private BigDecimal roomFee;
    private BigDecimal consumptionFee;
    private BigDecimal totalAmount;
    private BigDecimal depositUsed;
    private BigDecimal actualPaid;
    private BigDecimal changeAmount;
    private LocalDateTime billTime;
}
