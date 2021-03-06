package com.zrzhen.zetty.cms.service;

import java.util.Map;

/**
 * @author chenanlian
 */
public class ArticleService {

    /**
     * 判断访问者是否为文章作者
     *
     * @param result
     * @return
     */
    public static boolean permission(Map<String, Object> result) {
        if (result.get("status").equals(1)) {
            Long userid = UserService.getUserid();
            Long userId2 = (Long) result.get("userid");
            if (userid == null || !userid.equals(userId2)) {
                return false;
            }
        }
        return true;
    }
}
