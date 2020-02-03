package com.zrzhen.zetty.net.aio;

import com.zrzhen.zetty.net.SocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenanlian
 */
public class ReadHandler implements SocketReadHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(ReadHandler.class);

    @Override
    public void completed(Integer result, SocketSession session) {
        session.decode(result);
    }

    @Override
    public void failed(Throwable exc, SocketSession session) {
        log.error(exc.getMessage(), exc);
        session.destroy();
    }
}
