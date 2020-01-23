package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author chenanlian
 * <p>
 * Socket会话类，管理从TCP连接建立到关闭的整个生命周期
 */
public class SocketSession<T, O> {

    private static final Logger log = LoggerFactory.getLogger(SocketSession.class);

    protected SocketSessionStatus socketSessionStatus;

    protected AsynchronousSocketChannel socketChannel;

    protected ByteBuffer readBuffer;

    protected ByteBuffer writeBuffer;

    protected SocketReadHandler socketReadHandler;

    protected Builder builder;

    protected String remoteAddress;

    protected String localAddress;

    protected Decode decode;

    public Encode encode;

    protected Processor processor;

    protected T message;

    public SocketSession(AsynchronousSocketChannel socketChannel, Builder builder) {
        this.socketChannel = socketChannel;
        this.builder = builder;
        init();
    }

    private void init() {
        this.socketSessionStatus = SocketSessionStatus.NEW;
        this.readBuffer = ByteBuffer.allocate(builder.readBufSize);
        this.decode = builder.decode;
        this.encode = builder.encode;
        this.processor = builder.processor;

        try {
            //作为服务端时才有值
            SocketAddress socketAddress = socketChannel.getRemoteAddress();
            if (socketAddress != null) {
                this.remoteAddress = socketAddress.toString();
            }
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

    }

    public void decode(Integer readLength) {
        if (readLength == -1) {
            this.destroy();
            return;
        }

        boolean isReadEnd = this.decode.decode(this, readLength, message);
        if (!isReadEnd) {
            read();
        } else {
            this.processor.process(this, message);
        }
    }

    public void read() {
        readBuffer.clear();
        socketChannel.read(readBuffer, builder.socketReadTimeout, TimeUnit.SECONDS, this, builder.readHandler);
    }

    public void write(O out) {
        if (this.encode != null) {
            this.writeBuffer = this.encode.encode(this, out);
        } else {
            this.writeBuffer = (ByteBuffer) out;
        }
        socketChannel.write(writeBuffer, this, builder.writeHandler);
    }

    public void writeRemaining(ByteBuffer buffer) {
        this.writeBuffer = buffer;
        socketChannel.write(writeBuffer, this, builder.writeHandler);
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
                socketChannel.shutdownInput();
                socketChannel.shutdownOutput();

                this.socketChannel.close();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }

        writeBuffer = null;
        socketChannel = null;
        socketReadHandler = null;
        builder = null;
        socketSessionStatus = SocketSessionStatus.DESTROYED;
        Thread.currentThread().interrupt();
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

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return remoteAddress;
    }
}
