package com.zrzhen.zetty.http.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class TagDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(ArticleDao.class);


    public static final String tableName = "tag";

    public static List<String> tagsByUser(Long userId) {
        String sql = "SELECT DISTINCT(tag) FROM article_tag where uuser = ?";
        Object[] args = new Object[]{userId};
        List<Map<String, Object>> list = getList(sql, args);
        return map2String(list);
    }

    public static List<String> allTags() {
        String sql = "SELECT DISTINCT(tag) FROM article_tag";
        List<Map<String, Object>> list = getList(sql, null);
        return map2String(list);
    }

    public static List<String> tagsNotInArticleList(Integer article) {
        String sql = "SELECT DISTINCT(tag) FROM article_tag where tag NOT IN (SELECT DISTINCT(tag) FROM article_tag where article = ?)";
        Object[] args = new Object[]{article};
        List<Map<String, Object>> list = getList(sql, args);
        return map2String(list);
    }

    public static List<String> tagsInArticleList(Integer article) {
        String sql = "SELECT DISTINCT(tag) FROM article_tag where article =?";
        Object[] args = new Object[]{article};
        List<Map<String, Object>> list = getList(sql, args);
        return map2String(list);
    }

    static List<String> map2String(List<Map<String, Object>> list) {
        List<String> out = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                out.add((String) list.get(i).get("tag"));
            }
        }
        return out;
    }


}
