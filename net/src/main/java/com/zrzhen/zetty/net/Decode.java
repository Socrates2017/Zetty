package com.zrzhen.zetty.net;


/**
 * @author chenanlian
 */
public interface Decode<T> {
    /**
     *
     * @param session
     * @param readLength
     * @param t
     * @return 一次完整的消息是否读取完毕，是则返回true，否则返回否，socket将继续读取
     */
    boolean decode(SocketSession session, Integer readLength, T t);
}
