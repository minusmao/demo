package com.example.admin.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.example.admin.common.lang.Const;
import com.example.admin.common.lang.Result;
import com.example.admin.entity.SysUser;
import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

/**
 * 文件名：AuthController.java
 * 描述：用户身份验证的 Controller 层
 * 时间：2021-06-29
 * 作者：TechRice
 */
@RestController
public class AuthController extends BaseController {
    @Autowired
    Producer producer;    // 注入配置好的 DefaultKaptcha 对象，用于生成验证码图片

    // 响应验证码图片
    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        // 生成一个随机的 key（即 Token）和一个随机的验证码 code
        String key = UUID.randomUUID().toString();    // 随机码
        String code = producer.createText();          // 验证码

        // 测试
//        key = "abc";       // 随机码
//        code = "12345";    // 验证码

        // 根据 code 生成验证码图片
        BufferedImage image = producer.createImage(code);
        // 将图片转化为 Base64 编码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();    // 创建字节数组输出流
        ImageIO.write(image, "jpg", outputStream);                // 将图片加载到输出流中
        Base64 base64 = new Base64();
        String base64Img = "data:image/jpeg;base64," + base64.encodeToString(outputStream.toByteArray());

        // 随机码 key 和验证码 code 放入 Redis 的 hash 表中
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);    // 过期时间 120 秒

        // 返回结果
        return Result.succ(
                // 响应的 data 部分为 Map 结构
                MapUtil.builder()
                        .put("token", key)               // 随机码
                        .put("captchaImg", base64Img)    // 验证码图片的 Base64 编码
                        .build()
        );
    }

    // 响应 userInfo 信息
    @GetMapping("/sys/userInfo")
    public Result userInfo(Principal principal) {
        // 通过用户名查找用户（用户名由 security 的 Principal 提供，在 JWT 认证的时候放入的）
        SysUser sysUser = sysUserService.getUserByName(principal.getName());    // 调用 SysUserService 层

        return Result.succ(
                // 响应的 data 部分为 Map 结构
                MapUtil.builder()
                        .put("id", sysUser.getId())
                        .put("username", sysUser.getUsername())
                        .put("avatar", sysUser.getAvatar())
                        .put("created", sysUser.getCreated())
                        .build()
        );
    }
}
