package com.zrzhen.zetty.p2p.server;

import com.zrzhen.zetty.net.ZettyServer;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ZettyServer.config()
                .port(8080)
                .acceptCompletionHandlerClass(AcceptHandler.class)
                .readHandlerClass(ReadHandler.class)
                .socketReadTimeout(Long.MAX_VALUE)
                .buildServer()
                .start();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();

    }
}
