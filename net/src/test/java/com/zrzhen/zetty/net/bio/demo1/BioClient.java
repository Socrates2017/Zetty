package com.zrzhen.zetty.net.bio.demo1;

import com.zrzhen.zetty.net.Util;

import java.io.*;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 80);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
