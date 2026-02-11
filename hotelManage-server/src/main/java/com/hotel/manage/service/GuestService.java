package com.hotel.manage.service;

import com.hotel.manage.entity.Guest;
import java.util.List;
import java.util.Map;

/**
 * 普通客人服务接口
 */
public interface GuestService {
    
    /**
     * 获取客人列表
     * @param params 查询参数
     * @return 客人列表
     */
    List<Guest> getGuestList(Map<String, Object> params);
    
    /**
     * 根据ID获取客人信息
     * @param id 客人ID
     * @return 客人信息
     */
    Guest getGuestById(Long id);
    
    /**
     * 保存客人信息（新增或更新）
     * @param guest 客人信息
     * @return 是否成功
     */
    boolean saveGuest(Guest guest);
    
    /**
     * 删除客人信息
     * @param id 客人ID
     * @return 是否成功
     */
    boolean deleteGuest(Long id);
    
    /**
     * 根据手机号查找客人
     * @param phone 手机号
     * @return 客人信息
     */
    Guest getGuestByPhone(String phone);
    
    /**
     * 根据证件号查找客人
     * @param idNo 证件号
     * @return 客人信息
     */
    Guest getGuestByIdNo(String idNo);
}