package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.dto.CheckoutRequest;
import com.hotel.manage.dto.CheckoutResponse;
import com.hotel.manage.entity.CheckIn;
import com.hotel.manage.service.CheckInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 退房结账Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    
    private final CheckInService checkInService;
    
    /**
     * 获取待退房客人列表
     */
    @GetMapping("/pending")
    public Result<List<CheckIn>> getPendingCheckoutList() {
        try {
            List<CheckIn> list = checkInService.getStayingList();
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取待退房客人列表失败", e);
            return Result.error("获取待退房客人列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据入住ID获取退房详情（账单预览）
     */
    @GetMapping("/{checkInId}/preview")
    public Result<CheckoutResponse> getCheckoutPreview(@PathVariable Long checkInId) {
        try {
            CheckoutResponse response = checkInService.getCheckoutPreview(checkInId);
            return Result.success(response);
        } catch (Exception e) {
            log.error("获取退房预览信息失败，入住ID: {}", checkInId, e);
            return Result.error("获取退房预览信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 办理退房结账
     */
    @PostMapping("/checkout")
    public Result<CheckoutResponse> doCheckout(@RequestBody CheckoutRequest request) {
        try {
            CheckoutResponse response = checkInService.doCheckout(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("办理退房结账失败", e);
            return Result.error("办理退房结账失败: " + e.getMessage());
        }
    }
}