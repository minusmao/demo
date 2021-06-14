package com.example.fruit.vo;

/**
 * 前端视图 Pie 所需对象
 * @author TechRice
 * @since 2021-06-13
 * 前端视图 Pie 所需的 JSON 格式如下：
 *   [
 *      {value: 1048, name: 'name1'},
 *      {value: 735, name: 'name2'}
 *   ]
 */
public class FruitPieVO {
    /* 对象属性 */
    private String name;    // 对应 Fruit 对象的 name 属性
    private int value;      // 对应 Fruit 对象的 sale 属性

    /* 对象方法 */
    // getter 和 setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
