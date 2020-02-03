package com.zrzhen.zetty.net;


/**
 * @author chenanlian
 */
public interface Decode<T> {
    boolean decode(SocketSession session, Integer readLength, T t);
}
