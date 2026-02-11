package com.hotel.manage.mapper;

import com.hotel.manage.entity.DictReservationChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictReservationChannelMapper {

    @Select("SELECT * FROM dict_reservation_channel ORDER BY sort_order")
    List<DictReservationChannel> selectAll();
}
