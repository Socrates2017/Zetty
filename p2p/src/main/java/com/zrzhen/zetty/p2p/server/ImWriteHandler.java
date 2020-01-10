package com.zrzhen.zetty.p2p.server;

import com.zrzhen.zetty.net.SocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class ImWriteHandler implements CompletionHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(ImWriteHandler.class);

    ImSocketSession sender;

    public ImWriteHandler(ImSocketSession sender) {
        this.sender = sender;
    }

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
