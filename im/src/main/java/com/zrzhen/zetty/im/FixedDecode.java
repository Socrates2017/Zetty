package com.zrzhen.zetty.im;

import com.zrzhen.zetty.im.server.ImMessage;
import com.zrzhen.zetty.im.util.ByteUtil;
import com.zrzhen.zetty.net.Decode;
import com.zrzhen.zetty.net.SocketSession;

import java.nio.ByteBuffer;

public class FixedDecode implements Decode<ImMessage> {
    @Override
    public boolean decode(SocketSession socketSession, Integer readLength, ImMessage message) {
        if (message == null) {
            message = new ImMessage();
            socketSession.setMessage(message);
        }

        ByteBuffer readBuffer = socketSession.getReadBuffer();
        readBuffer.flip();
        int length = ByteUtil.msgLength(readBuffer);
        byte[] msg2 = ByteUtil.msgBytes(readBuffer, readLength - 4);
        message.setMsgIndex(message.getMsgIndex() + msg2.length);

        if (length == message.getMsgIndex()) {
            if (message.getMsg() == null) {
                message.setMsg(msg2);
            } else {
                byte[] msg = ByteUtil.byteMerger(message.getMsg(), msg2);
                message.setMsg(msg);
            }
            return true;
        } else {
            return false;
        }
    }
}
