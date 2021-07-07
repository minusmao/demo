package com.example.admin.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 文件名：KaptchaConfig.java
 * 描述：谷歌验证码生成工具的配置类，配置生成的 Base64 图片的长、宽、字符数等
 * 时间：2021-06-29
 * 作者：TechRice
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        // 创建一个 java.util.Properties 基本配置类（一般可以用来加载 *.properties 文件：https://www.cnblogs.com/lingiu/p/3468464.html）
        Properties properties = new Properties();

        // 在 properties 中填入 key-value 参数
        properties.put("kaptcha.border", "no");                         // 是否有边框
        properties.put("kaptcha.textproducer.font.color", "black");    // 文本的颜色
        properties.put("kaptcha.textproducer.char.space", "4");         // 字符间空格的个数，等于字符的个数减1
        properties.put("kaptcha.image.height", "40");                   // 图片的高度
        properties.put("kaptcha.image.width", "120");                   // 图片的宽度
        properties.put("kaptcha.textproducer.font.size", "30");         // 字符的大小

        // 创建一个 Config 对象，并传入 properties
        Config config = new Config(properties);

        // 创建 DefaultKaptcha 对象，并设置 config
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
