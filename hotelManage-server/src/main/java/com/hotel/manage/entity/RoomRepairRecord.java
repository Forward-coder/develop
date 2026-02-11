package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomRepairRecord {
    private Long id;
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
    private String remark;
}
