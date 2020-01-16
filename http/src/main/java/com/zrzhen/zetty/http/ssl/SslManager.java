package com.zrzhen.zetty.http.ssl;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

public class SslManager {

    private static Logger logger = Logger.getLogger(SslManager.class.getName());


    private static volatile SSLContext sslc;
    private static String keyStoreFile = "D:\\project\\private\\zetty\\zetty\\src\\test\\java\\com\\zrzhen\\zetty\\ssl\\keystore.jks";


    public static SSLContext getSSLContext() throws KeyStoreException, IOException, CertificateException,
            NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        if (sslc == null) {
            synchronized (SslManager.class) {
                if (sslc == null) {
                    char[] passphrase = "password".toCharArray();
                    KeyStore keyStore = KeyStore.getInstance("JKS");
                    keyStore.load(new FileInputStream(keyStoreFile), passphrase);
                    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                    kmf.init(keyStore, passphrase);

                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(kmf.getKeyManagers(), null, null);
                    sslc = sslContext;
                }
            }
        }
        return sslc;
    }

    public static SSLEngine createServerSSLEngine() throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        SSLEngine sslEngine = getSSLContext().createSSLEngine();
        sslEngine.setUseClientMode(false);
        return sslEngine;
    }


    public static void doHandshake(AsynchronousSocketChannel socketChannel, SSLEngine engine,
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

                    // Process incoming handshaking data
                    peerNetData.flip();
                    SSLEngineResult res = engine.unwrap(peerNetData, peerAppData);
                    peerNetData.compact();
                    hs = res.getHandshakeStatus();

                    // Check status
                    switch (res.getStatus()) {
                        case OK:
                            // Handle OK status
                            break;
                        // Handle other status: BUFFER_UNDERFLOW, BUFFER_OVERFLOW, CLOSED
                    }
                    break;

                case NEED_WRAP:
                    // Empty the local network packet buffer.
                    myNetData.clear();
                    res = engine.wrap(myAppData, myNetData);
//                    MySSlEngine.print(myNetData);
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

    }

}