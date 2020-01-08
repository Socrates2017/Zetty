package com.zrzhen.zetty.http.dao;

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

    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    public static List<Map<String, Object>> listByUserId(Long id) {
        String sql = "select a.id,a.userid,a.title,a.content,a.ctime from `article` a  " +
                "where a.userid = ?";
        Object[] bindArgs = new Object[]{id};
        return getList(sql, bindArgs);
    }

    /**
     * 根据主键查询一条数据
     *
     * @param id
     * @return
     */
    public static Map<String, Object> oneByKey(Integer id) {
        String sql = "select a.id,a.userid,a.title,a.content,a.ctime,trim(b.name) as username,a.status from `article` a  " +
                "left join `user` b on b.id = a.userid " +
                "where a.id = ?";
        Object[] bindArgs = new Object[]{id};
        return getOne(sql, bindArgs);
    }

    /**
     * 连接user表获取作者名
     * 根据id倒序排序
     * 分页查询
     * 非私密文章
     *
     * @param start 开始位置
     * @param row   返回条数
     * @return
     */
    public static List<Map<String, Object>> getList2(Integer start, Integer row) {
        String sql = "select a.id, a.title, a.ctime, u.name as username from `article` AS a " +
                "LEFT JOIN `user` AS u ON u.id = a.userid where a.status = 0 order by a.id desc limit ?,?";
        Object[] bindArgs = new Object[]{start, row};
        return getList(sql, bindArgs);
    }

    /**
     * 连接user表获取作者名
     * 根据id倒序排序
     * 分页查询
     * 非私密文章
     *
     * @param start 开始位置
     * @param row   返回条数
     * @return
     */
    public static List<Map<String, Object>> getList2(Integer start, Integer row, Long userId) {
        String sql = "select a.id, a.title, a.ctime, u.name as username from `article` AS a " +
                "LEFT JOIN `user` AS u ON u.id = a.userid where a.status = 0 and a.userid = ? order by a.id desc limit ?,?";
        Object[] bindArgs = new Object[]{userId, start, row};
        return getList(sql, bindArgs);
    }

    /**
     * 根据标签查询
     * 连接user表获取作者名
     * 根据id倒序排序
     * 分页查询
     *
     * @param tag   标签
     * @param start 起始位置
     * @param row   返回行数
     * @return
     */
    public static List<Map<String, Object>> listByPageTag(String tag, Integer start, Integer row) {
        String sql = "select a.id, a.title, a.ctime, u.name as username from `article` AS a LEFT JOIN `user` AS u ON u.id = a.userid " +
                "WHERE a.id IN (" +
                "SELECT t.article FROM (" +
                "SELECT article FROM article_tag WHERE article_tag.tag = ? order by article desc limit ?,?) AS t" +
                ") and a.status = 0";
        Object[] bindArgs = new Object[]{tag, start, row};
        return getList(sql, bindArgs);
    }


    /**
     * 根据标签查询
     * 连接user表获取作者名
     * 根据id倒序排序
     * 分页查询
     *
     * @param tag   标签
     * @param start 起始位置
     * @param row   返回行数
     * @return
     */
    public static List<Map<String, Object>> listByPageTag(String tag, Integer start, Integer row, Long userId) {
        String sql = "select a.id, a.title, a.ctime, u.name as username from `article` AS a LEFT JOIN `user` AS u ON u.id = a.userid " +
                "WHERE a.id IN (" +
                "SELECT t.article FROM (" +
                "SELECT article FROM article_tag WHERE article_tag.tag = ? and uuser = ? order by article desc limit ?,?) AS t" +
                ") and a.status = 0";
        Object[] bindArgs = new Object[]{tag, userId, start, row};
        return getList(sql, bindArgs);
    }

    /**
     * 根据标签查询
     * 连接user表获取作者名
     * 根据id倒序排序
     * 分页查询
     *
     * @param start 起始位置
     * @param row   返回行数
     * @return
     */
    public static List<Map<String, Object>> listPrivate( Integer start, Integer row, Long userId) {
        String sql = "select a.id, a.title, a.ctime, u.name as username from `article` AS a " +
                "LEFT JOIN `user` AS u ON u.id = a.userid where a.status = 1 and a.userid = ? order by a.id desc limit ?,?";
        Object[] bindArgs = new Object[]{userId, start, row};
        return getList(sql, bindArgs);
    }
}
