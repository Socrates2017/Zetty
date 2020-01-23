package com.zrzhen.zetty.im;

public class ImMessage {
    private byte[] msg;

    private int msgIndex = 0;

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public int getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(int msgIndex) {
        this.msgIndex = msgIndex;
    }
}
