package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.service.*;
import com.example.admin.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件名：BaseController.java
 * 描述：controller 层的公共类
 * 时间：2021-6-27
 * 作者：TechRice
 */
public class BaseController{
    @Autowired
    HttpServletRequest request;    // 注入请求 request 对象
    @Autowired
    RedisUtil redisUtil;           // 注入 RedisUtil 工具对象
    @Autowired
    SysUserService sysUserService;    // 注入 SysUserService 层
    @Autowired
    SysRoleService sysRoleService;    // 注入 SysRoleService 层
    @Autowired
    SysMenuService sysMenuService;    // 注入 SysMenuService 层
    @Autowired
    SysRoleMenuService sysRoleMenuService;    // 注入 SysRoleMenuService 层
    @Autowired
    SysUserRoleService sysUserRoleService;    // 注入 SysUserRoleService 层

    // 处理用户请求的分页参数
    public <T> Page<T> getPage() {
        // 得到请求的参数值
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);    // 页数，默认为1
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);         // 个数，默认为10

        return new Page<T>(current, size);
    }
}
