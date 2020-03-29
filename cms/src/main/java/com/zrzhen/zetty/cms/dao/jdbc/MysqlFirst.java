package com.zrzhen.zetty.cms.dao.jdbc;

import com.sun.istack.internal.Nullable;
import com.zrzhen.zatis.DbSource;
import com.zrzhen.zatis.DbSql;
import com.zrzhen.zatis.SqlNotFormatException;
import com.zrzhen.zetty.cms.dao.jdbc.core.DbConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Druid连接池
 *
 * @author chenanlian
 */
public class MysqlFirst {

    private static final Logger log = LoggerFactory.getLogger(MysqlFirst.class);

    private DbSource dbSource = new DbSource(
            "com.mysql.cj.jdbc.Driver",
            DbConstant.CMS_URL,
            DbConstant.CMS_USER,
            DbConstant.CMS_PASSWORD,
            true,
            DbConstant.CMS_INITIALSIZE,
            DbConstant.CMS_MAXACTIVE,
            DbConstant.CMS_MINIDLE,
            DbConstant.CMS_MAXWAIT
    );

    /**
     * 单例
     */
    private static MysqlFirst mysqlFirst;

    /**
     * 外部获取单例的方法
     *
     * @return
     */
    public static MysqlFirst instance() {
        if (null == mysqlFirst) {
            mysqlFirst = new MysqlFirst();
        }
        return mysqlFirst;
    }


    /**
     * 开始一个事务
     *
     * @throws SQLException
     */
    public void begin() throws SQLException {
        dbSource.begin();
    }

    /**
     * 提交一个事务
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        dbSource.commit();
    }

    /**
     * 回滚一个事务
     */
    public void rollback() {
        dbSource.rollback();
    }

    /**
     * 关闭ThreadLocal中的链接
     */
    public static void closeConnectionInThread() {

    }


    /**
     * 执行数据库插入操作
     *
     * @param valueMap  插入数据表中key为列名和value为列对应的值的Map对象
     * @param tableName 要插入的数据库的表名
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public int insert(String tableName, Map<String, Object> valueMap, boolean commit)
            throws SQLException, SqlNotFormatException {

        /**获取数据库插入的Map的键值对的值**/
        Set<String> keySet = valueMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        /**要插入的字段sql，其实就是用key拼起来的**/
        StringBuilder columnSql = new StringBuilder();
        /**要插入的字段值，其实就是？**/
        StringBuilder unknownMarkSql = new StringBuilder();
        Object[] bindArgs = new Object[valueMap.size()];
        int i = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(key);

            unknownMarkSql.append(i == 0 ? "" : ",");
            unknownMarkSql.append("?");
            bindArgs[i] = valueMap.get(key);
            i++;
        }
        /**开始拼插入的sql语句**/
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        sql.append(columnSql);
        sql.append(" )  VALUES (");
        sql.append(unknownMarkSql);
        sql.append(" )");


        DbSql dbSql = new DbSql(sql.toString(), bindArgs);
        if (commit) {
            return dbSource.operateAutocommit(dbSql);
        } else {
            return dbSource.operate(dbSql);
        }
    }

    public int insertAndGetKey(String tableName, Map<String, Object> valueMap, boolean commit)
            throws SQLException, SqlNotFormatException {

        /**获取数据库插入的Map的键值对的值**/
        Set<String> keySet = valueMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        /**要插入的字段sql，其实就是用key拼起来的**/
        StringBuilder columnSql = new StringBuilder();
        /**要插入的字段值，其实就是？**/
        StringBuilder unknownMarkSql = new StringBuilder();
        Object[] bindArgs = new Object[valueMap.size()];
        int i = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(key);

            unknownMarkSql.append(i == 0 ? "" : ",");
            unknownMarkSql.append("?");
            bindArgs[i] = valueMap.get(key);
            i++;
        }
        /**开始拼插入的sql语句**/
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        sql.append(columnSql);
        sql.append(" )  VALUES (");
        sql.append(unknownMarkSql);
        sql.append(" )");


        DbSql dbSql = new DbSql(sql.toString(), bindArgs);
        if (commit) {
            return dbSource.insertAndGetKeyAutocommit(dbSql);
        } else {
            return dbSource.insertAndGetKey(dbSql);
        }
    }


    /**
     * @param tableName 表名
     * @param valueMap  要更改的值
     * @param whereMap  条件
     * @param commit    是否自动提交
     * @return 影响的行数
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public int update(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap, boolean commit)
            throws SQLException, SqlNotFormatException {
        /**获取数据库插入的Map的键值对的值**/
        Set<String> keySet = valueMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        /**开始拼插入的sql语句**/
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(tableName);
        sql.append(" SET ");

        /**要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder columnSql = new StringBuilder();
        int i = 0;
        List<Object> objects = new ArrayList<>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            columnSql.append(i == 0 ? "" : ",");
            columnSql.append(key + " = ? ");
            objects.add(valueMap.get(key));
            i++;
        }
        sql.append(columnSql);

        /**更新的条件:要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder whereSql = new StringBuilder();
        int j = 0;
        if (whereMap != null && whereMap.size() > 0) {
            whereSql.append(" WHERE ");
            iterator = whereMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                whereSql.append(j == 0 ? "" : " AND ");
                whereSql.append(key + " = ? ");
                objects.add(whereMap.get(key));
                j++;
            }
            sql.append(whereSql);
        }

        DbSql dbSql = new DbSql(sql.toString(), objects.toArray());
        if (commit) {
            return dbSource.operateAutocommit(dbSql);
        } else {
            return dbSource.operate(dbSql);
        }

    }

    /**
     * @param tableName
     * @param whereMap
     * @param commit
     * @return
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public int delete(String tableName, Map<String, Object> whereMap, boolean commit)
            throws SQLException, SqlNotFormatException {
        /**准备删除的sql语句**/
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(tableName);

        /**更新的条件:要更改的的字段sql，其实就是用key拼起来的**/
        StringBuilder whereSql = new StringBuilder();
        Object[] bindArgs = null;
        if (whereMap != null && whereMap.size() > 0) {
            bindArgs = new Object[whereMap.size()];
            whereSql.append(" WHERE ");
            /**获取数据库插入的Map的键值对的值**/
            Set<String> keySet = whereMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                whereSql.append(i == 0 ? "" : " AND ");
                whereSql.append(key + " = ? ");
                bindArgs[i] = whereMap.get(key);
                i++;
            }
            sql.append(whereSql);
        }

        DbSql dbSql = new DbSql(sql.toString(), bindArgs);
        if (commit) {
            return dbSource.operateAutocommit(dbSql);
        } else {
            return dbSource.operate(dbSql);
        }
    }


    /**
     * 数量查询
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public int count(String sql, Object[] bindArgs) throws SQLException {

        DbSql dbSql = new DbSql(sql, bindArgs);
        return dbSource.count(dbSql);

    }

    /**
     * 通过sql查询数据,
     * 慎用，会有sql注入问题
     *
     * @param sql
     * @return 查询的数据集合
     * @throws SQLException
     */
    public List<Map<String, Object>> query(String sql) throws SQLException {
        DbSql dbSql = new DbSql(sql, null);
        return dbSource.query(dbSql);
    }


    /**
     * @param tableName
     * @param whereMap
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> query(String tableName, Map<String, Object> whereMap) throws Exception {
        String whereClause = "";
        Object[] whereArgs = null;
        if (whereMap != null && whereMap.size() > 0) {
            Iterator<String> iterator = whereMap.keySet().iterator();
            whereArgs = new Object[whereMap.size()];
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                whereClause += (i == 0 ? "" : " AND ");
                whereClause += (key + " = ? ");
                whereArgs[i] = whereMap.get(key);
                i++;
            }
        }
        return query(tableName, false, null, whereClause, whereArgs, null,
                null, null, null);
    }

    /**
     * 执行sql条件参数绑定形式的查询
     *
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> query(String tableName,
                                           String whereClause,
                                           String[] whereArgs)
            throws SQLException {
        return query(tableName, false, null, whereClause, whereArgs, null,
                null, null, null);
    }

    /**
     * 执行全部结构的sql查询
     *
     * @param tableName     表名
     * @param distinct      去重
     * @param columns       要查询的列名
     * @param selection     where条件
     * @param selectionArgs where条件中占位符中的值
     * @param groupBy       分组
     * @param having        筛选
     * @param orderBy       排序
     * @param limit         分页
     */
    public List<Map<String, Object>> query(String tableName,
                                           boolean distinct,
                                           String[] columns,
                                           String selection,
                                           Object[] selectionArgs,
                                           String groupBy,
                                           String having,
                                           String orderBy,
                                           String limit) throws SQLException {
        String sql = buildQueryString(distinct, tableName, columns, selection, groupBy, having, orderBy, limit);
        return executeQuery(sql, selectionArgs);

    }


    /**
     * @param sql
     * @param bindArgs 绑定的参数
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> executeQuery(String sql, Object[] bindArgs) throws SQLException {


        DbSql dbSql = new DbSql(sql, bindArgs);
        return dbSource.query(dbSql);
    }


    public String buildQueryString(
            boolean distinct, String tables, String[] columns, String where,
            String groupBy, String having, String orderBy, String limit) {
        if (isEmpty(groupBy) && !isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(120);

        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            appendColumns(query, columns);
        } else {
            query.append(" * ");
        }
        query.append("FROM ");
        query.append(tables);
        appendClause(query, " WHERE ", where);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);
        return query.toString();
    }

    /**
     * Add the names that are non-null in columns to s, separating
     * them with commas.
     */
    public void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;

        for (int i = 0; i < n; i++) {
            String column = columns[i];

            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(column);
            }
        }
        s.append(' ');
    }

    /**
     * addClause
     *
     * @param s      the add StringBuilder
     * @param name   clauseName
     * @param clause clauseSelection
     */
    public void appendClause(StringBuilder s, String name, String clause) {
        if (!isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * the pattern of limit
     */
    public final Pattern sLimitPattern =
            Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

}
