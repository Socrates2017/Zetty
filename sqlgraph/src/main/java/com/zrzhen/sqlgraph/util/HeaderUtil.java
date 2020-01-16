package com.zrzhen.sqlgraph.util;

import com.zrzhen.zetty.http.http.Cookie;
import com.zrzhen.zetty.http.http.Request;
import com.zrzhen.zetty.http.http.Response;
import com.zrzhen.zetty.http.util.ProUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author chenanlian
 */
public class HeaderUtil {


    public static final String SESSION = "SESSION";
    public static final Long SESSION_EXPIRE = ProUtil.getLong("session.expire");
    public static final Integer SYSCODE = ProUtil.getInteger("Constant.syscode");
    public static final String SYSCODE_STRING = String.valueOf(SYSCODE);
    public static final String DOMAIN = ProUtil.getString("cookie.domain");

    /**
     * 根据cookie key获取value
     *
     * @param name
     * @return
     */
    public static String getCookie(String name) {
        // Encode the cookie.
        Request request = Request.get();
        List<Cookie> cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (name.equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * @param name   key
     * @param value  值
     * @param domain 可以访问该Cookie的域名。如果设置为“.google.com”，则所有以“google.com”结尾的域名都可以访问该Cookie。注意第一个字符必须为“.”
     * @param path   该Cookie的使用路径。如果设置为“/sessionWeb/”，则只有contextPath为“/sessionWeb”的程序可以访问该Cookie。如果设置为“/”，
     *               则本域名下contextPath都可以访问该Cookie。注意最后一个字符必须为“/
     * @param maxAge cookie在浏览器端的有效时长，0表示删除cookie，负数，如-1表示不持久化cookie，浏览器关闭即删除，正数表示在多少秒后失效
     */
    public static void setCookie(String name, String value, String domain, String path, Long maxAge) {
        Cookie cookie = new Cookie(name, value);
        if (null != maxAge) {
            cookie.setAge(maxAge);
        }
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (StringUtils.isNotBlank(path)) {
            cookie.setPath(path);
        }
        Response.get().getCookies().put(name, cookie);
    }

    /**
     * 新增cookie，如果name相同，则最后设置的那个生效
     * 默认域名、有效时长、路径
     *
     * @param name
     * @param value
     */
    public static void setCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(DOMAIN);
        cookie.setAge(SESSION_EXPIRE);
        Response.get().getCookies().put(name, cookie);
    }



    /**
     * 获得sessionid
     *
     * @return
     */
    public static String getSession() {
        return getCookie(SESSION);
    }

    /**
     * 设置sessionid
     *
     * @param sessionid
     */
    public static void setSession(String sessionid) {
        setCookie(SESSION, sessionid);
    }

    /**
     * 设置前端的用户id
     *
     * @param userid
     */
    public static void setUser(Long userid) {
        setCookie(SYSCODE_STRING, String.valueOf(userid));
    }

    /**
     * 删除cookie，并没有真的删除，仅仅是设置值为空字符串
     *
     * @param name
     */
    public static void removeCookie(String name) {
        Cookie cookie = new Cookie(name);
        cookie.setPath("/");
        cookie.setAge(0);
        Response.get().getCookies().put(name, cookie);
    }

    /**
     * 删除session
     */
    public static void removeSession() {
        removeCookie(SESSION);
    }

    /**
     * 删除cookie中的userid
     */
    public static void removeUser() {
        removeCookie(SYSCODE_STRING);
    }


}
