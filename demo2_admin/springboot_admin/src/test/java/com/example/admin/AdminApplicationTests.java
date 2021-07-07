package com.example.admin;

import com.example.admin.entity.SysMenu;
import com.example.admin.entity.SysRole;
import com.example.admin.entity.SysUser;
import com.example.admin.service.SysMenuService;
import com.example.admin.service.SysRoleService;
import com.example.admin.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    SysRoleService sysRoleService;    // 注入 sysRoleService 层
    @Autowired
    SysMenuService sysMenuService;    // 注入 SysMenuService 层
    @Autowired
    SysUserService sysUserService;    // 注入 SysUserService 层

    @Test
    void contextLoads() {
    }

    // 测试：根据用户 id 查询用户的所有角色
    @Test
    void listRoleByUserIdTest() {
        List<SysRole> sysRoleList = sysRoleService.listByUserId(1L);
        sysRoleList.forEach(sysRole -> System.out.println(sysRole.toString()));
    }

    // 测试：根据用户 id 查询用户所有角色关联的菜单操作
    @Test
    void listMenuByUserIdTest() {
        List<SysMenu> sysMenuList = sysMenuService.listByUserId(1L);
        sysMenuList.forEach(sysMenu -> System.out.println(sysMenu.toString()));
    }

    // 测试：通过角色 id 得到拥有此角色的所有用户
    @Test
    void listUserByRoleIdTest() {
        List<SysUser> sysUserList = sysUserService.listByRoleId(6L);
        sysUserList.forEach(sysUser -> System.out.println(sysUser.toString()));
    }

    // 测试：通过菜单 id 得到拥有此菜单的所有用户
    @Test
    void listUserByMenuIdTest() {
        List<SysUser> sysUserList = sysUserService.listByMenuId(1L);
        sysUserList.forEach(sysUser -> System.out.println(sysUser.toString()));
    }

}
