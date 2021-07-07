package com.example.admin.service;

import com.example.admin.common.dto.SysMenuDto;
import com.example.admin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
public interface SysMenuService extends IService<SysMenu> {
    // 获得用户所有角色关联的菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
    List<SysMenu> listByUserId(Long userId);

    // 获得当前用户导航栏信息，借助 SysMenuDto 对象，该方法会根据上下文获取当前用户
    List<SysMenuDto> getCurrentUserNav();

    // 以树状结构返回所有的 sys_menu（树状结构根据父子关系生成，利用 SysMenu 对象中定义的 children 属性）
    List<SysMenu> tree();
}
