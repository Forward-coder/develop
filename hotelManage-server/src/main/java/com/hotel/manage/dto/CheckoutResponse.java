package com.hotel.manage.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退房结账响应DTO
 */
@Data
public class CheckoutResponse {
    /** 账单信息 */
    private BillInfo bill;
    
    /** 消费明细列表 */
    private List<ConsumptionDetail> consumptions;
    
    /** 退房结果 */
    private CheckoutResult result;
    
    @Data
    public static class BillInfo {
        /** 账单编号 */
        private String billNo;
        
        /** 房费 */
        private BigDecimal roomFee;
        
        /** 消费费用 */
        private BigDecimal consumptionFee;
        
        /** 总金额 */
        private BigDecimal totalAmount;
        
        /** 押金 */
        private BigDecimal depositTotal;
        
        /** 实际支付 */
        private BigDecimal actualPaid;
        
        /** 找零 */
        private BigDecimal changeAmount;
        
        /** 结账时间 */
        private LocalDateTime billTime;
    }
    
    @Data
    public static class ConsumptionDetail {
        /** 消费项目名称 */
        private String itemName;
        
        /** 数量 */
        private BigDecimal quantity;
        
        /** 单价 */
        private BigDecimal unitPrice;
        
        /** 金额 */
        private BigDecimal amount;
        
        /** 发生时间 */
        private LocalDateTime occurredAt;
        
        /** 备注 */
        private String remark;
    }
    
    @Data
    public static class CheckoutResult {
        /** 是否成功 */
        private Boolean success;
        
        /** 消息 */
        private String message;
        
        /** 房间ID */
        private Long roomId;
        
        /** 房间号 */
        private String roomNo;
    }
}