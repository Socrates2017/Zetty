package com.zrzhen.zetty.cms.dao.jdbc.core;


import com.zrzhen.zetty.http.util.ProUtil;

/**
 * @author chenanlian
 * 获取配置文件中的配置
 */
public class DbConstant {

    private static String CMS_PRE = "mysql.first";

    public static String CMS_URL = ProUtil.getString(CMS_PRE + ".url");
    ;
    public static String CMS_USER = ProUtil.getString(CMS_PRE + ".user", "root");
    public static String CMS_PASSWORD = ProUtil.getString(CMS_PRE + ".password", "root");

    public static Integer CMS_INITIALSIZE = ProUtil.getInteger(CMS_PRE + ".initialSize", 10);
    public static Integer CMS_MAXACTIVE = ProUtil.getInteger(CMS_PRE + ".maxActive", 1000);
    public static Integer CMS_MINIDLE = ProUtil.getInteger(CMS_PRE + ".minIdle", 20);
    public static Integer CMS_MAXWAIT = ProUtil.getInteger(CMS_PRE + ".maxWait", 6000);


}
