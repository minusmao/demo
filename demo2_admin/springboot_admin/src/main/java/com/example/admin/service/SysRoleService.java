package com.example.admin.service;

import com.example.admin.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
public interface SysRoleService extends IService<SysRole> {
    // 根据用户 id 查询该用户的所有角色
    List<SysRole> listByUserId(Long userId);
}
