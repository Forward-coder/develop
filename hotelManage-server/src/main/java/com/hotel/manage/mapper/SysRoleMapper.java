package com.hotel.manage.mapper;

import com.hotel.manage.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleMapper {
    int insert(SysRole role);
    int update(SysRole role);
    int deleteById(Long id);
    SysRole selectById(Long id);
    List<SysRole> selectList(Map<String, Object> params);
    List<SysRole> selectAll();
}
