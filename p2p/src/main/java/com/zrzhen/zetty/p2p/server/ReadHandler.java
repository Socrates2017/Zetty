package com.zrzhen.zetty.p2p.server;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.net.DefaultWriteHandler;
import com.zrzhen.zetty.net.SocketReadHandler;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.p2p.util.ByteUtil;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public class ReadHandler implements SocketReadHandler<Integer, SocketSession> {


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

            process((ImSocketSession) socketSession, msgResult);

        } else {
            if (msg == null) {
                msg = msg2;
            } else {
                msg = ByteUtil.byteMerger(msg, msg2);
            }
            socketSession.read();
        }
    }


    public void process(ImSocketSession socketSession, String msg) {

        if (msg.equalsIgnoreCase("natPort")) {
            String port = socketSession.getRemoteAddress().substring(socketSession.getRemoteAddress().indexOf(":") + 1);
            ByteBuffer writeBuffer = FileUtil.str2Buf("port:"+port);
            socketSession.write(writeBuffer, new DefaultWriteHandler());
        } else {
            ByteBuffer writeBuffer = FileUtil.str2Buf(msg);
            socketSession.write(writeBuffer, new ImWriteHandler(socketSession));
        }

    }

    @Override
    public void failed(Throwable exc, SocketSession attachment) {
        attachment.destroy();
    }
}
