package com.example.fruit.mapper;

import com.example.fruit.entity.Fruit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author TechRice
 * @since 2021-06-12
 */
public interface FruitMapper extends BaseMapper<Fruit> {
    /* 测试 mapper.xml 能否使用 */
    public Fruit findByFruitId(@Param("id") int id);
}
