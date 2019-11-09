package com.zrzhen.zetty.util;

/**
 * @author chenanlian
 */
public class UserUtil {


    public static Long userid() {
        String id = HeaderUtil.SYSCODE + String.valueOf(System.currentTimeMillis());
        return Long.valueOf(id);
    }

    public static void main(String[] args) {

        System.out.println(userid());

        System.out.println(System.currentTimeMillis());
    }
}
