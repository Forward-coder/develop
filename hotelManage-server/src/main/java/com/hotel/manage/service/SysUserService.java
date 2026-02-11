package com.hotel.manage.service;

import com.hotel.manage.entity.SysUser;
import java.util.List;
import java.util.Map;

public interface SysUserService {
    List<SysUser> getUserList(Map<String, Object> params);
    SysUser getUserById(Long id);
    boolean saveUser(SysUser user);
    boolean updateUser(SysUser user);
    boolean deleteUser(Long id);
    boolean updateUserStatus(Long id, Boolean isEnabled);
    boolean updateUserRoles(Long userId, List<Long> roleIds);
}
