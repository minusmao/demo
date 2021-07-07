package com.example.admin.common.dto;

import javax.validation.constraints.NotBlank;

/**
 * 文件名：PasswordDto
 * 描述：更新密码时，所用的 dto 数据对象
 * 时间：2021-07-07
 * 作者：TechRice
 */
public class PasswordDto {
    /* 对象属性 */
    @NotBlank(message = "新密码不能为空")
    private String password;       // 新密码
    private String checkPass;      // 确认密码
    @NotBlank(message = "旧密码不能为空")
    private String currentPass;    // 旧密码

    /* 对象方法 */
    //  getter 和 setter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckPass() {
        return checkPass;
    }

    public void setCheckPass(String checkPass) {
        this.checkPass = checkPass;
    }

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }
}
