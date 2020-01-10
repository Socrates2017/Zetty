package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Map;
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

        ExecutorService channelExcutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withCachedThreadPool(channelExcutor, 1);

        /*创建监听套接字*/
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(channelGroup);
        if (listener.isOpen()) {
            if (builder.socketOptions != null) {
                for (Map.Entry<SocketOption<Object>, Object> entry : builder.socketOptions.entrySet()) {
                    listener.setOption(entry.getKey(), entry.getValue());
                }
            }
            /*绑定端口*/
            listener.bind(new InetSocketAddress(builder.port));
        } else {
            throw new RuntimeException("Channel not opened!");
        }

        /*接收客户链接*/
        Constructor c1=builder.acceptCompletionHandlerClass.getDeclaredConstructor(new Class[]{Builder.class});
        c1.setAccessible(true);
        AcceptCompletionHandler acceptCompletionHandler=(AcceptCompletionHandler)c1.newInstance(new Object[]{builder});
        listener.accept(listener,acceptCompletionHandler);

        log.info("Aio Server has been started successfully, cost time:{}ms.", System.currentTimeMillis() - httpServerStart);

    }


}
