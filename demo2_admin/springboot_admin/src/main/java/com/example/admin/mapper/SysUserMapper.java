package com.example.admin.mapper;

import com.example.admin.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 通过角色 id 得到拥有此角色的所有用户
    List<SysUser> selectByRoleId(@Param("roleId") Long roleId);
    // 通过菜单 id 得到拥有此菜单的所有用户（注：多个角色会引起查询的用户重复，查询时需要使用 DISTINCT 关键字去重）
    List<SysUser> selectByMenuId(@Param("menuId") Long menuId);
}
