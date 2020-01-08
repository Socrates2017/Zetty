package com.zrzhen.zetty.http.http.bufpool;

import com.zrzhen.zetty.http.http.AutoByteBuffer;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author chenanlian
 */
public class AutoByteBufferFactory extends BasePooledObjectFactory<AutoByteBuffer> {
    @Override
    public AutoByteBuffer create() throws Exception {
        return AutoByteBuffer.newByteBuffer2();
    }

    @Override
    public PooledObject<AutoByteBuffer> wrap(AutoByteBuffer obj) {
        return new DefaultPooledObject<AutoByteBuffer>(obj);
    }
}
