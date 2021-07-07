package com.example.admin.security;

import cn.hutool.json.JSONUtil;
import com.example.admin.common.lang.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 文件名：LoginFailureHandler.java
 * 描述：登陆认证失败的 Handler
 * 时间：2021-06-30
 * 作者：TechRice
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    // note: 登陆认证失败时，会调用此方法，所以通过此方法返回 JSON 报文
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 响应报文头
        response.setContentType("application/json;charset=UTF-8");    // 设置响应的数据类型为 JSON、编码为 UTF-8
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);       // 设置状态码为 400，表示错误
        // 响应报文体
        Result result = Result.fail(e.getMessage());                  // 响应 400 和错误的 message 信息

        // 输出流
        ServletOutputStream out = response.getOutputStream();
        // 将 result 对象转化为 JSON 字符串，再按 UTF-8 编码为字节数组，最后输出到字节流
        out.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        // 结束输出流
        out.flush();    // 刷新缓冲区
        out.close();    // 关闭
    }
}
