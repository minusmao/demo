package com.example.admin.common.lang;

/**
 * 文件名：Const.java
 * 描述：存储常量的类
 * 时间：2021-06-29
 * 作者：TechRice
 */
public class Const {
    /* 类（常量）属性 */
    public final static String CAPTCHA_KEY = "captcha";    // 将随机码作为 item、验证码作为 Value，并存入 Redis 的 hash 表对应的 key

    // 数据库中数据的状态
    public final static Integer STATUS_ON = 1;
    public final static Integer STATUS_OFF = 0;

    // 用户默认信息
    public static final String DEFAULT_PASSWORD = "888888";    // 默认密码
    public static final String DEFAULT_AVATAR =
            "https://img2.baidu.com/it/u=1077360284,2857506492&fm=26&fmt=auto&gp=0.jpg";    // 默认头像
}
