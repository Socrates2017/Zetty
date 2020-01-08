package com.zrzhen.zetty.http.util;


import com.zrzhen.zetty.http.http.http.Request;

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
