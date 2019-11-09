package com.zrzhen.zetty.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author chenanlian
 */
public class UserDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(ArticleDao.class);


    public static final String tableName = "user";

    public static Integer statusById(Long id) {
        String sql = "select status from `user` where id = ? limit 1";
        Object[] args = new Object[]{id};
        Map<String, Object> map = getOne(sql, args);
        if (map != null) {
            return (Integer) map.get("status");
        } else {
            return null;
        }
    }

    /**
     * 根据email查询数量
     *
     * @param email
     * @return
     */
    public static long countByEmail(String email) {
        String sql = "select count(1) from `user` where email = ?";
        Object[] args = new Object[]{email};
        Map<String, Object> map = getOne(sql, args);
        return (Long) map.get("count(1)");
    }

    public static Map<String, Object> selectByPrimaryKey(Long userid) {
        String sql = "select id,name from `user` where id = ? limit 1";
        Object[] args = new Object[]{userid};
        return getOne(sql, args);
    }

    public static Map<String, Object> idByEmailAndPwd(String email, String password) {
        String sql = "select id from `user` where email = ? AND password = ? limit 1";
        Object[] args = new Object[]{email, password};
        return getOne(sql, args);
    }

    public static Map<String, Object> statusbyEmail(String email) {
        String sql = "select status from `user` where email = ? limit 1";
        Object[] args = new Object[]{email};
        return getOne(sql, args);
    }




}
