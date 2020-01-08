package com.zrzhen.zetty.http.http;

import com.zrzhen.zetty.http.http.util.ProUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenanlian
 */
public class Constant {


    /**
     * 默认请求缓冲区大小,默认1MB
     */
    public final static int BUFFER_SIZE = 1024 * 1024;


    /**
     * 请求行最大长度，默认2KB
     */
    public final static int maxInitialLineLength = 1024 * 2;

    /**
     * 请求头最大长度，默认4KB
     */
    public final static int maxHeaderSize = 1024 * 4;

    /**
     * 请求体最大长度，默认10G
     */
    public final static int maxContentSize = 1024 * 1024 * 1024 * 5;


    /**
     * 默认字符编码
     */
    public final static String DEFAULT_ENCODING = "UTF-8";

    /**
     * socket连接超时时长
     */
    public final static long SOCKET_READ_TIMEOUT = 30;

    /**
     * 静态文件在客户端的缓存时间，在有效期内，将不请求服务器
     * 如果静态文件有改动，需要修改引用的版本
     * 如果<link href="/static/css/public.css?v=2019092701" rel="stylesheet">
     * 修改v后面的版本号
     */
    public final static long HTTP_CACHE_SECONDS = ProUtil.getLong("http_static_cache_second");

    /**
     * 文件上传临时文件夹，避免所有数据都在加载进内存导致内存溢出
     */
    public static final String UPLOAD_FILEPATH_TMP = ProUtil.userDir + ProUtil.getString("upload.filePath.tmp");

    /**
     * 辅助临时文件命名，避免高并发重名
     */
    public static AtomicInteger num = new AtomicInteger(0);

}
