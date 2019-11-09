package com.zrzhen.zetty.core;

import com.zrzhen.zetty.core.util.ProUtil;

/**
 * @author chenanlian
 */
public class Constant {


    /**
     * 默认请求缓冲区大小,默认1MB
     */
    public static int BUFFER_SIZE = 1024 * 1024;


    /**
     * 请求行最大长度，默认2KB
     */
    public static int maxInitialLineLength = 1024 * 2;

    /**
     * 请求头最大长度，默认4KB
     */
    public static int maxHeaderSize = 1024 * 4;

    /**
     * 请求体最大长度，默认500MB
     */
    public static int maxContentSize = 1024 * 1024 * 500;


    /**
     * 默认字符编码
     */
    public String DEFAULT_ENCODING = "UTF-8";

    public static long SOCKET_READ_TIMEOUT = 30;

    /**
     * 静态文件在客户端的缓存时间，在有效期内，将不请求服务器
     * 如果静态文件有改动，需要修改引用的版本
     * 如果<link href="/static/css/public.css?v=2019092701" rel="stylesheet">
     * 修改v后面的版本号
     */
    public static final long HTTP_CACHE_SECONDS = ProUtil.getLong("http_static_cache_second");


}
