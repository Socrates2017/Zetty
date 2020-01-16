package com.zrzhen.zetty.cms.service;

import com.zrzhen.zetty.cms.util.HeaderUtil;
import com.zrzhen.zetty.cms.util.SessionUtil;
import com.zrzhen.zetty.common.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenanlian
 * <p>
 * 用来存储session，可用数据库、redis等替代
 */
public class SessionMapService {

    private static final Logger log = LoggerFactory.getLogger(SessionMapService.class);


    /**
     * key:sessionid
     * value:HashMap
     * <p>
     * HashMap
     * key:openid
     */
    public static ConcurrentHashMap<String, HashMap> sessions = new ConcurrentHashMap();

    /**
     * 获取sessionMap中的map,如果为空则新建
     *
     * @param sessionid
     * @return
     */
    public static HashMap getHashMap(String sessionid) {
        HashMap map = SessionMapService.sessions.get(sessionid);
        if (map == null) {
            map = new HashMap();
        }
        return map;
    }

    /**
     * 获得客户端传来的session，如无，则生成
     *
     * @return
     */
    public static String getSession() {
        String sessionid = HeaderUtil.getSession();
        if (StringUtils.isBlank(sessionid)) {
            sessionid = SessionUtil.genSessionid();
            /*添加sessionid给cookie*/
            HeaderUtil.setSession(sessionid);
        }
        return sessionid;
    }

    /**
     * sso登录
     *
     * @param sessionid
     * @param openid
     * @return code 0表示登录成功
     */
    public static boolean addSession(String sessionid, Long openid) {
        try {
            HashMap map = SessionMapService.sessions.get(sessionid);
            if (map == null) {
                map = new HashMap();
            }
            map.put("openid", openid);
            SessionMapService.sessions.put(sessionid, map);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 删除服务端session，置于未登录状态
     *
     * @param sessionid
     * @return
     */
    public static boolean rmSession(String sessionid) {
        try {
            HashMap map = SessionMapService.sessions.get(sessionid);
            if (map != null) {
                map.remove("openid");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void cleanMap() {

        ConcurrentHashMap<String, HashMap> sessions = SessionMapService.sessions;

        for (Iterator<Map.Entry<String, HashMap>> it = sessions.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, HashMap> item = it.next();
            String key = item.getKey();
            Long tamstamp = Long.valueOf(key.substring(0, key.indexOf("#")));
            Long distance = TimeUtil.timestampDistance(tamstamp, System.currentTimeMillis());

            if (distance > 60 * 24 * 2) {
                it.remove();
                log.info("清理的session：{}", item.getKey());
            }

        }
        for (Map.Entry<String, HashMap> item : sessions.entrySet()) {
            log.info("清理session后剩下：{}", item.getKey());
        }
    }
}
