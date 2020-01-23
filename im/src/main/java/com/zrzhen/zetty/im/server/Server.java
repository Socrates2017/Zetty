package com.zrzhen.zetty.im.server;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.im.FixedDecode;
import com.zrzhen.zetty.im.FixedEncode;
import com.zrzhen.zetty.net.Processor;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.ZettyServer;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ZettyServer.config()
                .port(8080)
                .socketReadTimeout(Integer.MAX_VALUE)
                .decode(new FixedDecode())
                .encode(new FixedEncode())
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
                            socketSession.write("登录成功，名字为：" + msg);
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
                            socketSession.write(response);

                        } else {

                            String sendTo = (String) Manager.getMapBySession(socketSession).get("sentTo");
                            if (sendTo == null) {
                                socketSession.write("请设置消息接收的对象，设置命令：>>send to:userName");
                            } else {

                                SocketSession sessionSendTo = Manager.getSocketSessionByUserName(sendTo);

                                if (sessionSendTo == null) {
                                    socketSession.write("消息发送失败，用户已下线：" + sendTo);
                                } else {
                                    socketSession.write(msg);
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
