package com.zrzhen.zetty.p2p.client;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.net.SocketReadHandler;
import com.zrzhen.zetty.net.SocketSession;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public class ReadHandler implements SocketReadHandler<Integer, ClientSocketSession> {


    @Override
    public void completed(Integer result, ClientSocketSession socketSession) {
        if (result == -1) {
            socketSession.destroy();
            return;
        }

        ByteBuffer readBuffer = socketSession.getReadBuffer();
        String msg =FileUtil.buf2Str(readBuffer);
        socketSession.read();

        System.out.println(msg);
        if (msg.startsWith("port:")){
            int port = Integer.valueOf(msg.substring(5));
            socketSession.setNatPort(port);
        }

    }

    @Override
    public void failed(Throwable exc, ClientSocketSession attachment) {
        attachment.destroy();
    }
}
