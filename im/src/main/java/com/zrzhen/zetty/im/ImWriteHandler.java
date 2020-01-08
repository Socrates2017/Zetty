package com.zrzhen.zetty.im;

import com.zrzhen.zetty.core.SocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class ImWriteHandler implements CompletionHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(ImWriteHandler.class);

    @Override
    public void completed(Integer result, SocketSession socketSession) {
        ByteBuffer buffer = socketSession.getWriteBuffer();
        if (buffer.hasRemaining()) {
            log.warn("buffer.hasRemaining()");
            //socketSession.write(this);
        }

    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        socketSession.destroy();
    }
}
