package com.example.fruit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 与 Fruit 相关的工具类
 */
public class FruitUtil {
    /* 类方法 */
    // 根据 Fruit 柱状图的 value 值返回一个 itemStyle
    public static Map<String, String> getFruitBarValueItemStyle(int value) {
        // 选择颜色，根据销量
        String color;
        switch (value/50) {
            case 0: color = "#e83838"; break;
            case 1: color = "#37eb48"; break;
            case 2: color = "#204bda"; break;
            case 3: color = "#8812e2"; break;
            case 4: color = "#a0a7e6"; break;
            case 5: color = "#a4c2f4"; break;
            case 6: color = "#ff9900"; break;
            case 7: color = "#ffd966"; break;
            case 8: color = "#7f6000"; break;
            default: color = "#e445dc"; break;
        }

        // 定义 itemStyle
        Map<String, String> itemStyle = new HashMap<>();
        itemStyle.put("color", color);

        // 返回 itemStyle
        return itemStyle;
    }
}
