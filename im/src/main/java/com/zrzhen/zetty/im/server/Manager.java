package com.zrzhen.zetty.im.server;

import com.zrzhen.zetty.net.SocketSession;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenanlian
 */
public class Manager {

    public static ConcurrentHashMap<String, HashMap<String, Object>> sessions = new ConcurrentHashMap();

    public static ConcurrentHashMap<String, String> loginUser = new ConcurrentHashMap();




    public static SocketSession getSocketSessionByUserName(String user) {
        String host = loginUser.get(user);
        if (host == null) {
            return null;
        }

        HashMap map = sessions.get(host);

        SocketSession socketSession = (SocketSession) map.get("session");
        if (socketSession == null) {
            loginUser.remove(user);
            return null;
        } else {
            return socketSession;
        }
    }

    public static HashMap<String, Object> getMapByUserName(String user) {
        String host = loginUser.get(user);
        if (host == null) {
            return null;
        }

        HashMap map = sessions.get(host);

        return map;
    }

    public static HashMap<String, Object> getMapBySession(SocketSession socketSession) {
        HashMap map = sessions.get(socketSession.getRemoteAddress());
        return map;
    }
}
