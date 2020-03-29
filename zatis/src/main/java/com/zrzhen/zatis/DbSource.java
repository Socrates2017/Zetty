package com.zrzhen.zatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据源对象
 * <p>
 * 使用方式
 * 创建一个数据源，可设置为静态的；
 * 操作的参数分为三类；
 * 1.SQL字符串与绑定的参数数组
 * 2.由SQL字符串与绑定的参数数组封装而成的Dbsql对象
 * 3.由表名、查询字段、查询参数等组成的参数
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
    boolean useConnectPool = true;

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

    /********************************写操作************************/

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
        return DbOperate.operate(this, dbSql);
    }

    public int operate(String sql, Object[] bindArgs)
            throws SQLException, SqlNotFormatException {
        return DbOperate.operate(this, new DbSql(sql, bindArgs));
    }

    /**
     * 写操作，自动提交
     *
     * @return
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public int operateAutocommit(DbSql dbSql) {
        return DbOperate.operateAutocommit(this, dbSql);
    }

    public int operateAutocommit(String sql, Object[] bindArgs) {
        return DbOperate.operateAutocommit(this, new DbSql(sql, bindArgs));
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

    public Integer insertAndGetKey(String sql, Object[] bindArgs)
            throws SQLException, SqlNotFormatException {
        return DbOperate.insertAndGetKey(this, new DbSql(sql, bindArgs));
    }

    /**
     * 插入并返回主键，自动提交事务
     *
     * @return
     */
    public Integer insertAndGetKeyAutocommit(DbSql dbSql) {
        return DbOperate.insertAndGetKeyAutocommit(this, dbSql);
    }

    public Integer insertAndGetKeyAutocommit(String sql, Object[] bindArgs) {
        return DbOperate.insertAndGetKeyAutocommit(this, new DbSql(sql, bindArgs));
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
        return DbOperate.insert(this, tableName, valueMap, commit);
    }

    public int insertAndGetKey(String tableName, Map<String, Object> valueMap, boolean commit)
            throws SQLException, SqlNotFormatException {
        return DbOperate.insertAndGetKey(this, tableName, valueMap, commit);
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
        return DbOperate.update(this, tableName, valueMap, whereMap, commit);
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
        return DbOperate.delete(this, tableName, whereMap, commit);
    }

    /***************************查询********************************/

    public List<Map<String, Object>> getList(DbSql dbSql) {
        return DbSelect.query(this, dbSql);
    }

    /**
     * 获取结果列表
     *
     * @param sql
     * @param bindArgs
     * @return
     */
    public List<Map<String, Object>> getList(String sql, Object[] bindArgs) {
        return getList(new DbSql(sql, bindArgs));
    }

    public Map<String, Object> getOne(DbSql dbSql) {
        List<Map<String, Object>> out = getList(dbSql);
        if (out != null && out.size() > 0) {
            return out.get(0);
        } else {
            return null;
        }
    }


    /**
     * 获取一个结果
     *
     * @param sql
     * @param bindArgs
     * @return
     */
    public Map<String, Object> getOne(String sql, Object[] bindArgs) {
        return getOne(new DbSql(sql, bindArgs));
    }


    /**
     * 查询数量
     *
     * @return
     */
    public int count(DbSql dbSql) {
        return DbSelect.count(this, dbSql);
    }

    public int count(String sql, Object[] bindArgs) {
        return DbSelect.count(this, new DbSql(sql, bindArgs));
    }

    /**
     * @param tableName
     * @param whereMap
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> query(String tableName, Map<String, Object> whereMap) {
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
     * @param tableName 表名
     * @param distinct  去重
     * @param columns   要查询的列名
     * @param selection where条件
     * @param bindArgs  where条件中占位符中的值
     * @param groupBy   分组
     * @param having    筛选
     * @param orderBy   排序
     * @param limit     分页
     */
    public List<Map<String, Object>> query(String tableName,
                                           boolean distinct,
                                           String[] columns,
                                           String selection,
                                           Object[] bindArgs,
                                           String groupBy,
                                           String having,
                                           String orderBy,
                                           String limit) {
        String sql = DbConvert.buildQueryString(distinct, tableName, columns, selection, groupBy, having, orderBy, limit);
        return this.getList(sql, bindArgs);

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
