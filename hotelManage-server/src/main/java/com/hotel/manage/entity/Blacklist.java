package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Blacklist {
    private Long id;
    private Long guestId;
    private String reason;
    private LocalDateTime createdAt;
    private Long operatorId;
}
