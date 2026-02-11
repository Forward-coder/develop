package com.hotel.manage.mapper;

import com.hotel.manage.entity.RoomType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomTypeMapper {
    int insert(RoomType roomType);
    int update(RoomType roomType);
    int deleteById(Long id);
    RoomType selectById(Long id);
    List<RoomType> selectList(@Param("hotelId") Long hotelId, @Param("isEnabled") Boolean isEnabled);
    List<RoomType> selectListByParams(Map<String, Object> params);
}
