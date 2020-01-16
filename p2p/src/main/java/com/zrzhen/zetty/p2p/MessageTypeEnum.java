package com.zrzhen.zetty.p2p;


import org.apache.commons.lang3.StringUtils;

/**
 * @author chenanlian
 */

public enum MessageTypeEnum {

    /**
     * 注册
     */
    REGISTER("register"),
    LOGIN_USER("login user"),;


    private String name;

    MessageTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MessageTypeEnum getByName(String name) {

        for (MessageTypeEnum api : MessageTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(name, api.getName())) {
                return api;
            }
        }
        return null;
    }

}
