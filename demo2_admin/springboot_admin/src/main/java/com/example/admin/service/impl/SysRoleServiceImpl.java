package com.example.admin.service.impl;

import com.example.admin.entity.SysRole;
import com.example.admin.mapper.SysRoleMapper;
import com.example.admin.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    /* 对象属性 */
    @Autowired
    private SysRoleMapper sysRoleMapper;    // 注入 Mapper 层

    /* 对象方法 */
    // 根据用户 id 查询该用户的所有角色
    @Override
    public List<SysRole> listByUserId(Long userId) {
        return sysRoleMapper.selectByUserId(userId);
    }
}
