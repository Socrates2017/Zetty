package com.zrzhen.zetty.im.client;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.im.FixedDecode;
import com.zrzhen.zetty.im.FixedEncode;
import com.zrzhen.zetty.im.ImMessage;
import com.zrzhen.zetty.net.Processor;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.aio.WriteHandler;
import com.zrzhen.zetty.net.ZettyClient;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;

/**
 * @author chenanlian
 */
public class Client {

    public static void main(String[] args) throws Exception {
        SocketSession socketSession = ZettyClient.config()
                .port(8080)
                .socketReadTimeout(Integer.MAX_VALUE)
                .decode(new FixedDecode())
                .encode(new FixedEncode())
                .processor(new Processor<ImMessage>() {
                    @Override
                    public boolean process(SocketSession socketSession, ImMessage message) {
                        System.out.println("receive the message:"+FileUtil.byte2Str(message.getMsg()));
                        message.setMsg(null);
                        message.setMsgIndex(0);
                        socketSession.read();
                        return true;
                    }
                })
                .writeHandler(new WriteHandler<Integer, SocketSession>() {
                    @Override
                    public void completed(Integer result, SocketSession socketSession) {
                        AsynchronousSocketChannel channel = socketSession.getSocketChannel();
                        ByteBuffer buffer = socketSession.getWriteBuffer();
                        if (buffer.hasRemaining()) {
                            channel.write(buffer, socketSession, this);
                        }
                        //长连接，所以，写完毕后不关闭通道
                    }

                    @Override
                    public void failed(Throwable exc, SocketSession socketSession) {
                        exc.getStackTrace();
                        socketSession.destroy();
                    }
                })
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

        socketSession.write(msg);
        return true;
    }


}
