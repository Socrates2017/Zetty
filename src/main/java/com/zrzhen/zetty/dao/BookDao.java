package com.zrzhen.zetty.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class BookDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(BookDao.class);


    public static final String tableName = "book";

    public static Map<String, Object> oneByKey(Long id) {
        String sql = "select a.id,a.name,a.description,b.name as creator FROM " + tableName + " a left join user b on a.creator = b.id  where a.id = ?";
        Object[] bindArgs = new Object[]{id};
        return getOne(sql, bindArgs);
    }

    public static List<Map<String, Object>> bookList() {
        String sql = "select a.id,a.name,a.description,a.creator as creatorId,b.name as creator FROM " + tableName
                + " a left join user b on a.creator = b.id  ORDER BY id limit ?";
        Object[] bindArgs = new Object[]{100};
        return getList(sql, bindArgs);
    }

    public static List<Map<String, Object>> bookList(Long userId) {
        String sql = "select a.id,a.name,a.description,a.creator as creatorId,b.name as creator FROM " + tableName
                + " a left join user b on a.creator = b.id where a.creator = ? ORDER BY id limit ?";
        Object[] bindArgs = new Object[]{userId, 100};
        return getList(sql, bindArgs);
    }

    public static long checkUser(Long id, Long creator) {
        String sql = "select count(1) FROM " + tableName + " where id =? and creator = ? limit 1";
        Object[] args = new Object[]{id, creator};
        Map<String, Object> map = getOne(sql, args);
        return (Long) map.get("count(1)");
    }

}
