package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author TechRice
 * @since 2021-06-27
 */
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;

    /**
     * 头像的URL
     */
    private String avatar;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private String city;

    private LocalDateTime lastLogin;

    /**
     * 存储拥有的角色 SysRole 对象，数据库表中没有此字段，所以 MyBatis-plus 需要忽略
     */
    @TableField(exist = false)    // 忽略
    private List<SysRole> sysRoles = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    @Override
    public String toString() {
        return "SysUser{" + super.toString() +
            ", username=" + username +
            ", password=" + password +
            ", avatar=" + avatar +
            ", email=" + email +
            ", city=" + city +
            ", lastLogin=" + lastLogin +
        "}";
    }
}
