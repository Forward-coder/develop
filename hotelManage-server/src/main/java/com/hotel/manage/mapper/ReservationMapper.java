package com.hotel.manage.mapper;

import com.hotel.manage.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {
    int insert(Reservation reservation);
    int update(Reservation reservation);
    int deleteById(Long id);
    Reservation selectById(Long id);
    List<Reservation> selectList(Map<String, Object> params);
    int updateStatus(@Param("id") Long id, @Param("statusId") Long statusId);
}