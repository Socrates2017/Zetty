package com.zrzhen.zetty.cms.dao.jdbc;

import com.zrzhen.zatis.DbSource;
import com.zrzhen.zatis.DbSql;
import com.zrzhen.zatis.SqlNotFormatException;
import com.zrzhen.zetty.cms.dao.jdbc.core.DbConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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

        return dbSource.insert(tableName, valueMap, commit);
    }

    public int insertAndGetKey(String tableName, Map<String, Object> valueMap, boolean commit)
            throws SQLException, SqlNotFormatException {
        return dbSource.insertAndGetKey(tableName, valueMap, commit);
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
        return dbSource.update(tableName, valueMap, whereMap, commit);

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
        return dbSource.delete(tableName, whereMap, commit);
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
        return dbSource.getList(dbSql);
    }


    /**
     * @param tableName
     * @param whereMap
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> query(String tableName, Map<String, Object> whereMap) throws Exception {

        return dbSource.query(tableName, whereMap);
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
                                           String[] whereArgs) throws SQLException {
        return dbSource.query(tableName, whereClause, whereArgs);
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
        return dbSource.query(tableName, distinct, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

    }


    /**
     * @param sql
     * @param bindArgs 绑定的参数
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> executeQuery(String sql, Object[] bindArgs) {
        return dbSource.getList(sql, bindArgs);
    }

}
