package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.Floor;
import com.hotel.manage.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/floor")
public class FloorController {

    @Autowired
    private FloorService floorService;

    @GetMapping("/list")
    public Result<List<Floor>> list(@RequestParam(required = false) Long hotelId,
                                    @RequestParam(required = false) Boolean isEnabled) {
        return Result.success(floorService.getFloorList(hotelId, isEnabled));
    }

    @GetMapping("/{id}")
    public Result<Floor> getById(@PathVariable Long id) {
        return Result.success(floorService.getFloorById(id));
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Floor floor) {
        if (floorService.saveFloor(floor)) {
            return Result.success();
        }
        return Result.error("保存失败");
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Floor floor) {
        if (floorService.updateFloor(floor)) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (floorService.deleteFloor(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }
}
