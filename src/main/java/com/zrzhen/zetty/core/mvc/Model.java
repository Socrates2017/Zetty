package com.zrzhen.zetty.core.mvc;

import java.util.Map;

/**
 * @author chenanlian
 *
 * html内容动态加载模板
 */
public class Model {

    /**
     * html路径
     */
    private String path;

    /**
     * 模板替换的数据，将html中的对应的key替换成value
     * html中的key需要以${}包围，如${title}
     */
    private Map<String, Object> map;

    public Model() {
    }

    public Model(String path) {
        this.path = path;
    }

    public Model(String path, Map<String, Object> map) {
        this.path = path;
        this.map = map;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
