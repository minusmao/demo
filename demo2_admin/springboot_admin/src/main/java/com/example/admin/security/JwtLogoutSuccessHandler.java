package com.example.admin.security;

import cn.hutool.json.JSONUtil;
import com.example.admin.common.lang.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 文件名：JwtLogoutSuccessHandler.java
 * 描述：登出成功的 Handler
 * 时间：2021-07-04
 * 作者：TechRice
 */
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    // 登出时，由 security 调用
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 先调用 security 的登出 Handler
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // 响应报文头
        response.setContentType("application/json;charset=UTF-8");    // 设置响应的数据类型为 JSON、编码为 UTF-8
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
