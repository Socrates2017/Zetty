package com.zrzhen.zetty.http.mvc.exception;

/**
 * @author chenanlian
 * <p>
 * 如果请求的内容类型是json，并且相应的方法上注解为jsonBody必传，且请求时为空，则抛出此异常，并将信息返回给客户端
 */
public class RequestBodyShouldNotBeEmptyException extends Exception {


    @Override
    public String getMessage() {
        return "HttpRequest body is null.";
    }
}
