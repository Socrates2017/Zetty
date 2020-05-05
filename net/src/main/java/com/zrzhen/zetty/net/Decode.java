package com.zrzhen.zetty.net;


/**
 * @author chenanlian
 * 解码
 */
public interface Decode<T> {
    /**
     * 对socket接收到的字节码进行解码。运行完毕后会进行判断：本次完整的消息是否读取完毕.\n是则返回true；否则返回否，socket将继续读取。
     *
     * @param session    socket会话
     * @param readLength 读取的长度
     * @param t          解码完成后消息结果，将会传给处理类进行处理
     * @return true:解码已完成；false：解码未完成，socket将继续读取，跟上次的消息进行合并
     */
    boolean decode(SocketSession session, Integer readLength, T t);
}
