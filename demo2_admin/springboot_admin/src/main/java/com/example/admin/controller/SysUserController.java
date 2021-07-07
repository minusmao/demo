package com.example.admin.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.common.dto.PasswordDto;
import com.example.admin.common.lang.Const;
import com.example.admin.common.lang.Result;
import com.example.admin.entity.SysUser;
import com.example.admin.entity.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;    // 密码加密对象

    // 根据 id 响应某个 sys_user 的详细信息
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result info(@PathVariable("id") Long id) {
        // 查询
        SysUser sysUser = sysUserService.getById(id);
        Assert.notNull(sysUser, "找不到该管理员");

        // 得到该用户拥有的 SysRole 对象
        sysUser.setSysRoles(sysRoleService.listByUserId(id));

        return Result.succ(sysUser);
    }

    // 根据传入的用户名 name （可能为空）和分页信息（由 this.getPage() 方法统一处理），响应 sys_user 列表
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result list(String username) {
        // 查询
        Page<SysUser> sysUserPage = sysUserService.page(this.getPage(),
                new QueryWrapper<SysUser>().like(StrUtil.isNotBlank(username), "username", username)    // 当 name 不为空时，才使用此条件
        );

        // 得到每个用户拥有的 SysRole 对象
        sysUserPage.getRecords().forEach(sysUser -> {
            sysUser.setSysRoles(sysRoleService.listByUserId(sysUser.getId()));
        });

        return Result.succ(sysUserPage);
    }

    // 响应新增 sys_user 的请求
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result save(@Validated @RequestBody SysUser sysUser) {
        // 判断用户名是否已存在
        if (sysUserService.getUserByName(sysUser.getUsername()) != null) {
            return Result.fail("该用户名已存在");
        }
        // 设置信息
        sysUser.setCreated(LocalDateTime.now());        // 创建时间
        sysUser.setAvatar(Const.DEFAULT_AVATAR);        // 默认头像
        sysUser.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));    // 默认密码：888888
        // 保存到数据库
        sysUserService.save(sysUser);

        return Result.succ(sysUser);
    }

    // 响应更新 sys_user 的请求
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result update(@Validated @RequestBody SysUser sysUser) {
        // 判断用户名是否已存在，如果用户更新了用户名，不能与其他用户相同
        SysUser anotherSysUser = sysUserService.getUserByName(sysUser.getUsername());
        if (anotherSysUser != null) {
            // 如果根据用户名查出的 id 与本身 id 不同，说明该用户名已被其他用户使用
            if (!anotherSysUser.getId().equals(sysUser.getId())) {
                return Result.fail("该用户名已存在");
            }
        }
        // 设置信息
        sysUser.setUpdated(LocalDateTime.now());    // 更新时间
        // 更新到数据库
        sysUserService.updateById(sysUser);

        // sys_user 发生更新，删除该用户的缓存
        sysUserService.clearUserAuthorityInfo(sysUser);

        return Result.succ(sysUser);
    }

    // 响应删除 sys_user 的请求（传入 ids 数组，批量删除）
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @Transactional   // 增加事务功能
    public Result delete(@RequestBody Long[] ids) {
        // ids 转化为 list 集合
        List<Long> idList = Arrays.asList(ids);

        // 清除缓存（因为需要从数据库中获得 SysUser 对象，所以不能删除数据库后，再清除缓存）
        idList.forEach(id -> {
            SysUser sysUser = sysUserService.getById(id);      // 得到 SysUser 对象
            sysUserService.clearUserAuthorityInfo(sysUser);    // 清除缓存
        });

        // 删除
        sysUserService.removeByIds(idList);    // sys_user 表
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", idList));    // sys_user_role 表

        return Result.succ();
    }

    // 响应给用户分配角色
    @PostMapping("/role/{userId}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    public Result role(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
        // 创建 SysUserRole 集合
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        // 根据 roleIds 和 suerId 创建 SysUserRole 对象并存入集合
        Arrays.asList(roleIds).forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);

            sysUserRoleList.add(sysUserRole);
        });

        // 删除 sys_user_role 表中与 userId 关联的所有数据
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));

        // 将新的角色数据放入 sys_user_role 表中
        sysUserRoleService.saveBatch(sysUserRoleList);

        // 清除该用户的缓存
        sysUserService.clearUserAuthorityInfo(sysUserService.getById(userId));

        return Result.succ();
    }

    // 根据传入的 id 重置该用户的密码 888888
    @PostMapping("/repass")
    @PreAuthorize("hasAuthority('sys:user:repass')")
    public Result repass(@RequestBody Long id) {
        // 根据 id 得到 SysUser 对象
        SysUser sysUser = sysUserService.getById(id);

        // 重置密码
        sysUser.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));    // 默认密码 888888
        sysUser.setUpdated(LocalDateTime.now());    // 更新时间
        sysUserService.updateById(sysUser);

        return Result.succ();
    }

    // 更新密码，通过上下文获得当前登陆的用户名（通过 security 的 Principal 对象）
    @PostMapping("updatePass")
    public Result updatePass(@Validated @RequestBody PasswordDto passwordDto, Principal principal) {
        // 通过用户名获取当前登陆的用户
        SysUser sysUser = sysUserService.getUserByName(principal.getName());

        // 校验旧密码是否正确
        boolean matches = passwordEncoder.matches(passwordDto.getCurrentPass(), sysUser.getPassword());
        if (!matches) {
            return Result.fail("旧密码错误");
        }

        // 更新密码
        sysUser.setPassword(passwordEncoder.encode(passwordDto.getPassword()));    // 更新密码
        sysUser.setUpdated(LocalDateTime.now());    // 更新时间
        sysUserService.updateById(sysUser);

        return Result.succ();
    }

}
