package com.zrzhen.zetty.p2p.client;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.net.SocketReadHandler;
import com.zrzhen.zetty.net.SocketSession;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public class ReadHandler implements SocketReadHandler<Integer, SocketSession> {


    @Override
    public void completed(Integer result, SocketSession socketSession) {
        if (result == -1) {
            socketSession.destroy();
            return;
        }


        ByteBuffer readBuffer = socketSession.getReadBuffer();
        System.out.println(FileUtil.buf2Str(readBuffer));
        socketSession.read();

    }

    @Override
    public void failed(Throwable exc, SocketSession attachment) {
        attachment.destroy();
    }
}
