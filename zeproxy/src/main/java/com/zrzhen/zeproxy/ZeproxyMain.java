package com.zrzhen.zeproxy;

import com.zrzhen.zetty.http.HttpDecode;
import com.zrzhen.zetty.http.HttpWriteHandler;
import com.zrzhen.zetty.http.http.Request;
import com.zrzhen.zetty.net.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

public class ZeproxyMain {

    public static void main(String[] args) throws Exception {
        ZettyServer.config()
                .port(9090)
                .socketType(SocketEnum.AIO)
                .decode(new HttpDecode())
                .processor(new Processor<Request>() {

                    public boolean process(final SocketSession session, Request message) {

                        try {
                            ZettyClient.config()
                                    .host(message.getHeaders().get("Host"))
                                    .port(80)
                                    .socketType(SocketEnum.AIO)
                                    .socketReadTimeout(Integer.MAX_VALUE)
                                    .decode(new Decode<ByteBuffer>() {
                                        public boolean decode(SocketSession clientSession, Integer readLength, ByteBuffer o) {
                                            o = clientSession.getReadBuffer();
                                            return true;
                                        }
                                    })
                                    .processor(new Processor<ByteBuffer>() {
                                        public boolean process(SocketSession clientSession, ByteBuffer message) {

                                            session.write(message);

                                            return false;
                                        }
                                    })
                                    .writeHandler(new HttpWriteHandler())
                                    .buildClient()
                                    .connect();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }
                })
                .writeHandler(new HttpWriteHandler())
                .buildServer()
                .start();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();

    }
}
