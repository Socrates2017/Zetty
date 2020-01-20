package com.zrzhen.zetty.http;

import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.WriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class HttpWriteHandler extends WriteHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(HttpWriteHandler.class);


    public HttpWriteHandler() {

    }

    @Override
    public void completed(Integer result, SocketSession socketSession) {
        AsynchronousSocketChannel channel = socketSession.getSocketChannel();
        ByteBuffer buffer = socketSession.getWriteBuffer();
        //如果没有发送完，就继续发送直到完成
        if (buffer.hasRemaining()) {
            log.warn("buffer.hasRemaining()");
            channel.write(buffer, socketSession, this);

        } else {
            socketSession.destroy();
//            if (!isKeepAlive) {
//                socketSession.destroy();
//            } else if (br != null) {
//                byte[] line = br.nextPart();
//                ByteBuffer byteBuffer1 = ByteBuffer.wrap(line);
//                log.info("downloading,closing...,read end:{}", br.isReadEnd());
//                byteBuffer1.flip();
//                socketSession.write(byteBuffer1, new HttpWriteHandler(isKeepAlive));
//            }
        }
    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        socketSession.destroy();
    }
}
