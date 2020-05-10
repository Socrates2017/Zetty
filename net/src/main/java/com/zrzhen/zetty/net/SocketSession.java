package com.zrzhen.zetty.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.nio.ch.DirectBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author chenanlian
 * <p>
 * Socket会话类，管理从TCP连接建立到关闭的整个生命周期
 */
public class SocketSession<T, O> {

    private static final Logger log = LoggerFactory.getLogger(SocketSession.class);

    private SocketSessionStatus socketSessionStatus;

    private AsynchronousSocketChannel socketChannel;

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    private byte[] contextBytes;

    private Builder builder;

    private String remoteAddress;

    private String localAddress;

    private Decode decode;

    private Encode encode;

    private Processor processor;

    private T message;

    private Socket socket;

    public SocketSession(AsynchronousSocketChannel socketChannel, Builder builder) {
        this.socketChannel = socketChannel;
        this.builder = builder;
        init();
    }

    public SocketSession(Socket socket, Builder builder) {
        this.builder = builder;
        this.socket = socket;
        init();
    }

    private void init() {
        this.socketSessionStatus = SocketSessionStatus.NEW;

        this.decode = builder.decode;
        this.encode = builder.encode;
        this.processor = builder.processor;
        //this.readBuffer = ByteBuffer.allocate(builder.readBufSize);
        this.readBuffer = ByteBuffer.allocateDirect(builder.readBufSize);

        if (builder.socketType == SocketEnum.AIO) {
            try {
                //作为服务端时才有值
                SocketAddress socketAddress = socketChannel.getRemoteAddress();
                if (socketAddress != null) {
                    this.remoteAddress = socketAddress.toString();
                }
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
        } else {
            contextBytes = new byte[builder.readBufSize];
            try {
                //作为服务端时才有值
                SocketAddress socketAddress = socket.getRemoteSocketAddress();
                if (socketAddress != null) {
                    this.remoteAddress = socketAddress.toString();
                }
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
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

        if (builder.socketType == SocketEnum.AIO) {
            socketChannel.read(readBuffer, builder.socketReadTimeout, TimeUnit.SECONDS, this, builder.readHandler);
        } else {
            InputStream inputStream = null;
            try {
                inputStream = socket.getInputStream();
                //read方法处会被阻塞，直到操作系统有数据准备好
                int realLen = inputStream.read(contextBytes, 0, builder.readBufSize);

                if (realLen == -1) {
                    this.destroy();
                    return;
                }

                readBuffer.put(Arrays.copyOfRange(contextBytes, 0, realLen));
                this.decode(realLen);

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }


        }

    }

    public void write(O out) {
        if (this.encode != null) {
            this.writeBuffer = this.encode.encode(this, out);
        } else {
            this.writeBuffer = (ByteBuffer) out;
        }

        if (builder.socketType == SocketEnum.AIO) {
            socketChannel.write(writeBuffer, this, builder.writeHandler);
        } else {

            byte[] bytes = buf2Bytes(writeBuffer, writeBuffer.limit());
            OutputStream os = null;
            try {
                try {
                    os = socket.getOutputStream();
                    os.write(bytes);
                    os.flush();//自动关闭流

                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }

                builder.writeHandler.completed(0, this);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }


        }


    }

    public static byte[] buf2Bytes(ByteBuffer buffer, int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
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

        if (builder.socketType == SocketEnum.AIO) {
            if (this.socketChannel != null) {
                try {
                    socketChannel.shutdownInput();
                    socketChannel.shutdownOutput();

                    this.socketChannel.close();
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }

            clean(writeBuffer);
            clean(readBuffer);
            writeBuffer = null;
            readBuffer = null;
            socketChannel = null;
            builder = null;
            socketSessionStatus = SocketSessionStatus.DESTROYED;
        } else {

            try {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }

                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }

                if (!socket.isClosed()) {
                    socket.close();
                }
                writeBuffer = null;
                builder = null;
                socketSessionStatus = SocketSessionStatus.DESTROYED;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        //Thread.currentThread().interrupt();
    }

    public static void clean(final ByteBuffer byteBuffer) {
        if (byteBuffer != null && byteBuffer.isDirect()) {
            ((DirectBuffer) byteBuffer).cleaner().clean();
        }
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

    public byte[] getContextBytes() {
        return contextBytes;
    }

    public void setContextBytes(byte[] contextBytes) {
        this.contextBytes = contextBytes;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Decode getDecode() {
        return decode;
    }

    public void setDecode(Decode decode) {
        this.decode = decode;
    }

    public Encode getEncode() {
        return encode;
    }

    public void setEncode(Encode encode) {
        this.encode = encode;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return remoteAddress;
    }
}
