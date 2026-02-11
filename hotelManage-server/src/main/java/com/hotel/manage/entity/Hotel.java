package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Hotel {
    private Long id;
    private String code;
    private String name;
    private String address;
    private String phone;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
