package com.zrzhen.zetty.http.http.http;


/**
 * @author chenanlian
 *
 * 请求方法
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
            throw new HttpException(HttpResponseStatus.METHOD_NOT_ALLOWED, "Unsupported HTTP Method " + name);
        }
    }
}
