package com.zrzhen.zetty.net.aio;

import com.zrzhen.zetty.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 * <p>
 * 服务端
 */
public class ZettyServerTest2 {
    private static final Logger log = LoggerFactory.getLogger(ZettyServerTest2.class);

    public static void main(String[] args) throws Exception {
        ZettyServer.config()
                .port(80)
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
                        String response = "get:" + message;
                        session.write(response);
                        return false;
                    }
                })
                .writeHandler(new WriteHandler<Integer, SocketSession>() {
                    @Override
                    public void completed(Integer result, SocketSession socketSession) {
                        AsynchronousSocketChannel channel = socketSession.getSocketChannel();
                        ByteBuffer buffer = socketSession.getWriteBuffer();
                        if (buffer.hasRemaining()) {
                            log.warn("buffer.hasRemaining()");
                            channel.write(buffer, socketSession, this);
                        }else {
                            socketSession.destroy();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, SocketSession socketSession) {
                        log.error(exc.getMessage(), exc);
                        socketSession.destroy();
                    }
                })
                .buildServer()
                .start();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }


}
