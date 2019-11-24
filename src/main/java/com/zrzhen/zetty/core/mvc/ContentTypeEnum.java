package com.zrzhen.zetty.core.mvc;

import org.apache.commons.lang3.StringUtils;

/**
 * @author chenanlian
 *
 * 内容类型枚举
 */

public enum ContentTypeEnum {

    /**
     * 纯文本格式
     */
    TEXT("text/plain; charset=UTF-8"),

    /**
     * HTML格式
     */
    HTML("text/html; charset=UTF-8", "html"),

    /**
     * JSON格式
     */
    JSON("application/json; charset=UTF-8"),


    /**
     * JS
     */
    JS("application/javascript; charset=UTF-8", "js"),

    /**
     * CSS
     */
    CSS("text/css; charset=UTF-8", "css"),

    /**
     * 二进制流数据（如常见的文件下载）
     */
    OCTET("application/octet-stream; charset=UTF-8"),

    /**
     * 文件
     */
    FORM_DATA("multipart/form-data;"),

    /**
     * 二进制流数据（如常见的文件下载）
     */
    URL_ENCODED("application/x-www-form-urlencoded; charset=UTF-8"),

    JPG("image/jpeg; charset=UTF-8", "jpg"),

    PNG("image/png;", "png"),

    ICO("image/png; charset=UTF-8", "ico"),

    AUDIO_MP3("audio/mpeg; ", "mp3"),
    ;

    private String type;

    private String suffix;

    ContentTypeEnum(String type) {
        this.type = type;
    }

    ContentTypeEnum(String type, String suffix) {
        this.type = type;
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String getTypeBySuffix(String suffix) {

        for (ContentTypeEnum api : ContentTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(suffix, api.getSuffix())) {
                return api.getType();
            }
        }
        return null;
    }
}
