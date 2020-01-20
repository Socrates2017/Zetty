package com.zrzhen.zetty.net;


/**
 * @author chenanlian
 */
public interface Protocol<T> {
    /**
     * 对于从Socket流中获取到的数据采用当前Protocol的实现类协议进行解析。
     *
     * @param session 本次需要解码的session
     */
    boolean decode(SocketSession session, Integer readLength,T t);
}
