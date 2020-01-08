package com.zrzhen.zetty.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenanlian
 *
 * 服务端
 */
public class ZettyServer {

    private static Logger log = LoggerFactory.getLogger(ZettyServer.class);

    private volatile boolean initialized = false;
    private volatile boolean started = false;

    public Builder builder;

    public ZettyServer(Builder builder) {
        this.builder = builder;
    }

    public static Builder config(){
        return new Builder();
    }

    public synchronized void start() throws IOException {
        if (!initialized) {
            initialized = true;
        }

        if (started) {
            return;
        }
        started = true;
        log.info("server startup");


        Long httpServerStart = System.currentTimeMillis();

        ExecutorService channelExcutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withCachedThreadPool(channelExcutor, 1);

        /*创建监听套接字*/
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(channelGroup);
        if (listener.isOpen()) {
            listener.setOption(StandardSocketOptions.SO_RCVBUF, builder.readBufSize);
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            /*绑定端口*/
            listener.bind(new InetSocketAddress(builder.port));
        } else {
            throw new RuntimeException("Channel not opened!");
        }

        /*接收客户链接*/
        listener.accept(listener, new AcceptCompletionHandler(builder));
        log.info("Aio Server has been started successfully, cost time:{}ms.", System.currentTimeMillis() - httpServerStart);

    }


}
