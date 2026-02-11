package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysOperationLog {
    private Long id;
    private Long userId;
    private String username;
    private String module;
    private String operation;
    private String requestUri;
    private String method;
    private String ip;
    private LocalDateTime createdAt;
}
