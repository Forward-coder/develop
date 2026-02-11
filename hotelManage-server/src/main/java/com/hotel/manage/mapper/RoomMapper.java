package com.hotel.manage.mapper;

import com.hotel.manage.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper {
    int insert(Room room);
    int update(Room room);
    int deleteById(Long id);
    Room selectById(Long id);
    List<Room> selectList(Map<String, Object> params);
    int updateStatus(@Param("id") Long id, @Param("statusId") Long statusId);
}