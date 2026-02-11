package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomCleanRecord {
    private Long id;
    private Long roomId;
    private LocalDateTime cleanTime;
    private String remark;
}
