package com.hotel.manage.service;

import com.hotel.manage.entity.Floor;
import java.util.List;

public interface FloorService {
    List<Floor> getFloorList(Long hotelId, Boolean isEnabled);
    Floor getFloorById(Long id);
    boolean saveFloor(Floor floor);
    boolean updateFloor(Floor floor);
    boolean deleteFloor(Long id);
}
