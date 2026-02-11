package com.hotel.manage.service.impl;

import com.hotel.manage.entity.RoomTypePrice;
import com.hotel.manage.mapper.RoomTypePriceMapper;
import com.hotel.manage.service.RoomTypePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomTypePriceServiceImpl implements RoomTypePriceService {

    @Autowired
    private RoomTypePriceMapper roomTypePriceMapper;

    @Override
    public List<RoomTypePrice> getPricesByRoomType(Long roomTypeId) {
        return roomTypePriceMapper.selectByRoomTypeId(roomTypeId);
    }

    @Override
    public RoomTypePrice getEffectivePrice(Long roomTypeId, LocalDate date, String priceType) {
        return roomTypePriceMapper.selectEffectivePrice(roomTypeId, date, priceType);
    }

    @Override
    public boolean savePrice(RoomTypePrice price) {
        return roomTypePriceMapper.insert(price) > 0;
    }

    @Override
    public boolean updatePrice(RoomTypePrice price) {
        return roomTypePriceMapper.update(price) > 0;
    }

    @Override
    public boolean deletePrice(Long id) {
        return roomTypePriceMapper.deleteById(id) > 0;
    }
}
