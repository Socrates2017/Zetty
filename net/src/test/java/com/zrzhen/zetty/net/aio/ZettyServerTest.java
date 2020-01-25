package com.zrzhen.zetty.net.aio;

import com.zrzhen.zetty.net.ExecutorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * @author chenanlian
 * <p>
 * 服务端
 */
public class ZettyServerTest {
    private static final Logger log = LoggerFactory.getLogger(ZettyServerTest.class);

    public static void main(String[] args) throws Exception {
        start();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    public static void start() throws Exception {
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(ExecutorUtil.channelExcutor);
        final AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(channelGroup);
        listener.bind(new InetSocketAddress(80));

        Future<AsynchronousSocketChannel> accept;
        while (true) {
            accept = listener.accept();
            final AsynchronousSocketChannel channel = accept.get();

            ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 1024);
            channel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer readBuffer) {
                    readBuffer.flip();
                    Charset charset = Charset.forName("utf-8");
                    String msg = charset.decode(readBuffer).toString();
                    System.out.println("get the message:" + msg);
                    ByteBuffer response = null;
                    try {
                        response = ByteBuffer.wrap(("get:" + msg).getBytes("utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    channel.write(response, response, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            //如果没有发送完，就继续发送直到完成
                            if (buffer.hasRemaining()) {
                                log.warn("buffer.hasRemaining()");
                                channel.write(buffer, buffer, this);

                            }else {
                                try {
                                    channel.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            log.error(exc.getMessage(), exc);
                        }
                    });
                }

                @Override
                public void failed(Throwable exc, ByteBuffer channel) {
                    log.error(exc.getMessage(), exc);
                }
            });

        }

    }


}
