package com.zrzhen.zetty.im;

import com.zrzhen.zetty.core.ZettyServer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 */
public class ImServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        ZettyServer.config()
                .port(8080)
                .readHandlerClass(ImReadHandler.class)
                .socketReadTimeout(Long.MAX_VALUE)
                .buildServer()
                .start();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();

    }
}
