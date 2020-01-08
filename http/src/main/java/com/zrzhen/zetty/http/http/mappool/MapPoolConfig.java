package com.zrzhen.zetty.http.http.mappool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class MapPoolConfig extends GenericObjectPoolConfig {

    public MapPoolConfig() {
        // defaults to make your life with connection pool easier :)
        setMinIdle(5);
        setTestOnBorrow(true);
    }

}
