package com.zrzhen.zetty.p2p.client;

import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.net.DefaultWriteHandler;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.ZettyClient;
import com.zrzhen.zetty.p2p.MessageTypeEnum;
import com.zrzhen.zetty.p2p.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author chenanlian
 */
public class Client {
    private static Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws Exception {
        SocketSession socketSession = ZettyClient.config()
                .port(8080)
                .setOption(StandardSocketOptions.SO_REUSEADDR, true)
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

        if (msg.startsWith("set msg type:")) {
            String messageType = msg.substring(14);

            MessageTypeEnum typeEnum = MessageTypeEnum.getByName(messageType);

            if (typeEnum != null) {
                Manager.messageType = messageType;
                log.info("Set the message type successful! The message type is:{}", messageType);
            } else {
                log.info("Set the message type fail! Wrong message type:{}", messageType);
            }
            return true;
        }

        Map map = new HashMap<>(2);
        map.put("type", Manager.messageType);
        map.put("payload", msg);

        String json = JsonUtil.obj2Json(map);

        ByteBuffer writeBuffer = ByteUtil.msgEncode(json);
        writeBuffer.flip();
        socketSession.write(writeBuffer, new DefaultWriteHandler());
        return true;
    }


}
