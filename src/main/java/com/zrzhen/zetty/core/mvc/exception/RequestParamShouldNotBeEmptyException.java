package com.zrzhen.zetty.core.mvc.exception;

/**
 * @author chenanlian
 * 如果注解了某个uri参数为必传，而请求时为空，则抛出此异常，并将信息返回给客户端
 */
public class RequestParamShouldNotBeEmptyException extends Exception {
    private final String parameter;

    public RequestParamShouldNotBeEmptyException(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getMessage() {
        return "Parameter Should Not Be Empty:" + this.parameter;
    }
}
