package com.example.admin.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 文件名：CaptchaException.java
 * 描述：验证码错误时，抛出此异常，由 LoginFailureHandler 处理
 * 时间：2021-07-03
 * 作者：TechRice
 */
public class CaptchaException extends AuthenticationException {
    // 有参构造方法，需传入 msg 异常信息
    public CaptchaException(String msg) {
        super(msg);
    }
}
