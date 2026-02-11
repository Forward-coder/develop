package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BillPayment {
    private Long id;
    private Long billId;
    private Long paymentMethodId;
    private BigDecimal amount;
}
