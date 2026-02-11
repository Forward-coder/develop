package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.CheckIn;
import com.hotel.manage.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 入住管理控制器
 */
@RestController
@RequestMapping("/api/check-in")
public class CheckInController {
    
    @Autowired
    private CheckInService checkInService;
    
    /**
     * 获取入住列表
     */
    @GetMapping("/list")
    public Result<List<CheckIn>> getList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String roomNo,
            @RequestParam(required = false) String guestName) {
        List<CheckIn> list = checkInService.getList(status, roomNo, guestName);
        return Result.success(list);
    }
    
    /**
     * 根据ID获取入住信息
     */
    @GetMapping("/{id}")
    public Result<CheckIn> getById(@PathVariable Long id) {
        CheckIn checkIn = checkInService.getById(id);
        return Result.success(checkIn);
    }
    
    /**
     * 办理入住
     */
    @PostMapping("/check-in")
    public Result<CheckIn> doCheckIn(@RequestBody CheckIn checkIn) {
        CheckIn result = checkInService.doCheckIn(checkIn);
        return Result.success(result);
    }
    
    /**
     * 办理退房
     */
    @PostMapping("/{id}/check-out")
    public Result<CheckIn> doCheckOut(@PathVariable Long id) {
        CheckIn result = checkInService.doCheckOut(id);
        return Result.success(result);
    }
    
    /**
     * 续住
     */
    @PostMapping("/{id}/extend")
    public Result<CheckIn> extendStay(@PathVariable Long id, @RequestBody LocalDate newCheckOutDate) {
        CheckIn result = checkInService.extendStay(id, newCheckOutDate);
        return Result.success(result);
    }
    
    /**
     * 获取在住客人列表
     */
    @GetMapping("/staying")
    public Result<List<CheckIn>> getStayingList() {
        List<CheckIn> list = checkInService.getStayingList();
        return Result.success(list);
    }
}