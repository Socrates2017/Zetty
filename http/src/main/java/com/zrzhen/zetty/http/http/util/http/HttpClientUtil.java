package com.zrzhen.zetty.http.http.util.http;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.http.http.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 */
public class HttpClientUtil {

    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);


    public static void doRequest(Request request, AsyHttpClientCallback callback) throws IOException {
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        String host = request.getHost();

        int port = request.getPort() == 0 ? 80 : request.getPort();
        client.connect(new InetSocketAddress(host, port), null, new ConnectHandler(client, request, callback));
    }

    public static void get(String url, AsyHttpClientCallback callback) throws IOException {

        String host = "";
        String uri = "/";
        if (url.startsWith("http://")) {
            String tmp = url.substring(7);
            host = tmp;
            int index = tmp.indexOf("/");
            if (index > 0) {
                host = tmp.substring(0, index);
                if (host.startsWith("www.")) {
                    host = host.substring(4);
                }
                uri = tmp.substring(index);
                log.info(uri);
            }

            Request request = new Request();
            request.setMethod(HttpMethod.GET);
            request.setVersion(HttpVersion.HTTP_1_1);
            request.setUri(uri);

            int maoHao = host.lastIndexOf(":");
            if (maoHao > 0) {
                int port = Integer.valueOf(host.substring(maoHao + 1));
                host = host.substring(0, maoHao);
                request.setPort(port);
            }
            request.setHost(host);

            Map map = new HashMap<>();
            map.put(HttpHeaders.Names.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
            request.setHeaders(map);
            doRequest(request, callback);

        } else if (url.startsWith("https://")) {

        }


    }


    public static void main(String[] args) throws IOException {
        get("http://www.zrzhen.com", new AsyHttpClientCallback() {
            @Override
            public void run() {
                Response response = this.getResponse();
                String body = FileUtil.byte2Str(response.getContent());
                System.out.println(body);
                System.out.println(response.getHeaders());
                System.out.println(response.getVersion());
                System.out.println(response.getStatus());
            }
        });

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
