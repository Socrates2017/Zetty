package com.zrzhen.zetty.core.http;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class Request {

    private static Logger log = LoggerFactory.getLogger(Request.class);

    private static ThreadLocal<Request> REQUEST_THREADLOCAL = new ThreadLocal<>();

    private HttpMethod method;
    private HttpVersion version;
    private String uri;
    private String host;
    private int port;
    private Map<String, String> parameters;
    private JsonNode jsonBody;
    private Multipart multipart;
    private Map<String, String> headers;
    private List<Cookie> cookies;
    private String contentType;
    private boolean isKeepAlive;
    private byte[] content;
    private int contentLength;
    private int contentIndex;

    public Request() {
        parameters = new HashMap<>();
        headers = new HashMap<>();
        cookies = new ArrayList<>();
    }

    public static Request get() {
        return REQUEST_THREADLOCAL.get();
    }

    public static void set(Request request) {
        REQUEST_THREADLOCAL.set(request);
    }

    public static void remove() {
        Request request = get();
        if (request != null) {
            request = null;
        }
        REQUEST_THREADLOCAL.remove();
    }


    /**
     * 初始化请求体
     *
     * @param contentLength
     * @param contentType
     * @throws IOException
     */
    public void createContentBuffer(int contentLength, String contentType) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentIndex = 0;
        content = new byte[contentLength];
    }


    /**
     * 读取请求体中内容
     *
     * @param buffer
     * @return
     */
    public boolean readContentBuffer(ByteBuffer buffer) {
        int remain = buffer.remaining();
        for (; this.contentIndex < this.contentLength && remain > 0; this.contentIndex++, remain--) {
            content[this.contentIndex] = buffer.get();
        }
        boolean finished = this.contentIndex == this.contentLength;
        return finished;
    }


    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public JsonNode getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(JsonNode jsonBody) {
        this.jsonBody = jsonBody;
    }

    public Multipart getMultipart() {
        return multipart;
    }

    public void setMultipart(Multipart multipart) {
        this.multipart = multipart;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isKeepAlive() {
        return isKeepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        isKeepAlive = keepAlive;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
