package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.admin.common.dto.SysMenuDto;
import com.example.admin.entity.SysMenu;
import com.example.admin.entity.SysUser;
import com.example.admin.mapper.SysMenuMapper;
import com.example.admin.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    /* 对象属性 */
    @Autowired
    SysMenuMapper sysMenuMapper;       // 注入 Mapper 层
    @Autowired
    SysUserService sysUserService;     // 注入 SysUserService 层

    /* 对象方法 */
    // 获得用户所有角色关联的菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
    @Override
    public List<SysMenu> listByUserId(Long userId) {
        return sysMenuMapper.selectByUserId(userId);
    }

    // 获得当前用户的导航栏信息，借助 SysMenuDto 对象，该方法会根据上下文获取当前用户
    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        // 从 security 的上下文中获取当前用户
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getUserByName(username);

        // 获得用户所有角色关联的菜单操作
        List<SysMenu> sysMenuList = this.listByUserId(sysUser.getId());

        // 转化为 List<SysMenuDto> 并返回
        return this.convertToDto(sysMenuList);    // 调用 convertToDto() 私有方法
    }

    // 以树状结构返回所有的 sys_menu（树状结构根据父子关系生成，利用 SysMenu 对象中定义的 children 属性）
    @Override
    public List<SysMenu> tree() {
        // 查询所有的 menu 并按照 order_num 字段排序
        List<SysMenu> sysMenuList = this.list(new QueryWrapper<SysMenu>().orderByAsc("order_num"));

        // 转化为树状结构并返回
        return this.buildTreeMenu(sysMenuList);    // 调用 buildTreeMenu() 私有方法
    }

    // 将 List<SysMenu> 转化为 List<SysMenuDto>
    private List<SysMenuDto> convertToDto(List<SysMenu> sysMenuList) {
        // 定义 DTO 集合
        List<SysMenuDto> sysMenuDtoList = new ArrayList<>();

        /* sysMenuList 为并列关系，通过判断他们的 parentId 把他们变成父子关系（最多三代），装载到 sysMenuDtoList 中 */
        // 先全部转化为 SysMenuDto 对象，放入 sysMenuDtoMap 中（id 作为 key）
        Map<Long, SysMenuDto> sysMenuDtoMap = new HashMap<>();    // 临时存储所有的 DTO 对象（id 作为 key）
        for (SysMenu sm : sysMenuList) {
                // 转化为 SysMenuDto 对象
                SysMenuDto smd = new SysMenuDto();
                smd.setId(sm.getId());                  // id
                smd.setName(sm.getPerms());             // 编码
                smd.setTitle(sm.getName());             // 名称
                smd.setIcon(sm.getIcon());              // 图标
                smd.setPath(sm.getPath());              // 路由
                smd.setComponent(sm.getComponent());    // 动态路由所需的 Component 信息
                // 添加到 sysMenuDtoList 中
                sysMenuDtoMap.put(sm.getId(), smd);
        }
        // 判断父子关系，将子节点 DTO 添加到父节点 DTO 的 children 中，再将第一层（parentId 为 0）放入  sysMenuDtoList 中
        for (SysMenu sm : sysMenuList) {
            // 获得 parentId
            Long parentId = sm.getParentId();
            // 判断 parentId
            if (parentId == 0) {
                // 当 parentId 为 0 时（即第一层节点），从 sysMenuDtoMap 中提取出来放入 sysMenuDtoList
                sysMenuDtoList.add(sysMenuDtoMap.get(sm.getId()));
            } else {
                // 根据 parentId 添加到父节点的 children 中
                SysMenuDto child = sysMenuDtoMap.get(sm.getId());        // 从 Map 中找到当前节点 DTO
                sysMenuDtoMap.get(parentId).getChildren().add(child);    // 从 Map 中找到父节点 DTO，并添加到父节点的 children 中
            }
        }

        return sysMenuDtoList;
    }

    // 将 sysMenuList 变成树状结构，根据父子关系生成，利用 SysMenu 对象中定义的 children 属性
    private List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
        // 要返回的树状结构数据
        List<SysMenu> treeMenuList = new ArrayList<>();

        // 生成树状结构
        for (SysMenu sm : sysMenuList) {
            // 找到自己的 children 并添加
            for (SysMenu m : sysMenuList) {
                if (sm.getId() == m.getParentId()) {
                    sm.getChildren().add(m);    // 添加子节点
                }
            }

            // 将第一层，即 parentId 为 0 的节点添加到 treeMenuList 中
            if (sm.getParentId() == 0L) {
                treeMenuList.add(sm);
            }
        }

        return treeMenuList;
    }
}
