package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.SysUser;
import com.hotel.manage.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public Result<List<SysUser>> list(@RequestParam Map<String, Object> params) {
        return Result.success(sysUserService.getUserList(params));
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getUserById(id));
    }

    @PostMapping("/save")
    public Result<SysUser> save(@RequestBody SysUser user) {
        if (sysUserService.saveUser(user)) {
            return Result.success(user);
        }
        return Result.error("保存失败");
    }

    @PutMapping("/{id}")
    public Result<SysUser> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        if (sysUserService.updateUser(user)) {
            return Result.success(user);
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (sysUserService.deleteUser(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @PatchMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Boolean isEnabled) {
        if (sysUserService.updateUserStatus(id, isEnabled)) {
            return Result.success();
        }
        return Result.error("状态更新失败");
    }

    @PatchMapping("/{id}/roles")
    public Result<Void> updateRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        if (sysUserService.updateUserRoles(id, roleIds)) {
            return Result.success();
        }
        return Result.error("角色更新失败");
    }
}
