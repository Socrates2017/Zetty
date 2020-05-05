package com.zrzhen.zetty.net;


/**
 * @author chenanlian
 * 业务处理类
 */
public interface Processor<T> {

    /**
     * 进行业务处理的类
     *
     * @param session socket会话
     * @param message 接收到的消息（已经完成解码）
     * @return
     */
    boolean process(SocketSession session, T message);
}
