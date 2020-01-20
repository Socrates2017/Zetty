package com.zrzhen.zetty.net;


/**
 * @author chenanlian
 */
public interface Processor<T> {

    boolean process(SocketSession session, T message);
}
