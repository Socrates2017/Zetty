package com.zrzhen.zetty.core.http;

/**
 * @author chenanlian
 */

public enum HttpVersion {
    /**
     * 1.0默认不开启长链接
     */
	HTTP_1_0(false,"HTTP/1.0"),

    /**
     * 1.1默认开启长连接
     */
    HTTP_1_1(true,"HTTP/1.1");

	private boolean keepAliveDefault;

	private String name;


	public static HttpVersion getHttpVersion(String name) throws HttpException {
		if (name.equals(HTTP_1_0.name)) {
			return HTTP_1_0;
		} else if (name.equals(HTTP_1_1.name)) {
			return HTTP_1_1;
		} else {
			throw new HttpException(HttpResponseStatus.BAD_REQUEST, "Unsuported HTTP Protocol " + name);
		}
	}

	HttpVersion(boolean keepAliveDefault, String name){
		this.keepAliveDefault = keepAliveDefault;
		this.name = name;
	}

	public boolean isKeepAliveDefault() {
		return keepAliveDefault;
	}

	@Override
	public String toString() {
		return name;
	}
}
