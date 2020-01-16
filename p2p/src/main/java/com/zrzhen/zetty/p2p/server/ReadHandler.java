package com.zrzhen.zetty.p2p.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.net.DefaultWriteHandler;
import com.zrzhen.zetty.net.SocketReadHandler;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.p2p.MessageTypeEnum;
import com.zrzhen.zetty.p2p.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public class ReadHandler implements SocketReadHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(ReadHandler.class);

    private byte[] msg;

    private int msgIndex = 0;


    @Override
    public void completed(Integer result, SocketSession socketSession) {
        if (result == -1) {
            socketSession.destroy();
            return;
        }

        ByteBuffer readBuffer = socketSession.getReadBuffer();
        readBuffer.flip();
        int length = ByteUtil.msgLength(readBuffer);
        byte[] msg2 = ByteUtil.msgBytes(readBuffer, result - 4);
        msgIndex += msg2.length;

        if (length == msgIndex) {
            if (msg == null) {
                msg = msg2;
            } else {
                msg = ByteUtil.byteMerger(msg, msg2);

            }

            String msgResult = FileUtil.byte2Str(msg);

            msg = null;
            msgIndex = 0;
            socketSession.read();

            process((P2pSocketSession) socketSession, msgResult);

        } else {
            if (msg == null) {
                msg = msg2;
            } else {
                msg = ByteUtil.byteMerger(msg, msg2);
            }
            socketSession.read();
        }
    }


    public void process(P2pSocketSession socketSession, String msg) {

        log.info("receive the message:{}", msg);

        JsonNode jsonNode = JsonUtil.str2JsonNode(msg);

        String type = jsonNode.get("type").asText();

        String response = "";

        if (type.equalsIgnoreCase(MessageTypeEnum.REGISTER.getName())) {
            String payload = jsonNode.get("payload").asText();
            Manager.register.put(payload, socketSession.getRemoteAddress());
        } else if (type.equalsIgnoreCase(MessageTypeEnum.LOGIN_USER.getName())) {
            response = Manager.loginUser.toString();
        } else {
            response = "I had receive your message successfully. message: " + msg;
        }


        ByteBuffer writeBuffer = FileUtil.str2Buf(response);
        socketSession.write(writeBuffer, new DefaultWriteHandler());


    }

    @Override
    public void failed(Throwable exc, SocketSession attachment) {
        attachment.destroy();
    }
}
