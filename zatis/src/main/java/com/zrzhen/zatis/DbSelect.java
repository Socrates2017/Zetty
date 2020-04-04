package com.zrzhen.zatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * 封装查询方法
 */
public class DbSelect {

    private static final Logger log = LoggerFactory.getLogger(DbSelect.class);

    /**
     * 查询
     *
     * @param db
     * @return
     */
    protected static List<Map<String, Object>> query(DbSource db, DbSql dbSql) {
        if (db.isUseConnectPool()) {
            return queryWithPool(db, dbSql);
        } else {
            return queryWithOutPool(db, dbSql);
        }
    }


    /**
     * 返回一个数量
     *
     * @param db
     * @return
     */
    protected static Integer count(DbSource db, DbSql dbSql) {
        int rowCount = -1;

        String sql = dbSql.getSql();
        Object[] bindArgs = dbSql.getBindArgs();

        long startTime = System.currentTimeMillis();

        if (db.isUseConnectPool()) {
            Connection conn = DbConnect.getConnectionFromPool(db);
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                preparedStatement = conn.prepareStatement(sql);
                if (bindArgs != null) {
                    for (int i = 0; i < bindArgs.length; i++) {
                        preparedStatement.setObject(i + 1, bindArgs[i]);
                    }
                }
                log.debug(DbConvert.sqlStatement(sql, bindArgs));
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    rowCount = resultSet.getInt(1);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if (resultSet != null) {
                    try {
                        resultSet.close();
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
        } else {
            try {
                Class.forName(db.getDriver());
                Connection conn = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    preparedStatement = conn.prepareStatement(sql);
                    if (bindArgs != null) {
                        for (int i = 0; i < bindArgs.length; i++) {
                            preparedStatement.setObject(i + 1, bindArgs[i]);
                        }
                    }
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        rowCount = resultSet.getInt(1);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        log.debug("Sql cost time:{} ms. sql:\n{}", System.currentTimeMillis() - startTime, DbConvert.sqlStatement(sql, bindArgs));

        return rowCount;
    }

    /**
     * 有连接池的查询
     *
     * @param db
     * @return
     */
    private static List<Map<String, Object>> queryWithPool(DbSource db, DbSql dbSql) {
        Connection conn = DbConnect.getConnectionFromPool(db);
        try {
            if (conn == null) {
                throw new DbException("Get connection fail");
            } else {
                return queryByConnection(conn, dbSql.getSql(), dbSql.getBindArgs());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 无连接池查询
     *
     * @param db
     * @return
     */
    private static List<Map<String, Object>> queryWithOutPool(DbSource db, DbSql dbSql) {
        try {
            Class.forName(db.getDriver());
            Connection conn = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
            return queryByConnection(conn, dbSql.getSql(), dbSql.getBindArgs());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 通过sql查询数据,
     * 慎用，如果传入的参数直接进行拼接，会有sql注入问题
     *
     * @param sql
     * @return 查询的数据集合
     * @throws SQLException
     */
    private static List<Map<String, Object>> query(Connection conn, String sql) throws SQLException {
        return executeQuery(conn, sql, null);
    }


    /**
     * 查询
     *
     * @param conn
     * @param sql
     * @param bindArgs
     * @return
     * @throws SQLException
     */
    private static List<Map<String, Object>> queryByConnection(Connection conn, String sql, Object[] bindArgs) throws SQLException {
        return executeQuery(conn, sql, bindArgs);
    }


    /**
     * @param sql      sql语句
     * @param bindArgs 绑定的参数
     * @return
     * @throws SQLException
     */
    private static List<Map<String, Object>> executeQuery(Connection conn, String sql, Object[] bindArgs) throws SQLException {
        List<Map<String, Object>> datas;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        long startTime = System.currentTimeMillis();
        try {
            preparedStatement = conn.prepareStatement(sql);
            if (bindArgs != null) {
                for (int i = 0; i < bindArgs.length; i++) {
                    preparedStatement.setObject(i + 1, bindArgs[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            datas = DbConvert.getDatas(resultSet);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
        log.debug("Sql cost time:{} ms. sql:\n{}", System.currentTimeMillis() - startTime, DbConvert.sqlStatement(sql, bindArgs));
        return datas;
    }


}
