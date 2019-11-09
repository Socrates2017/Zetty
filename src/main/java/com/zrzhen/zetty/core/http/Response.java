package com.zrzhen.zetty.core.http;

import com.zrzhen.zetty.core.util.FileUtil;
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

    private byte[] body;

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


    public ByteBuffer toByteBuffer() {
        String responseLine = version.toString() + (char) HttpConstants.SP + status.toString();
        String headerStr = headers2Str();
        String cookiesStr = cookies2Str();
        String headers = headerStr + cookiesStr;

        if (body == null) {
            body = new byte[0];
        }
        int lenght = responseLine.length() + HttpConstants.CRLF.length + headers.length() + HttpConstants.CRLF.length + body.length;

        ByteBuffer buf = ByteBuffer.allocate(lenght);
        buf.put(FileUtil.str2Byte(responseLine));
        buf.put(HttpConstants.CRLF);
        buf.put(FileUtil.str2Byte(headers));
        buf.put(HttpConstants.CRLF);
        buf.put(body);
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
                log.warn("Cookie name or value is null");
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

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
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
}
