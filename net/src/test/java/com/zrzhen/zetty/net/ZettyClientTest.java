package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

/**
 * @author chenanlian
 * 客户端
 */
public class ZettyClientTest {

    private static Logger log = LoggerFactory.getLogger(ZettyClientTest.class);

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 10000; i++) {
            send();
            try {
                Thread.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void send() throws IOException {
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
        channel.connect(new InetSocketAddress("127.0.0.1", 80), channel, new CompletionHandler<Void, AsynchronousSocketChannel>() {

            @Override
            public void completed(Void result, AsynchronousSocketChannel attachment) {

                ByteBuffer response = null;
                try {
                    response = ByteBuffer.wrap(("test message!!!!").getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                attachment.write(response, attachment, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
                    @Override
                    public void completed(Integer result, AsynchronousSocketChannel channel) {
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 1024);

                        channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                attachment.flip();
                                Charset charset = Charset.forName("utf-8");
                                String msg = charset.decode(attachment).toString();
                                System.out.println("get the message:" + msg);
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                log.error(exc.getMessage(), exc);
                            }
                        });
                    }

                    @Override
                    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                        log.error(exc.getMessage(), exc);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

            }
        });


    }


}
