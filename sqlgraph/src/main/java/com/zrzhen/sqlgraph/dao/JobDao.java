package com.zrzhen.sqlgraph.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class JobDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(JobDao.class);


    public static final String tableName = "job";

    public static List<Map<String, Object>> allJob() {
        String sql = "select job.*,user_base.name as username from job left join  user_base on job.user_id = user_base.id";
        return getList(sql, null);
    }


}
