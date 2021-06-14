package com.example.fruit.service.impl;

import com.example.fruit.entity.Fruit;
import com.example.fruit.mapper.FruitMapper;
import com.example.fruit.service.FruitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fruit.util.FruitUtil;
import com.example.fruit.vo.FruitBarVO;
import com.example.fruit.vo.FruitBarValueVO;
import com.example.fruit.vo.FruitPieVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author TechRice
 * @since 2021-06-12
 */
@Service
public class FruitServiceImpl extends ServiceImpl<FruitMapper, Fruit> implements FruitService {
    // 返回前端视图 Pie 所需对象集合
    @Override
    public List<FruitPieVO> listFruitPieVO() {
        // 定义返回的数据 List 对象
        List<FruitPieVO> fruitPieVOList = new ArrayList<>();

        // 查询数据，并封装到新的数据对象中
        List<Fruit> fruitList = this.list();    // 调用父类的 list 方法，得到数据库中的数据
        for (Fruit fruit : fruitList) {
            // 新数据对象
            FruitPieVO fruitPieVO = new FruitPieVO();
            fruitPieVO.setName(fruit.getName());
            fruitPieVO.setValue(fruit.getSale());
            // 填入数据对象
            fruitPieVOList.add(fruitPieVO);
        }

        return fruitPieVOList;
    }
    // 返回前端视图 Bar 所需对象
    @Override
    public FruitBarVO getFruitBarVO() {
        // 定义返回的数据对象
        FruitBarVO fruitBarVO = new FruitBarVO();
        List<String> names = new ArrayList<>();              // 定义 names 集合，对应数据对象的 names 属性
        List<FruitBarValueVO> values = new ArrayList<>();    // 定义 values 集合，对应数据对象的 values 属性

        // 查询数据数据库中的原始数据
        List<Fruit> fruitList = this.list();    // 调用父类的 list 方法，得到数据库中的数据

        // 封装到 fruitBarVO 数据对象中
        for (Fruit fruit : fruitList) {
            /* 填充 names 集合 */
            names.add(fruit.getName());

            /* 填充 values 集合 */
            // 创建 FruitBarValueVO 对象
            FruitBarValueVO fruitBarValueVO = new FruitBarValueVO();
            // 设置 value 属性
            fruitBarValueVO.setValue(fruit.getSale());    // 对应 fruit.sale 即水果的销量
            // 设置 itemStyle 属性
            fruitBarValueVO.setItemStyle(FruitUtil.getFruitBarValueItemStyle(fruit.getSale()));
            // 最后填充 values 集合
            values.add(fruitBarValueVO);
        }
        fruitBarVO.setNames(names);      // 设置数据对象的 names 属性
        fruitBarVO.setValues(values);    // 设置数据对象的 values 属性

        return fruitBarVO;
    }
}
