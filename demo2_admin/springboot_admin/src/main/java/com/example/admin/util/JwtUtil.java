package com.example.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 文件名：JwtUtil.java
 * 描述：JWT 的工具类
 * 时间：2021-07-03
 * 作者：TechRice
 */
@Component
@ConfigurationProperties(prefix = "admin.jwt")
public class JwtUtil {
    /* 对象属性 */
    private long expire;      // 过期时长，单位：秒
    private String secret;    // 加密密钥
    private String header;    // 本项目会将 JWT 作为响应报文的首部字段的一行（header 作为 key : jwt 数据作为 value）

    /* 对象方法 */
    // getter 和 setter
    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    // 生成 JWT 信息，传入用户名
    public String generateToken(String username) {
        // 时间信息
        Date nowDate = new Date();                                             // 当前时间
        Date expireDate = new Date(nowDate.getTime() + 1000 * this.expire);    // 过期时间

        // 生成 JWT 数据，包括三个部分：头部(header).载荷(payload).签证(signature)
        // 关于 JWT 数据格式的详解，请参考：https://www.jianshu.com/p/576dbf44b2ae
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")    // 头部 - 设置类型为 JWT
                .setSubject(username)                  // 载荷 - 所面向的用户
                .setIssuedAt(nowDate)                  // 载荷 - 签发时间
                .setExpiration(expireDate)             // 载荷 - 过期时间
                .signWith(SignatureAlgorithm.HS512, this.secret)    // 签证 - 加密算法和加密密钥
                .compact();
    }

    // 解析 JWT 信息，如果信息不合法返回 null
    public Claims getClaimsByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;    // 信息不合法返回 null
        }
    }

    // 判断 JWT 是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration()    // 获得过期时间
                .before(new Date());     // 判断是否在当前时间之前
    }
}
