package com.hotel.manage.service.impl;

import com.hotel.manage.entity.SysRole;
import com.hotel.manage.mapper.SysRoleMapper;
import com.hotel.manage.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> getRoleList(Map<String, Object> params) {
        return sysRoleMapper.selectList(params);
    }

    @Override
    public SysRole getRoleById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    public boolean saveRole(SysRole role) {
        return sysRoleMapper.insert(role) > 0;
    }

    @Override
    public boolean updateRole(SysRole role) {
        return sysRoleMapper.update(role) > 0;
    }

    @Override
    public boolean deleteRole(Long id) {
        return sysRoleMapper.deleteById(id) > 0;
    }

    @Override
    public List<SysRole> getAllRoles() {
        return sysRoleMapper.selectAll();
    }
}
