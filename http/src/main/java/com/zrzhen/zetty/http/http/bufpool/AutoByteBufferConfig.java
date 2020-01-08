package com.zrzhen.zetty.http.http.bufpool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author chenanlian
 */
public class AutoByteBufferConfig extends GenericObjectPoolConfig {

    public AutoByteBufferConfig() {
        setMaxTotal(20);
        setBlockWhenExhausted(false);
        setTestOnBorrow(true);
        setTestOnReturn(true);
        setTestOnCreate(true);
    }
}
