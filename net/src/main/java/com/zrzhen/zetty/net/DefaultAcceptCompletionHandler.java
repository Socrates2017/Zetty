package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author chenanlian
 * <p>
 * 服务端建立TCP连接后回调
 */
public class DefaultAcceptCompletionHandler extends AcceptCompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final Logger log = LoggerFactory.getLogger(DefaultAcceptCompletionHandler.class);

    public DefaultAcceptCompletionHandler(Builder builder) {
        super(builder);
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
