package com.zrzhen.zetty.net.aio;

import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.net.SocketSessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 * 客户端建立连接后回调
 */
public class ConnectCompletionHandler implements CompletionHandler<Void, SocketSession> {

    private static final Logger log = LoggerFactory.getLogger(ConnectCompletionHandler.class);

    private CountDownLatch latch;

    public ConnectCompletionHandler(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void completed(Void result, SocketSession socketSession) {
        latch.countDown();
        try {
            SocketAddress socketAddress = socketSession.getSocketChannel().getLocalAddress();
            if (socketAddress != null) {
                socketSession.setLocalAddress(socketAddress.toString());
            }
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }

        socketSession.setSocketSessionStatus(SocketSessionStatus.CONNECTED);
        socketSession.read();

    }

    @Override
    public void failed(Throwable exc, SocketSession socketSession) {
        log.error(exc.getMessage(), exc);
        socketSession.destroy();
    }
}
