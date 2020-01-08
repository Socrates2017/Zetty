package com.zrzhen.zetty.http.http.http;

import java.nio.ByteBuffer;

/**
 * @author chenanlian
 */
public interface RequestOut {

    /**
     * 转换为字节缓冲
     * @return 字节缓冲
     */
    ByteBuffer toByteBuffer();
}
