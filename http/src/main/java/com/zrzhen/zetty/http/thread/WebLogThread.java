package com.zrzhen.zetty.http.thread;

import com.zrzhen.zetty.http.dao.WebLogDao;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 * <p>
 * 通过异步线程持久化登陆日志
 */
public class WebLogThread implements Runnable {

    private Long userId;
    private String sessionid;
    private Long duration;
    private String uri;
    private String host;
    private String userAgent;

    public WebLogThread(Long userId, String sessionid, Long duration, String uri, String host, String userAgent) {
        this.userId = userId;
        this.sessionid = sessionid;
        this.duration = duration;
        this.uri = uri;
        this.host = host;
        this.userAgent = userAgent;
    }

    @Override
    public void run() {
        Map<String, Object> value = new HashMap<>(8);

        value.put("duration", duration);
        value.put("uri", uri);
        value.put("session_id", sessionid);

        value.put("user_agent", userAgent);
        value.put("host", host);
        if (userId != null) {
            value.put("user_id", userId);
        }
        WebLogDao.insert(WebLogDao.tableName, value);

    }
}
