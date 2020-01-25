package com.zrzhen.zetty.net.bio;


/**
 * @author chenanlian
 *
 * socket类型
 */

public enum SocketEnum {

    BIO(0),
    NIO(1),
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
