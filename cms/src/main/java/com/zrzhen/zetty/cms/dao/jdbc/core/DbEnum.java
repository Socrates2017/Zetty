package com.zrzhen.zetty.cms.dao.jdbc.core;


import com.zrzhen.zatis.DbSource;

/**
 * 数据源枚举
 */
public enum DbEnum {

    CMS("cms", new DbSource(
            "com.mysql.cj.jdbc.Driver",
            DbConstant.CMS_URL,
            DbConstant.CMS_USER,
            DbConstant.CMS_PASSWORD,
            true,
            DbConstant.CMS_INITIALSIZE,
            DbConstant.CMS_MAXACTIVE,
            DbConstant.CMS_MINIDLE,
            DbConstant.CMS_MAXWAIT
    ));

    private String name;
    private DbSource db;

    public static DbSource getDbByName(String name) {
        DbEnum[] dbEnums = DbEnum.values();
        for (int i = 0; i < dbEnums.length; i++) {
            if (dbEnums[i].getName().equals(name)) {
                return dbEnums[i].getDb();
            }
        }
        return null;
    }


    DbEnum(String name, DbSource db) {
        this.name = name;
        this.db = db;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DbSource getDb() {
        return db;
    }

    public void setDb(DbSource db) {
        this.db = db;
    }
}
