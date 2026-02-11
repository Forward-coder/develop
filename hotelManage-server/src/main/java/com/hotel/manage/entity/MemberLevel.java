package com.hotel.manage.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MemberLevel {
    private Long id;
    private String name;
    private BigDecimal discountRate;
    private Integer minPoints;
    private Boolean isEnabled;
}
