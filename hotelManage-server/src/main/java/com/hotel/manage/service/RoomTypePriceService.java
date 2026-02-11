package com.hotel.manage.service;

import com.hotel.manage.entity.RoomTypePrice;
import java.time.LocalDate;
import java.util.List;

public interface RoomTypePriceService {
    List<RoomTypePrice> getPricesByRoomType(Long roomTypeId);
    RoomTypePrice getEffectivePrice(Long roomTypeId, LocalDate date, String priceType);
    boolean savePrice(RoomTypePrice price);
    boolean updatePrice(RoomTypePrice price);
    boolean deletePrice(Long id);
}
