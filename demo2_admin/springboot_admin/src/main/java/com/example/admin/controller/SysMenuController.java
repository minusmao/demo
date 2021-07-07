package com.example.admin.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.admin.common.dto.SysMenuDto;
import com.example.admin.common.lang.Result;
import com.example.admin.entity.SysMenu;
import com.example.admin.entity.SysRoleMenu;
import com.example.admin.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.admin.controller.BaseController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    // 响应前端所需的左侧导航的信息（包括菜单、路由、权限等）
    @RequestMapping("/nav")
    public Result nav(Principal principal) {
        // 通过用户名查找用户（用户名由 security 的 Principal 提供，在 JWT 认证的时候放入的）
        SysUser sysUser = sysUserService.getUserByName(principal.getName());    // 调用 SysUserService 层

        // 获得权限信息
        String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser);    // 返回的格式为：ROLE_admin,ROLE_normal,sys:user:list,....
        String[] authorities = StringUtils.tokenizeToStringArray(authorityInfo, ",");    // 解析为数组

        // 获得导航栏信息，借助 SysMenuDto 对象
        List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();    // 调用 SysUserService 层，且该方法会根据上下文获取当前用户

        return Result.succ(
                // 响应的 data 部分为 Map 结构
                MapUtil.builder()
                        .put("authorities", authorities)    // 权限信息
                        .put("nav", navs)                   // 导航信息
                        .build()
        );
    }

    // 根据 id 响应某个 sys_menu 的详细信息
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result info(@PathVariable(name = "id") Long id) {
        return Result.succ(sysMenuService.getById(id));
    }

    // 响应所有 sys_menu（注意需要根据父子关系返回树状结构）
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result list() {
        return Result.succ(sysMenuService.tree());
    }

    // 响应新增 sys_menu 的请求
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    /* note: @Validated 可以进行实体校验（比如：实体属性配置了 @NotNull 那么用户提交时就不能为空），
             不满足则抛出异常，由 GlobalExceptionHandle 处理 */
    public Result save(@Validated @RequestBody SysMenu sysMenu) {
        // 设置创建时间
        sysMenu.setCreated(LocalDateTime.now());    // 当前时间
        // 保存到数据库
        sysMenuService.save(sysMenu);

        return Result.succ(sysMenu);
    }

    // 响应更新 sys_menu 的请求
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result update(@Validated @RequestBody SysMenu sysMenu) {
        // 设置更新时间
        sysMenu.setUpdated(LocalDateTime.now());    // 当前时间
        // 更新数据库
        sysMenuService.updateById(sysMenu);

        // sys_menu 发生更新，所以需清除与之关联的用户的缓存
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());

        return Result.succ(sysMenu);
    }

    // 根据传入的 id 响应删除 sys_menu 的请求
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @Transactional    // 增加事务功能
    public Result delete(@PathVariable(name = "id") Long id) {
        // 查看子菜单的个数
        int count = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id));// eq 表示：parent_id = id
        if (count > 0) {
            // 如果个数不为零，则响应提示
            return Result.fail("请先删除子菜单");
        }

        // 数据库删除
        sysMenuService.removeById(id);    // sys_menu 表
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", id));    // sys_role_menu 表

        // sys_menu 发生删除，所以需清除与之关联的用户的缓存
        sysUserService.clearUserAuthorityInfoByMenuId(id);

        return Result.succ();
    }

}
