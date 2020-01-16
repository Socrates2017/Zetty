package com.zrzhen.sqlgraph.pojo.result;


import com.zrzhen.zetty.common.JsonUtil;

/**
 * 统一API响应结果封装
 *
 * @author chenanlian
 */
public class Result<T> {
    private int code;
    private String message;
    private T data;


    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtil.obj2Json(this);
    }
}
