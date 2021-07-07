package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.admin.common.dto.SysMenuDto;
import com.example.admin.entity.SysMenu;
import com.example.admin.entity.SysRole;
import com.example.admin.entity.SysUser;
import com.example.admin.mapper.SysUserMapper;
import com.example.admin.service.SysMenuService;
import com.example.admin.service.SysRoleService;
import com.example.admin.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    /* 对象属性 */
    @Autowired
    private RedisUtil redisUtil;              // 注入 RedisUtil 工具对象
    @Autowired
    private SysUserMapper sysUserMapper;      // 注入 Mapper 层
    @Autowired
    private SysRoleService sysRoleService;    // 注入 SysRoleService 层
    @Autowired
    private SysMenuService sysMenuService;    // 注入 SysMenuService 层

    /* 对象方法 */
    // 通过用户名查找用户
    @Override
    public SysUser getUserByName(String username) {
        // 返回查到的 SysUser 对象
        return this.getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    // 通过用户 id 查找用户角色、权限信息
    @Override
    public String getUserAuthorityInfo(SysUser sysUser) {
        // 要返回的权限信息，格式为：ROLE_admin,ROLE_normal,sys:user:list,....
        StringBuilder authority = new StringBuilder();

        // 查看缓存
        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            /* 有缓存，则直接取出 */
            authority.append((String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername()));
        } else {
            /* 没有缓存，则从数据库取出，并存入缓存 */

            // 1、获得用户的所有角色
            List<SysRole> sysRoleList = sysRoleService.listByUserId(sysUser.getId());    // 通过用户 id 查询
            // 2、如果角色个数大于零，则将角色编码添加到 authority 中，格式为：ROLE_admin,ROLE_normal
            if (sysRoleList.size() > 0) {
                authority.append(
                        // 使用 stream 流的方式添加
                        sysRoleList.stream()
                                .map(sysRole -> "ROLE_" + sysRole.getCode())
                                .collect(Collectors.joining(","))
                );
            }

            // 3、获得用户所有角色关联的菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
            List<SysMenu> sysMenuList = sysMenuService.listByUserId(sysUser.getId());    // 通过用户 id 查询
            // 4、如果菜单操作个数大于零，则将权限编码添加到 authority 中，格式为：sys:user:list,....
            if (sysMenuList.size() > 0) {
                // 和前面的角色编码用 “,” 隔开
                if (authority.length() > 0) {
                    authority.append(",");
                }
                // 添加
                authority.append(
                        // 使用 stream 流的方式添加
                        sysMenuList.stream()
                                .map(SysMenu::getPerms)
                                .collect(Collectors.joining(","))
                );
            }

            // 5、放入缓存
            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(), authority.toString());
        }

        return authority.toString();
    }

    // 删除 “某个用户” 的 “权限信息” 在 Redis 中的缓存（当用户的角色、权限发生改变时，需及时调用此方法清空缓存）
    @Override
    public void clearUserAuthorityInfo(SysUser sysUser) {
        redisUtil.del("GrantedAuthority:" + sysUser.getUsername());
    }

    // 删除 “所有与该角色关联的用户” 的 “权限信息” 在 Redis 中的缓存（角色的权限发生改变，需及时调用此方法清空 “拥有此角色的用户” 的缓存）
    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        // 通过角色 id 得到拥有此角色的所有用户
        List<SysUser> sysUserList = this.listByRoleId(roleId);    // 通过角色 id 查询
        // 循环清除用户的缓存
        sysUserList.forEach(this::clearUserAuthorityInfo);
    }

    // 删除 “所有与该菜单权限关联的所有用户” 的 “权限信息“ 在 Redis 中的缓存（菜单发生改变，需及时调用此方法清空 “拥有此菜单的用户” 的缓存）
    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        // 通过菜单 id 得到拥有此菜单的所有用户
        List<SysUser> sysUserList = this.listByMenuId(menuId);    // 通过菜单 id 查询
        // 循环清除用户的缓存
        sysUserList.forEach(this::clearUserAuthorityInfo);
    }

    // 通过角色 id 得到拥有此角色的所有用户
    @Override
    public List<SysUser> listByRoleId(Long roleId) {
        return sysUserMapper.selectByRoleId(roleId);
    }

    // 通过菜单 id 得到拥有此菜单的所有用户（注：多个角色会引起查询的用户重复，查询时需要使用 DISTINCT 关键字去重）
    @Override
    public List<SysUser> listByMenuId(Long menuId) {
        return sysUserMapper.selectByMenuId(menuId);
    }

}
