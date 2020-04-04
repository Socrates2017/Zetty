package com.zrzhen.zetty.http.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenanlian
 * <p>
 * 配置文件加载类
 */
public class ProUtil {

    private final static Logger log = LoggerFactory.getLogger(ProUtil.class);

    /**
     * 是否使用的是application.properties配置的环境，如果是由java -jar 启动，并且传了server.profiles.active=dev 参数
     * 则为false；dev可以换成其他环境后缀{env}，配置文件名为application_{env}.properties
     */
    public static boolean innerEnv = true;

    /**
     * 环境标识
     */
    public static String env = "dev";

    /**
     * 项目的上一层目录
     */
    public final static String userDir = System.getProperty("user.dir") + File.separator;

    /**
     * 配置文件加载进来的key-value
     */
    public static Map<String, String> map = new ConcurrentHashMap<>();

    /**
     * 配置文件的加载顺序为：
     * 1.resources目录下的共同配置文件application.properties。
     * 2.放在与jar包同一个目录下的共同配置文件，application.properties。
     * 3.如果启动参数传入了指定的环境。如java -jar server.profiles.active=prod，则环境指定为了prod。
     * 否则，则读取共同配置文件application.properties中配置的环境，如配置：server.profiles.active=prod。
     * 如果都没有配置，则环境默认为dev，将默认使用application_dev.properties配置文件。
     * 4.后面配置的值如果key与前面已经加载的相同，则覆盖之。即四个配置文件的优先顺序为：
     * a.jar包外的指定环境配置文件，如application_dev.properties
     * b.jar包内的指定环境配置文件，如application_dev.properties
     * c.jar包外的共同配置文件，application.properties
     * d.jar包内的共同配置文件，application.properties
     */
    public static void init() {
        /**
         * jar包内的共同配置文件，application.properties
         */
        Properties innerPro = loadInnerProperties("/application.properties");
        if (null != innerPro) {
            iteratorProperties(innerPro);
            log.info("jar包内application.properties加载完毕.");
        }

        /**
         * jar包外的共同配置文件，application.properties
         */

        Properties OutterPro = loadOutterProperties(userDir + "application.properties");
        if (null != OutterPro) {
            iteratorProperties(OutterPro);
            log.info("jar包外application.properties加载完毕.");
        }

        /**
         * 如果启动参数没有传入指定的环境，则取配置文件中配置的环境，如果同样没有，则取默认值：dev
         */
        if (innerEnv) {
            ProUtil.map.put("env", ProUtil.getString("server.profiles.active", "dev"));
            env = map.get("server.profiles.active");
        }

        /**
         * jar包内的指定环境配置文件，如application_dev.properties
         */
        String envProperties = "/application_" + ProUtil.env + ".properties";
        Properties innerProEnv = loadInnerProperties(envProperties);
        if (null != innerProEnv) {
            iteratorProperties(innerProEnv);
            log.info("jar包内{}加载完毕.", envProperties);
        }


        /**
         * jar包外的指定环境配置文件，如application_dev.properties
         */
        Properties outterProEnv = loadOutterProperties(userDir + envProperties);
        if (null != outterProEnv) {
            iteratorProperties(outterProEnv);
            log.info("jar包外{}加载完毕.", envProperties);
        }

    }

    /**
     * 根据key获得字符串值
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return map.get(key);
    }

    /**
     * 根据key获得整型值
     *
     * @param key
     * @return
     */
    public static Integer getInteger(String key) {
        String result = getString(key).trim();
        if (StringUtils.isNotBlank(result)) {
            return Integer.valueOf(result);
        } else {
            return null;
        }
    }

    /**
     * 根据key获得整型值
     *
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        String result = getString(key).trim();
        if (StringUtils.isNotBlank(result)) {
            return Long.valueOf(result);
        } else {
            return null;
        }
    }

    /**
     * 根据key获得字符串值
     *
     * @param key
     * @param defaultValue 当缺失时返回的的默认值
     * @return
     */
    public static String getString(String key, String defaultValue) {
        String result = map.get(key);
        if (result == null) {
            return defaultValue;
        } else {
            return result;
        }
    }

    /**
     * 根据key获得整型值
     *
     * @param key
     * @param defaultValue 当缺失时返回的的默认值
     * @return
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        String result = getString(key, String.valueOf(defaultValue)).trim();
        if (StringUtils.isNotBlank(result)) {
            return Integer.valueOf(result);
        } else {
            return defaultValue;
        }
    }

    /**
     * 加载放在项目内resources目录下的配置文件
     *
     * @param filePath
     * @return
     */
    private static Properties loadInnerProperties(String filePath) {
        try {
            Properties properties = new Properties();
            properties.load(ProUtil.class.getClass().getResourceAsStream(filePath));
            return properties;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载与jar包同一目录下的配置文件
     *
     * @param filePath
     * @return
     */
    private static Properties loadOutterProperties(String filePath) {
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            return properties;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 遍历Properties并复制进map变量中
     *
     * @param properties
     */
    private static void iteratorProperties(Properties properties) {
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            map.put(key.toString(), value != null ? value.toString() : null);
        }
    }


}
