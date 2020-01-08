package com.zrzhen.zetty.core;

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

    public Builder() {
    }

    public ZettyServer buildServer() {
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

}
