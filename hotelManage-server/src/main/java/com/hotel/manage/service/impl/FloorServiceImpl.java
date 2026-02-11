package com.hotel.manage.service.impl;

import com.hotel.manage.entity.Floor;
import com.hotel.manage.mapper.FloorMapper;
import com.hotel.manage.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImpl implements FloorService {

    @Autowired
    private FloorMapper floorMapper;

    @Override
    public List<Floor> getFloorList(Long hotelId, Boolean isEnabled) {
        return floorMapper.selectList(hotelId, isEnabled);
    }

    @Override
    public Floor getFloorById(Long id) {
        return floorMapper.selectById(id);
    }

    @Override
    public boolean saveFloor(Floor floor) {
        return floorMapper.insert(floor) > 0;
    }

    @Override
    public boolean updateFloor(Floor floor) {
        return floorMapper.update(floor) > 0;
    }

    @Override
    public boolean deleteFloor(Long id) {
        return floorMapper.deleteById(id) > 0;
    }
}
