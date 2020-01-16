package com.zrzhen.zetty.http.bufpool;

import com.zrzhen.zetty.http.AutoByteBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author chenanlian
 */
public class AutoByteBufferPool extends GenericObjectPool<AutoByteBuffer> {

    public AutoByteBufferPool() {
        super(new AutoByteBufferFactory(), new AutoByteBufferConfig());
    }

    public AutoByteBufferPool(AutoByteBufferConfig config) {
        super(new AutoByteBufferFactory(), config);
    }
}