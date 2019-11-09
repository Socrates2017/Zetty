package com.zrzhen.zetty.core.http;


/**
 * @author chenanlian
 */

public enum HttpMethod {
    /**
     * get请求方法
     */
    GET,

    /**
     * post请求方法
     */
    POST;

    public static HttpMethod getHttpMethod(String name) throws HttpException {
        if (name.equals("GET")) {
            return GET;
        } else if (name.equals("POST")) {
            return POST;
        } else {
            throw new HttpException(HttpResponseStatus.METHOD_NOT_ALLOWED, "Unsuported HTTP Method " + name);
        }
    }
}
