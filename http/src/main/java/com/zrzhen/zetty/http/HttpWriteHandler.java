package com.zrzhen.zetty.http;

import com.zrzhen.zetty.net.SocketEnum;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.aio.WriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;

/**
 * @author chenanlian
 * <p>
 * 消息响应的回调
 */
public class HttpWriteHandler extends WriteHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(HttpWriteHandler.class);


    public HttpWriteHandler() {

    }

    @Override
    public void completed(Integer result, SocketSession socketSession) {

        if (socketSession.getBuilder().socketType == SocketEnum.AIO) {
            ByteBuffer buffer = socketSession.getWriteBuffer();
            //如果没有发送完，就继续发送直到完成
            if (buffer.hasRemaining()) {
                log.warn("buffer.hasRemaining()");
                AsynchronousSocketChannel channel = socketSession.getSocketChannel();
                channel.write(buffer, socketSession, this);
            } else {
                boolean isWriteEnd = socketSession.getWriteEnd();
                log.info("isWriteEnd: {}", isWriteEnd);
                if (isWriteEnd) {
                    socketSession.destroy();
                } else {
                    int writeNum = socketSession.getWriteNum();
                    int writeTotalNum = socketSession.getWriteTotalNum();
                    int writeMaxSize = socketSession.getWriteMaxSize();

                    int count = writeMaxSize;
                    byte[] writeBytes = socketSession.getWriteBytes();
                    byte[] bytes1 = null;
                    int length = writeBytes.length;

                    writeNum++;
                    socketSession.setWriteNum(writeNum);
                    if (writeNum == writeTotalNum) {
                        socketSession.setWriteEnd(true);
                        count = length - writeNum * writeMaxSize;

                        bytes1 = Arrays.copyOfRange(writeBytes, writeNum * writeMaxSize, writeNum * writeMaxSize + count);

                        ByteBuffer writeBuffer = socketSession.getWriteBuffer();
                        SocketSession.clean(writeBuffer);

                        writeBuffer = ByteBuffer.allocateDirect(count);
                        log.info("xx count,will end:{}", count);

                        writeBuffer.put(bytes1);
                        writeBuffer.flip();

                        AsynchronousSocketChannel socketChannel = socketSession.getSocketChannel();
                        socketChannel.write(writeBuffer, socketSession, this);
                    } else {
                        bytes1 = Arrays.copyOfRange(writeBytes, writeNum * writeMaxSize, writeNum * writeMaxSize + writeMaxSize);
                        ByteBuffer writeBuffer = socketSession.getWriteBuffer();
                        writeBuffer.clear();
                        writeBuffer.put(bytes1);
                        writeBuffer.flip();
                        log.info("xx count:{}", count);
                        AsynchronousSocketChannel socketChannel = socketSession.getSocketChannel();
                        socketChannel.write(writeBuffer, socketSession, this);
                    }


                }
//            if (!isKeepAlive) {
//                socketSession.destroy();
//            } else if (br != null) {
//                byte[] line = br.nextPart();
//                ByteBuffer byteBuffer1 = ByteBuffer.wrap(line);
//                log.info("downloading,closing...,read end:{}", br.isReadEnd());
//                byteBuffer1.flip();
//                socketSession.write(byteBuffer1, new HttpWriteHandler(isKeepAlive));
//            }
            }
        } else {
            socketSession.destroy();
        }
    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        socketSession.destroy();
    }
}
