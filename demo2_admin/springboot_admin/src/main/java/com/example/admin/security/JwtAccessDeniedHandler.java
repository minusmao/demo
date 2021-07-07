package com.example.admin.security;

import cn.hutool.json.JSONUtil;
import com.example.admin.common.lang.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 文件名：JwtAccessDeniedHandler.java
 * 描述：权限不足处理器
 * 时间：2021-07-03
 * 作者：TechRice
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        // 响应报文头
        response.setContentType("application/json;charset=UTF-8");    // 设置响应的数据类型为 JSON、编码为 UTF-8
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);      // 设置状态码为 401，表示权限不足
        // 响应报文体
        Result result = Result.fail(401, "权限不足，请重新登录");    // 响应 401 和错误的 message 信息

        // 输出流
        ServletOutputStream out = response.getOutputStream();
        // 将 result 对象转化为 JSON 字符串，再按 UTF-8 编码为字节数组，最后输出到字节流
        out.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        // 结束输出流
        out.flush();    // 刷新缓冲区
        out.close();    // 关闭
    }
}
