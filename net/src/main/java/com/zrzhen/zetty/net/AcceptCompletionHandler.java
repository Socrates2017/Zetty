package com.zrzhen.zetty.net;

import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public abstract class AcceptCompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    protected Builder builder;

    public AcceptCompletionHandler(Builder builder) {
        this.builder = builder;
    }
}
