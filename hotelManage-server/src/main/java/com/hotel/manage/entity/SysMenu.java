package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysMenu {
    private Long id;
    private Long parentId;
    private String title;
    private String name;
    private String path;
    private String component;
    private String icon;
    private Integer sortOrder;
    private Boolean isVisible;
    private LocalDateTime createdAt;
}
