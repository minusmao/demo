package com.example.admin.common.lang;

/**
 * 文件名：Result.java
 * 描述：统一封装后端返回的结果
 * 时间：2021-6-27
 * 作者：TechRice
 */
public class Result {
    /* 对象属性 */
    private int code;       // 状态码（200 为成功）
    private String msg;     // 结果消息
    private Object data;    // 结果数据

    /* 对象方法 */
    // getter 和 setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /* 类方法 */
    // 封装响应结果：成功（succ）
    public static Result succ() {
        return Result.succ(null);
    }
    public static Result succ(Object data) {
        return Result.succ(200, "操作成功", data);
    }
    public static Result succ(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // 封装响应结果：失败（fail）
    public static Result fail(String msg) {
        return Result.fail(400, msg, null);
    }
    public static Result fail(String msg, Object data) {
        return Result.fail(400, msg, data);
    }
    public static Result fail(int code, String msg) {
        return Result.fail(code, msg, null);
    }
    public static Result fail(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
