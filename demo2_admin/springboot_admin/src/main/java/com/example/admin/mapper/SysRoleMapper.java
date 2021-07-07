package com.example.admin.mapper;

import com.example.admin.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
@Repository    // 因为全局配置了扫描 mapper 的路径，所以这里不用 @Mapper，用 @Repository 即可，详情：https://www.jianshu.com/p/3942f6b4fa75
public interface SysRoleMapper extends BaseMapper<SysRole> {
    // 根据用户 id 查询该用户的所有角色
    List<SysRole> selectByUserId(@Param("userId") Long userId);
}
