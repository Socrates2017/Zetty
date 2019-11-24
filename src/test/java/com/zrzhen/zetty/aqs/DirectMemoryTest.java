package com.zrzhen.zetty.aqs;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

public class DirectMemoryTest {

    public static void main(String[] args) {

        ByteBuffer buffer = null;
        try {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                buffer = ByteBuffer.allocateDirect(1024 * 1024 * 250);
                clean(buffer);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

             //System.out.println(null+"");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void clean(final ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            ((DirectBuffer) byteBuffer).cleaner().clean();
        }
    }

}

