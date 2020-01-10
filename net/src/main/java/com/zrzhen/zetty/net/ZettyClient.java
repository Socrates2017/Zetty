package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    public synchronized SocketSession connect() throws Exception {
        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();

        if (builder.socketOptions != null) {
            for (Map.Entry<SocketOption<Object>, Object> entry : builder.socketOptions.entrySet()) {
                clientChannel.setOption(entry.getKey(), entry.getValue());
            }
        }

        if (builder.socketSessionClass != null) {
            Constructor c1 = builder.socketSessionClass.getDeclaredConstructor(new Class[]{AsynchronousSocketChannel.class, Builder.class});
            c1.setAccessible(true);
            socketSession = (SocketSession) c1.newInstance(new Object[]{clientChannel, builder});
        } else {
            socketSession = new SocketSession(clientChannel, builder);
        }
        CountDownLatch latch = new CountDownLatch(1);
        clientChannel.connect(new InetSocketAddress(builder.host, builder.port), socketSession, new ConnectCompletionHandler(latch));
        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return socketSession;
    }


}
