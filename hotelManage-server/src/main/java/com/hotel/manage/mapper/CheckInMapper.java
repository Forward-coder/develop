package com.hotel.manage.mapper;

import com.hotel.manage.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入住Mapper接口
 */
@Mapper
public interface CheckInMapper {
    
    /**
     * 获取入住列表
     * @param status 状态筛选
     * @param roomNo 房号筛选
     * @param guestName 客人姓名筛选
     * @return 入住列表
     */
    List<CheckIn> getList(@Param("status") String status, 
                         @Param("roomNo") String roomNo, 
                         @Param("guestName") String guestName);
    
    /**
     * 根据ID获取入住信息
     * @param id 入住ID
     * @return 入住信息
     */
    CheckIn getById(Long id);
    
    /**
     * 插入入住记录
     * @param checkIn 入住信息
     * @return 影响行数
     */
    int insert(CheckIn checkIn);
    
    /**
     * 更新入住记录
     * @param checkIn 入住信息
     * @return 影响行数
     */
    int update(CheckIn checkIn);
    
    /**
     * 获取今日最大ID用于生成单号
     * @return 最大ID
     */
    Long getMaxIdToday();
    
    /**
     * 更新房间状态
     * @param roomId 房间ID
     * @param statusId 状态ID
     * @return 影响行数
     */
    int updateRoomStatus(@Param("roomId") Long roomId, @Param("statusId") Long statusId);
}