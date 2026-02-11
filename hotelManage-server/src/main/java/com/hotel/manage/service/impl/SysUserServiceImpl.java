package com.hotel.manage.service.impl;

import com.hotel.manage.entity.SysUser;
import com.hotel.manage.mapper.SysUserMapper;
import com.hotel.manage.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> getUserList(Map<String, Object> params) {
        return sysUserMapper.selectList(params);
    }

    @Override
    public SysUser getUserById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public boolean saveUser(SysUser user) {
        return sysUserMapper.insert(user) > 0;
    }

    @Override
    public boolean updateUser(SysUser user) {
        return sysUserMapper.update(user) > 0;
    }

    @Override
    public boolean deleteUser(Long id) {
        return sysUserMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateUserStatus(Long id, Boolean isEnabled) {
        return sysUserMapper.updateStatus(id, isEnabled) > 0;
    }

    @Override
    public boolean updateUserRoles(Long userId, List<Long> roleIds) {
        // 先删除用户原有的角色关联
        sysUserMapper.deleteUserRoles(userId);
        // 再添加新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                sysUserMapper.insertUserRole(userId, roleId);
            }
        }
        return true;
    }
}
