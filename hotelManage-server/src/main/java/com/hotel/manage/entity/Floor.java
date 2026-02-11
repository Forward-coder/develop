package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Floor {
    private Long id;
    private Long hotelId;
    private String code;
    private String name;
    private Integer sortOrder;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
