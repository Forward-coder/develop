package com.hotel.manage.entity;

import lombok.Data;

@Data
public class DictReservationStatus {
    private Long id;
    private String code;
    private String name;
    private Integer sortOrder;
    private Boolean isEnabled;
}
