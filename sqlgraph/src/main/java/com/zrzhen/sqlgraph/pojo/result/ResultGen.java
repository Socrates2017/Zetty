package com.zrzhen.sqlgraph.pojo.result;

/**
 * 响应结果生成工具
 *
 * @author chenanlian
 */
public class ResultGen {

    public static Result genSuccessResult() {
        return new Result(ResultCode.SUCCESS);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result(ResultCode.SUCCESS, data);
    }

    public static Result genResult(ResultCode resultCode) {
        return new Result(resultCode);
    }

    public static <T> Result<T> genResult(ResultCode resultCode, T data) {
        return new Result(resultCode, data);
    }
}
