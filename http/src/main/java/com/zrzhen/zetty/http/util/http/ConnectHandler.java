package com.zrzhen.zetty.http.util.http;

import com.zrzhen.zetty.http.http.Request;
import com.zrzhen.zetty.http.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author chenanlian
 */
public class ConnectHandler implements CompletionHandler<Void, Object> {

    private static Logger log = LoggerFactory.getLogger(ConnectHandler.class);

    AsynchronousSocketChannel client;
    Request request;
    AsyHttpClientCallback callback;

    public ConnectHandler(AsynchronousSocketChannel client, Request request, AsyHttpClientCallback callback) {
        this.client = client;
        this.request = request;
        this.callback = callback;
    }

    public ConnectHandler(AsynchronousSocketChannel client, Request request) {
        this.client = client;
        this.request = request;
    }

    @Override
    public void completed(Void result, Object attachment) {
        ByteBuffer buffer = request.toByteBuffer();
        buffer.flip();
        client.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (buffer.hasRemaining()) {
                    log.warn("buffer.hasRemaining()");
                    client.write(attachment, attachment, this);
                } else {
                    ByteBuffer bufferForRead = ByteBuffer.allocate(1024 * 1024);
                    client.read(bufferForRead, bufferForRead, new ReadHandler(new Response(), client, callback));
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                log.error("Tcp write failed!");
                throw new RuntimeException(exc);
            }
        });


    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        log.error("Tcp connect is failed!");
        throw new RuntimeException(exc);
    }
}
