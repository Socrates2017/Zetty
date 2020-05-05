package com.zrzhen.zetty.net;


import java.nio.ByteBuffer;

/**
 * @author chenanlian
 * <p>
 * 编码
 */
public interface Encode<O> {

    /**
     * 在数据写出之前，进行编码，例如可以添加头信息、添加消息长度等。并转成ByteBuffer，以方便socket写出
     *
     * @param session socket会话
     * @param out     消息内容
     * @return 要写出的缓冲
     */
    ByteBuffer encode(SocketSession session, O out);
}
