package com.zrzhen.sqlgraph.dao.jdbc.core;

import com.alibaba.druid.pool.DruidDataSource;
import com.sun.istack.internal.Nullable;
import com.zrzhen.zetty.http.util.ProUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Druid连接池
 *
 * @author chenanlian
 */
public abstract class CommonSql {

    private static final Logger log = LoggerFactory.getLogger(CommonSql.class);

    protected static ThreadLocal<Connection> connectionHoders = new ThreadLocal<>();

    protected DruidDataSource dataSource = null;

    /**
     * @param dataSourcePre
     * @return
     */
    public DruidDataSource getDataSource(String dataSourcePre) {
        String driver = ProUtil.getString(dataSourcePre + ".driver");
        String url = ProUtil.getString(dataSourcePre + ".url");
        String user = ProUtil.getString(dataSourcePre + ".user", "root");
        String password = ProUtil.getString(dataSourcePre + ".password", "root");
        Integer initialSize = ProUtil.getInteger(dataSourcePre + ".initialSize", 10);
        Integer maxActive = ProUtil.getInteger(dataSourcePre + ".maxActive", 1000);
        Integer minIdle = ProUtil.getInteger(dataSourcePre + ".minIdle", 20);
        Integer maxWait = ProUtil.getInteger(dataSourcePre + ".maxWait", 6000);
        return getDataSource(driver, url, user, password, initialSize, maxActive, minIdle, maxWait, true);
    }

    /**
     * @param driver
     * @param url
     * @param user
     * @param password
     * @param initialSize
     * @param maxActive
     * @param minIdle
     * @param maxWait
     * @param keepAlive
     * @return
     */
    public DruidDataSource getDataSource(String driver, String url, String user, String password,
                                         int initialSize, int maxActive, int minIdle, int maxWait,
                                         boolean keepAlive) {
        if (dataSource == null) {
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            dataSource.setInitialSize(initialSize);
            dataSource.setMaxActive(maxActive);
            dataSource.setMinIdle(minIdle);
            dataSource.setMaxWait(maxWait);
            dataSource.setKeepAlive(keepAlive);
            dataSource.setValidationQuery("SELECT 1");
            dataSource.setTestOnBorrow(false);
            dataSource.setTestOnReturn(false);
            dataSource.setTestWhileIdle(true);
        }
        return dataSource;
    }


    /**
     * 获取链接，自动提交
     *
     * @return
     */
    protected abstract Connection getConnection();

    /**
     * 获取链接，手动提交事务
     *
     * @return
     */
    protected abstract Connection getConnection2();


    /**
     * 关闭ThreadLocal中的链接
     */
    public static void closeConnectionInThread() {
        // 以当前线程对象作为key，从HashMap中取对应的value
        Connection conn = connectionHoders.get();
        if (conn != null) {
            try {
                /*清空容器*/
                connectionHoders.remove();
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 获取ThreadLocal中的链接
     *
     * @return
     */
    public static Connection getConnectionInThread() {
        return connectionHoders.get();
    }


    /**
     * 开始一个事务
     *
     * @throws SQLException
     */
    public void begin() throws SQLException {
        Connection conn = getConnection2();
        conn.setAutoCommit(false);
    }

    /**
     * 提交一个事务
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        Connection conn = getConnectionInThread();
        conn.commit();
        closeConnectionInThread();
    }

    /**
     * 回滚一个事务
     */
    public void rollback() {
        try {
            Connection conn = getConnectionInThread();
            conn.rollback();
            closeConnectionInThread();
        } catch (SQLException e) {
            log.error("数据库回滚错误:", e);
        }
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
        return executeUpdate(sql.toString(), bindArgs, commit);
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
        return executeUpdateAndGetKey(sql.toString(), bindArgs, commit);
    }

    /**
     * 插入操作
     *
     * @param tableName 要插入的数据库的表名
     * @param datas     插入数据表中key为列名和value为列对应的值的Map对象的List集合
     * @param commit    是否自动提交
     * @return 影响的行数
     * @throws SQLException
     */
    public int insertAll(String tableName, List<Map<String, Object>> datas, boolean commit) throws SQLException {
        /**影响的行数**/
        int affectRowCount = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            /**从数据库连接池中获取数据库连接**/
            connection = getConnection2();

            Map<String, Object> valueMap = datas.get(0);
            /**获取数据库插入的Map的键值对的值**/
            Set<String> keySet = valueMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            /**要插入的字段sql，其实就是用key拼起来的**/
            StringBuilder columnSql = new StringBuilder();
            /**要插入的字段值，其实就是？**/
            StringBuilder unknownMarkSql = new StringBuilder();
            Object[] keys = new Object[valueMap.size()];
            int i = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                keys[i] = key;
                columnSql.append(i == 0 ? "" : ",");
                columnSql.append(key);

                unknownMarkSql.append(i == 0 ? "" : ",");
                unknownMarkSql.append("?");
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

            /**执行SQL预编译**/

            String sqlStr = sql.toString();
            preparedStatement = connection.prepareStatement(sqlStr);
            /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
            connection.setAutoCommit(false);
            log.debug(sqlStr);
            for (int j = 0; j < datas.size(); j++) {
                for (int k = 0; k < keys.length; k++) {
                    preparedStatement.setObject(k + 1, datas.get(j).get(keys[k]));
                }
                preparedStatement.addBatch();
            }
            int[] arr = preparedStatement.executeBatch();
            if (commit) {
                commit();
            }
            affectRowCount = arr.length;
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            closeConnectionInThread();
        }
        return affectRowCount;
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
        return executeUpdate(sql.toString(), objects.toArray(), commit);
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
        return executeUpdate(sql.toString(), bindArgs, commit);
    }

    /**
     * 可以执行新增，修改，删除
     *
     * @param sql      sql语句
     * @param bindArgs 绑定参数
     * @param commit
     * @return 影响的行数
     * @throws SQLException
     * @throws SqlNotFormatException 绑定参数不能为空异常
     */
    public int executeUpdate(String sql, Object[] bindArgs, boolean commit)
            throws SQLException, SqlNotFormatException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        /**从数据库连接池中获取数据库连接**/
        connection = getConnection2();
        /**执行SQL预编译**/
        preparedStatement = connection.prepareStatement(sql.toString());
        /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
        connection.setAutoCommit(false);
        if (bindArgs != null) {
            /**绑定参数设置sql占位符中的值**/
            for (int i = 0; i < bindArgs.length; i++) {
                preparedStatement.setObject(i + 1, bindArgs[i]);
            }
        } else {
            throw new SqlNotFormatException("危险sql,绑定参数为空！SQL:" + sql);
        }
        /**执行sql**/
        /**影响的行数**/
        int affectRowCount = preparedStatement.executeUpdate();
        if (commit) {
            connection.commit();
        }
        String operate;
        if (sql.toUpperCase().indexOf("DELETE FROM") != -1) {
            operate = "删除";
        } else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
            operate = "新增";
        } else {
            operate = "修改";
        }

        if (log.isDebugEnabled()) {
            log.debug("成功{}了{}行,sql:{}", operate, affectRowCount, getExecSQL(sql, bindArgs));
        }
        return affectRowCount;
    }

    public Integer executeUpdateAndGetKey(String sql, Object[] bindArgs, boolean commit)
            throws SQLException, SqlNotFormatException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        /**从数据库连接池中获取数据库连接**/
        connection = getConnection2();
        /**执行SQL预编译**/
        preparedStatement = connection.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
        /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
        connection.setAutoCommit(false);
        if (bindArgs != null) {
            /**绑定参数设置sql占位符中的值**/
            for (int i = 0; i < bindArgs.length; i++) {
                preparedStatement.setObject(i + 1, bindArgs[i]);
            }
        } else {
            throw new SqlNotFormatException("危险sql,绑定参数为空！SQL:" + sql);
        }
        /**执行sql**/
        preparedStatement.executeUpdate();
        if (commit) {
            connection.commit();
        }

        Integer result=null;
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if(rs.next()){
            result = rs.getInt(1);
        }

        return result;
    }

    /**
     * 数量查询
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public int count(String sql) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rowCount = -1;
        try {
            /**获取数据库连接池中的连接**/
            connection = getConnection2();
            preparedStatement = connection.prepareStatement(sql);
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1);
            }
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            closeConnectionInThread();
        }
        return rowCount;
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
        return executeQuery(sql, null);
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
        List<Map<String, Object>> datas = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            /**获取数据库连接池中的连接**/
            connection = getConnection2();
            preparedStatement = connection.prepareStatement(sql);
            if (bindArgs != null) {
                /**设置sql占位符中的值**/
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            //log.debug(getExecSQL(sql, bindArgs));
            /**执行sql语句，获取结果集**/
            resultSet = preparedStatement.executeQuery();
            datas = getDatas(resultSet);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            closeConnectionInThread();
        }
        return datas;
    }


    /**
     * 将结果集对象封装成List<Map<String, Object>> 对象
     *
     * @param resultSet 结果多想
     * @return 结果的封装
     * @throws SQLException
     */
    public List<Map<String, Object>> getDatas(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> datas = new ArrayList<>();
        /**获取结果集的数据结构对象**/
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowMap.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            datas.add(rowMap);
        }
        return datas;
    }


    /**
     * Build an SQL query string from the given clauses.
     *
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param tables   The table names to compile the query against.
     * @param columns  A list of which columns to return. Passing null will
     *                 return all columns, which is discouraged to prevent reading
     *                 data from storage that isn't going to be used.
     * @param where    A filter declaring which rows to return, formatted as an SQL
     *                 WHERE clause (excluding the WHERE itself). Passing null will
     *                 return all rows for the given URL.
     * @param groupBy  A filter declaring how to group rows, formatted as an SQL
     *                 GROUP BY clause (excluding the GROUP BY itself). Passing null
     *                 will cause the rows to not be grouped.
     * @param having   A filter declare which row groups to include in the cursor,
     *                 if row grouping is being used, formatted as an SQL HAVING
     *                 clause (excluding the HAVING itself). Passing null will cause
     *                 all row groups to be included, and is required when row
     *                 grouping is not being used.
     * @param orderBy  How to order the rows, formatted as an SQL ORDER BY clause
     *                 (excluding the ORDER BY itself). Passing null will use the
     *                 default sort order, which may be unordered.
     * @param limit    Limits the number of rows returned by the query,
     *                 formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return the SQL query string
     */
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

    /**
     * After the execution of the complete SQL statement, not necessarily the actual implementation of the SQL statement
     *
     * @param sql      SQL statement
     * @param bindArgs Binding parameters
     * @return Replace? SQL statement executed after the
     */
    public String getExecSQL(String sql, Object[] bindArgs) {
        StringBuilder sb = new StringBuilder(sql);
        if (bindArgs != null && bindArgs.length > 0) {
            int index = 0;
            for (int i = 0; i < bindArgs.length; i++) {
                index = sb.indexOf("?", index);
                sb.replace(index, index + 1, String.valueOf(bindArgs[i]));
            }
        }
        return sb.toString();
    }
}

