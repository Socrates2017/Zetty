package com.zrzhen.zetty.p2p.server;


/**
 * @author chenanlian
 */

public enum ConmmanEnum {

    /**
     * 注册
     */
    REGISTER("注册"),
    Client("账户不存在，请检查链接是否有误，"),;


    private String name;

    ConmmanEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
