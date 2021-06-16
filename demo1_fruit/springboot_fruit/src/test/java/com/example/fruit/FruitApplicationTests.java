package com.example.fruit;

import com.example.fruit.mapper.FruitMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FruitApplicationTests {
    @Autowired
    FruitMapper fruitMapper;

    @Test
    void contextLoads() {
        /* 测试 mapper.xml 能否使用 */
        System.out.println(fruitMapper.findByFruitId(8));
    }

}
