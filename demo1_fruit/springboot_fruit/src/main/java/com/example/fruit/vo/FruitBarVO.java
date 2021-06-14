package com.example.fruit.vo;

import java.util.List;

/**
 * 前端视图 Bar 所需对象
 * @author TechRice
 * @since 2021-06-13
 * 前端视图 Bar 所需的 JSON 格式如下：
 *   {
 *     names: ['name1', 'name2'],
 *     values: [
 *       {
 *         value: 'value1',
 *         itemStyle: {color: "#3fb1e3"}
 *       },
 *       {
 *         value: 'value2',
 *         itemStyle: {color: "#3fb1e3"}
 *       }
 *     ]
 *   }
 */
public class FruitBarVO {
    /* 对象属性 */
    private List<String> names;
    private List<FruitBarValueVO> values;

    /* 对象方法 */
    // getter 和 setter
    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<FruitBarValueVO> getValues() {
        return values;
    }

    public void setValues(List<FruitBarValueVO> values) {
        this.values = values;
    }
}
