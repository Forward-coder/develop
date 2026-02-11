package com.hotel.manage.service.impl;

import com.hotel.manage.entity.RoomType;
import com.hotel.manage.mapper.RoomTypeMapper;
import com.hotel.manage.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Override
    public List<RoomType> getRoomTypeList(Long hotelId, Boolean isEnabled) {
        return roomTypeMapper.selectList(hotelId, isEnabled);
    }

    @Override
    public List<RoomType> getRoomTypeList(Map<String, Object> params) {
        return roomTypeMapper.selectListByParams(params);
    }

    @Override
    public RoomType getRoomTypeById(Long id) {
        return roomTypeMapper.selectById(id);
    }

    @Override
    public boolean saveRoomType(RoomType roomType) {
        // 如果未指定酒店ID，设置默认值为1（适用于单酒店系统）
        if (roomType.getHotelId() == null) {
            roomType.setHotelId(1L);
        }
        // 如果未指定启用状态，默认为启用
        if (roomType.getIsEnabled() == null) {
            roomType.setIsEnabled(true);
        }
        return roomTypeMapper.insert(roomType) > 0;
    }

    @Override
    public boolean updateRoomType(RoomType roomType) {
        return roomTypeMapper.update(roomType) > 0;
    }

    @Override
    public boolean deleteRoomType(Long id) {
        return roomTypeMapper.deleteById(id) > 0;
    }
}
