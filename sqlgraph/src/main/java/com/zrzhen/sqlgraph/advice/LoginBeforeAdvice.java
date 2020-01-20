package com.zrzhen.sqlgraph.advice;


import com.zrzhen.sqlgraph.dao.UserTokenApiDao;
import com.zrzhen.sqlgraph.dao.UserTokenDao;
import com.zrzhen.zetty.http.http.Request;
import com.zrzhen.zetty.http.http.Response;
import com.zrzhen.zetty.http.mvc.IBeforeAdvice;
import com.zrzhen.zetty.http.mvc.anno.BeforeAdvice;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author chenanlian
 * <p>
 * 未登录拦截器
 */
@BeforeAdvice(id = "loginBeforeAdvice")
public class LoginBeforeAdvice implements IBeforeAdvice {

    @Override
    public Response before() {

        Request request = Request.get();

        String userIdCookie = request.getHeaders().get("userId");
        String tokenCookie = request.getHeaders().get("token");

        if (StringUtils.isNotBlank(userIdCookie) && StringUtils.isNotBlank(tokenCookie)) {
            String tokenSql = UserTokenDao.getUserToken(Long.valueOf(userIdCookie));
            if (tokenSql == tokenCookie) {

                if (UserTokenApiDao.getUserApiToken(Long.valueOf(userIdCookie)) == null) {
                    String token = System.currentTimeMillis() + "";
                    Map map = new HashMap();
                    map.put("user_id", "");
                    map.put("token", "");
                    UserTokenApiDao.insert(UserTokenApiDao.tableName, map);
                }



            }
        }

        Response response = Response.get();

        return response;
    }
}
