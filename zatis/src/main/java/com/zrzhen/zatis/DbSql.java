package com.zrzhen.zatis;

public class DbSql {

    /**
     * 执行的sql
     */
    String sql;
    /**
     * 绑定参数；使用绑定参数防御sql注入
     */
    Object[] bindArgs;

    public DbSql(String sql) {
        this.sql = sql;
    }

    public DbSql(String sql, Object[] bindArgs) {
        this.sql = sql;
        this.bindArgs = bindArgs;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getBindArgs() {
        return bindArgs;
    }

    public void setBindArgs(Object[] bindArgs) {
        this.bindArgs = bindArgs;
    }
}
