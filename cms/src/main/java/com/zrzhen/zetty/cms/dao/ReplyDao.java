package com.zrzhen.zetty.cms.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class ReplyDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(ReplyDao.class);


    public static final String tableName = "reply";


    public static List<Map<String, Object>> getList2(Integer id) {
        String sql = "select a.user_name as username, a.content, a.ctime from `reply` AS a  where a.article_id = ?";
        Object[] bindArgs = new Object[]{id};
        return getList(sql, bindArgs);
    }
}
