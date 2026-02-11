package com.hotel.manage.service.impl;

import com.hotel.manage.entity.*;
import com.hotel.manage.mapper.*;
import com.hotel.manage.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictBedTypeMapper bedTypeMapper;

    @Autowired
    private DictRoomStatusMapper roomStatusMapper;

    @Autowired
    private DictReservationStatusMapper reservationStatusMapper;

    @Autowired
    private DictReservationChannelMapper reservationChannelMapper;

    @Autowired
    private DictIdTypeMapper idTypeMapper;

    @Autowired
    private DictPaymentMethodMapper paymentMethodMapper;

    @Override
    public List<DictBedType> getBedTypes() {
        return bedTypeMapper.selectAll();
    }

    @Override
    public List<DictRoomStatus> getRoomStatusList() {
        return roomStatusMapper.selectAll();
    }

    @Override
    public List<DictReservationStatus> getReservationStatusList() {
        return reservationStatusMapper.selectAll();
    }

    @Override
    public List<DictReservationChannel> getReservationChannelList() {
        return reservationChannelMapper.selectAll();
    }

    @Override
    public List<DictIdType> getIdTypeList() {
        return idTypeMapper.selectAll();
    }

    @Override
    public List<DictPaymentMethod> getPaymentMethodList() {
        return paymentMethodMapper.selectAll();
    }
}
