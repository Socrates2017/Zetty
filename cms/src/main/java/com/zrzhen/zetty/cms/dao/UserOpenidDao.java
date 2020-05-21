package com.zrzhen.zetty.cms.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author chenanlian
 */
public class UserOpenidDao extends MysqlFirstBaseDao {

    private static final Logger log = LoggerFactory.getLogger(ArticleDao.class);


    public static final String tableName = "user_openid";


    public static String selectUserid(Integer sysCode, Long openid) {
        String sql = "select userid from `user_openid` where sys_code = ? and openid = ? limit 1";
        Object[] args = new Object[]{sysCode, openid};
        Map<String, Object> out = getOne(sql, args);
        if (out != null) {
            return (String) out.get("userid");
        } else {
            return null;
        }
    }

    public static Long selectOpenidByKey(Integer sysCode, String userid) {
        String sql = "select openid from `user_openid` where sys_code = ? and userid = ? limit 1";
        Object[] args = new Object[]{sysCode, userid};

        Map<String, Object> out = getOne(sql, args);
        if (out != null) {
            return (Long) out.get("openid");
        } else {
            return null;
        }
    }


}
