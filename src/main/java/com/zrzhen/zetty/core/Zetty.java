package com.zrzhen.zetty.core;

import com.zrzhen.zetty.core.http.Request;
import com.zrzhen.zetty.core.mvc.ServerContext;
import com.zrzhen.zetty.core.util.ProUtil;
import com.zrzhen.zetty.core.util.ServerUtil;
import com.zrzhen.zetty.util.quartz.MyScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author chenanlian
 */
public class Zetty {

    private static final Logger log = LoggerFactory.getLogger(Zetty.class);

    public static void run(String[] args) throws Exception {

        try {

            /*初始化环境配置文件*/
            log.info("Environment properties init start......");
            Long envInitStart = System.currentTimeMillis();
            ServerUtil.initEnv(args);
            log.info("Environment properties init end, cost time:{}ms.", System.currentTimeMillis() - envInitStart);

            /*初始化路由映射*/
            log.info("Server Context init start......");
            Long serverContextStart = System.currentTimeMillis();
            ServerContext.init();
            log.info("Server Context init end, cost time:{}ms.", System.currentTimeMillis() - serverContextStart);

            /*运行端口*/
            Integer port = ProUtil.getInteger("server.port", 8080);
            log.info("Aio server is going to start, the server port is:{}, environment is {}, whether use the environment properties in jar {}.", port, ProUtil.env, ProUtil.innerEnv);


            /*启动定时任务*/
            MyScheduler.start();

            Long httpServerStart = System.currentTimeMillis();

            ExecutorService channelExcutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withCachedThreadPool(channelExcutor, 1);

            /*创建监听套接字*/
            AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(channelGroup);
            if (listener.isOpen()) {
                listener.setOption(StandardSocketOptions.SO_RCVBUF, Constant.BUFFER_SIZE);
                listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                /*绑定端口*/
                listener.bind(new InetSocketAddress(port));
            } else {
                throw new RuntimeException("Channel not opened!");
            }

            /*接收客户链接*/
            listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                @Override
                public void completed(AsynchronousSocketChannel ch, Void att) {
                    //accept the next connection
                    listener.accept(null, this);

                    // handle this connection
                    ByteBuffer buffer = ByteBuffer.allocate(Constant.BUFFER_SIZE);
                    ch.read(buffer, Constant.SOCKET_READ_TIMEOUT, TimeUnit.SECONDS, buffer, new ReadHandler(new Request(), ch));
                }

                @Override
                public void failed(Throwable exc, Void att) {
                    //accept the next connection
                    log.error(exc.getMessage(), exc);
                    listener.accept(null, this);
                }
            });

            log.info("Aio Server has been started successfully, cost time:{}ms.", System.currentTimeMillis() - httpServerStart);
            log.info("The application has been started successfully, cost time:{}ms.", System.currentTimeMillis() - envInitStart);


            while (true) {
                Thread.sleep(Long.MAX_VALUE);
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


}
