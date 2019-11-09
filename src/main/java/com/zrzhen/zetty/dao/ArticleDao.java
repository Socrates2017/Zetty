package com.zrzhen.zetty.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class ArticleDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(ArticleDao.class);


    public static final String tableName = "article";

    public static Map<String, Object> oneByKey(Integer id) {
        String sql = "select a.id,a.userid,a.title,a.content,a.ctime,trim(b.name) as username from `article` a  " +
                "left join `user` b on b.id = a.userid " +
                "where a.id = ?";
        Object[] bindArgs = new Object[]{id};
        return getOne(sql, bindArgs);
    }

    public static List<Map<String, Object>> getList2(Integer start, Integer row) {
        String sql = "select a.id, a.title, a.ctime, u.name as username from `article` AS a " +
                "LEFT JOIN `user` AS u ON u.id = a.userid order by a.id desc limit ?,?";
        Object[] bindArgs = new Object[]{start, row};
        return getList(sql, bindArgs);
    }


    public static List<Map<String, Object>> listByPageTag(String tag, Integer start, Integer row) {
        String sql = "select a.id, a.title, a.ctime, u.name as username from `article` AS a LEFT JOIN `user` AS u ON u.id = a.userid " +
                "WHERE a.id IN (" +
                "SELECT t.article FROM (" +
                "SELECT article FROM article_tag WHERE article_tag.tag = ? order by article desc limit ?,?) AS t" +
                ") ";
        Object[] bindArgs = new Object[]{tag, start, row};
        return getList(sql, bindArgs);
    }


}
