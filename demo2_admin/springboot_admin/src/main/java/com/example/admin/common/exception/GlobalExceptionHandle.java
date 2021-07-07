package com.example.admin.common.exception;

import com.example.admin.common.lang.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 文件名：GlobalExceptionHandle.java
 * 描述：统一异常处理（有时候不可避免服务器报错的情况，如果不配置异常处理机制，就会默认返回tomcat或者nginx的5XX页面）
 * 时间：2021-06-27
 * 作者：TechRice
 */
@RestControllerAdvice
public class GlobalExceptionHandle {
    // 日志对象
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    // 非法数据异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)    // 设置响应报文的 HTTP 状态码为 400
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        // 日志打印
        log.error("Assert异常：----------------{}", e.getMessage());
        // 响应错误信息
        return Result.fail(e.getMessage());
    }

    // 运行时异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)    // 设置响应报文的 HTTP 状态码为 400
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        // 日志打印
        log.error("运行时异常：----------------{}", e.getMessage());
        // 响应错误信息
        return Result.fail(e.getMessage());
    }

    // 实体校验异常捕获（在 Controller 层中使用 @Validated 注解会校验用户提交的实体类数据，不满足则抛出异常）
    @ResponseStatus(HttpStatus.BAD_REQUEST)    // 设置响应报文的 HTTP 状态码为 400
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        // 获得错误信息，即 entity 层实体类上 @NotNull、@NotBlank 注解里的备注内容
        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();

        log.error("实体校验异常：----------------{}", objectError.getDefaultMessage());
        return Result.fail(objectError.getDefaultMessage());
    }
}
