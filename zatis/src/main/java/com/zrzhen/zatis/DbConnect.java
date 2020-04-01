package com.zrzhen.zatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 封装数据库连接的相关方法，都是静态方法，不对外公开的类
 */
public class DbConnect {

    private static final Logger log = LoggerFactory.getLogger(DbConnect.class);


    /********************************************手动事务*****************************************/
    /**
     * 开始一个事务,关闭事务自动提交;连接会放进线程，用完后必须要清理threadlocal。
     * 在commit方法和rollback方法中都有清理动作；所以不需要额外显示调用
     *
     * @throws SQLException
     */
    protected static void begin(DbSource db) throws SQLException {
        Connection conn = getConnectionAndSetThread(db);
        conn.setAutoCommit(false);
    }


    /**
     * 提交一个事务
     *
     * @throws SQLException
     */
    protected static void commit(DbSource db) throws SQLException {
        Connection conn = getConnectionInThread(db);
        conn.commit();
        closeConnectionInThread(db);
    }

    /**
     * 回滚一个事务
     */
    protected static void rollback(DbSource db) {
        try {
            Connection conn = getConnectionInThread(db);
            conn.rollback();
            closeConnectionInThread(db);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 关闭线程中的连接
     *
     * @param db
     */
    protected static void closeConnectionInThread(DbSource db) {
        db.closeConnectionInThread();
    }


    /**
     * 从thread local中获得连接
     *
     * @param db
     * @return
     */
    protected static Connection getConnectionInThread(DbSource db) {
        return db.getConnectionInThread();
    }


    /**
     * 获取一个连接，并放入thread local中；在开启手动事务的时候从此处获取
     *
     * @param db
     * @return
     */
    protected static Connection getConnectionAndSetThread(DbSource db) {
        return db.getConnectionAndSetThread();
    }


    /***********************************自动事务***********************************************/
    /**
     * 从连接池中获取连接；如果不开启手动提交事务，则从此处获取
     *
     * @param db
     * @return
     */
    protected static Connection getConnectionFromPool(DbSource db) {
        DruidDataSource dataSource = db.getDataSource();
        /**
         * 如果连接池为空，则创建
         */
        if (dataSource == null) {
            synchronized (DbConnect.class) {
                if (dataSource == null) {//利用双检锁，避免创建多个数据源
                    dataSource = getDataSource(db);
                    db.setDataSource(dataSource);
                }
            }
        }
        try {
            Connection conn = dataSource.getConnection();
            return conn;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 获取连接池
     *
     * @param db
     * @return
     */
    protected static DruidDataSource getDataSource(DbSource db) {
        return getDataSource(db.getDriver(), db.getUrl(), db.getUser(), db.getPassword(),
                db.getInitialSize(), db.getMaxActive(), db.getMinIdle(), db.getMaxWait(), true);
    }

    /**
     * 获取连接池
     *
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
    protected static DruidDataSource getDataSource(String driver, String url, String user, String password,
                                                   int initialSize, int maxActive, int minIdle, int maxWait,
                                                   boolean keepAlive) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setKeepAlive(keepAlive);
        dataSource.setValidationQuery("SELECT 1");//SQL查询,用来验证从连接池取出的连接,在将连接返回给调用者之前.如果指定, 则查询必须是一个SQL SELECT并且必须返回至少一行记录
        dataSource.setTestOnBorrow(false);//指明是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个.
        dataSource.setTestOnReturn(false);//指明是否在归还到池中前进行检验  注意: 设置为true后如果要生效,validationQuery参数必须设置为非空字符串
        dataSource.setTestWhileIdle(true);//指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败, 则连接将被从池中去除.
        return dataSource;
    }


}
