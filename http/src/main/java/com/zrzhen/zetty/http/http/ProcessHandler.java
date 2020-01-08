package com.zrzhen.zetty.http.http;

import com.zrzhen.zetty.core.SocketSession;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class ProcessHandler implements CompletionHandler<Integer, ByteBuffer> {

    private SocketSession socketSession;



    @Override
    public void completed(Integer result, ByteBuffer attachment) {

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }

    public SocketSession getSocketSession() {
        return socketSession;
    }

    public void setSocketSession(SocketSession socketSession) {
        this.socketSession = socketSession;
    }
}
