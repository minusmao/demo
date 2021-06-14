package com.example.fruit.service;

import com.example.fruit.entity.Fruit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.fruit.vo.FruitBarVO;
import com.example.fruit.vo.FruitPieVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-12
 */
public interface FruitService extends IService<Fruit> {
    // 返回前端视图 Pie 所需对象集合
    public List<FruitPieVO> listFruitPieVO();
    // 返回前端视图 Bar 所需对象集合
    public FruitBarVO getFruitBarVO();
}
