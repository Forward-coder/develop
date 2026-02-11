package com.hotel.manage.controller;

import com.hotel.manage.common.Result;
import com.hotel.manage.entity.SysRole;
import com.hotel.manage.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/list")
    public Result<List<SysRole>> list(@RequestParam Map<String, Object> params) {
        return Result.success(sysRoleService.getRoleList(params));
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.success(sysRoleService.getRoleById(id));
    }

    @PostMapping("/save")
    public Result<SysRole> save(@RequestBody SysRole role) {
        if (sysRoleService.saveRole(role)) {
            return Result.success(role);
        }
        return Result.error("保存失败");
    }

    @PutMapping("/{id}")
    public Result<SysRole> update(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        if (sysRoleService.updateRole(role)) {
            return Result.success(role);
        }
        return Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (sysRoleService.deleteRole(id)) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @GetMapping("/all")
    public Result<List<SysRole>> getAll() {
        return Result.success(sysRoleService.getAllRoles());
    }
}
