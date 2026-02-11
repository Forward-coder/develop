package com.hotel.manage.service.impl;

import com.hotel.manage.entity.Guest;
import com.hotel.manage.mapper.GuestMapper;
import com.hotel.manage.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 普通客人服务实现类
 */
@Service
public class GuestServiceImpl implements GuestService {
    
    @Autowired
    private GuestMapper guestMapper;
    
    @Override
    public List<Guest> getGuestList(Map<String, Object> params) {
        return guestMapper.selectList(params);
    }
    
    @Override
    public Guest getGuestById(Long id) {
        return guestMapper.selectById(id);
    }
    
    @Transactional
    @Override
    public boolean saveGuest(Guest guest) {
        // 数据校验
        validateGuest(guest);
        
        if (guest.getId() != null) {
            // 更新
            Guest existing = guestMapper.selectById(guest.getId());
            if (existing == null) {
                throw new RuntimeException("客人信息不存在");
            }
            
            // 检查手机号重复
            if (guest.getPhone() != null && !guest.getPhone().isEmpty()) {
                int phoneCount = guestMapper.existsByPhone(guest.getPhone(), guest.getId());
                if (phoneCount > 0) {
                    throw new RuntimeException("手机号已存在");
                }
            }
            
            // 检查证件号重复
            if (guest.getIdNo() != null && !guest.getIdNo().isEmpty()) {
                int idNoCount = guestMapper.existsByIdNo(guest.getIdNo(), guest.getId());
                if (idNoCount > 0) {
                    throw new RuntimeException("证件号已存在");
                }
            }
            
            return guestMapper.update(guest) > 0;
        } else {
            // 新增
            // 检查手机号重复
            if (guest.getPhone() != null && !guest.getPhone().isEmpty()) {
                int phoneCount = guestMapper.existsByPhone(guest.getPhone(), null);
                if (phoneCount > 0) {
                    throw new RuntimeException("手机号已存在");
                }
            }
            
            // 检查证件号重复
            if (guest.getIdNo() != null && !guest.getIdNo().isEmpty()) {
                int idNoCount = guestMapper.existsByIdNo(guest.getIdNo(), null);
                if (idNoCount > 0) {
                    throw new RuntimeException("证件号已存在");
                }
            }
            
            return guestMapper.insert(guest) > 0;
        }
    }
    
    @Transactional
    @Override
    public boolean deleteGuest(Long id) {
        Guest guest = guestMapper.selectById(id);
        if (guest == null) {
            throw new RuntimeException("客人信息不存在");
        }
        
        return guestMapper.deleteById(id) > 0;
    }
    
    @Override
    public Guest getGuestByPhone(String phone) {
        return guestMapper.selectByPhone(phone);
    }
    
    @Override
    public Guest getGuestByIdNo(String idNo) {
        return guestMapper.selectByIdNo(idNo);
    }
    
    /**
     * 数据校验
     */
    private void validateGuest(Guest guest) {
        if (guest.getName() == null || guest.getName().trim().isEmpty()) {
            throw new RuntimeException("姓名不能为空");
        }
        
        if (guest.getIdTypeId() == null) {
            throw new RuntimeException("请选择证件类型");
        }
    }
}