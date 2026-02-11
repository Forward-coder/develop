package com.hotel.manage.mapper;

import com.hotel.manage.entity.Floor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FloorMapper {
    int insert(Floor floor);
    int update(Floor floor);
    int deleteById(Long id);
    Floor selectById(Long id);
    List<Floor> selectList(@Param("hotelId") Long hotelId, @Param("isEnabled") Boolean isEnabled);
}
