package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.*;
import com.hotel.manage.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/bed-types")
    public Result<List<DictBedType>> getBedTypes() {
        return Result.success(dictService.getBedTypes());
    }

    @GetMapping("/room-status")
    public Result<List<DictRoomStatus>> getRoomStatusList() {
        return Result.success(dictService.getRoomStatusList());
    }

    @GetMapping("/reservation-status")
    public Result<List<DictReservationStatus>> getReservationStatusList() {
        return Result.success(dictService.getReservationStatusList());
    }

    @GetMapping("/reservation-channel")
    public Result<List<DictReservationChannel>> getReservationChannelList() {
        return Result.success(dictService.getReservationChannelList());
    }

    @GetMapping("/id-type")
    public Result<List<DictIdType>> getIdTypeList() {
        return Result.success(dictService.getIdTypeList());
    }

    @GetMapping("/payment-method")
    public Result<List<DictPaymentMethod>> getPaymentMethodList() {
        return Result.success(dictService.getPaymentMethodList());
    }
}
