package com.zrzhen.zetty.net;

import java.net.SocketOption;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 *
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


    public Builder() {
    }

    public ZettyServer buildServer() {
        if (acceptCompletionHandlerClass==null){
            acceptCompletionHandlerClass=DefaultAcceptCompletionHandler.class;
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
}
