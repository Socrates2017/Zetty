package com.zrzhen.zetty.net;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

/**
 * @author chenanlian
 * <p>
 * 服务端
 */
public class ZettyServer {

    private static Logger log = LoggerFactory.getLogger(ZettyServer.class);

    private volatile boolean initialized = false;
    private volatile boolean started = false;

    private GenericObjectPool<ByteBuffer> byteBufferPool = null;


    public Builder builder;

    public ZettyServer(Builder builder) {
        this.builder = builder;
    }

    public static Builder config() {
        return new Builder();
    }

    public synchronized void start() throws Exception {
        if (!initialized) {
            initialized = true;
        }

        if (started) {
            return;
        }
        started = true;
        log.info("server startup");


        Long httpServerStart = System.currentTimeMillis();

        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(ExecutorUtil.channelExcutor);

        /*创建监听套接字*/
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(channelGroup);
        if (listener.isOpen()) {
            listener.setOption(StandardSocketOptions.SO_RCVBUF, builder.readBufSize);
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, false);
            /*绑定端口*/
            listener.bind(new InetSocketAddress(builder.port));
        } else {
            throw new RuntimeException("Channel not opened!");
        }

        log.info("Aio Server has been started successfully, cost time:{}ms.", System.currentTimeMillis() - httpServerStart);

        Future<AsynchronousSocketChannel> accept;
        while (true) {
            accept = listener.accept();
            final AsynchronousSocketChannel channel = accept.get();

            ExecutorUtil.processorExcutor.execute(new Runnable() {
                @Override
                public void run() {
                    new SocketSession(channel, builder).read();
                }
            });

        }
    }
}
