package com.hotel.manage.mapper;

import com.hotel.manage.entity.Guest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 客人Mapper接口
 */
@Mapper
public interface GuestMapper {
    
    /**
     * 获取客人列表
     * @param params 查询参数
     * @return 客人列表
     */
    List<Guest> selectList(Map<String, Object> params);
    
    /**
     * 根据ID获取客人信息
     * @param id 客人ID
     * @return 客人信息
     */
    Guest selectById(Long id);
    
    /**
     * 插入客人信息
     * @param guest 客人信息
     * @return 影响行数
     */
    int insert(Guest guest);
    
    /**
     * 更新客人信息
     * @param guest 客人信息
     * @return 影响行数
     */
    int update(Guest guest);
    
    /**
     * 删除客人信息
     * @param id 客人ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    /**
     * 根据手机号查找客人
     * @param phone 手机号
     * @return 客人信息
     */
    Guest selectByPhone(String phone);
    
    /**
     * 根据证件号查找客人
     * @param idNo 证件号
     * @return 客人信息
     */
    Guest selectByIdNo(String idNo);
    
    /**
     * 检查手机号是否存在（排除指定ID）
     * @param phone 手机号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    int existsByPhone(@Param("phone") String phone, @Param("excludeId") Long excludeId);
    
    /**
     * 检查证件号是否存在（排除指定ID）
     * @param idNo 证件号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    int existsByIdNo(@Param("idNo") String idNo, @Param("excludeId") Long excludeId);
}