package com.zrzhen.zetty.net;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;

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
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open();
        if (listener.isOpen()) {
            listener.setOption(StandardSocketOptions.SO_RCVBUF, builder.readBufSize);
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            /*绑定端口*/
            listener.bind(new InetSocketAddress(builder.port));
        } else {
            throw new RuntimeException("Channel not opened!");
        }

        /*接收客户链接*/
        Constructor c1 = builder.acceptCompletionHandlerClass.getDeclaredConstructor(new Class[]{Builder.class});
        c1.setAccessible(true);
        AcceptCompletionHandler acceptCompletionHandler = (AcceptCompletionHandler) c1.newInstance(new Object[]{builder});
        listener.accept(listener, acceptCompletionHandler);

        log.info("Aio Server has been started successfully, cost time:{}ms.", System.currentTimeMillis() - httpServerStart);

    }


}
