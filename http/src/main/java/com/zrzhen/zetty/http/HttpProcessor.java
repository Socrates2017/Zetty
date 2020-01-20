package com.zrzhen.zetty.http;


import com.zrzhen.zetty.http.http.Request;
import com.zrzhen.zetty.net.Processor;
import com.zrzhen.zetty.net.SocketSession;

/**
 * @author chenanlian
 */
public class HttpProcessor implements Processor<Request>{


    @Override
    public boolean process(SocketSession socketSession, Request message) {
        MvcUtil.handle(socketSession, message);
        return false;
    }
}
