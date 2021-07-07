package com.example.admin.service;

import com.example.admin.common.dto.SysMenuDto;
import com.example.admin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
public interface SysUserService extends IService<SysUser> {
    // 通过用户名查找用户
    SysUser getUserByName(String username);

    // 通过 id 查找用户角色、权限信息（已手动增加了 Redis 缓存功能）
    String getUserAuthorityInfo(SysUser sysUser);

    // 删除 “某个用户” 的 “权限信息” 在 Redis 中的缓存（当用户的角色、权限发生改变时，需及时调用此方法清空缓存）
    void clearUserAuthorityInfo(SysUser sysUser);

    // 删除 “所有与该角色关联的用户” 的 “权限信息” 在 Redis 中的缓存（角色的权限发生改变，需及时调用此方法清空 “拥有此角色的用户” 的缓存）
    void clearUserAuthorityInfoByRoleId(Long roleId);

    // 删除 “所有与该菜单权限关联的所有用户” 的 “权限信息“ 在 Redis 中的缓存（菜单发生改变，需及时调用此方法清空 “拥有此菜单的用户” 的缓存）
    void clearUserAuthorityInfoByMenuId(Long menuId);

    // 通过角色 id 得到拥有此角色的所有用户
    List<SysUser> listByRoleId(Long roleId);

    // 通过菜单 id 得到拥有此菜单的所有用户（注：多个角色会引起查询的用户重复，查询时需要使用 DISTINCT 关键字去重）
    List<SysUser> listByMenuId(Long menuId);

}
