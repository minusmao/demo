package com.example.admin.mapper;

import com.example.admin.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    // 获得用户所有角色关联的菜单操作（注：不同角色可能有相同的菜单操作，查询时需要使用 DISTINCT 关键字去重）
    List<SysMenu> selectByUserId(@Param("userId") Long userId);
}
