package com.zrzhen.zetty.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author chenanlian
 * 客户端
 */
public class ZettyClient {

    private static Logger log = LoggerFactory.getLogger(ZettyClient.class);

    private Builder builder;

    SocketSession socketSession;

    public ZettyClient(Builder builder) {
        this.builder = builder;
    }

    public static Builder config() {
        return new Builder();
    }

    public synchronized SocketSession connect() throws IOException {
        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
        socketSession = new SocketSession(clientChannel, builder);
        clientChannel.connect(new InetSocketAddress(builder.host, builder.port), socketSession, new ConnectCompletionHandler());
        return socketSession;
    }



}
