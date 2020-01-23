package com.zrzhen.zetty.net;


import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public interface Encode<O> {
    ByteBuffer encode(SocketSession session, O out);
}
