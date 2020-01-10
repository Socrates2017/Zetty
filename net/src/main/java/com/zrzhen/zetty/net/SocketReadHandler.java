package com.zrzhen.zetty.net;


import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 * 消息读取回调规范接口
 */
public interface SocketReadHandler<Integer, SocketSession> extends CompletionHandler<Integer, SocketSession> {

}
