package com.example.admin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 文件名：RedisConfig.java
 * 描述：Redis 的配置类，配置序列化规则
 * 时间：2021-06-28
 * 作者：TechRice
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建 RedisTemplate 对象
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);           // 设置连接工厂
        // 创建序列化规则对象
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(new ObjectMapper());
        // 设置序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());          // key
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);        // value
        // hash
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());      // hash key
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);    // hash value

        return redisTemplate;
    }
}
