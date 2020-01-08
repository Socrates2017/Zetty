package com.zrzhen.zetty.http.http;

import com.zrzhen.zetty.core.SocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class WriteHandler implements CompletionHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(WriteHandler.class);

    ByteLineHardReader br;
    AsynchronousSocketChannel channel;

    boolean isKeepAlive;

    public WriteHandler(ByteLineHardReader br, boolean isKeepAlive) {
        this.br = br;
        this.isKeepAlive = isKeepAlive;
    }

    public WriteHandler(boolean isKeepAlive) {
        this.isKeepAlive = isKeepAlive;
    }

    @Override
    public void completed(Integer result, SocketSession socketSession) {
        channel = socketSession.getSocketChannel();
        ByteBuffer buffer = socketSession.getWriteBuffer();
        //如果没有发送完，就继续发送直到完成
        if (buffer.hasRemaining()) {
            log.warn("buffer.hasRemaining()");
            channel.write(buffer, socketSession, this);

        } else {
            if (!isKeepAlive) {
                socketSession.destroy();
            } else if (br != null) {
                byte[] line = br.nextPart();
                ByteBuffer byteBuffer1 = ByteBuffer.wrap(line);
                log.info("downloading,closing...,read end:{}", br.isReadEnd());
                socketSession.write(byteBuffer1, new WriteHandler(isKeepAlive));
            }
        }
    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        socketSession.destroy();
    }
}
