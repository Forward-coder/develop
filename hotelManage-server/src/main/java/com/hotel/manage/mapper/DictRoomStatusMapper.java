package com.hotel.manage.mapper;

import com.hotel.manage.entity.DictRoomStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictRoomStatusMapper {

    @Select("SELECT * FROM dict_room_status ORDER BY sort_order")
    List<DictRoomStatus> selectAll();
}
