package com.zrzhen.zetty.p2p.server;

import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.ZettyServer;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
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

        Scanner scanner = new Scanner(System.in);

        while (sendMsg(scanner.nextLine())) {
        }

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    public static boolean sendMsg(String msg) throws Exception {

        System.out.println("接收到命令：" + msg);

        if (msg.equalsIgnoreCase("get session")) {
            Iterator<Map.Entry<String, SocketSession>> iterator = Manager.sessions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, SocketSession> entry = iterator.next();
                String key = entry.getKey();
                System.out.println(key);
            }
        }


        return true;
    }


}
