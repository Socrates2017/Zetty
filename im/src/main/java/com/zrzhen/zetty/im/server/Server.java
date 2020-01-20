package com.zrzhen.zetty.im.server;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.im.util.ByteUtil;
import com.zrzhen.zetty.net.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ZettyServer.config()
                .port(8080)
                .protocol(new Protocol<ImMessage>() {
                    @Override
                    public boolean decode(SocketSession socketSession, Integer readLength, ImMessage message) {


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
                })
                .processor(new Processor<ImMessage>() {
                    @Override
                    public boolean process(SocketSession socketSession, ImMessage message) {

                        HashMap<String, Object> map = Manager.getMapBySession(socketSession);
                        if (map == null) {
                            map = new HashMap();
                            map.put("session", socketSession);
                            Manager.sessions.put(socketSession.getRemoteAddress(), map);
                        }

                        String msg = FileUtil.byte2Str(message.getMsg());

                        message.setMsg(null);
                        message.setMsgIndex(0);
                        socketSession.read();

                        if (msg.startsWith(">>login:")) {
                            String userName = msg.substring(8);
                            Manager.loginUser.put(userName, socketSession.getRemoteAddress());
                            ByteBuffer writeBuffer = FileUtil.str2Buf("登录成功，名字为：" + msg);
                            socketSession.write(writeBuffer);
                        } else if (msg.startsWith(">>send to:")) {
                            String targetUser = msg.substring(10);
                            HashMap map2 = Manager.getMapByUserName(targetUser);

                            String response = null;
                            if (map2 == null) {
                                response = "用户不存在，设置失败：" + targetUser;
                            } else {
                                map2.put("sentTo", targetUser);
                                response = "已设置消息接收对象：" + targetUser;
                            }
                            ByteBuffer writeBuffer = FileUtil.str2Buf(response);
                            socketSession.write(writeBuffer);

                        } else {

                            String sendTo = (String) Manager.getMapBySession(socketSession).get("sentTo");
                            if (sendTo == null) {
                                ByteBuffer writeBuffer = FileUtil.str2Buf("请设置消息接收的对象，设置命令：>>send to:userName");
                                socketSession.write(writeBuffer);
                            } else {

                                SocketSession sessionSendTo = Manager.getSocketSessionByUserName(sendTo);

                                if (sessionSendTo == null) {
                                    ByteBuffer writeBuffer = FileUtil.str2Buf("消息发送失败，用户已下线：" + sendTo);
                                    socketSession.write(writeBuffer);
                                } else {
                                    ByteBuffer writeBuffer = FileUtil.str2Buf(msg);
                                    socketSession.write(writeBuffer);
                                }
                            }
                        }
                        return false;
                    }
                })
                .writeHandler(new ImWriteHandler())
                .buildServer()
                .start();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();

    }
}
