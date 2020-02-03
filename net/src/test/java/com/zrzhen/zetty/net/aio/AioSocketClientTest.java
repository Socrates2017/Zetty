package com.zrzhen.zetty.net.aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 * aio socket 客户端demo，测试aio client 的api
 * 可批量多线程启动，用来做性能测试
 */
public class AioSocketClientTest {

    private static Logger log = LoggerFactory.getLogger(AioSocketClientTest.class);

    public static void main(String[] args) throws Exception {

        //调用的总次数
        int n = 10;
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            send();
            try {
                System.out.println(i);
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();

        long qps = n / ((end - start) / 1000);
        System.out.println("qps:" + qps);
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    public static void send() throws IOException {
        AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
        channel.connect(new InetSocketAddress("127.0.0.1", 80), channel, new CompletionHandler<Void, AsynchronousSocketChannel>() {

            @Override
            public void completed(Void result, AsynchronousSocketChannel attachment) {

                ByteBuffer request = ByteBuffer.wrap(file2Byte("E:\\github\\zetty\\cms\\src\\main\\resources\\html\\index.html"));
                attachment.write(request, attachment, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
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

    public static byte[] file2Byte(String path) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[in.available()];
            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
