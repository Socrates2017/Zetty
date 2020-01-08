package com.zrzhen.zetty.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 *
 * 服务端建立TCP连接后回调
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final Logger log = LoggerFactory.getLogger(AcceptCompletionHandler.class);

    private Builder builder;

    public AcceptCompletionHandler(Builder builder) {
        this.builder = builder;
    }

    @Override
    public void completed(AsynchronousSocketChannel ch, AsynchronousServerSocketChannel listener) {
        //accept the next connection
        listener.accept(listener, this);
        new SocketSession(ch, builder).read();
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {
        //accept the next connection
        log.error(exc.getMessage(), exc);
        listener.accept(listener, this);
    }
}
