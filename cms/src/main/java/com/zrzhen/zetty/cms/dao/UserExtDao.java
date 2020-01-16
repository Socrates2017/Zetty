package com.zrzhen.zetty.cms.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * @author chenanlian
 */
public class UserExtDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(ArticleDao.class);


    public static final String tableName = "user_ext";


    public static Date tokenExptimeByUserid(Long userid) {
        String sql = "select token_exptime from `user_ext` where userid = ? limit 1";
        Object[] args = new Object[]{userid};

        Map<String, Object> map = getOne(sql, args);
        if (map != null) {
            return (Date) map.get("token_exptime");
        } else {
            return null;
        }
    }


}
