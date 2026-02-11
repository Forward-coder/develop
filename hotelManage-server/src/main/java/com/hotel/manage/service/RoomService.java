package com.hotel.manage.service;

import com.hotel.manage.entity.Room;
import java.util.List;
import java.util.Map;

public interface RoomService {
    List<Room> getRoomList(Map<String, Object> params);
    Room getRoomById(Long id);
    boolean saveRoom(Room room);
    boolean updateRoom(Room room);
    boolean deleteRoom(Long id);
    boolean updateRoomStatus(Long id, Long statusId);
}