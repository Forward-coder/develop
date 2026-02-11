package com.hotel.manage.service.impl;

import com.hotel.manage.dto.CheckoutRequest;
import com.hotel.manage.dto.CheckoutResponse;
import com.hotel.manage.entity.CheckIn;
import com.hotel.manage.mapper.CheckInMapper;
import com.hotel.manage.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 入住服务实现类
 */
@Service
public class CheckInServiceImpl implements CheckInService {
    
    @Autowired
    private CheckInMapper checkInMapper;
    
    @Override
    public List<CheckIn> getList(String status, String roomNo, String guestName) {
        return checkInMapper.getList(status, roomNo, guestName);
    }
    
    @Override
    public CheckIn getById(Long id) {
        return checkInMapper.getById(id);
    }
    
    @Transactional
    @Override
    public CheckIn doCheckIn(CheckIn checkIn) {
        // 生成入住单号
        String checkInNo = generateCheckInNo();
        checkIn.setCheckInNo(checkInNo);
        checkIn.setStatus("staying");
        checkIn.setActualCheckInTime(LocalDateTime.now());
        checkIn.setHotelId(1L); // 默认酒店ID
        
        // 插入入住记录
        checkInMapper.insert(checkIn);
        
        // 更新房间状态为入住
        checkInMapper.updateRoomStatus(checkIn.getRoomId(), 2L); // 2为入住状态
        
        return checkIn;
    }
    
    @Transactional
    @Override
    public CheckIn doCheckOut(Long id) {
        CheckIn checkIn = checkInMapper.getById(id);
        if (checkIn == null) {
            throw new RuntimeException("入住记录不存在");
        }
        
        if (!"staying".equals(checkIn.getStatus())) {
            throw new RuntimeException("该客人已退房");
        }
        
        // 更新入住记录状态和退房时间
        checkIn.setStatus("checked_out");
        checkIn.setActualCheckOutTime(LocalDateTime.now());
        checkInMapper.update(checkIn);
        
        // 更新房间状态为空房
        checkInMapper.updateRoomStatus(checkIn.getRoomId(), 1L); // 1为空房状态
        
        return checkIn;
    }
    
    @Override
    public CheckIn extendStay(Long id, LocalDate newCheckOutDate) {
        CheckIn checkIn = checkInMapper.getById(id);
        if (checkIn == null) {
            throw new RuntimeException("入住记录不存在");
        }
        
        if (!"staying".equals(checkIn.getStatus())) {
            throw new RuntimeException("该客人已退房，无法续住");
        }
        
        checkIn.setPlannedCheckOutDate(newCheckOutDate);
        checkInMapper.update(checkIn);
        
        return checkIn;
    }
    
    @Override
    public List<CheckIn> getStayingList() {
        return checkInMapper.getList("staying", null, null);
    }
    
    @Override
    public CheckoutResponse getCheckoutPreview(Long checkInId) {
        CheckIn checkIn = checkInMapper.getById(checkInId);
        if (checkIn == null) {
            throw new RuntimeException("入住记录不存在");
        }
        
        if (!"staying".equals(checkIn.getStatus())) {
            throw new RuntimeException("该客人已退房");
        }
        
        // 计算账单信息
        CheckoutResponse response = new CheckoutResponse();
        CheckoutResponse.BillInfo billInfo = new CheckoutResponse.BillInfo();
        
        // 生成账单编号
        String billNo = "B" + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + 
                       String.format("%04d", checkInId);
        billInfo.setBillNo(billNo);
        
        // 计算房费（简单计算：天数 × 房价）
        long days = calculateDays(checkIn.getActualCheckInTime().toLocalDate(), 
                                 LocalDate.now());
        BigDecimal roomFee = checkIn.getRoomRate().multiply(BigDecimal.valueOf(days));
        billInfo.setRoomFee(roomFee);
        
        // 消费费用（暂设为0，后续需要关联消费记录表）
        billInfo.setConsumptionFee(BigDecimal.ZERO);
        
        // 总金额
        BigDecimal totalAmount = roomFee.add(billInfo.getConsumptionFee());
        billInfo.setTotalAmount(totalAmount);
        
        // 押金
        billInfo.setDepositTotal(checkIn.getDepositTotal() != null ? checkIn.getDepositTotal() : BigDecimal.ZERO);
        
        // 时间
        billInfo.setBillTime(LocalDateTime.now());
        
        response.setBill(billInfo);
        
        // 消费明细（暂为空，后续补充）
        response.setConsumptions(List.of());
        
        return response;
    }
    
    @Transactional
    @Override
    public CheckoutResponse doCheckout(CheckoutRequest request) {
        CheckIn checkIn = checkInMapper.getById(request.getCheckInId());
        if (checkIn == null) {
            throw new RuntimeException("入住记录不存在");
        }
        
        if (!"staying".equals(checkIn.getStatus())) {
            throw new RuntimeException("该客人已退房");
        }
        
        // 获取账单预览
        CheckoutResponse response = getCheckoutPreview(request.getCheckInId());
        CheckoutResponse.BillInfo billInfo = response.getBill();
        
        // 设置实际支付金额
        billInfo.setActualPaid(request.getActualPaid() != null ? request.getActualPaid() : BigDecimal.ZERO);
        
        // 计算找零
        BigDecimal changeAmount = BigDecimal.ZERO;
        if ("refund".equals(request.getDepositHandling())) {
            // 退款：实际支付 - 总金额 + 押金
            changeAmount = billInfo.getActualPaid()
                           .subtract(billInfo.getTotalAmount())
                           .add(billInfo.getDepositTotal());
        } else if ("excess".equals(request.getDepositHandling())) {
            // 补缴：实际支付 - 总金额
            changeAmount = billInfo.getActualPaid().subtract(billInfo.getTotalAmount());
        }
        billInfo.setChangeAmount(changeAmount);
        
        // 更新入住记录
        checkIn.setStatus("checked_out");
        checkIn.setActualCheckOutTime(LocalDateTime.now());
        checkIn.setRemark(request.getRemark());
        checkInMapper.update(checkIn);
        
        // 更新房间状态为空房
        checkInMapper.updateRoomStatus(checkIn.getRoomId(), 1L);
        
        // 设置退房结果
        CheckoutResponse.CheckoutResult result = new CheckoutResponse.CheckoutResult();
        result.setSuccess(true);
        result.setMessage("退房结账成功");
        result.setRoomId(checkIn.getRoomId());
        result.setRoomNo(checkIn.getRoomNo());
        response.setResult(result);
        
        return response;
    }
    
    /**
     * 计算入住天数
     */
    private long calculateDays(LocalDate checkInDate, LocalDate checkOutDate) {
        return Math.max(1, checkOutDate.toEpochDay() - checkInDate.toEpochDay());
    }
    
    /**
     * 生成入住单号
     * 格式: CI + YYYYMMDD + 4位序号
     */
    private String generateCheckInNo() {
        String dateStr = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long maxId = checkInMapper.getMaxIdToday();
        long seq = (maxId == null ? 0 : maxId) + 1;
        return "CI" + dateStr + String.format("%04d", seq);
    }
}