package com.zrzhen.zetty.net;

import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 * <p>
 * 构建通信服务配置类
 */
public class Builder {

    public String host = "localhost";

    public int port = 80;

    public int readBufSize = 1024 * 1024;

    public long socketReadTimeout = 30;

    public Class<? extends SocketReadHandler> readHandlerClass;

    public Class<? extends AcceptCompletionHandler> acceptCompletionHandlerClass;

    public Map<SocketOption<Object>, Object> socketOptions;

    public ReadHandler readHandler = new ReadHandler();

    public WriteHandler writeHandler;

    public ByteBuffer writeBuffer;

    public Decode decode;

    public Encode encode;

    public Processor processor;

    public Builder() {
    }

    public ZettyServer buildServer() {
        if (acceptCompletionHandlerClass == null) {
            acceptCompletionHandlerClass = DefaultAcceptCompletionHandler.class;
        }

        if (readHandler == null) {
            readHandler = new ReadHandler();
        }

        if (writeBuffer == null) {
            writeBuffer = ByteBuffer.allocateDirect(readBufSize);
        }
        return new ZettyServer(this);
    }

    public ZettyClient buildClient() {
        return new ZettyClient(this);
    }

    public Builder host(String host) {
        this.host = host;
        return this;
    }

    public Builder port(int port) {
        this.port = port;
        return this;
    }

    public Builder readBufSize(int readBufSize) {
        this.readBufSize = readBufSize;
        return this;
    }

    public Builder socketReadTimeout(long socketReadTimeout) {
        this.socketReadTimeout = socketReadTimeout;
        return this;
    }

    public Builder readHandlerClass(Class<? extends SocketReadHandler> readHandlerClass) {
        this.readHandlerClass = readHandlerClass;
        return this;
    }

    public Builder acceptCompletionHandlerClass(Class<? extends AcceptCompletionHandler> acceptCompletionHandlerClass) {
        this.acceptCompletionHandlerClass = acceptCompletionHandlerClass;
        return this;
    }

    public Builder setOption(SocketOption socketOption, Object f) {
        if (socketOptions == null) {
            socketOptions = new HashMap<>(5);
        }
        socketOptions.put(socketOption, f);
        return this;
    }

    public Builder decode(Decode decode) {
        this.decode = decode;
        return this;
    }

    public Builder encode(Encode encode) {
        this.encode = encode;
        return this;
    }

    public Builder processor(Processor processor) {
        this.processor = processor;
        return this;
    }

    public Builder writeHandler(WriteHandler writeHandler) {
        this.writeHandler = writeHandler;
        return this;
    }
}