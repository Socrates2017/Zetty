package com.zrzhen.zetty.im;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.WriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author chenanlian
 */
public class ImWriteHandler extends WriteHandler<Integer, SocketSession> {

    private static Logger log = LoggerFactory.getLogger(ImWriteHandler.class);



    @Override
    public void completed(Integer result, SocketSession socketSession) {
        AsynchronousSocketChannel channel = socketSession.getSocketChannel();
        ByteBuffer buffer = socketSession.getWriteBuffer();
        //如果没有发送完，就继续发送直到完成
        if (buffer.hasRemaining()) {
            log.warn("buffer.hasRemaining()");
            channel.write(buffer, socketSession, this);

        }
    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        ByteBuffer writeBuffer = FileUtil.str2Buf("消息发送失败");

        //sender.write(writeBuffer);
        socketSession.destroy();
    }
}
