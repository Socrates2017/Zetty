package com.zrzhen.zetty.http.http.util.http;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.security.Security;

/**
 * @author chenanlian
 */
public class SslUtil {

    public static SSLEngine prepareEngine(String host, int port) throws Exception {
        char[] passphrase = "changeit".toCharArray();

        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        KeyStore ks = ks = KeyStore.getInstance("JKS");

        String JAVA_HOME = System.getenv("JAVA_HOME");
        ks.load(new FileInputStream(JAVA_HOME + "/jre/lib/security/cacerts"), passphrase);

        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);
        SSLEngine sslEngine = ctx.createSSLEngine(host, port);
        sslEngine.setUseClientMode(true);

        return sslEngine;
    }

    public static SocketChannel prepareChannel(String host, int port) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));

        return socketChannel;
    }

    public static void doHandshake(SocketChannel socketChannel, SSLEngine engine,
                                   ByteBuffer myNetData, ByteBuffer peerNetData) throws Exception {

        // Create byte buffers to use for holding application data
        int appBufferSize = engine.getSession().getApplicationBufferSize();
        ByteBuffer myAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);

        // Begin handshake
        engine.beginHandshake();
        SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();

        // Process handshaking message
        while (hs != SSLEngineResult.HandshakeStatus.FINISHED &&
                hs != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {

            switch (hs) {

                case NEED_UNWRAP:
                    // Receive handshaking data from peer
                    if (socketChannel.read(peerNetData) < 0) {
                        // The channel has reached end-of-stream
                        System.out.println("The channel has reached end-of-stream");
                    }

                    // Process incoming handshaking data
                    peerNetData.flip();
                    SSLEngineResult res = engine.unwrap(peerNetData, peerAppData);
                    peerNetData.compact();
                    hs = res.getHandshakeStatus();

                    // Check status
                    switch (res.getStatus()) {
                        case OK:
                            // Handle OK status
                            System.out.println("Handle OK status");
                            break;

                        // Handle other status: BUFFER_UNDERFLOW, BUFFER_OVERFLOW, CLOSED
                    }
                    break;

                case NEED_WRAP:
                    // Empty the local network packet buffer.
                    myNetData.clear();
                    // Generate handshaking data
                    res = engine.wrap(myAppData, myNetData);
                    hs = res.getHandshakeStatus();
                    // Check status
                    switch (res.getStatus()) {
                        case OK:
                            myNetData.flip();
                            // Send the handshaking data to peer
                            while (myNetData.hasRemaining()) {
                                socketChannel.write(myNetData);
                            }
                            break;
                        // Handle other status:  BUFFER_OVERFLOW, BUFFER_UNDERFLOW, CLOSED
                    }
                    break;

                case NEED_TASK:
                    // Handle blocking tasks
                    Runnable task;
                    while ((task = engine.getDelegatedTask()) != null) {
                        new Thread(task).start();
                    }
                    hs = engine.getHandshakeStatus();
                    break;

                // Handle other status:  // FINISHED or NOT_HANDSHAKING
            }
        }

        // Processes after handshaking
        System.out.println("after handshaking");
    }

    private static void runDelegatedTasks(SSLEngineResult result,
                                          SSLEngine engine) throws Exception {

        if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
            Runnable runnable;
            while ((runnable = engine.getDelegatedTask()) != null) {
                System.out.println("\trunning delegated task...");
                runnable.run();
            }
            SSLEngineResult.HandshakeStatus hsStatus = engine.getHandshakeStatus();
            if (hsStatus == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                throw new Exception("handshake shouldn't need additional tasks");
            }
            System.out.println("\tnew HandshakeStatus: " + hsStatus);
        }
    }

    public static void sendRequest(String host, int port, String path,
                                   SSLEngine sslEngine, ByteBuffer myAppData, ByteBuffer myNetData, SocketChannel socketChannel)
            throws Exception {
        StringBuilder header = new StringBuilder();
        header.append("GET " + path + " HTTP/1.1\r\n");
        header.append("Host: " + host + "\r\n");
        header.append("Connection: keep-alive\r\n");
        header.append("\r\n");

        myAppData.put(header.toString().getBytes());
        myAppData.flip();
        System.out.println("Send...");
        print(myAppData);
        myNetData.clear();
        SSLEngineResult res = sslEngine.wrap(myAppData, myNetData);
        if (res.getStatus() == SSLEngineResult.Status.OK) {
            myNetData.flip();
            socketChannel.write(myNetData);
        }
    }

    public static void main(String[] args) throws Exception {

        String host = "blog.csdn.net";
        int port = 443;
        String path = "/";

        SSLEngine sslEngine = prepareEngine(host, port);
        SocketChannel socketChannel = prepareChannel(host, port);

        while (!socketChannel.finishConnect()) {
        }
        System.out.println("connected..............");

        //prepare the four ByteBuffer
        SSLSession session = sslEngine.getSession();
        ByteBuffer myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        ByteBuffer peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        doHandshake(socketChannel, sslEngine, myNetData, peerNetData);

        sendRequest(host, port, path, sslEngine, myAppData, myNetData, socketChannel);

        int num = 0;
        int readCount=0;
        while ((num = socketChannel.read(peerNetData)) != -1) {

            if (num > 0) {

                peerNetData.flip();
                SSLEngineResult res = sslEngine.unwrap(peerNetData, peerAppData);

                if (res.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                    peerNetData.compact();
                    continue;
                }
                if (res.getStatus() == SSLEngineResult.Status.OK) {
                    peerNetData.compact();

                    // Use peerAppData
                    int len = peerAppData.position();
                    int count = 0;
                    while (len > 0) {
                        System.out.print((char) peerAppData.get(count));
                        count++;
                        len--;
                    }
                    peerAppData.clear();


                    System.out.println();
                    System.out.println("readCount:"+readCount++ +" num:"+num+".......");
                    System.out.println(peerNetData+"......................"+session.getPacketBufferSize());
                }
            }
        }
    }


    public static void print(ByteBuffer peerAppData){
        // Use peerAppData
        int len = peerAppData.position();
        int count = 0;
        System.out.println("read begin...");
        while (len > 0) {
            System.out.print((char) peerAppData.get(count));
            count++;
            len--;
        }
    }
}
