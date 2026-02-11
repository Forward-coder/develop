package com.hotel.manage.service;

import com.hotel.manage.entity.*;

import java.util.List;

public interface DictService {

    List<DictBedType> getBedTypes();

    List<DictRoomStatus> getRoomStatusList();

    List<DictReservationStatus> getReservationStatusList();

    List<DictReservationChannel> getReservationChannelList();

    List<DictIdType> getIdTypeList();

    List<DictPaymentMethod> getPaymentMethodList();
}
