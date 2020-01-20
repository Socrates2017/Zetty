package com.zrzhen.zetty.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.http.*;
import com.zrzhen.zetty.http.mvc.*;
import com.zrzhen.zetty.http.mvc.exception.RequestBodyShouldNotBeEmptyException;
import com.zrzhen.zetty.http.mvc.exception.RequestParamShouldNotBeEmptyException;
import com.zrzhen.zetty.http.mvc.exception.UriNotFoundException;
import com.zrzhen.zetty.http.util.ServerUtil;
import com.zrzhen.zetty.net.SocketSession;
import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.common.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class MvcUtil {

    private static Logger log = LoggerFactory.getLogger(MvcUtil.class);

    /**
     * 静态文件请求的uri前缀，带有此前缀的uri直接读取resource下的文件，不经过路由控制类
     */
    private static final String STATIC_REQUEST = "/static/";

    private static final String FAVICON_ICO = "/favicon.ico";

    public static void handle(SocketSession socketSession, Request request) {
        try {
            Request.set(request);
            String uri = request.getUri();
            log.info("request uri:{}。", uri);

            Response response = new Response();
            Response.set(response);

            if (uri == null) {
                throw new HttpException(HttpResponseStatus.BAD_REQUEST, "uri is null!");
            } else if (uri.equals(FAVICON_ICO)) {
                byte[] data = FileUtil.file2ByteByRelativePath("static/img/favicon.ico");
                response.setContent(data);
                response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, "image/x-icon");
            } else if (uri.startsWith(STATIC_REQUEST)) {
                /*
                  如果是GET 方法，且以“/static/”开头，即请求静态文件，则不经由controller处理
                 */

                dealWithStaticRequest();
            } else {
                dealWithController();
            }

            Response out = Response.get();

            if (out.isDownload()) {
                String path = out.getDownloadPath();
                out.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(new File(path).length()));
                ByteBuffer byteBuffer = out.toByteBufferForDownload(path);
                byteBuffer.flip();
                socketSession.write(byteBuffer);
//                ByteLineHardReader br = new ByteLineHardReader(path);
//                ByteBuffer byteBuffer = ByteBuffer.wrap(br.nextPart());
//                write(ch, byteBuffer, br, !br.isReadEnd());

            } else {
                //设置返回头中的内容大小
                if (out.getContent() != null) {
                    out.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(out.getContent().length));
                }

                ByteBuffer byteBuffer = out.toByteBuffer();
                socketSession.setWriteBuffer(byteBuffer);
                byteBuffer.flip();
                socketSession.write(byteBuffer);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            writeError(socketSession, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        } finally {
            Request.remove();
            Response.remove();
        }

    }

    /**
     * 处理静态文件请求
     */
    private static void dealWithStaticRequest() {
        Request request = Request.get();
        String uri = request.getUri();
        Response response = Response.get();
        byte[] body = FileUtil.file2ByteByRelativePath(uri);
        if (null == body) {
            response.setStatus(HttpResponseStatus.NOT_FOUND);
        } else {
            response.setStatus(HttpResponseStatus.OK);
            response.setContent(body);
            /*设置缓存*/
            response.getHeaders().put(HttpHeaders.Names.CACHE_CONTROL, "private, max-age=" + Constant.HTTP_CACHE_SECONDS);
        }

        int indxVersion = uri.indexOf("?");
        int indexDot = uri.lastIndexOf("/") + 1;
        String fileName;
        if (indxVersion > 0) {
            fileName = uri.substring(indexDot, indxVersion);
        } else {
            fileName = uri.substring(indexDot);
        }
        String contentType = ServerUtil.contentTypeByFileName(fileName);
        if (StringUtils.isNotBlank(contentType)) {
            response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, contentType);
        }
        setConnetion(response, request);

    }

    /**
     * 处理动态链接
     *
     * @throws RequestParamShouldNotBeEmptyException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws RequestBodyShouldNotBeEmptyException
     * @throws UriNotFoundException
     */
    private static void dealWithController() throws RequestParamShouldNotBeEmptyException, InvocationTargetException,
            IllegalAccessException, RequestBodyShouldNotBeEmptyException, UriNotFoundException {
        Request request = Request.get();
        String path = request.getUri();

        Response response = Response.get();
        response.setStatus(HttpResponseStatus.OK);

        JsonNode jsonBody = request.getJsonBody();

        /*根据uri获得要执行的Controller方法*/
        ControllerMethod controllerMethod = ServerContext.getControllerMethod(path);

        String pathVariable = "";
        /*兼容uri中的动态参数*/
        if (controllerMethod == null) {
            controllerMethod = ServerContext.getControllerMethod(path.substring(0, path.lastIndexOf("/")));
            if (controllerMethod != null) {
                pathVariable = path.substring(path.lastIndexOf("/") + 1);
            }
        }

        if (controllerMethod == null) {
            dealWithNotFoundHtml(response);
        } else {

            /*前增强器*/
            List<IBeforeAdvice> iBeforeAdviceList = controllerMethod.getBeforeAdviceList();
            Iterator<IBeforeAdvice> iBeforeAdviceIterator = iBeforeAdviceList.iterator();
            while (iBeforeAdviceIterator.hasNext() && response.isFlag()) {
                IBeforeAdvice interceptor = iBeforeAdviceIterator.next();
                response = interceptor.before();
            }

            if (response.isFlag()) {
                /*要执行的方法*/
                Method method = controllerMethod.getMethod();
                /*要执行方法的参数的传入值*/
                Object[] paramValues = ServerUtil.getParameterValues(method, request.getParameters(), jsonBody, request, response, pathVariable);
                /*执行结果*/
                Object result = controllerMethod.call(paramValues);

                /*后增强器*/
                List<IAfterAdvice> iAfterAdviceList = controllerMethod.getAfterAdviceList();
                Iterator<IAfterAdvice> iAfterAdviceIterator = iAfterAdviceList.iterator();

                while (iAfterAdviceIterator.hasNext()) {
                    IAfterAdvice interceptor = iAfterAdviceIterator.next();
                    result = interceptor.after(result);
                }

                /*执行方法中注解的返回内容类型*/
                ContentTypeEnum contentTypeEnum = controllerMethod.getContentType();

                if (contentTypeEnum.equals(ContentTypeEnum.HTML)) {
                    /*如果内容类型为html，根据返回结果（文件路径）读取文件*/
                    String responseBody = null;
                    if (result instanceof Model) {
                        Model model = (Model) result;
                        String htmlPath = model.getPath();
                        htmlPath = ServerUtil.covPath(htmlPath);
                        responseBody = getAndCacheStaticFileString(htmlPath);
                        Map<String, Object> map = model.getMap();
                        if (map != null) {
                            responseBody = ServerUtil.setModel(responseBody, map);
                        }
                    } else {
                        String htmlPath = String.valueOf(result);
                        htmlPath = ServerUtil.covPath(htmlPath);
                        responseBody = getAndCacheStaticFileString(htmlPath);
                    }

                    if (null == responseBody) {
                        dealWithNotFoundHtml(response);
                    } else {
                        response.setContent(FileUtil.str2Byte(responseBody));
                        setContentType(response, contentTypeEnum);
                    }
                    setConnetion(response, request);
                } else if (contentTypeEnum.equals(ContentTypeEnum.JSON)) {
                    /*如果内容类型为json，则对结果进行转换，主要是实体类会转成json字符串*/
                    response.setContent(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
                    setContentType(response, contentTypeEnum);
                    setConnetion(response, request);
                } else if (result instanceof Response) {
                    /*不设置内容类型，由业务进行控制*/
                    Response.set((Response) result);
                } else if (result != null) {
                    /*默认返回字符串*/
                    response.setContent(FileUtil.str2Byte(String.valueOf(result)));
                    setContentType(response, contentTypeEnum);
                    setConnetion(response, request);
                }
            }
        }

    }

    private static void dealWithNotFoundHtml(Response response) {
        byte[] body = FileUtil.file2ByteByRelativePath("/html/404.html");
        response.setContent(body);
        response.setStatus(HttpResponseStatus.NOT_FOUND);
        setContentType(response, ContentTypeEnum.HTML);
    }

    /**
     * 获取并缓存静态文件字节数组
     *
     * @param path
     * @return
     */
    private static byte[] getAndCacheStaticFile(String path) {
        byte[] responseBody = FileUtil.file2ByteByRelativePath(path);

        return responseBody;
    }

    /**
     * 获取并缓存html文件内容，以字符串形式
     *
     * @param path
     * @return
     */
    private static String getAndCacheStaticFileString(String path) {
        String responseBody = ServerUtil.getHtml(path);
        return responseBody;
    }

    /**
     * 设置返回头中的内容类型
     *
     * @param response
     * @param contentTypeEnum
     */
    private static void setContentType(Response response, ContentTypeEnum contentTypeEnum) {
        response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, contentTypeEnum.getType());
    }


    /**
     * 设置是否长连接
     *
     * @param response
     * @param request
     */
    private static void setConnetion(Response response, Request request) {
        if (request.isKeepAlive()) {
            response.setKeepAlive(true);
            response.getHeaders().put(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        } else {
            response.setKeepAlive(false);
            response.getHeaders().put(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        }
    }


    /**
     * 输出错误消息到客户端
     */
    public static void writeError(SocketSession socketSession, HttpResponseStatus status) {
        Request request = Request.get();
        Response response = new Response();
        if (request == null) {
            response.setVersion(HttpVersion.HTTP_1_1);
        } else {
            response.setVersion(request.getVersion());
        }
        response.setStatus(status);
        byte[] content = status.getBytes();
        response.setContent(content);
        response.getHeaders().put(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);

        ByteBuffer byteBuffer = response.toByteBuffer();
        byteBuffer.flip();
        socketSession.write(byteBuffer);
    }

    /**
     * 关闭通道
     */
    public static void closeChannel(AsynchronousSocketChannel channel) {
        try {
            if (channel != null) {
                synchronized (channel) {
                    if (channel != null) {
                        channel.close();
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
