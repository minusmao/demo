package com.example.admin.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：SysMenuDto.java
 * 描述：前端所需的左侧导航栏的数据对象
 * 时间：2021-07-04
 * 作者：TechRice
 * 数据形式如下：
 *     {
 * 			name: 'SysManga',
 * 			title: '系统管理',
 * 			icon: 'el-icon-s-operation',
 * 			component: '',
 * 			path: '',
 * 			children: []
 *     }
 */
public class SysMenuDto {
    /* 对象属性 */
    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<SysMenuDto> children = new ArrayList<>();

    /* 对象方法 */
    // getter 和 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<SysMenuDto> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuDto> children) {
        this.children = children;
    }
}
