package com.zrzhen.zetty.net.bio;

import com.zrzhen.zetty.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 * zetty bio 服务端 测试
 */
public class BioZettyServerTest {
    private static final Logger log = LoggerFactory.getLogger(BioZettyServerTest.class);

    public static void main(String[] args) throws Exception {
        ZettyServer.config()
                .port(80)
                .socketType(SocketEnum.BIO)
                .socketReadTimeout(Integer.MAX_VALUE)
                .decode(new Decode<String>() {
                    @Override
                    public boolean decode(SocketSession socketSession, Integer readLength, String o) {
                        ByteBuffer readBuffer = socketSession.getReadBuffer();
                        readBuffer.flip();
                        Charset charset = Charset.forName("utf-8");
                        String msg = charset.decode(readBuffer).toString();
                        socketSession.setMessage(msg);
                        return true;
                    }
                })
                .encode(new Encode<String>() {
                    @Override
                    public ByteBuffer encode(SocketSession session, String out) {

                        ByteBuffer response = null;
                        try {
                            response = ByteBuffer.wrap(out.getBytes("utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return response;
                    }
                })
                .processor(new Processor<String>() {
                    @Override
                    public boolean process(SocketSession session, String message) {
                        String response = message;
                        session.write(response);
                        return false;
                    }
                })
                .buildServer()
                .start();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }


}
