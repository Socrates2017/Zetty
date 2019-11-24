package com.zrzhen.zetty.thread;

import com.zrzhen.zetty.dao.UserPositionLogDao;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 * <p>
 * 通过异步线程持久化登陆日志
 */
public class UserPositionLogThread implements Runnable {

    private Long userId;
    private String host;
    private String uri;
    private String userAgent;
    private String sessionid;

    private String latitude;
    private String longitude;
    private String altitude;
    private String accuracy;
    private String altitudeAccuracy;
    private String heading;
    private String speed;
    private String timestamp;

    public UserPositionLogThread(Long userId, String host, String uri, String userAgent, String sessionid, String latitude, String longitude, String altitude, String accuracy,
                                 String altitudeAccuracy, String heading, String speed, String timestamp) {
        this.userId = userId;
        this.host = host;
        this.uri = uri;
        this.userAgent = userAgent;
        this.sessionid = sessionid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.altitudeAccuracy = altitudeAccuracy;
        this.heading = heading;
        this.speed = speed;
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        Map<String, Object> value = new HashMap<>(16);


        if (StringUtils.isNotBlank(host)) {
            value.put("host", host);
        }

        if (userId != null) {
            value.put("user_id", userId);
        }

        if (StringUtils.isNotBlank(uri)) {
            value.put("uri", uri);
        }

        if (StringUtils.isNotBlank(userAgent)) {
            value.put("user_agent", userAgent);
        }

        if (StringUtils.isNotBlank(sessionid)) {
            value.put("session_id", sessionid);
        }


        if (checkNum(latitude)) {
            value.put("latitude", latitude);
        }
        if (checkNum(longitude)) {
            value.put("longitude", longitude);
        }
        if (checkNum(altitude)) {
            value.put("altitude", altitude);
        }
        if (checkNum(accuracy)) {
            value.put("accuracy", accuracy);
        }
        if (checkNum(altitudeAccuracy)) {
            value.put("altitudeAccuracy", altitudeAccuracy);
        }
        if (checkNum(heading)) {
            value.put("heading", Double.valueOf(heading));
        }
        if (checkNum(speed)) {
            value.put("speed", speed);
        }

        // value.put("p_timestamp", timestamp);


        UserPositionLogDao.insert(UserPositionLogDao.tableName, value);

    }

    boolean checkNum(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        } else if (str.equalsIgnoreCase("null") || str.equalsIgnoreCase("NaN")) {
            return false;
        } else {
            return true;
        }
    }
}
