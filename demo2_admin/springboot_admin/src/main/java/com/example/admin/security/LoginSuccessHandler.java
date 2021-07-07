package com.example.admin.security;

import cn.hutool.json.JSONUtil;
import com.example.admin.common.lang.Result;
import com.example.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 文件名：LoginFailureHandler.java
 * 描述：登陆认证成功的 Handler
 * 时间：2021-06-30
 * 作者：TechRice
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    /* 对象方法 */
    @Autowired
    JwtUtil jwtUtil;    // 注入 JWT 工具类

    // note: 登陆认证成功时，会调用此方法，所以通过此方法返回 JSON 报文，且响应头带有 jwt 数据
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 生成 jwt 数据
        String jwt = jwtUtil.generateToken(authentication.getName());

        // 响应报文头
        response.setContentType("application/json;charset=UTF-8");    // 设置响应的数据类型为 JSON、编码为 UTF-8
        response.setHeader(jwtUtil.getHeader(), jwt);                 // 本项目会将 JWT 作为响应报文的首部字段的一行（header 作为 key : jwt 数据作为 value）
        // 响应报文体
        Result result = Result.succ(null);                            // 响应 200

        // 输出流
        ServletOutputStream out = response.getOutputStream();
        // 将 result 对象转化为 JSON 字符串，再按 UTF-8 编码为字节数组，最后输出到字节流
        out.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        // 结束输出流
        out.flush();    // 刷新缓冲区
        out.close();    // 关闭
    }
}
