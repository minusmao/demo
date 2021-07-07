package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.admin.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    /**
     * 唯一编码
     */
    @NotBlank(message = "角色编码不能为空")
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 存储拥有的菜单操作 menuId，数据库表中没有此字段，所以 MyBatis-plus 需要忽略
     */
    @TableField(exist = false)    // 忽略
    private List<Long> menuIds = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    @Override
    public String toString() {
        return "SysRole{" + super.toString() +
            ", name=" + name +
            ", code=" + code +
            ", remark=" + remark +
        "}";
    }
}
