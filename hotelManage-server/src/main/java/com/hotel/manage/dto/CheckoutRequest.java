package com.hotel.manage.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 退房结账请求DTO
 */
@Data
public class CheckoutRequest {
    /** 入住ID */
    private Long checkInId;
    
    /** 实际退房时间 */
    private String actualCheckOutTime;
    
    /** 押金处理方式 (refund-退款 excess-补缴) */
    private String depositHandling;
    
    /** 实际收款金额 */
    private BigDecimal actualPaid;
    
    /** 备注 */
    private String remark;
}