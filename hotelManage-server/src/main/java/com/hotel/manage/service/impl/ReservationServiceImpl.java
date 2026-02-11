package com.hotel.manage.service.impl;

import com.hotel.manage.entity.Reservation;
import com.hotel.manage.mapper.ReservationMapper;
import com.hotel.manage.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public List<Reservation> getReservationList(Map<String, Object> params) {
        return reservationMapper.selectList(params);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationMapper.selectById(id);
    }

    @Override
    public boolean saveReservation(Reservation reservation) {
        // 如果未指定酒店ID，设置默认值为1
        if (reservation.getHotelId() == null) {
            reservation.setHotelId(1L);
        }
        // 如果未指定预订状态，设置默认值为1（待入住）
        if (reservation.getStatusId() == null) {
            reservation.setStatusId(1L);
        }
        // 生成预订编号
        if (reservation.getReservationNo() == null || reservation.getReservationNo().isEmpty()) {
            String date = java.time.LocalDate.now().toString().replace("-", "");
            // 生成带时间戳的唯一编号
            long timestamp = System.currentTimeMillis() % 1000000; // 取后6位毫秒数
            reservation.setReservationNo("RSV" + date + String.format("%06d", timestamp));
        }
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationMapper.insert(reservation) > 0;
    }

    @Override
    public boolean updateReservation(Reservation reservation) {
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationMapper.update(reservation) > 0;
    }

    @Override
    public boolean deleteReservation(Long id) {
        return reservationMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateReservationStatus(Long id, Long statusId) {
        return reservationMapper.updateStatus(id, statusId) > 0;
    }
}