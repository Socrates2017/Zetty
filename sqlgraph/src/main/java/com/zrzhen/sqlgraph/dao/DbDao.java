package com.zrzhen.sqlgraph.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class DbDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DbDao.class);


    public static final String tableName = "db";

    public static List<Map<String, Object>> allJob() {
        String sql = "select db.* from db left join user_base on db.user_id=user_base.id";
        return getList(sql, null);
    }


}
