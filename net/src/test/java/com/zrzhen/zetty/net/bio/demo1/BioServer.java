package com.zrzhen.zetty.net.bio.demo1;

import com.zrzhen.zetty.net.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    private static final Integer port = 80;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器端口:" + serverSocket.getLocalPort());
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println("接收到一个请求，请求地址:" + socket.getInetAddress() + ":" + socket.getPort());
                service(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void service(final Socket socket) {
        new Thread() {
            public void run() {
                try {

                    new Thread(new BioReadThread(socket)).start();

                    OutputStream outSocket = socket.getOutputStream();
                    BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
                    String readline = sin.readLine();
                    while (!readline.equals("bye")) {
                        outSocket.write(Util.str2Byte(readline));
                        outSocket.flush();
                        System.out.println("Send:" + readline);
                        readline = sin.readLine();
                    }
                    outSocket.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}

