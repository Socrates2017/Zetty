package com.zrzhen.zetty.im.server;

import com.zrzhen.zetty.net.SocketSession;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenanlian
 */
public class Manager {

    public static ConcurrentHashMap<String, SocketSession> sessions = new ConcurrentHashMap();

    public static ConcurrentHashMap<String, String> loginUser = new ConcurrentHashMap();


    public static void logout(ImSocketSession socketSession) {
        String host = socketSession.getRemoteAddress();
        Manager.sessions.remove(host);
        Iterator<Map.Entry<String, String>> iterator = loginUser.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (Objects.equals(value, host)) {
                loginUser.remove(key);
            }
        }
    }

    public static SocketSession getSocketSessionByUserName(String user) {
        String host = loginUser.get(user);
        if (host == null) {
            return null;
        }

        SocketSession socketSession = sessions.get(host);
        if (socketSession == null) {
            loginUser.remove(user);
            return null;
        } else {
            return socketSession;
        }
    }
}
