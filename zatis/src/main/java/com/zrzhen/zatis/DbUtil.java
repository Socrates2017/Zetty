package com.zrzhen.zatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作的入口，传入db
 */
public class DbUtil {

    private static final Logger log = LoggerFactory.getLogger(DbUtil.class);


    /********************************************手动事务*****************************************/
    /**
     * 开始一个事务,关闭事务自动提交;连接会放进线程，用完后必须要清理threadlocal。在commit方法和rollback方法中都有清理动作；所以不需要额外显示调用
     *
     * @throws SQLException
     */
    public static void begin(DbSource db) throws SQLException {
        DbConnect.begin(db);
    }


    /**
     * 提交一个事务
     *
     * @throws SQLException
     */
    public static void commit(DbSource db) throws SQLException {
        DbConnect.commit(db);
    }

    /**
     * 回滚一个事务
     */
    public static void rollback(DbSource db) {
        DbConnect.rollback(db);
    }


    /**
     * 可以执行新增，修改，删除
     * 手动提交，操作完成后需要执行commit，或rollback
     *
     * @return 影响的行数
     * @throws SQLException
     * @throws SqlNotFormatException 绑定参数不能为空异常
     */
    public static int operate(DbSource db)
            throws SQLException, SqlNotFormatException {
        return DbOperate.update(db);
    }


    /**
     * 写操作，自动提交
     *
     * @param db
     * @return
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public static int operateAutocommit(DbSource db) {
        return DbOperate.updateAutocommit(db);
    }

    /**
     * 插入并返回主键
     * @param db
     * @return
     * @throws SQLException
     * @throws SqlNotFormatException
     */
    public static Integer insertAndGetKey(DbSource db)
            throws SQLException, SqlNotFormatException {
        return DbOperate.insertAndGetKey(db);
    }

    /**
     * 插入并返回主键，自动提交事务
     * @param db
     * @return
     */
    public static Integer insertAndGetKeyAutocommit(DbSource db) {
        return DbOperate.insertAndGetKeyAutocommit(db);
    }

    /**
     * 查询
     *
     * @param db
     * @return
     */
    public static List<Map<String, Object>> query(DbSource db) {
        return DbSelect.query(db);
    }


    /**
     * 查询数量
     * @param db
     * @return
     */
    public static int count(DbSource db) {
        return DbSelect.count(db);
    }

}
