package com.zrzhen.zetty.http.http.util.http;



import com.zrzhen.zetty.http.http.Constant;
import com.zrzhen.zetty.http.http.MvcUtil;
import com.zrzhen.zetty.http.http.http.*;
import com.zrzhen.zetty.http.thread.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

import static com.zrzhen.zetty.http.http.http.HttpSocketStatus.*;


/**
 * @author chenanlian
 */
public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static Logger log = LoggerFactory.getLogger(ReadHandler.class);


    /**
     * socket状态
     */
    private HttpSocketStatus socketStatus;

    /**
     * 请求消息
     */
    private Response response;

    /**
     * 异步socket通道，用于读取消息和发送应答
     */
    private AsynchronousSocketChannel ch;

    AsyHttpClientCallback callback;


    public ReadHandler(Response response, AsynchronousSocketChannel ch, AsyHttpClientCallback callback) {
        this.socketStatus = SKIP_CONTROL_CHARS;
        this.response = response;
        this.ch = ch;
        this.callback = callback;
    }

    /**
     * 读取到消息后的处理，由于一次读取，可能不会读取完全部消息，需要反复读取，直至读取消息完全
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (result == -1) {
            /*
             * 如果客户端关闭了TCP连接，这儿会返回一个-1
             */
            MvcUtil.closeChannel(ch);
            return;
        }
        execute(attachment);
    }


    public void execute(ByteBuffer attachment) {
        attachment.flip();
        try {
            if (socketStatus.equals(SKIP_CONTROL_CHARS)) {
                skipControlCharacters(attachment);
                this.socketStatus = READ_INITIAL;
            }
            if (socketStatus.equals(READ_INITIAL)) {
                String line = readLine(attachment, 1024 * 1024);
                if (line == null) {
                    throw new HttpException(HttpResponseStatus.BAD_REQUEST, "Request line is empty!");
                }
                parseLine(response, line);
                this.socketStatus = READ_HEADER;
            }
            if (socketStatus.equals(READ_HEADER)) {
                if (!readHeaders(attachment, response)) {
                    throw new HttpException(HttpResponseStatus.BAD_REQUEST, "Request header take wrong!");
                }
                long contentLength = getContentLength(response, -1);
                if (contentLength > 0) {
                    if (contentLength > 1024 * 1024 * 1024) {
                        throw new HttpException(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, "Request Entity Too Large : " + contentLength);
                    }
                    try {
                        response.createContentBuffer((int) contentLength, response.getHeaders().get(HttpHeaders.Names.CONTENT_TYPE));
                    } catch (Exception e) {
                        log.info(e.getMessage(), e);
                        throw new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    this.socketStatus = READ_VARIABLE_LENGTH_CONTENT;
                } else {
                    this.socketStatus = HttpSocketStatus.READ_REQUEST_FINISH;
                }
            }
            if (socketStatus.equals(READ_VARIABLE_LENGTH_CONTENT)) {
                try {
                    if (response.readContentBuffer(attachment)) {
                        parseBody(response);
                        this.socketStatus = HttpSocketStatus.READ_REQUEST_FINISH;
                    } else {
                        attachment.clear();
                        ch.read(attachment, Constant.SOCKET_READ_TIMEOUT, TimeUnit.SECONDS, attachment, this);
                        return;
                    }
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                    throw new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                }
            }

            if (socketStatus.equals(READ_REQUEST_FINISH)) {
                socketStatus = HttpSocketStatus.PROCESSING;
                parseConnection(response);
                /**
                 * 至此，http请求消息已经解析完毕，可以进行业务逻辑处理
                 */
                callback.setResponse(response);
                ThreadPoolUtil.logExecutor.submit(callback);
                return;
            } else {
                throw new HttpException(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Unknown error!");
            }
        } catch (Throwable t) {
            if (t instanceof HttpException) {
                HttpException e = (HttpException) t;
            }
            log.error(t.getMessage(), t);
        }
    }


    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error(exc.getMessage(), exc);
        MvcUtil.closeChannel(ch);
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
     * @param response
     * @param line
     * @throws HttpException
     */
    private void parseLine(Response response, String line) throws HttpException {
        String[] initialLine = splitInitialLine(line);
        String text = initialLine[0].toUpperCase();
        HttpVersion version = HttpVersion.getHttpVersion(text);
        response.setVersion(version);
        String status = initialLine[1];
        HttpResponseStatus status1 = HttpResponseStatus.valueOf(Integer.valueOf(status));
        response.setStatus(status1);
    }


    /**
     * 读取请求头
     *
     * @param buffer
     * @param response
     * @return
     * @throws HttpException
     */
    private boolean readHeaders(ByteBuffer buffer, Response response) throws HttpException {

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
                    readHeader(response, sb.toString());
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
                readHeader(response, sb.toString());
                lineLength = 0;
                sb.setLength(0);
                index++;
            } else {
                if (lineLength >= 1024 * 1024) {
                    throw new HttpException(HttpResponseStatus.BAD_REQUEST, "An HTTP header is larger than " + Constant.maxHeaderSize + " bytes.");
                }
                lineLength++;
                sb.append((char) nextByte);
            }
        }
        return false;
    }

    private static void readHeader(Response response, String header) {
        String[] kv = splitHeader(header);
        response.getHeaders().put(kv[0], kv[1]);
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
     * @param response
     * @param defaultValue
     * @return
     */
    public static long getContentLength(Response response, long defaultValue) {
        String contentLength = response.getHeaders().get(HttpHeaders.Names.CONTENT_LENGTH);
        if (contentLength != null) {
            return Long.parseLong(contentLength);
        }
        return defaultValue;
    }


    /**
     * 解析是否是长连接
     *
     * @param response
     */
    public static void parseConnection(Response response) {
        String connection = response.getHeaders().get(HttpHeaders.Names.CONNECTION);
        boolean isKeepAlive;
        if (connection != null && HttpHeaders.Values.CLOSE.equalsIgnoreCase(connection)) {
            response.setKeepAlive(false);
            isKeepAlive = false;
        } else {
            isKeepAlive = HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(connection);
        }
        response.setKeepAlive(isKeepAlive);
    }


    /**
     * 解析消息体
     *
     * @param response
     */
    private void parseBody(Response response) {
        if (response.isContentTmp()) {
//            String path = Constant.UPLOAD_FILEPATH_TMP + response.getContentTmpPath();
//            response.setContentTmpPath(path);
        }
    }


}
