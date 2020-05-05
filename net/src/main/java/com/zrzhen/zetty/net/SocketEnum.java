package com.zrzhen.zetty.net;


/**
 * @author chenanlian
 *
 * socket类型
 */

public enum SocketEnum {

    /**
     * BIO，同步阻塞
     */
    BIO(0),

    /**
     * 同步非阻塞，多路复用
     */
    NIO(1),

    /**
     * 异步
     */
    AIO(2),

    ;

    private int type;

    SocketEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
