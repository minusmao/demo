package com.example.admin.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件名：MyBatisPlusConfig.java
 * 描述：mybatis-plus 框架的配置类
 * 时间：2021-6-27
 * 作者：TechRice
 */
@Configuration
@MapperScan("com.example.admin.mapper")    // 自动生成的代码中 Mapper 层没加注解，所以这里添加一个扫描 Mapper 层的注解即可
public class MybatisPlusConfig {
    // 拦截器
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 定义拦截器对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加：分页功能
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 添加：防止全表更新和删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }

    // 配置分页功能时，需要配置下面内容，具体看官网：https://mp.baomidou.com/guide/interceptor.html#mybatisplusinterceptor
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
