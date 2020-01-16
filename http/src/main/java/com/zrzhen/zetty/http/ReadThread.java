package com.zrzhen.zetty.http;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public class ReadThread implements Runnable {

    ReadHandler readHandler;

    ByteBuffer attachment;

    public ReadThread(ReadHandler readHandler, ByteBuffer attachment) {
        this.readHandler = readHandler;
        this.attachment = attachment;
    }

    @Override
    public void run() {
        //readHandler.execute(attachment);
    }

}
