package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.Guest;
import com.hotel.manage.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 普通客人管理Controller
 */
@RestController
@RequestMapping("/api/guest")
public class GuestController {
    
    @Autowired
    private GuestService guestService;
    
    /**
     * 获取客人列表
     */
    @GetMapping("/list")
    public Result<List<Guest>> getGuestList(@RequestParam Map<String, Object> params) {
        try {
            List<Guest> list = guestService.getGuestList(params);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取客人列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取客人信息
     */
    @GetMapping("/{id}")
    public Result<Guest> getGuestById(@PathVariable Long id) {
        try {
            Guest guest = guestService.getGuestById(id);
            if (guest == null) {
                return Result.error("客人信息不存在");
            }
            return Result.success(guest);
        } catch (Exception e) {
            return Result.error("获取客人信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存客人信息（新增或更新）
     */
    @PostMapping("/save")
    public Result<Guest> saveGuest(@RequestBody Guest guest) {
        try {
            boolean success = guestService.saveGuest(guest);
            if (success) {
                // 如果是新增，重新查询返回完整信息
                if (guest.getId() == null) {
                    // 获取最新插入的记录
                    List<Guest> list = guestService.getGuestList(Map.of());
                    guest = list.get(0); // 获取最新的记录
                }
                return Result.success(guest);
            }
            return Result.error("保存失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除客人信息
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteGuest(@PathVariable Long id) {
        try {
            boolean success = guestService.deleteGuest(id);
            if (success) {
                return Result.success();
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据手机号查找客人
     */
    @GetMapping("/by-phone/{phone}")
    public Result<Guest> getGuestByPhone(@PathVariable String phone) {
        try {
            Guest guest = guestService.getGuestByPhone(phone);
            return Result.success(guest);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据证件号查找客人
     */
    @GetMapping("/by-id-no/{idNo}")
    public Result<Guest> getGuestByIdNo(@PathVariable String idNo) {
        try {
            Guest guest = guestService.getGuestByIdNo(idNo);
            return Result.success(guest);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
}