package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoomStatusLog {
    private Long id;
    private Long roomId;
    private Long oldStatusId;
    private Long newStatusId;
    private Long operatorId;
    private String remark;
    private LocalDateTime createdAt;
}
