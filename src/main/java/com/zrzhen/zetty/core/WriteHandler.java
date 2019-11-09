package com.zrzhen.zetty.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static Logger log = LoggerFactory.getLogger(ReadHandler.class);


    AsynchronousSocketChannel channel;

    boolean isKeepAlive;

    public WriteHandler(AsynchronousSocketChannel channel, boolean isKeepAlive) {
        this.channel = channel;
        this.isKeepAlive = isKeepAlive;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        //如果没有发送完，就继续发送直到完成
        if (buffer.hasRemaining()) {
            log.warn("buffer.hasRemaining()");
            channel.write(buffer, buffer, this);
        } else {
            if (!isKeepAlive) {
                MvcUtil.closeChannel(channel);
            }
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error(exc.getMessage(), exc);
        MvcUtil.closeChannel(channel);
    }
}
