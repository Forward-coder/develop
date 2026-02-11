package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysRole {
    private Long id;
    private String code;
    private String name;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
