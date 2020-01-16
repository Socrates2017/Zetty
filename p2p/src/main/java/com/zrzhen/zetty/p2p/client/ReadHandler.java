package com.zrzhen.zetty.p2p.client;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.net.SocketReadHandler;
import com.zrzhen.zetty.net.SocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public class ReadHandler implements SocketReadHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(ReadHandler.class);


    @Override
    public void completed(Integer result, SocketSession socketSession) {
        if (result == -1) {
            socketSession.destroy();
            return;
        }


        ByteBuffer readBuffer = socketSession.getReadBuffer();
        String msg = FileUtil.buf2Str(readBuffer);
        log.info("Receive the message: {}", msg);
        socketSession.read();

    }

    @Override
    public void failed(Throwable exc, SocketSession attachment) {
        attachment.destroy();
    }
}
