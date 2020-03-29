package com.zrzhen.zatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据源对象
 */
public class DbSource {

    private static final Logger log = LoggerFactory.getLogger(DbSource.class);


    /**
     * 驱动
     */
    String driver;
    /**
     * 连接地址
     */
    String url;
    /**
     * 账户名
     */
    String user;
    /**
     * 密码
     */
    String password;


    /**
     * 是否使用连接池，true则连接，false不连接；当设为true时，后面几个参数需要赋值
     */
    boolean useConnectPool;

    /**
     * 初始化连接池大小
     */
    Integer initialSize;
    /**
     * 最大连接数
     */
    Integer maxActive;
    /**
     * 最小空闲时刻连接数
     */
    Integer minIdle;
    /**
     * 不活跃连接池保持时间，超过该时长则断开
     */
    Integer maxWait;

    /**
     * 阿里德鲁伊数据库连接池
     */
    DruidDataSource dataSource;


    private ThreadLocal<Connection> THREADLOCAL_CONNECTION = new ThreadLocal<>();


    /**
     * 获取连接
     * 先从thread local 中获取；无则从连接池中获取，并放入thread local
     *
     * @return
     */
    public /*synchronized*/ Connection getConnectionAndSetThread() {
        // 先从线程局部变量（看成一个容器）中取
        Connection conn = getConnectionInThread();
        try {
            if (conn == null || conn.isClosed()) {
                conn = DbConnect.getConnectionFromPool(this);
                // 以当前线程对象作为key,以conn作为value放到一个HashMap里面
                THREADLOCAL_CONNECTION.set(conn);

            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return conn;
    }

    /**
     * 获取ThreadLocal中的链接
     *
     * @return
     */
    public Connection getConnectionInThread() {
        return THREADLOCAL_CONNECTION.get();
    }

    /**
     * 关闭ThreadLocal中的链接
     */
    public void closeConnectionInThread() {
        // 以当前线程对象作为key，从HashMap中取对应的value
        Connection conn = THREADLOCAL_CONNECTION.get();
        if (conn != null) {
            try {
                /*清空容器*/
                THREADLOCAL_CONNECTION.remove();
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    /********************************************手动事务*****************************************/
    /**
     * 开始一个事务,关闭事务自动提交;连接会放进线程，用完后必须要清理threadlocal。在commit方法和rollback方法中都有清理动作；所以不需要额外显示调用
     *
     * @throws SQLException
     */
    public void begin() throws SQLException {
        DbConnect.begin(this);
    }


    /**
     * 提交一个事务
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        DbConnect.commit(this);
    }

    /**
     * 回滚一个事务
     */
    public void rollback() {
        DbConnect.rollback(this);
    }


    /**
     * 可以执行新增，修改，删除
     * 手动提交，操作完成后需要执行commit，或rollback
     *
     * @return 影响的行数
     * @throws SQLException
     * @throws SqlNotFormatException 绑定参数不能为空异常
     */
    public int operate(DbSql dbSql)
            throws SQLException, SqlNotFormatException {
        return DbOperate.update(this, dbSql);
    }


    /**
     * 写操作，自动提交
     *
     * @return
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public int operateAutocommit(DbSql dbSql) {
        return DbOperate.updateAutocommit(this, dbSql);
    }

    /**
     * 插入并返回主键
     *
     * @return
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public Integer insertAndGetKey(DbSql dbSql)
            throws SQLException, SqlNotFormatException {
        return DbOperate.insertAndGetKey(this, dbSql);
    }

    /**
     * 插入并返回主键，自动提交事务
     *
     * @return
     */
    public Integer insertAndGetKeyAutocommit(DbSql dbSql) {
        return DbOperate.insertAndGetKeyAutocommit(this, dbSql);
    }

    /**
     * 查询
     *
     * @return
     */
    public List<Map<String, Object>> query(DbSql dbSql) {
        return DbSelect.query(this, dbSql);
    }


    /**
     * 查询数量
     *
     * @return
     */
    public int count(DbSql dbSql) {
        return DbSelect.count(this, dbSql);
    }

    /*********************/

    public DbSource(String driver, String url, String user, String password,
                    boolean useConnectPool, Integer initialSize, Integer maxActive,
                    Integer minIdle, Integer maxWait) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.useConnectPool = useConnectPool;
        this.initialSize = initialSize;
        this.maxActive = maxActive;
        this.minIdle = minIdle;
        this.maxWait = maxWait;
    }

    public DbSource(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }


    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    /**
     * 获取数据源时加锁，避免创建多个同源数据源
     *
     * @return
     */
    public synchronized DruidDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DruidDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isUseConnectPool() {
        return useConnectPool;
    }

    public void setUseConnectPool(boolean useConnectPool) {
        this.useConnectPool = useConnectPool;
    }


    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
