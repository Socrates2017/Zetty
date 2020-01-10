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

        if (msg.startsWith(">>login:")) {
            String userName = msg.substring(8);
            Manager.loginUser.put(userName, socketSession.getRemoteAddress());
            ByteBuffer writeBuffer = FileUtil.str2Buf("登录成功，名字为：" + msg);
            socketSession.write(writeBuffer, new DefaultWriteHandler());
        } else if (msg.startsWith(">>send to:")) {
            String targetUser = msg.substring(10);
            SocketSession socketSession1 = Manager.getSocketSessionByUserName(targetUser);

            String response = null;
            if (socketSession1 == null) {
                response = "用户不存在，设置失败：" + targetUser;
            } else {
                socketSession.setSentTo(targetUser);
                response = "已设置消息接收对象：" + targetUser;
            }
            ByteBuffer writeBuffer = FileUtil.str2Buf(response);
            socketSession.write(writeBuffer, new DefaultWriteHandler());

        } else {

            String sendTo = socketSession.getSentTo();
            if (sendTo == null) {
                ByteBuffer writeBuffer = FileUtil.str2Buf("请设置消息接收的对象，设置命令：>>send to:userName");
                socketSession.write(writeBuffer, new DefaultWriteHandler());
            } else {

                SocketSession sessionSendTo = Manager.getSocketSessionByUserName(sendTo);

                if (sessionSendTo == null) {
                    ByteBuffer writeBuffer = FileUtil.str2Buf("消息发送失败，用户已下线：" + sendTo);
                    socketSession.write(writeBuffer, new DefaultWriteHandler());
                } else {
                    ByteBuffer writeBuffer = FileUtil.str2Buf(msg);
                    sessionSendTo.write(writeBuffer, new ImWriteHandler(socketSession));
                }
            }
        }
    }

    @Override
    public void failed(Throwable exc, SocketSession attachment) {
        attachment.destroy();
    }
}
