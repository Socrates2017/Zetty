package com.zrzhen.zetty.http.http.mappool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.HashMap;

/**
 * @author chenanlian
 */
public class MapFactory extends BasePooledObjectFactory<HashMap> {


    @Override
    public HashMap create() throws Exception {
        return new HashMap(10);
    }

    @Override
    public PooledObject<HashMap> wrap(HashMap map) {
        return new DefaultPooledObject<HashMap>(map);
    }
}