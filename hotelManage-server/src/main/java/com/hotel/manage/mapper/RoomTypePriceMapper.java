package com.hotel.manage.mapper;

import com.hotel.manage.entity.RoomTypePrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RoomTypePriceMapper {
    int insert(RoomTypePrice price);
    int update(RoomTypePrice price);
    int deleteById(Long id);
    RoomTypePrice selectById(Long id);
    List<RoomTypePrice> selectByRoomTypeId(@Param("roomTypeId") Long roomTypeId);
    RoomTypePrice selectEffectivePrice(@Param("roomTypeId") Long roomTypeId, @Param("date") LocalDate date, @Param("priceType") String priceType);
}
