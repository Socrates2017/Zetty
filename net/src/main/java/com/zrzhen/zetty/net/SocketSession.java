package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

/**
 * @author chenanlian
 * <p>
 * Socket会话类，管理从TCP连接建立到关闭的整个生命周期
 */
public class SocketSession {

    private static final Logger log = LoggerFactory.getLogger(SocketSession.class);

    protected SocketSessionStatus socketSessionStatus;

    protected AsynchronousSocketChannel socketChannel;

    protected ByteBuffer readBuffer;

    protected ByteBuffer writeBuffer;

    protected SocketReadHandler socketReadHandler;

    protected Builder builder;

    protected String remoteAddress;

    protected String localAddress;


    public SocketSession(AsynchronousSocketChannel socketChannel, Builder builder) {
        this.socketChannel = socketChannel;
        this.builder = builder;
        init();
    }

    private void init() {
        this.socketSessionStatus = SocketSessionStatus.NEW;
        this.readBuffer = ByteBuffer.allocate(builder.readBufSize);

        try {
            //作为服务端时才有值
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (socketAddress != null) {
                this.remoteAddress = socketAddress.toString();
            }
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        try {
            socketReadHandler = builder.readHandlerClass.newInstance();
        } catch (InstantiationException e) {
            log.debug(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.debug(e.getMessage(), e);
        }
    }

    public void read() {
        readBuffer.clear();
        socketChannel.read(readBuffer, builder.socketReadTimeout, TimeUnit.SECONDS, this, socketReadHandler);
    }

    public void write(ByteBuffer buffer, CompletionHandler<Integer, SocketSession> writeHandler) {
        writeBuffer = buffer;
        socketChannel.write(writeBuffer, this, writeHandler);
    }

    /**
     * 销毁
     */
    public void destroy() {
        log.debug("The socket will close.");

        if (socketSessionStatus == SocketSessionStatus.DESTROYED) {
            return;
        }

        if (this.socketChannel != null) {
            try {
                this.socketChannel.close();
            } catch (Throwable e) {
                //ignore
            }
        }

        readBuffer = null;
        writeBuffer = null;
        socketChannel = null;
        socketReadHandler = null;
        builder = null;
        socketSessionStatus = SocketSessionStatus.DESTROYED;
    }

    public AsynchronousSocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public void setReadBuffer(ByteBuffer readBuffer) {
        this.readBuffer = readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public void setWriteBuffer(ByteBuffer writeBuffer) {
        this.writeBuffer = writeBuffer;
    }

    public SocketSessionStatus getSocketSessionStatus() {
        return socketSessionStatus;
    }

    public void setSocketSessionStatus(SocketSessionStatus socketSessionStatus) {
        this.socketSessionStatus = socketSessionStatus;
    }

    public SocketReadHandler getSocketReadHandler() {
        return socketReadHandler;
    }

    public void setSocketReadHandler(SocketReadHandler socketReadHandler) {
        this.socketReadHandler = socketReadHandler;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }
}
