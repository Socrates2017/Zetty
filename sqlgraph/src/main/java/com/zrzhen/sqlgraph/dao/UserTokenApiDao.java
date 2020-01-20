package com.zrzhen.sqlgraph.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author chenanlian
 */
public class UserTokenApiDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(UserTokenApiDao.class);


    public static final String tableName = "user_api_token";

    public static String getUserApiToken(Long userId) {
        String sql = "select token from user_api_token where user_id =?";
        Object[] bindArgs = new Object[]{userId};
        Map<String, Object> result = getOne(sql, bindArgs);
        if (result != null && result.get("token") != null) {
            return (String) result.get("token");
        }
        return null;
    }



}
