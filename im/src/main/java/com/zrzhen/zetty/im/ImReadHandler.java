package com.zrzhen.zetty.im;

import com.zrzhen.zetty.core.SocketReadHandler;
import com.zrzhen.zetty.core.SocketSession;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ImReadHandler implements SocketReadHandler<Integer, SocketSession> {


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
        System.out.println("The server get the message: " + expression);
        // String message = FileUtil.buf2Str(readBuffer);
        socketSession.read();

        String response = "Hi,I am the server,I got your message:" + expression;
        byte[] req = response.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        socketSession.write(writeBuffer, new ImWriteHandler());

    }

    @Override
    public void failed(Throwable exc, SocketSession attachment) {
        attachment.destroy();
    }
}
