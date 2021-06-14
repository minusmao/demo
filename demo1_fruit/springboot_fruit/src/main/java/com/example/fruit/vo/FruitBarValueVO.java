package com.example.fruit.vo;

import java.util.Map;

/**
 * 前端视图 Bar 所需对象中的 Value 对象
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
public class FruitBarValueVO {
    /* 对象属性 */
    private int value;
    private Map<String, String> itemStyle;

    /* 对象方法 */
    // getter 和 setter
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Map<String, String> getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(Map<String, String> itemStyle) {
        this.itemStyle = itemStyle;
    }
}
