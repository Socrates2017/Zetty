package com.zrzhen.zetty.http.dao;

import com.zrzhen.zetty.http.controller.IndexController;
import com.zrzhen.zetty.http.dao.jdbc.MysqlFirst;
import com.zrzhen.zetty.http.dao.jdbc.core.SqlNotFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */
public class MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);


    public static Map<String, Object> getOne(String sql, Object[] bindArgs) {

        List<Map<String, Object>> out = getList(sql, bindArgs);
        if (out != null && out.size() > 0) {
            return out.get(0);
        } else {
            return null;
        }
    }

    public static List<Map<String, Object>> getList(String sql, Object[] bindArgs) {

        List<Map<String, Object>> out = null;
        try {
            out = MysqlFirst.instance().executeQuery(sql, bindArgs);
        } catch (SQLException e) {
            log.error(e.getMessage() + sql, e);
        }
        return out;
    }

    /**
     * 自动提交
     *
     * @param valueMap
     * @return
     */
    public static int insert(String tableName, Map<String, Object> valueMap) {
        try {
            return MysqlFirst.instance().insert(tableName, valueMap, true);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } catch (SqlNotFormatException e) {
            log.error(e.getMessage(), e);
        }
        return -1;
    }

    /**
     * 自动提交
     *
     * @param valueMap
     * @return
     */
    public static long insertAndGetKey(String tableName, Map<String, Object> valueMap) {
        try {
            return MysqlFirst.instance().insertAndGetKey(tableName, valueMap, true);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } catch (SqlNotFormatException e) {
            log.error(e.getMessage(), e);
        }
        return -1;
    }

    /**
     * @param valueMap
     * @param commit   为false必须手动提交
     * @return
     * @throws SQLException
     */
    public static int insert(String tableName, Map<String, Object> valueMap, boolean commit) throws SQLException, SqlNotFormatException {
        return MysqlFirst.instance().insert(tableName, valueMap, commit);
    }

    public static int insertAndGetKey(String tableName, Map<String, Object> valueMap, boolean commit) throws SQLException, SqlNotFormatException {
        return MysqlFirst.instance().insertAndGetKey(tableName, valueMap, commit);
    }

    /**
     * 自动提交
     *
     * @param valueMap
     * @return
     */
    public static int update(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) {
        try {
            return MysqlFirst.instance().update(tableName, valueMap, whereMap, true);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } catch (SqlNotFormatException e) {
            log.error(e.getMessage(), e);
        }
        return -1;
    }

    /**
     * @param valueMap
     * @param commit   为false必须手动提交
     * @return
     * @throws SQLException
     */
    public static int update(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap, boolean commit) throws SQLException, SqlNotFormatException {
        return MysqlFirst.instance().update(tableName, valueMap, whereMap, commit);
    }

    /**
     * 自动提交
     *
     * @return
     */
    public static int delete(String tableName, Map<String, Object> whereMap) {
        try {
            return MysqlFirst.instance().delete(tableName, whereMap, true);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } catch (SqlNotFormatException e) {
            log.error(e.getMessage(), e);
        }
        return -1;
    }

    /**
     * @param commit 为false必须手动提交
     * @return
     * @throws SQLException
     */
    public static int delete(String tableName, Map<String, Object> whereMap, boolean commit) throws SQLException, SqlNotFormatException {
        return MysqlFirst.instance().delete(tableName, whereMap, commit);
    }

}
