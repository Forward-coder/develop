package com.hotel.manage.service;

import com.hotel.manage.entity.Reservation;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    List<Reservation> getReservationList(Map<String, Object> params);
    Reservation getReservationById(Long id);
    boolean saveReservation(Reservation reservation);
    boolean updateReservation(Reservation reservation);
    boolean deleteReservation(Long id);
    boolean updateReservationStatus(Long id, Long statusId);
}