package com.zrzhen.zetty.im.server;

import com.zrzhen.zetty.net.Builder;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.SocketSessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author chenanlian
 * <p>
 * Socket会话类，管理从TCP连接建立到关闭的整个生命周期
 */
public class ImSocketSession extends SocketSession {

    private static final Logger log = LoggerFactory.getLogger(ImSocketSession.class);

    public ImSocketSession(AsynchronousSocketChannel socketChannel, Builder builder) {
        super(socketChannel, builder);
    }

    private String sentTo;

    /**
     * 销毁
     */
    @Override
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

        Manager.logout(this);
        readBuffer = null;
        writeBuffer = null;
        socketChannel = null;
        socketReadHandler = null;
        builder = null;
        socketSessionStatus = SocketSessionStatus.DESTROYED;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }
}
