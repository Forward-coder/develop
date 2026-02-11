package com.hotel.manage.service.impl;

import com.hotel.manage.entity.Room;
import com.hotel.manage.mapper.RoomMapper;
import com.hotel.manage.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Room> getRoomList(Map<String, Object> params) {
        return roomMapper.selectList(params);
    }

    @Override
    public Room getRoomById(Long id) {
        return roomMapper.selectById(id);
    }

    @Override
    public boolean saveRoom(Room room) {
        // 如果未指定酒店ID，设置默认值为1（适用于单酒店系统）
        if (room.getHotelId() == null) {
            room.setHotelId(1L);
        }
        // 如果未指定启用状态，默认为启用
        if (room.getIsEnabled() == null) {
            room.setIsEnabled(true);
        }
        // 如果未指定房间状态，默认为空房（状态ID=1）
        if (room.getRoomStatusId() == null) {
            room.setRoomStatusId(1L);
        }
        return roomMapper.insert(room) > 0;
    }

    @Override
    public boolean updateRoom(Room room) {
        return roomMapper.update(room) > 0;
    }

    @Override
    public boolean deleteRoom(Long id) {
        return roomMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateRoomStatus(Long id, Long statusId) {
        return roomMapper.updateStatus(id, statusId) > 0;
    }
}