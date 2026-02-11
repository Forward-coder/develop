package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.RoomType;
import com.hotel.manage.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room-type")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/list")
    public Result<List<RoomType>> list(@RequestParam Map<String, Object> params) {
        return Result.success(roomTypeService.getRoomTypeList(params));
    }

    @GetMapping("/{id}")
    public Result<RoomType> getById(@PathVariable Long id) {
        return Result.success(roomTypeService.getRoomTypeById(id));
    }

    @PostMapping("/save")
    public Result<RoomType> save(@RequestBody RoomType roomType) {
        if (roomTypeService.saveRoomType(roomType)) {
            return Result.success(roomType);
        }
        return Result.error("保存失败");
    }

    @PutMapping("/{id}")
    public Result<RoomType> update(@PathVariable Long id, @RequestBody RoomType roomType) {
        roomType.setId(id);
        if (roomTypeService.updateRoomType(roomType)) {
            return Result.success(roomType);
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (roomTypeService.deleteRoomType(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
