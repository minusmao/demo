package com.example.admin.config;

import com.example.admin.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 文件名：SecurityConfig.java
 * 描述：springboot security 的配置类
 * 时间：2021-06-29
 * 作者：TechRice
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)    // 开启基于方法的安全认证机制，即 Controller 层的方法上可以使用 @PreAuthorize 注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /* 类属性 */
    // 不经过 security 拦截的白名单
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",
    };

    /* 对象属性 */
    @Autowired
    private LoginFailureHandler loginFailureHandler;    // 注入登陆失败的 Handler
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;    // 注入登陆成功的 Handler
    @Autowired
    private CaptchaFilter captchaFilter;                // 验证码拦截器
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;    // JWT 认证失败处理器
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;              // 权限不足处理器
    @Autowired
    private UserDetailsServiceImpl userDetailService;    // 注入 Service 层，获取用户基本信息、权限信息（security 会通过它获得这些信息）
    @Autowired
    private JwtLogoutSuccessHandler jwtLogoutSuccessHandler;    // 注入登出成功的 Handler

    /* 对象方法 */
    // 验证 JWT 信息的拦截器
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }
    // 数据库用户密码的加密方式
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置 security 处理处理流程需要的对象
    protected void configure(HttpSecurity http) throws Exception {
        // 参考：Spring security 处理流程图（https://www.processon.com/view/link/606b0b5307912932d09adcb3）
        http.cors().and().csrf().disable()     // 支持跨域，并关闭对 csrf 攻击的预防
                // 登录配置
                .formLogin()    // 注意：因为这里是 formLogin()，所以前端登陆时必须使用 form 表单形式的数据，不能是 JSON 格式
                .successHandler(loginSuccessHandler)    // 登陆成功的 Handler
                .failureHandler(loginFailureHandler)    // 登陆失败的 Handler

                // 登出配置
                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)    // 登出成功的 Handler

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()    // 与白名单匹配，则跳过
                .anyRequest().authenticated()              // 其它请求均拦截

                // 配置异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)    // JWT 认证失败处理器
                .accessDeniedHandler(jwtAccessDeniedHandler)              // 权限不足处理器

                // 配置自定义的过滤器，
                .and()
                .addFilter(jwtAuthenticationFilter())      // 验证 JWT 信息的拦截器
                // 配置 CaptchaFilter 验证码拦截器，在 UsernamePasswordAuthenticationFilter 过滤器之前
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }

    // 配置获得用户基本信息、权限信息的 Service 层（security 会通过它的 loadUserByUsername() 方法获得这些信息）
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
