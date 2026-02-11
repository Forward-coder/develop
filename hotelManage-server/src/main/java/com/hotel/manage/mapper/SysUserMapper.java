package com.hotel.manage.mapper;

import com.hotel.manage.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserMapper {
    int insert(SysUser user);
    int update(SysUser user);
    int deleteById(Long id);
    SysUser selectById(Long id);
    List<SysUser> selectList(Map<String, Object> params);
    int updateStatus(@Param("id") Long id, @Param("isEnabled") Boolean isEnabled);
    int deleteUserRoles(@Param("userId") Long userId);
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
