package com.hotel.manage.service;

import com.hotel.manage.entity.SysRole;
import java.util.List;
import java.util.Map;

public interface SysRoleService {
    List<SysRole> getRoleList(Map<String, Object> params);
    SysRole getRoleById(Long id);
    boolean saveRole(SysRole role);
    boolean updateRole(SysRole role);
    boolean deleteRole(Long id);
    List<SysRole> getAllRoles();
}
