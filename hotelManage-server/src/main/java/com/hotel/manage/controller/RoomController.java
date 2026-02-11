package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.Room;
import com.hotel.manage.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/list")
    public Result<List<Room>> list(@RequestParam Map<String, Object> params) {
        return Result.success(roomService.getRoomList(params));
    }

    @GetMapping("/{id}")
    public Result<Room> getById(@PathVariable Long id) {
        return Result.success(roomService.getRoomById(id));
    }

    @PostMapping("/save")
    public Result<Room> save(@RequestBody Room room) {
        if (roomService.saveRoom(room)) {
            return Result.success(room);
        }
        return Result.error("保存失败");
    }

    @PutMapping("/{id}")
    public Result<Room> update(@PathVariable Long id, @RequestBody Room room) {
        room.setId(id);
        if (roomService.updateRoom(room)) {
            return Result.success(room);
        }
        return Result.error("更新失败");
    }

    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Long statusId) {
        if (roomService.updateRoomStatus(id, statusId)) {
            return Result.success();
        }
        return Result.error("状态更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (roomService.deleteRoom(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}