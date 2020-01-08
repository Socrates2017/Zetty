package com.zrzhen.zetty.http.dao.jdbc;

import com.zrzhen.zetty.http.dao.jdbc.core.CommonSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Druid连接池
 *
 * @author chenanlian
 */
public class MysqlFirst extends CommonSql {


    private static final Logger log = LoggerFactory.getLogger(MysqlFirst.class);

    private String dataSourcePre = "mysql.first";

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


    @Override
    protected Connection getConnection() {
        try {
            if (dataSource == null) {
                dataSource = getDataSource(dataSourcePre);
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public synchronized Connection getConnection2() {
        // 先从线程局部变量（看成一个容器）中取
        Connection conn = connectionHoders.get();
        try {
            if (conn == null || conn.isClosed()) {
                conn = getConnection();
                // 以当前线程对象作为key,以conn作为value放到一个HashMap里面
                connectionHoders.set(conn);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return conn;
    }

}
