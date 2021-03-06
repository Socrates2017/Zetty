package com.zrzhen.zetty.http;

import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.http.http.*;
import com.zrzhen.zetty.net.Decode;
import com.zrzhen.zetty.net.SocketSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 * <p>
 * Http请求消息解析，二进制转Request实体类
 */
public class HttpDecode implements Decode<Request> {

    private static Logger log = LoggerFactory.getLogger(HttpDecode.class);

    /**
     * 重写解析方法
     *
     * @param socketSession socket会话
     * @param readLength    读取的长度
     * @param request       解析完毕后传给处理类的消息类
     * @return 如果已解析完毕，返回true；未解析完毕，返回true，将继续读取socket中数据进行解析。
     */
    @Override
    public boolean decode(SocketSession socketSession, Integer readLength, Request request) {
        if (request == null) {
            request = new Request();
            socketSession.setMessage(request);
        }

        if (request.socketStatus == null) {
            request.socketStatus = HttpSocketStatus.SKIP_CONTROL_CHARS;
        }

        ByteBuffer attachment = socketSession.getReadBuffer();
        attachment.flip();
        try {
            if (request.socketStatus.equals(HttpSocketStatus.SKIP_CONTROL_CHARS)) {
                skipControlCharacters(attachment);
                request.socketStatus = HttpSocketStatus.READ_INITIAL;
            }
            if (request.socketStatus.equals(HttpSocketStatus.READ_INITIAL)) {
                String line = readLine(attachment, Constant.maxInitialLineLength);
                if (line == null) {
                    throw new HttpException(HttpResponseStatus.BAD_REQUEST, "Request line is empty!");
                }
                parseLine(request, line);
                request.socketStatus = HttpSocketStatus.READ_HEADER;
            }
            if (request.socketStatus.equals(HttpSocketStatus.READ_HEADER)) {
                if (!readHeaders(attachment, request)) {
                    throw new HttpException(HttpResponseStatus.BAD_REQUEST, "Request header take wrong!");
                }
                long contentLength = getContentLength(request, -1);
                if (contentLength > 0) {
                    if (contentLength > Constant.maxContentSize) {
                        throw new HttpException(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, "Request Entity Too Large : " + contentLength);
                    }
                    try {
                        request.createContentBuffer((int) contentLength, request.getHeaders().get(HttpHeaders.Names.CONTENT_TYPE));
                    } catch (Exception e) {
                        log.info(e.getMessage(), e);
                        throw new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    request.socketStatus = HttpSocketStatus.READ_VARIABLE_LENGTH_CONTENT;
                } else {
                    request.socketStatus = HttpSocketStatus.READ_REQUEST_FINISH;
                }
            }
            if (request.socketStatus.equals(HttpSocketStatus.READ_VARIABLE_LENGTH_CONTENT)) {
                try {
                    if (request.readContentBuffer(attachment)) {
                        parseBody(request);
                        request.socketStatus = HttpSocketStatus.READ_REQUEST_FINISH;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                    throw new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                }
            }

            if (request.socketStatus.equals(HttpSocketStatus.READ_REQUEST_FINISH)) {
                request.socketStatus = HttpSocketStatus.PROCESSING;
                parseCookie(request);
                parseConnection(request);
                parseHost(request, socketSession);
                /**
                 * 至此，http请求消息已经解析完毕，可以进行业务逻辑处理
                 */
                return true;
            } else {
                throw new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unknown error!");
            }
        } catch (Throwable t) {
            HttpResponseStatus status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            if (t instanceof HttpException) {
                HttpException e = (HttpException) t;
                status = e.getStatus();
            }
            log.error(t.getMessage(), t);

            MvcUtil.writeError(socketSession, status);
        }

        return false;
    }

    /**
     * 跳过无效字符
     *
     * @param buffer
     */
    private static void skipControlCharacters(ByteBuffer buffer) {
        int limit = buffer.limit();
        int position = buffer.position();
        for (int index = position; index < limit; index++) {
            char c = (char) (buffer.get(index) & 0xFF);
            if (!Character.isISOControl(c) &&
                    !Character.isWhitespace(c)) {
                buffer.position(index);
                break;
            }
        }
    }

    /**
     * 读取请求行
     *
     * @param buffer
     * @param maxLineLength
     * @return
     * @throws HttpException
     */
    private static String readLine(ByteBuffer buffer, int maxLineLength) throws HttpException {
        StringBuilder sb = new StringBuilder(64);
        int lineLength = 0;
        int limit = buffer.limit();
        int position = buffer.position();
        for (int index = position; index < limit; index++) {
            byte nextByte = buffer.get(index);
            if (nextByte == HttpConstants.CR) {
                nextByte = buffer.get(index + 1);
                if (nextByte == HttpConstants.LF) {
                    buffer.position(index + 2);
                    return sb.toString();
                }
            } else if (nextByte == HttpConstants.LF) {
                buffer.position(index + 2);
                return sb.toString();
            } else {
                if (lineLength >= maxLineLength) {
                    throw new HttpException(HttpResponseStatus.REQUEST_URI_TOO_LONG, "An HTTP line is larger than " + maxLineLength + " bytes.");
                }
                lineLength++;
                sb.append((char) nextByte);
            }
        }
        return null;
    }

    /**
     * 解析请求行
     *
     * @param request
     * @param line
     * @throws HttpException
     */
    private void parseLine(Request request, String line) throws HttpException {
        String[] initialLine = splitInitialLine(line);
        String text = initialLine[0].toUpperCase();
        HttpMethod method = HttpMethod.getHttpMethod(text);
        request.setMethod(method);
        String uri = initialLine[1];
        text = initialLine[2].toUpperCase();
        HttpVersion version = HttpVersion.getHttpVersion(text);
        request.setVersion(version);
        String[] uri1 = uri.split("\\?");

        try {
            request.setUri(URLDecoder.decode(uri1[0], "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(HttpResponseStatus.BAD_REQUEST);
        }

        if (uri1.length > 1) {
            request.getParameters().putAll(resolveRequestArgs(uri1[1]));
        }
    }

    /**
     * 解析arg1=val1&arg2=val2格式的请求参数<br />
     *
     * @param args - 请求参数字符串
     * @return
     */
    private Map<String, String> resolveRequestArgs(String args) {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(args)) {
            String[] argss = args.split("&+");
            for (int i = 0; i < argss.length; i++) {
                String arg = argss[i];
                String[] nameAndValue = arg.split("\\s*=\\s*");
                try {
                    if (nameAndValue.length == 2) {
                        map.put(URLDecoder.decode(nameAndValue[0], "utf-8"), URLDecoder.decode(nameAndValue[1], "utf-8"));
                    } else if (nameAndValue.length == 1) {
                        map.put(URLDecoder.decode(nameAndValue[0], "utf-8"), "");
                    }
                } catch (UnsupportedEncodingException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
        return map;
    }


    /**
     * 读取请求头
     *
     * @param buffer
     * @param request
     * @return
     * @throws HttpException
     */
    private boolean readHeaders(ByteBuffer buffer, Request request) throws HttpException {

        StringBuilder sb = new StringBuilder(64);
        int limit = buffer.limit();
        int position = buffer.position();
        int lineLength = 0;
        for (int index = position; index < limit; index++) {
            byte nextByte = buffer.get(index);
            if (nextByte == HttpConstants.CR) {
                nextByte = buffer.get(index + 1);
                if (nextByte == HttpConstants.LF) {
                    buffer.position(index);
                    if (lineLength == 0) {
                        buffer.position(index + 2);
                        return true;
                    } else {
                        buffer.position(index);
                    }
                    readHeader(request, sb.toString());
                    lineLength = 0;
                    sb.setLength(0);
                    index++;
                }
            } else if (nextByte == HttpConstants.LF) {
                if (lineLength == 0) {
                    buffer.position(index + 2);
                    return true;
                } else {
                    buffer.position(index);
                }
                readHeader(request, sb.toString());
                lineLength = 0;
                sb.setLength(0);
                index++;
            } else {
                if (lineLength >= Constant.maxHeaderSize) {
                    throw new HttpException(HttpResponseStatus.BAD_REQUEST, "An HTTP header is larger than " + Constant.maxHeaderSize + " bytes.");
                }
                lineLength++;
                sb.append((char) nextByte);
            }
        }
        return false;
    }

    private static void readHeader(Request request, String header) {
        String[] kv = splitHeader(header);
        request.getHeaders().put(kv[0], kv[1]);
    }

    private static String[] splitHeader(String sb) {
        final int length = sb.length();
        int nameStart;
        int nameEnd;
        int colonEnd;
        int valueStart;
        int valueEnd;

        nameStart = findNonWhitespace(sb, 0);
        for (nameEnd = nameStart; nameEnd < length; nameEnd++) {
            char ch = sb.charAt(nameEnd);
            if (ch == ':' || Character.isWhitespace(ch)) {
                break;
            }
        }

        for (colonEnd = nameEnd; colonEnd < length; colonEnd++) {
            if (sb.charAt(colonEnd) == ':') {
                colonEnd++;
                break;
            }
        }

        valueStart = findNonWhitespace(sb, colonEnd);
        if (valueStart == length) {
            return new String[]{
                    sb.substring(nameStart, nameEnd),
                    ""
            };
        }

        valueEnd = findEndOfString(sb);
        return new String[]{
                sb.substring(nameStart, nameEnd),
                sb.substring(valueStart, valueEnd)
        };
    }

    /**
     * 按空格解析请求行
     *
     * @param sb
     * @return
     */
    private static String[] splitInitialLine(String sb) {
        int aStart;
        int aEnd;
        int bStart;
        int bEnd;
        int cStart;
        int cEnd;

        aStart = findNonWhitespace(sb, 0);
        aEnd = findWhitespace(sb, aStart);

        bStart = findNonWhitespace(sb, aEnd);
        bEnd = findWhitespace(sb, bStart);

        cStart = findNonWhitespace(sb, bEnd);
        cEnd = findEndOfString(sb);

        return new String[]{
                sb.substring(aStart, aEnd),
                sb.substring(bStart, bEnd),
                cStart < cEnd ? sb.substring(cStart, cEnd) : ""};
    }

    private static int findNonWhitespace(String sb, int offset) {
        int result;
        for (result = offset; result < sb.length(); result++) {
            if (!Character.isWhitespace(sb.charAt(result))) {
                break;
            }
        }
        return result;
    }

    private static int findWhitespace(String sb, int offset) {
        int result;
        for (result = offset; result < sb.length(); result++) {
            if (Character.isWhitespace(sb.charAt(result))) {
                break;
            }
        }
        return result;
    }

    private static int findEndOfString(String sb) {
        int result;
        for (result = sb.length(); result > 0; result--) {
            if (!Character.isWhitespace(sb.charAt(result - 1))) {
                break;
            }
        }
        return result;
    }


    /**
     * 获取请求体长度
     *
     * @param message
     * @param defaultValue
     * @return
     */
    public static long getContentLength(Request message, long defaultValue) {
        String contentLength = message.getHeaders().get(HttpHeaders.Names.CONTENT_LENGTH);
        if (contentLength != null) {
            return Long.parseLong(contentLength);
        }
        return defaultValue;
    }


    /**
     * 解析cookie
     *
     * @param request
     */
    private void parseCookie(Request request) {
        // 获取请求cookie
        String cookieHeader = request.getHeaders().get("Cookie");
        if (!StringUtils.isBlank(cookieHeader)) {
            String[] cookiesArray = cookieHeader.split(";\\s*");
            for (int i = 0; i < cookiesArray.length; i++) {
                String cookieStr = cookiesArray[i];
                if (!StringUtils.isBlank(cookieStr)) {
                    String[] cookieArray = cookieStr.split("=");
                    if (cookieArray.length == 2) {
                        Cookie c = new Cookie(cookieArray[0], cookieArray[1],
                                -1);
                        request.getCookies().add(c);
                    }
                }
            }
        }
    }


    /**
     * 解析是否是长连接
     *
     * @param request
     */
    public static void parseConnection(Request request) {
        String connection = request.getHeaders().get(HttpHeaders.Names.CONNECTION);
        boolean isKeepAlive;
        if (connection != null && HttpHeaders.Values.CLOSE.equalsIgnoreCase(connection)) {
            request.setKeepAlive(false);
            isKeepAlive = false;
        } else if (request.getVersion().isKeepAliveDefault()) {
            isKeepAlive = !HttpHeaders.Values.CLOSE.equalsIgnoreCase(connection);
        } else {
            isKeepAlive = HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(connection);
        }
        request.setKeepAlive(isKeepAlive);
    }


    /**
     * 解析消息体
     *
     * @param request
     */
    private void parseBody(Request request) {

        if (request.isContentTmp()) {
            String path = Constant.UPLOAD_FILEPATH_TMP + request.getContentTmpPath();
            String contentType = request.getContentType().toLowerCase();
            if (contentType.startsWith("multipart/")) {
                String boundary = contentType.substring(contentType.indexOf("boundary") + "boundary=".length());

                ByteLineHardReader br = new ByteLineHardReader(path);

                byte[] bs;
                String str;
                while ((bs = br.nextLine()) != null) {

                    do {
                        str = FileUtil.byte2Str(bs);
                        /**
                         * 如果是文件
                         */
                        if (str.indexOf("Content-Disposition:") != -1 && str.indexOf("filename") != -1) {

                            str = str.substring("Content-Disposition:".length());
                            String[] strs = str.split(";");
                            String fileName = strs[strs.length - 1].replace("\"", "").split("=")[1];

                            do {
                                bs = br.nextLine();
                                /*遇到换行，下面就是文件正式内容*/
                            } while (bs.length == 0);
                            br.nextLine();

                            String tmpPath = Constant.UPLOAD_FILEPATH_TMP + Constant.num.incrementAndGet() + System.currentTimeMillis();

                            //AutoByteBuffer mutilBody = AutoByteBuffer.newByteBuffer();
                            boolean notFirstLine = false;
                            while ((bs = br.nextLine()) != null) {
                                str = FileUtil.byte2Str(bs);
                                if (str.toLowerCase().startsWith("--" + boundary.toLowerCase())) {
                                    break;
                                }
                                if (notFirstLine) {
                                    //mutilBody.writeBytes(new byte[]{'\r', '\n'});
                                    FileUtil.byte2File(new byte[]{'\r', '\n'}, tmpPath, true);
                                }
                                //mutilBody.writeBytes(bs);
                                FileUtil.byte2File(bs, tmpPath, true);
                                notFirstLine = true;

                            }
                            request.setMultipart(new Multipart(fileName, null, tmpPath));
                            //mutilBody.returnPool();
                        }

                        if (str.indexOf("Content-Disposition:") != -1) {
                            str = str.substring("Content-Disposition:".length());
                            String[] strs = str.split(";");
                            String name = strs[strs.length - 1].replace("\"", "").split("=")[1];
                            br.nextLine();
                            StringBuilder stringBuilder = new StringBuilder();
                            while (true) {
                                str = FileUtil.byte2Str(br.nextLine());
                                if (str.toLowerCase().startsWith("--" + boundary.toLowerCase())) {
                                    break;
                                }
                                stringBuilder.append(str);
                            }
                            request.getParameters().put(name, stringBuilder.toString());
                        }


                    } while (("--" + boundary).equals(str));

                    //解析结束
                    if (str.equals("--" + boundary + "--")) {
                        break;
                    }
                }

            } else if (contentType.contains("urlencoded")) {
                Map<String, String> map = resolveRequestArgs(FileUtil.byte2Str(FileUtil.file2Byte(path)));
                request.getParameters().putAll(map);
                request.setJsonBody(JsonUtil.map2JsonNode(map));
            } else if (contentType.contains("json")) {
                request.setJsonBody(JsonUtil.str2JsonNode(FileUtil.byte2Str(FileUtil.file2Byte(path))));
            }

        } else {
            byte[] body = request.getContent();

            String contentType = request.getContentType().toLowerCase();
            if (contentType.startsWith("multipart/")) {
                String boundary = contentType.substring(contentType.indexOf("boundary") + "boundary=".length());
                ByteLineReader br = new ByteLineReader(body);

                byte[] bs;
                String str;
                while ((bs = br.nextLine()) != null) {

                    do {
                        str = FileUtil.byte2Str(bs);
                        /**
                         * 如果是文件
                         */
                        if (str.indexOf("Content-Disposition:") != -1 && str.indexOf("filename") != -1) {

                            str = str.substring("Content-Disposition:".length());
                            String[] strs = str.split(";");
                            String fileName = strs[strs.length - 1].replace("\"", "").split("=")[1];

                            do {
                                bs = br.nextLine();
                                /*遇到换行，下面就是文件正式内容*/
                            } while (bs.length == 0);
                            br.nextLine();

                            String tmpPath = Constant.UPLOAD_FILEPATH_TMP + Constant.num.incrementAndGet() + System.currentTimeMillis();

                            //AutoByteBuffer mutilBody = AutoByteBuffer.newByteBuffer();
                            boolean notFirstLine = false;
                            while ((bs = br.nextLine()) != null) {
                                str = FileUtil.byte2Str(bs);
                                if (str.toLowerCase().startsWith("--" + boundary.toLowerCase())) {
                                    break;
                                }
                                if (notFirstLine) {
                                    //mutilBody.writeBytes(new byte[]{'\r', '\n'});
                                    FileUtil.byte2File(new byte[]{'\r', '\n'}, tmpPath, true);
                                }
                                //mutilBody.writeBytes(bs);
                                FileUtil.byte2File(bs, tmpPath, true);
                                notFirstLine = true;

                            }
                            request.setMultipart(new Multipart(fileName, null, tmpPath));
                            //mutilBody.returnPool();
                        }

                        if (str.indexOf("Content-Disposition:") != -1) {
                            str = str.substring("Content-Disposition:".length());
                            String[] strs = str.split(";");
                            String name = strs[strs.length - 1].replace("\"", "").split("=")[1];
                            br.nextLine();
                            StringBuilder stringBuilder = new StringBuilder();
                            while (true) {
                                str = FileUtil.byte2Str(br.nextLine());
                                if (str.toLowerCase().startsWith("--" + boundary.toLowerCase())) {
                                    break;
                                }
                                stringBuilder.append(str);
                            }
                            request.getParameters().put(name, stringBuilder.toString());
                        }

                    } while (("--" + boundary).equals(str));

                    //解析结束
                    if (str.equals("--" + boundary + "--")) {
                        break;
                    }
                }

            } else if (contentType.contains("urlencoded")) {
                Map<String, String> map = resolveRequestArgs(FileUtil.byte2Str(body));
                request.getParameters().putAll(map);
                request.setJsonBody(JsonUtil.map2JsonNode(map));
            } else if (contentType.contains("json")) {
                request.setJsonBody(JsonUtil.str2JsonNode(FileUtil.byte2Str(body)));
            }
        }


    }

    public static void parseHost(Request request, SocketSession socketSession) {
        /**
         * 如果中间层有HTTP代理需要从client-ip或则x-forwarded-for中取客户端IP
         */
        String clientIp = getIpAddress(request);
        if (StringUtils.isBlank(clientIp)) {
            clientIp = socketSession.getRemoteAddress();
        }
        request.setHost(clientIp);
    }

    public static String getIpAddress(Request request) {
        String Xip = request.getHeaders().get("X-Real-IP");
        String XFor = request.getHeaders().get("X-Forwarded-For");

        if (StringUtils.isNotBlank(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if (index != -1) {
                return XFor.substring(0, index);
            } else {
                return XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotBlank(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return XFor;
        }
        if ("unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeaders().get("Proxy-Client-IP");
        }
        if ("unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeaders().get("WL-Proxy-Client-IP");
        }
        if ("unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeaders().get("HTTP_CLIENT_IP");
        }
        if ("unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeaders().get("HTTP_X_FORWARDED_FOR");
        }
        if ("unknown".equalsIgnoreCase(XFor)) {
            XFor = null;
        }
        return XFor;
    }


}
