package com.zrzhen.zetty.http.mappool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.HashMap;

/**
 * @author chenanlian
 */
public class MapPool extends GenericObjectPool<HashMap> {

    public MapPool() {
        super(new MapFactory(), new MapPoolConfig());
    }

    public MapPool(MapPoolConfig mapPoolConfig) {
        super(new MapFactory(), mapPoolConfig);
    }
}
