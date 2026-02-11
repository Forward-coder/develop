package com.hotel.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Member {
    private Long id;
    private Long guestId;
    private String memberNo;
    private Long levelId;
    private Integer points;
    private Integer totalPoints;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联查询字段
    private String name;
    private String phone;
    private Long idTypeId;
    private String idNo;
    private String remark;
    private String levelName;
    
    // 获取状态名称
    public String getStatusName() {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "正常";
            case 2: return "冻结";
            case 3: return "黑名单";
            default: return "未知";
        }
    }
}
