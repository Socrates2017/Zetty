package com.zrzhen.zetty.http.http.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.http.Constant;
import com.zrzhen.zetty.http.http.util.ByteUtil;
import com.zrzhen.zetty.http.http.util.FileUtil;
import com.zrzhen.zetty.http.http.util.ProUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 * <p>
 * 请求实体
 */
public class Request implements RequestIn, RequestOut {

    private static Logger log = LoggerFactory.getLogger(Request.class);

    /**
     * 方便传参
     */
    private static ThreadLocal<Request> REQUEST_THREADLOCAL = new ThreadLocal<>();

    /**
     * 请求体方法
     */
    private HttpMethod method;

    /**
     * 协议版本
     */
    private HttpVersion version;

    /**
     * uri
     */
    private String uri;

    /**
     * 请求地址，远程地址。
     */
    private String host;

    /**
     * 请求端口
     */
    private int port;

    /**
     * 请求参数
     */
    private Map<String, String> parameters;

    /**
     * json格式的请求体数据
     */
    private JsonNode jsonBody;

    /**
     * 文件上传数据实体
     */
    private Multipart multipart;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * Cookie
     */
    private List<Cookie> cookies;

    /**
     * 请求体数据类型
     */
    private String contentType;

    /**
     * 是否长连接
     */
    private boolean isKeepAlive;

    /**
     * 请求体
     */
    private byte[] content;

    /**
     * 是否使用硬盘缓存
     */
    private boolean contentTmp;

    /**
     * 硬盘缓存地址
     */
    private String contentTmpPath;

    /**
     * 请求体长度
     */
    private int contentLength;

    /**
     * 请求体已经读到的位置
     */
    private int contentIndex;

    /**
     * 构造函数
     */
    public Request() {
        parameters = new HashMap<>();
        headers = new HashMap<>();
        cookies = new ArrayList<>();
    }

    /**
     * 在threadlocal中获取自己
     *
     * @return 请求实体
     */
    public static Request get() {
        return REQUEST_THREADLOCAL.get();
    }

    /**
     * 放进threadlocal中
     *
     * @param request 要放进去的请求实体
     */
    public static void set(Request request) {
        REQUEST_THREADLOCAL.set(request);
    }

    /**
     * 从threadlocal中删除请求实体，在每一次请求结束时，必须执行此操作，以免内存泄露
     */
    public static void remove() {
        Request request = get();
        if (request != null) {
            request = null;
        }
        REQUEST_THREADLOCAL.remove();
    }


    @Override
    public ByteBuffer toByteBuffer() {
        log.info("Tcp is connected!");
        String request = this.getMethod().name() + " " + this.getUri() + " " + this.getVersion().toString() + "\r\n";
        Map<String, String> headers = this.getHeaders();
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request += entry.getKey() + ": " + entry.getValue() + "\r\n";
            }
        }
        request += "\r\n";
        int length = request.length();
        ByteBuffer buffer;
        if (this.getContent() != null) {
            length += this.getContent().length;
            buffer = ByteBuffer.allocate(length);
            buffer.put(FileUtil.str2Byte(request));
            buffer.put(this.getContent());
        } else {
            buffer = ByteBuffer.allocate(length);
            buffer.put(FileUtil.str2Byte(request));
        }
        return buffer;
    }


    @Override
    public void createContentBuffer(int contentLength, String contentType) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentIndex = 0;
        if (contentLength > ProUtil.getInteger("server.request.contentTmp.size", 1024)) {
            contentTmp = true;
            contentTmpPath = "r" + Constant.num.incrementAndGet() + System.currentTimeMillis();
        } else {
            content = new byte[contentLength];
        }
    }

    @Override
    public boolean readContentBuffer(ByteBuffer buffer) {
        int remain = buffer.remaining();
        if (contentTmp && remain > 0) {
            FileUtil.byte2File(ByteUtil.buf2Bytes(buffer), Constant.UPLOAD_FILEPATH_TMP + contentTmpPath, true);
            contentIndex = contentIndex + remain;
        } else {
            for (; this.contentIndex < this.contentLength && remain > 0; this.contentIndex++, remain--) {
                content[this.contentIndex] = buffer.get();
            }
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

    public boolean isContentTmp() {
        return contentTmp;
    }

    public void setContentTmp(boolean contentTmp) {
        this.contentTmp = contentTmp;
    }

    public String getContentTmpPath() {
        return contentTmpPath;
    }

    public void setContentTmpPath(String contentTmpPath) {
        this.contentTmpPath = contentTmpPath;
    }
}
