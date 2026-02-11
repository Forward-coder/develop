package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Guest {
    private Long id;
    private String name;
    private Long idTypeId;
    private String idNo;
    private String phone;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
