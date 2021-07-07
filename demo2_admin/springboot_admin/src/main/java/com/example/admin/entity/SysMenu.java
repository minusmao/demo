package com.example.admin.entity;

import com.example.admin.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    /**
     * 前端动态路由信息：视图的位置
     */
    private String component;

    /**
     * 类型  0：目录  1：菜单  2：按钮
     */
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 存储子 SysMenu 对象，数据库表中没有此字段，所以 MyBatis-plus 需要忽略
     */
    @TableField(exist = false)    // 忽略
    private List<SysMenu> children = new ArrayList<>();

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "SysMenu{" + super.toString() +
            ", parentId=" + parentId +
            ", name=" + name +
            ", path=" + path +
            ", perms=" + perms +
            ", component=" + component +
            ", type=" + type +
            ", icon=" + icon +
            ", ordernum=" + orderNum +
        "}";
    }
}
