package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.Reservation;
import com.hotel.manage.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/list")
    public Result<List<Reservation>> list(@RequestParam Map<String, Object> params) {
        return Result.success(reservationService.getReservationList(params));
    }

    @GetMapping("/{id}")
    public Result<Reservation> getById(@PathVariable Long id) {
        return Result.success(reservationService.getReservationById(id));
    }

    @PostMapping("/save")
    public Result<Reservation> save(@RequestBody Reservation reservation) {
        if (reservationService.saveReservation(reservation)) {
            return Result.success(reservation);
        }
        return Result.error("保存失败");
    }

    @PutMapping("/{id}")
    public Result<Reservation> update(@PathVariable Long id, @RequestBody Reservation reservation) {
        reservation.setId(id);
        if (reservationService.updateReservation(reservation)) {
            return Result.success(reservation);
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (reservationService.deleteReservation(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Long statusId) {
        if (reservationService.updateReservationStatus(id, statusId)) {
            return Result.success();
        }
        return Result.error("状态更新失败");
    }
}