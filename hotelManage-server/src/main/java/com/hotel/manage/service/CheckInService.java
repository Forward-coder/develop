package com.hotel.manage.service;

import com.hotel.manage.dto.CheckoutRequest;
import com.hotel.manage.dto.CheckoutResponse;
import com.hotel.manage.entity.CheckIn;

import java.time.LocalDate;
import java.util.List;

/**
 * 入住服务接口
 */
public interface CheckInService {
    
    /**
     * 获取入住列表
     * @param status 状态筛选
     * @param roomNo 房号筛选
     * @param guestName 客人姓名筛选
     * @return 入住列表
     */
    List<CheckIn> getList(String status, String roomNo, String guestName);
    
    /**
     * 根据ID获取入住信息
     * @param id 入住ID
     * @return 入住信息
     */
    CheckIn getById(Long id);
    
    /**
     * 办理入住
     * @param checkIn 入住信息
     * @return 入住信息
     */
    CheckIn doCheckIn(CheckIn checkIn);
    
    /**
     * 办理退房
     * @param id 入住ID
     * @return 退房结果
     */
    CheckIn doCheckOut(Long id);
    
    /**
     * 续住
     * @param id 入住ID
     * @param newCheckOutDate 新的退房日期
     * @return 更新后的入住信息
     */
    CheckIn extendStay(Long id, LocalDate newCheckOutDate);
    
    /**
     * 获取在住客人列表
     * @return 在住客人列表
     */
    List<CheckIn> getStayingList();
    
    /**
     * 获取退房预览信息（账单计算）
     * @param checkInId 入住ID
     * @return 退房预览信息
     */
    CheckoutResponse getCheckoutPreview(Long checkInId);
    
    /**
     * 办理退房结账
     * @param request 退房请求
     * @return 退房结果
     */
    CheckoutResponse doCheckout(CheckoutRequest request);
}