package com.hotel.manage.mapper;

import com.hotel.manage.entity.DictReservationStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictReservationStatusMapper {

    @Select("SELECT * FROM dict_reservation_status ORDER BY sort_order")
    List<DictReservationStatus> selectAll();
}
