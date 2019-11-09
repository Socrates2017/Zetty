package com.zrzhen.zetty.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class BookIndexDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(BookIndexDao.class);


    public static final String tableName = "book_index";


    public static List<Map<String, Object>> listByParent(Long book, Integer parent) {
        String sql = "select id,parent,name,url,index_order as indexOrder FROM " + tableName + " WHERE book = ? and parent = ? ORDER BY indexOrder";
        Object[] bindArgs = new Object[]{book,parent};
        return getList(sql, bindArgs);
    }

    public static List<Map<String, Object>> listByParent(Integer parent) {
        String sql = "select id,parent,name,url,index_order as indexOrder FROM " + tableName + " WHERE parent = ? ORDER BY indexOrder";
        Object[] bindArgs = new Object[]{parent};
        return getList(sql, bindArgs);
    }

    public static List<Map<String, Object>> allNodeByParent(Integer parent) {
        String sql = "with subRecord(id,parent,name,url) as ( select id,parent,name,url FROM " + tableName
                + " WHERE id = ? union all select zebook.id,zebook.parent,zebook.name ,zebook.url from zebook,subRecord where zebook.parent = subRecord.id) select * from subRecord;";
        Object[] bindArgs = new Object[]{parent};
        return getList(sql, bindArgs);
    }


}
