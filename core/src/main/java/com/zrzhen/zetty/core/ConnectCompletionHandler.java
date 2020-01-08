package com.zrzhen.zetty.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 * 客户端建立连接后回调
 */
public class ConnectCompletionHandler implements CompletionHandler<Void, SocketSession> {

    private static final Logger log = LoggerFactory.getLogger(ConnectCompletionHandler.class);


    public ConnectCompletionHandler() {
    }


    @Override
    public void completed(Void result, SocketSession socketSession) {
        socketSession.setSocketSessionStatus(SocketSessionStatus.CONNECTED);
        socketSession.read();
    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        socketSession.destroy();
    }
}
