package com.example.admin.security;

import com.example.admin.common.exception.CaptchaException;
import com.example.admin.common.lang.Const;
import com.example.admin.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件名：CaptchaFilter.java
 * 描述：校验验证码的拦截器
 * 时间：2021-06-30
 * 作者：TechRice
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    /* 类属性 */
    private static final String LOGIN_URL = "/login";    // 登陆的 url

    /* 对象属性 */
    @Autowired
    private RedisUtil redisUtil;                         // 注入 RedisUtil 工具对象
    @Autowired
    private LoginFailureHandler loginFailureHandler;     // 注入登陆失败的 Handler

    /* 对象方法 */
    // 在校验用户名密码前，先调用此方法校验验证码是否正确
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 请求报文
        String uri = request.getRequestURI();    // 得到请求的 uri
        String method = request.getMethod();     // 得到请求的方法

        // 判断，只有客户端使用 POST 请求 /login 时才拦截处理，其他直接跳过
        if(LOGIN_URL.equals(uri) && method.equals("POST")) {
            try {
                // 校验验证码
                validate(request);    // 验证码错误时抛出 CaptchaException 异常
            } catch (CaptchaException e) {
                // 验证码错误时直接交给 LoginFailureHandler 处理
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }

        // 验证码正确，该拦截器结束，执行下一步（用户名和密码验证）
        filterChain.doFilter(request, response);
    }

    // 校验验证码
    private void validate(HttpServletRequest request) throws CaptchaException {
        // 请求报文
        String code = request.getParameter("code");    // 用户输入的验证码 code
        String key = request.getParameter("token");    // 用户的随机码 token

        // 非空校验
        if(StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        // 验证码校验（请求参数的 code 和 Redis 中 key 对应的 code 对比）
        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 清除 Redis 中 key 对应的 code 记录，一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY, key);
    }
}
