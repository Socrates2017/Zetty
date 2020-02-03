package com.zrzhen.zetty.net.bio;

import com.zrzhen.zetty.net.Builder;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ReadThread implements Runnable {

    public Builder builder;

    Socket socket;


    public ReadThread(Builder builder, Socket socket) {
        this.builder = builder;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            while (true) {
                InputStream inputStream = socket.getInputStream();
                int maxLen = 1024;
                byte[] contextBytes = new byte[maxLen];
                //read方法处会被阻塞，直到操作系统有数据准备好
                int realLen = inputStream.read(contextBytes, 0, maxLen);

                String message = new String(contextBytes, 0, realLen);
                System.out.println("Get:" + message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
