package com.hotel.manage.entity;

import lombok.Data;

@Data
public class DictPaymentMethod {
    private Long id;
    private String code;
    private String name;
    private Integer sortOrder;
    private Boolean allowChange;
    private Boolean isEnabled;
}
