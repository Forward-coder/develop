package com.hotel.manage.service;

import com.hotel.manage.entity.RoomType;
import java.util.List;
import java.util.Map;

public interface RoomTypeService {
    List<RoomType> getRoomTypeList(Long hotelId, Boolean isEnabled);
    List<RoomType> getRoomTypeList(Map<String, Object> params);
    RoomType getRoomTypeById(Long id);
    boolean saveRoomType(RoomType roomType);
    boolean updateRoomType(RoomType roomType);
    boolean deleteRoomType(Long id);
}
