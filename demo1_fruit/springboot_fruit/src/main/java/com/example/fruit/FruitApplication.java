package com.example.fruit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.fruit.mapper")    // 自动生成代码中 Mapper 层没加注解，所以这里添加一个扫描 Mapper 层的注解即可
public class FruitApplication {

    public static void main(String[] args) {
        SpringApplication.run(FruitApplication.class, args);
    }

}
