package com.zrzhen.zetty.p2p.server;

import com.zrzhen.zetty.net.AcceptCompletionHandler;
import com.zrzhen.zetty.net.Builder;
import com.zrzhen.zetty.net.SocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author chenanlian
 * <p>
 * 服务端建立TCP连接后回调
 */
public class AcceptHandler extends AcceptCompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final Logger log = LoggerFactory.getLogger(AcceptHandler.class);

    public AcceptHandler(Builder builder) {
        super(builder);
    }

    @Override
    public void completed(AsynchronousSocketChannel ch, AsynchronousServerSocketChannel listener) {
        //accept the next connection
        listener.accept(listener, this);
        SocketSession socketSession = new ImSocketSession(ch, builder);
        Manager.sessions.put(socketSession.getRemoteAddress(),socketSession);
        socketSession.read();

    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {
        //accept the next connection
        log.error(exc.getMessage(), exc);
        listener.accept(listener, this);
    }
}
