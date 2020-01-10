package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class DefaultWriteHandler implements CompletionHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(DefaultWriteHandler.class);

    @Override
    public void completed(Integer result, SocketSession socketSession) {
        ByteBuffer buffer = socketSession.getWriteBuffer();
        if (buffer.hasRemaining()) {
            log.warn("buffer.hasRemaining()");
            socketSession.getSocketChannel().write(buffer, socketSession, this);
        }
    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        socketSession.destroy();
    }
}
