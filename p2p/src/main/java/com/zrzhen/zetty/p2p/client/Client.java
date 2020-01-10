package com.zrzhen.zetty.p2p.client;

import com.zrzhen.zetty.net.DefaultAcceptCompletionHandler;
import com.zrzhen.zetty.net.DefaultWriteHandler;
import com.zrzhen.zetty.net.ZettyClient;
import com.zrzhen.zetty.net.ZettyServer;
import com.zrzhen.zetty.p2p.util.ByteUtil;

import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author chenanlian
 */
public class Client {

    public static void main(String[] args) throws Exception {
        ClientSocketSession socketSession = (ClientSocketSession) ZettyClient.config()
                .port(8080)
                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                .readHandlerClass(ReadHandler.class)
                .socketSessionClass(ClientSocketSession.class)
                .socketReadTimeout(Long.MAX_VALUE)
                .buildClient()
                .connect();

        //sendMsg(socketSession, "Hi, I am client, testing");
        Scanner scanner = new Scanner(System.in);

        while (sendMsg(socketSession, scanner.nextLine())) {
        }


    }

    public static boolean sendMsg(ClientSocketSession socketSession, String msg) throws Exception {
        if (msg.equals("listen")) {
            int port = Integer.valueOf(socketSession.getLocalAddress().substring((socketSession.getLocalAddress().lastIndexOf(":") + 1)));

            ZettyServer.config()
                    .port(port)
                    .acceptCompletionHandlerClass(DefaultAcceptCompletionHandler.class)
                    .readHandlerClass(ReadHandler.class)
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .socketReadTimeout(Long.MAX_VALUE)
                    .buildServer()
                    .start();

            return false;
        }
        ByteBuffer writeBuffer = ByteUtil.msgEncode(msg);
        writeBuffer.flip();
        socketSession.write(writeBuffer, new DefaultWriteHandler());
        return true;
    }


}
