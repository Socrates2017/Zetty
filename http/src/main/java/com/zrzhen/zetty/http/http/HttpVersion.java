package com.zrzhen.zetty.http.http;

/**
 * @author chenanlian
 * <p>
 * Http协议版本
 */

public enum HttpVersion {
    /**
     * 1.0默认不开启长链接
     */
    HTTP_1_0(false, "HTTP/1.0"),

    /**
     * 1.1默认开启长连接
     */
    HTTP_1_1(true, "HTTP/1.1");

    /**
     * 是否默认开启长连接
     */
    private boolean keepAliveDefault;

    /**
     * 名称，如HTTP/1.1
     */
    private String name;

    /**
     * 构造函数
     *
     * @param keepAliveDefault 是否默认长连接
     * @param name             名称
     */
    HttpVersion(boolean keepAliveDefault, String name) {
        this.keepAliveDefault = keepAliveDefault;
        this.name = name;
    }

    /**
     * 获取是否默认长连接
     *
     * @return true：more长连接；false：默认短连接
     */
    public boolean isKeepAliveDefault() {
        return keepAliveDefault;
    }

    /**
     * 返回名称
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * 根据名称返回Http版本
     *
     * @param name 名称
     * @return Http版本
     * @throws HttpException
     */
    public static HttpVersion getHttpVersion(String name) throws HttpException {
        if (name.equals(HTTP_1_0.name)) {
            return HTTP_1_0;
        } else if (name.equals(HTTP_1_1.name)) {
            return HTTP_1_1;
        } else {
            throw new HttpException(HttpResponseStatus.BAD_REQUEST, "Unsuported HTTP Protocol " + name);
        }
    }


}
