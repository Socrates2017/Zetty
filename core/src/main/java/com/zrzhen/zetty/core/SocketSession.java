package com.zrzhen.zetty.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
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

    private SocketSessionStatus socketSessionStatus;

    private AsynchronousSocketChannel socketChannel;

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    private SocketReadHandler socketReadHandler;

    private Builder builder;

    private String hostAddress;


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
                InetAddress remoteAddress = ((InetSocketAddress) socketAddress).getAddress();
                this.hostAddress = remoteAddress.getHostAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            socketReadHandler = builder.readHandlerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        readBuffer.clear();
        socketChannel.read(readBuffer, builder.socketReadTimeout, TimeUnit.SECONDS, this, socketReadHandler);
    }

    public void write(ByteBuffer buffer, CompletionHandler<Integer, SocketSession> writeHandler) {
        writeBuffer = buffer;
        writeBuffer.flip();
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


    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }
}
