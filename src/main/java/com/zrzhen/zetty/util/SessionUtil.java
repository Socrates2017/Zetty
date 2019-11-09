package com.zrzhen.zetty.util;


import com.zrzhen.zetty.core.http.Request;

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
