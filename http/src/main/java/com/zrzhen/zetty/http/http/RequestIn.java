package com.zrzhen.zetty.http.http;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public interface RequestIn {

    /**
     * 初始化请求体
     *
     * @param contentLength 请求体长度
     * @param contentType   请求类型
     */
    void createContentBuffer(int contentLength, String contentType);

    /**
     * 读取请求体中内容
     *
     * @param buffer socket读到的数据
     * @return 是否读取完成。true：请求体已经读取完成；false：请求体未读取完毕，还需要继续读取。
     */
    boolean readContentBuffer(ByteBuffer buffer);
}
