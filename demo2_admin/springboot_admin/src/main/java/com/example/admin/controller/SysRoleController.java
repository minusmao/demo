package com.example.admin.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.common.lang.Const;
import com.example.admin.common.lang.Result;
import com.example.admin.entity.SysRole;
import com.example.admin.entity.SysRoleMenu;
import com.example.admin.entity.SysUserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.admin.controller.BaseController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {
    // 根据 id 响应某个 sys_role 的详细信息
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result info(@PathVariable("id") Long id) {
        // 根据 id 查询
        SysRole sysRole = sysRoleService.getById(id);

        /* 得到该角色拥有的 menuId */
        // 查询出该 role_id 对应的 SysRoleMenu 对象
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        // 取出 SysRoleMenu 对象的 menuId 放入 sysRole 的 menuIds
        sysRoleMenuList.stream()
                .map(SysRoleMenu::getMenuId)
                .forEach(menuId -> sysRole.getMenuIds().add(menuId));

        return Result.succ(sysRole);
    }

    // 根据传入的用户名 name （可能为空）和分页信息（由 this.getPage() 方法统一处理），响应 sys_role 列表
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list(String name) {
        // 查询
        Page<SysRole> sysRolePage = sysRoleService.page(this.getPage(),
                new QueryWrapper<SysRole>().like(StrUtil.isNotBlank(name), "name", name)    // 当 name 不为空时，才使用此条件
        );

        return Result.succ(sysRolePage);
    }

    // 响应新增 sys_role 的请求
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@Validated @RequestBody SysRole sysRole) {
        // 设置信息
        sysRole.setCreated(LocalDateTime.now());    // 创建时间
        // 保存到数据库
        sysRoleService.save(sysRole);

        return Result.succ(sysRole);
    }

    // 响应更新 sys_role 的请求
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@Validated @RequestBody SysRole sysRole) {
        // 设置信息
        sysRole.setUpdated(LocalDateTime.now());    // 更新时间
        // 保存到数据库
        sysRoleService.updateById(sysRole);

        // sys_role 发生更新，所以需清除与之关联的用户的缓存
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());

        return Result.succ(sysRole);
    }

    // 响应删除 sys_role 的请求（传入 ids 数组，批量删除）
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional    // 增加事务功能
    public Result delete(@RequestBody Long[] ids) {
        // ids 数组转化为集合
        List<Long> idList = Arrays.asList(ids);

        // 删除
        sysRoleService.removeByIds(idList);    // 表 sys_role

        // 删除中间表
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", idList));    // 表 sys_role_menu
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", idList));    // 表 sys_user_role

        // sys_role 发生更新，所以需清除与之关联的用户的缓存
        idList.forEach(id -> sysUserService.clearUserAuthorityInfoByRoleId(id));

        return Result.succ();
    }

    // 响应给角色分配权限
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @Transactional    // 增加事务功能
    public Result perm(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds) {
        // 定义 SysRoleMenu 对象集合
        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        // 根据 menuIds 创建多个 SysRoleMenu 对象，并放入集合
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);

            sysRoleMenuList.add(sysRoleMenu);
        });

        // 在 sys_role_menu 表中删除与 roleId 关联的所有权限数据
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));

        // 将新的权限数据存入数据库
        sysRoleMenuService.saveBatch(sysRoleMenuList);

        // 该角色权限发生变更，所以需清除与之关联的用户的缓存
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.succ();
    }

}
