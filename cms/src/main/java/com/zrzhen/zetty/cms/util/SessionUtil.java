package com.zrzhen.zetty.cms.util;


import com.zrzhen.zetty.http.http.Request;

/**
 * @author chenanlian
 */
public class SessionUtil {


    /**
     * 创建sessionid
     *
     * @return
     */
    public static String genSessionid() {
        String sessionid = System.currentTimeMillis() + "#" +Request.get().getHost();
        return sessionid;
    }


}
