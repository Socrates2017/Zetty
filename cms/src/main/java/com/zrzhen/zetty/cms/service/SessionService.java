package com.zrzhen.zetty.cms.service;

import com.zrzhen.zetty.cms.dao.UserOpenidDao;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * @author chenanlian
 */
public class SessionService {


    /**
     * 根据系统码和openid查找userid
     *
     * @param sys    系统码
     * @param openid
     * @return
     */
    public static Long getUseridByOpenid(Integer sys, Long openid) {
        Long out = null;
        String userid = UserOpenidDao.selectUserid(sys, openid);
        if (StringUtils.isNotBlank(userid)) {
            out = Long.valueOf(userid);
        }
        return out;
    }

    /**
     * 根据sessionid获得缓存中的openid
     *
     * @param sessionid
     * @return
     */
    public static Long getOpenidBySessionid(String sessionid) {
        /*检查sessionMap是否有sessionid*/
        HashMap map = SessionMapService.sessions.get(sessionid);
        Long openid = null;
        if (map != null) {
            openid = (Long) map.get("openid");
        }
        return openid;
    }

    /**
     * 根据系统码和sessionid获得userid
     *
     * @param sys
     * @param sessionid
     * @return
     */
    public static Long getUseridBySessionid(Integer sys, String sessionid) {
        Long openid = getOpenidBySessionid(sessionid);
        if (null != openid) {
            return getUseridByOpenid(sys, openid);
        } else {
            return null;
        }
    }

    /**
     * 检查sessionid查出来的map中是否带有openid，即是否是在登录状态
     * @param sessionid
     * @return
     */
    public static boolean checkLogin(String sessionid) {
        HashMap map = SessionMapService.sessions.get(sessionid);
        if (map != null && map.containsKey("openid")) {
            return true;
        } else {
            return false;
        }
    }
}
