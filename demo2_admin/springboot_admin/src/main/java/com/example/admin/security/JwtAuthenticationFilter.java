package com.example.admin.security;

import cn.hutool.core.util.StrUtil;
import com.example.admin.entity.SysUser;
import com.example.admin.service.SysUserService;
import com.example.admin.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件名：JwtAuthenticationFilter.java
 * 描述：拦截器，验证用户每次提交时，报文头中的 JWT 信息
 * 时间：2021-07-03
 * 作者：TechRice
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    /* 对象属性 */
    @Autowired
    private JwtUtil jwtUtil;                             // 注入 JWT 工具类
    @Autowired
    private SysUserService sysUserService;               // 注入 SysUserService 层，获取用户基本信息
    @Autowired
    private UserDetailsServiceImpl userDetailService;    // 注入 UserDetailsService 层，获取用户基本信息、权限信息

    /* 构造方法 */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /* 对象方法 */
    // 拦截请求，验证请求报文中的 JWT 信息
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, JwtException {
        // 请求报文
        String jwt = request.getHeader(jwtUtil.getHeader());    // 获得请求报文中的 JWT 信息

        // 如果 jwt 为空，不验证，直接跳过，由后面的权限拦截器决定用户是否重新登陆
        if(StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        // 解析 jwt 信息
        Claims claims = jwtUtil.getClaimsByToken(jwt);
        if(claims == null) {
            // 解析结果为空
            throw new JwtException("token 异常");
        }
        if (jwtUtil.isTokenExpired(claims)) {
            // 过期
            throw new JwtException("token 已过期");
        }

        // 获得 jwt 中的用户名
        String username = claims.getSubject();
        // 再通过 Service 层获得用户在数据库中的基本信息、角色权限信息
        SysUser sysUser = sysUserService.getUserByName(username);                              // 基本信息
        List<GrantedAuthority> userAuthority = userDetailService.getUserAuthority(sysUser);    // 角色、权限信息

        // 将用户信息放入 security 的上下文中，方便后续（包括后面的 Filter、Controller）获取用户信息
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, null, userAuthority);      // 先转化为指定形式的 token
        SecurityContextHolder.getContext().setAuthentication(token);                                   // 放入上下文

        chain.doFilter(request, response);
    }
}
