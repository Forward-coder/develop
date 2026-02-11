package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.RoomTypePrice;
import com.hotel.manage.service.RoomTypePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/room-price")
public class RoomTypePriceController {

    @Autowired
    private RoomTypePriceService roomTypePriceService;

    @GetMapping("/list/{roomTypeId}")
    public Result<List<RoomTypePrice>> listByRoomType(@PathVariable Long roomTypeId) {
        return Result.success(roomTypePriceService.getPricesByRoomType(roomTypeId));
    }

    @GetMapping("/effective")
    public Result<RoomTypePrice> getEffectivePrice(@RequestParam Long roomTypeId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                   @RequestParam String priceType) {
        return Result.success(roomTypePriceService.getEffectivePrice(roomTypeId, date, priceType));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody RoomTypePrice price) {
        if (roomTypePriceService.savePrice(price)) {
            return Result.success();
        }
        return Result.error("保存失败");
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody RoomTypePrice price) {
        if (roomTypePriceService.updatePrice(price)) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (roomTypePriceService.deletePrice(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
