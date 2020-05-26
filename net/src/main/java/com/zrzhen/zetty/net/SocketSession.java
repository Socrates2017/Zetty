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
import java.util.concurrent.Semaphore;
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

    private ByteBuffer writeBufferAll;

    /**
     * 输出信号量,防止并发write导致异常
     */
    private final Semaphore semaphore = new Semaphore(1);

    private byte[] contextBytes;

    private Builder builder;

    private String remoteAddress;

    private String localAddress;

    private Decode decode;

    private Encode encode;

    private Processor processor;

    private T message;

    private Socket socket;

    private Boolean isWriteEnd;

    private int writeMaxSize = 1024 * 1024;
    private int writeNum;
    private int writeTotalNum;
    private byte[] writeBytes;

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
            this.writeBufferAll = this.encode.encode(this, out);
        } else {
            this.writeBufferAll = (ByteBuffer) out;
        }

        if (builder.socketType == SocketEnum.AIO) {

            writeBytes = buf2Bytes(writeBufferAll, writeBufferAll.limit());

            int length = writeBytes.length;

            if (length > writeMaxSize) {
                isWriteEnd = false;
                writeNum = 0;
                writeTotalNum = (int) Math.ceil(length / writeMaxSize);//需要写多少次
                int count = writeMaxSize;
                byte[] bytes1 = Arrays.copyOfRange(writeBytes, 0, writeMaxSize);
                clean(writeBuffer);
                writeBuffer = null;
                writeBuffer = ByteBuffer.allocateDirect(count);
                log.info("xx count:{}", count);
                writeBuffer.put(bytes1);
                writeBuffer.flip();
                socketChannel.write(writeBuffer, this, builder.writeHandler);

            } else {
                isWriteEnd = true;
                clean(writeBuffer);
                writeBuffer = writeBufferAll;
                writeBuffer.flip();
                socketChannel.write(writeBuffer, this, builder.writeHandler);
            }
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

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
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
        log.debug("The socket will close.socketSessionStatus:{}", socketSessionStatus);

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
            clean(writeBufferAll);
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

    public Boolean getWriteEnd() {
        return isWriteEnd;
    }

    public void setWriteEnd(Boolean writeEnd) {
        isWriteEnd = writeEnd;
    }

    public ByteBuffer getWriteBufferAll() {
        return writeBufferAll;
    }

    public void setWriteBufferAll(ByteBuffer writeBufferAll) {
        this.writeBufferAll = writeBufferAll;
    }

    public int getWriteMaxSize() {
        return writeMaxSize;
    }

    public void setWriteMaxSize(int writeMaxSize) {
        this.writeMaxSize = writeMaxSize;
    }

    public int getWriteNum() {
        return writeNum;
    }

    public void setWriteNum(int writeNum) {
        this.writeNum = writeNum;
    }

    public int getWriteTotalNum() {
        return writeTotalNum;
    }

    public void setWriteTotalNum(int writeTotalNum) {
        this.writeTotalNum = writeTotalNum;
    }

    public byte[] getWriteBytes() {
        return writeBytes;
    }

    public void setWriteBytes(byte[] writeBytes) {
        this.writeBytes = writeBytes;
    }

    @Override
    public String toString() {
        return remoteAddress;
    }
}
