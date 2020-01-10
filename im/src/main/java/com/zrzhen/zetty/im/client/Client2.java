package com.zrzhen.zetty.im.client;

import com.zrzhen.zetty.im.util.ByteUtil;
import com.zrzhen.zetty.net.DefaultWriteHandler;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.ZettyClient;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author chenanlian
 */
public class Client2 {

    public static void main(String[] args) throws Exception {
        SocketSession socketSession = ZettyClient.config()
                .port(8080)
                .readHandlerClass(ReadHandler.class)
                .socketReadTimeout(Long.MAX_VALUE)
                .buildClient()
                .connect();

        //sendMsg(socketSession, "Hi, I am client, testing");
        Scanner scanner = new Scanner(System.in);

        while (sendMsg(socketSession, scanner.nextLine())) {
        }


    }

    public static boolean sendMsg(SocketSession socketSession, String msg) throws Exception {
        if (msg.equals("q")) {
            return false;
        }
        ByteBuffer writeBuffer = ByteUtil.msgEncode(msg);
        writeBuffer.flip();
        socketSession.write(writeBuffer, new DefaultWriteHandler());
        return true;
    }


}
