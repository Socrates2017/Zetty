package com.zrzhen.zetty.http.http;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.http.util.ByteUtil;
import com.zrzhen.zetty.http.util.ProUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chenanlian
 */
public class Response {

    private static Logger log = LoggerFactory.getLogger(Response.class);

    private static ThreadLocal<Response> RESPONSE_THREADLOCAL = new ThreadLocal<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);

    private HttpVersion version;
    private HttpResponseStatus status;
    private Map<String, String> headers;
    private Map<String, Cookie> cookies;
    private boolean isKeepAlive;

    private byte[] content;

    private boolean download;
    private String downloadPath;


    private String contentType;
    private boolean contentTmp;
    private String contentTmpPath;
    private int contentLength;
    private int contentIndex;
    /**
     * 是否可以往下执行，默认为true，即没有被拦截
     */
    private boolean flag = true;


    public Response(HttpVersion version) {
        this.version = version;
        status = HttpResponseStatus.OK;
        headers = new HashMap<>();
        cookies = new HashMap<>();
    }

    public Response() {
        status = HttpResponseStatus.OK;
        version = HttpVersion.HTTP_1_1;
        headers = new HashMap<>();
        cookies = new HashMap<>();

    }

    public static Response get() {
        Response response = RESPONSE_THREADLOCAL.get();
        if (response == null) {
            response = new Response();
            set(response);
        }
        return response;

    }

    public static void set(Response Response) {
        Response response = RESPONSE_THREADLOCAL.get();
        if (response != null) {
            response = null;
        }
        RESPONSE_THREADLOCAL.set(Response);
    }

    public static void remove() {
        RESPONSE_THREADLOCAL.remove();
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
        if (contentLength > ProUtil.getInteger("server.request.contentTmp.size", 1024 * 1024 * 1024)) {
            contentTmp = true;
            contentTmpPath = "c" + System.currentTimeMillis();
        } else {
            content = new byte[contentLength];
        }

    }

    /**
     * 读取请求体中内容
     *
     * @param buffer
     * @return
     */
    public boolean readContentBuffer(ByteBuffer buffer) {
        int remain = buffer.remaining();
        if (contentTmp && remain > 0) {
            FileUtil.byte2File(ByteUtil.buf2Bytes(buffer), contentTmpPath, true);
            contentIndex = contentIndex + remain;
        } else {
            for (; this.contentIndex < this.contentLength && remain > 0; this.contentIndex++, remain--) {
                content[this.contentIndex] = buffer.get();
            }
        }
        boolean finished = this.contentIndex == this.contentLength;
        return finished;
    }

    public ByteBuffer toByteBuffer() {
        String responseLine = version.toString() + (char) HttpConstants.SP + status.toString();
        String headerStr = headers2Str();
        String cookiesStr = cookies2Str();
        String headers = headerStr + cookiesStr;

        if (content == null) {
            content = new byte[0];
        }
        int lenght = responseLine.length() + HttpConstants.CRLF.length + headers.length() + HttpConstants.CRLF.length + content.length;

        ByteBuffer buf = ByteBuffer.allocateDirect(lenght);
        buf.put(FileUtil.str2Byte(responseLine));
        buf.put(HttpConstants.CRLF);
        buf.put(FileUtil.str2Byte(headers));
        buf.put(HttpConstants.CRLF);
        buf.put(content);
        return buf;

    }

    public ByteBuffer toByteBufferForDownload(String path) {
        String responseLine = version.toString() + (char) HttpConstants.SP + status.toString();
        String headerStr = headers2Str();
        String cookiesStr = cookies2Str();
        String headers = headerStr + cookiesStr;

        byte[] bytes = new byte[0];

        //bytes = Files.readAllBytes(new File(path).toPath());
        bytes = FileUtil.file2Byte(path);


        if (bytes == null) {
            bytes = new byte[0];
        }
        int lenght = responseLine.length() + HttpConstants.CRLF.length + headers.length() + HttpConstants.CRLF.length + bytes.length;

        ByteBuffer buf = ByteBuffer.allocateDirect(lenght);
        buf.put(FileUtil.str2Byte(responseLine));
        buf.put(HttpConstants.CRLF);
        buf.put(FileUtil.str2Byte(headers));
        buf.put(HttpConstants.CRLF);
        buf.put(bytes);
        return buf;

    }

    public ByteBuffer header2ByteBuffer() {
        String responseLine = version.toString() + (char) HttpConstants.SP + status.toString();
        String headerStr = headers2Str();
        String cookiesStr = cookies2Str();
        String headers = headerStr + cookiesStr;

        if (content == null) {
            content = new byte[0];
        }
        int lenght = responseLine.length() + HttpConstants.CRLF.length + headers.length() + HttpConstants.CRLF.length;

        ByteBuffer buf = ByteBuffer.allocateDirect(lenght);
        buf.put(FileUtil.str2Byte(responseLine));
        buf.put(HttpConstants.CRLF);
        buf.put(FileUtil.str2Byte(headers));
        buf.put(HttpConstants.CRLF);
        return buf;
    }

    private String headers2Str() {
        String out = "";
        Set<Map.Entry<String, String>> entrys = headers.entrySet();
        for (Iterator<Map.Entry<String, String>> i = entrys.iterator(); i.hasNext(); ) {
            Map.Entry<String, String> entry = i.next();
            String headerContent = entry.getKey() + HttpConstants.COLON_SP_STRING + entry.getValue();
            out += headerContent;
            out += HttpConstants.CRLF_STRING;
        }
        return out;
    }


    /**
     * 把全部cookie写到响应通道<br />
     * <br />
     *
     * @throws IOException
     */
    private String cookies2Str() {
        StringBuilder out = new StringBuilder("");
        Set<Map.Entry<String, Cookie>> entrys = cookies.entrySet();
        for (Iterator<Map.Entry<String, Cookie>> i = entrys.iterator(); i.hasNext(); ) {
            Map.Entry<String, Cookie> entry = i.next();
            Cookie cookie = entry.getValue();
            String name = cookie.getName();
            String value = cookie.getValue();
            if (StringUtils.isBlank(name)
                    || StringUtils.isBlank(value)) {
                continue;
            }
            // 构造cookie响应头
            StringBuilder s = new StringBuilder("Set-Cookie: ");
            // cookie名字和值
            s.append(name);
            s.append("=");
            s.append(value);
            s.append("; ");
            // 设置过期时间
            long age = cookie.getAge();
            if (age > -1) {
                long expiresTimeStamp = System.currentTimeMillis() + age;
                s.append("Expires=");
                s.append(sdf.format(new Date(expiresTimeStamp)));
                s.append("; ");
            }
            // 设置path
            String path = cookie.getPath();
            if (!StringUtils.isBlank(path)) {
                s.append("Path=");
                s.append(path);
                s.append("; ");
            }
            // 设置domain
            String domain = cookie.getDomain();
            if (!StringUtils.isBlank(domain)) {
                s.append("Domain=");
                s.append(domain);
                s.append("; ");
            }
            // http only
//            s.append("HttpOnly");
//            out += s.toString();
            s.append(HttpConstants.CRLF_STRING);
            out.append(s);
        }
        return out.toString();
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, Cookie> cookies) {
        this.cookies = cookies;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isKeepAlive() {
        return isKeepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        isKeepAlive = keepAlive;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
}
