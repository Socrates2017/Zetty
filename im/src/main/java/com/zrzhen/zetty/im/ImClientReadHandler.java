package com.zrzhen.zetty.im;

import com.zrzhen.zetty.core.SocketReadHandler;
import com.zrzhen.zetty.core.SocketSession;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ImClientReadHandler implements SocketReadHandler<Integer, SocketSession> {


    @Override
    public void completed(Integer result, SocketSession socketSession) {
        if (result == -1) {
            socketSession.destroy();
            return;
        }

        ByteBuffer readBuffer = socketSession.getReadBuffer();
        readBuffer.flip();

        byte[] message = new byte[readBuffer.remaining()];
        readBuffer.get(message);

        String expression = null;
        try {
            expression = new String(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("The client get the message: " + expression);
        socketSession.read();
    }

    @Override
    public void failed(Throwable exc, SocketSession attachment) {
        attachment.destroy();
    }
}
