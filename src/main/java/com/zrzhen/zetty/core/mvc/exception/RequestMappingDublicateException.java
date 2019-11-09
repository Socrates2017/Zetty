package com.zrzhen.zetty.core.mvc.exception;


/**
 * @author chenanlian
 *
 * 如果多个路由方法注解了相同的路由，则在启动过程中抛出此异常，并导致启动失败
 */
public class RequestMappingDublicateException extends Exception {


    public RequestMappingDublicateException() {
        super();
    }

    public RequestMappingDublicateException(String message) {
        super(message);
    }

    public RequestMappingDublicateException(Throwable cause) {
        super(cause);
    }

    public RequestMappingDublicateException(String message, Throwable cause) {
        super(message, cause);
    }

}
