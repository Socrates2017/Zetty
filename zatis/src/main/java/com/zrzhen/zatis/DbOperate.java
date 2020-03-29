package com.zrzhen.zatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * 封装更新、删除、插入等操作
 */
public class DbOperate {

    private static final Logger log = LoggerFactory.getLogger(DbOperate.class);


    /**
     * 可以执行新增，修改，删除
     * 手动提交，操作完成后需要执行commit，或rollback
     *
     * @return 影响的行数
     * @throws SQLException
     * @throws SqlNotFormatException 绑定参数不能为空异常
     */
    public static int operate(DbSource db, DbSql dbSql)
            throws SQLException, SqlNotFormatException {
        String sql = dbSql.getSql();
        Object[] bindArgs = dbSql.getBindArgs();

        Connection connection = DbConnect.getConnectionAndSetThread(db);
        connection.setAutoCommit(false);

        /**执行SQL预编译**/
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
        String operate;
        if (sql.toUpperCase().indexOf("DELETE FROM") != -1) {
            operate = "删除";
        } else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
            operate = "新增";
        } else {
            operate = "修改";
        }

        if (log.isDebugEnabled()) {
            log.debug("成功{}了{}行,sql:\n{}", operate, affectRowCount, DbConvert.sqlStatement(sql, bindArgs));
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        return affectRowCount;
    }


    /**
     * 写操作，自动提交
     *
     * @param db
     * @return
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public static Integer operateAutocommit(DbSource db, DbSql dbSql) {
        String sql = dbSql.getSql();
        Object[] bindArgs = dbSql.getBindArgs();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Integer affectRowCount = null;
        try {

            connection = DbConnect.getConnectionFromPool(db);
            connection.setAutoCommit(true);

            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql);
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
            affectRowCount = preparedStatement.executeUpdate();

            String operate;
            if (sql.toUpperCase().indexOf("DELETE FROM") != -1) {
                operate = "删除";
            } else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
                operate = "新增";
            } else {
                operate = "修改";
            }

            if (log.isDebugEnabled()) {
                log.debug("成功{}了{}行,sql:\n{}", operate, affectRowCount, DbConvert.sqlStatement(sql, bindArgs));
            }
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            log.error(e.getMessage(), e);

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return affectRowCount;
    }

    protected static Integer insertAndGetKeyAutocommit(DbSource db, DbSql dbSql) {

        String sql = dbSql.getSql();
        Object[] bindArgs = dbSql.getBindArgs();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Integer result = null;
        try {

            connection = DbConnect.getConnectionFromPool(db);
            connection.setAutoCommit(true);

            /**执行SQL预编译**/
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
            int row = preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }

            if (log.isDebugEnabled()) {
                log.debug("成功插入了{}行,自增id为：{};sql:\n{}", row, result, DbConvert.sqlStatement(sql, bindArgs));
            }
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            log.error(e.getMessage(), e);

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 可以执行新增，修改，删除
     * 手动提交，操作完成后需要执行commit，或rollback
     *
     * @return 影响的行数
     * @throws SQLException
     * @throws SqlNotFormatException 绑定参数不能为空异常
     */
    protected static Integer insertAndGetKey(DbSource db, DbSql dbSql)
            throws SQLException, SqlNotFormatException {
        String sql = dbSql.getSql();
        Object[] bindArgs = dbSql.getBindArgs();

        Connection connection = DbConnect.getConnectionAndSetThread(db);
        connection.setAutoCommit(false);

        /**执行SQL预编译**/
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
        int row = preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        Integer result = null;
        if (resultSet.next()) {
            result = resultSet.getInt(1);
        }

        if (log.isDebugEnabled()) {
            log.debug("成功插入了{}行,自增id为：{};sql:\n{}", row, result, DbConvert.sqlStatement(sql, bindArgs));
        }

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        return result;
    }


    /**
     * 执行数据库插入操作
     *
     * @param valueMap  插入数据表中key为列名和value为列对应的值的Map对象
     * @param tableName 要插入的数据库的表名
     * @return 影响的行数
     * @throws SQLException SQL异常
     */
    public static int insert(DbSource db, String tableName, Map<String, Object> valueMap, boolean commit)
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
            return operateAutocommit(db, dbSql);
        } else {
            return operate(db, dbSql);
        }
    }

    public static int insertAndGetKey(DbSource db, String tableName, Map<String, Object> valueMap, boolean commit)
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
            return insertAndGetKeyAutocommit(db, dbSql);
        } else {
            return insertAndGetKey(db, dbSql);
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
    public static int update(DbSource db, String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap, boolean commit)
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
            return operateAutocommit(db, dbSql);
        } else {
            return operate(db, dbSql);
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
    public static int delete(DbSource db, String tableName, Map<String, Object> whereMap, boolean commit)
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
            return operateAutocommit(db, dbSql);
        } else {
            return operate(db, dbSql);
        }
    }
}
