package com.zrzhen.zetty.advice;

import com.zrzhen.zetty.core.http.HttpHeaders;
import com.zrzhen.zetty.core.http.Response;
import com.zrzhen.zetty.core.mvc.*;
import com.zrzhen.zetty.core.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.core.util.FileUtil;
import com.zrzhen.zetty.core.util.JsonUtil;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
import com.zrzhen.zetty.service.UserService;


/**
 * @author chenanlian
 * <p>
 * 未登录拦截器
 */
@BeforeAdvice(id = "loginBeforeAdvice")
public class LoginBeforeAdvice implements IBeforeAdvice {

    @Override
    public Response before() {
        Long userid = UserService.getUserid();
        Response response = Response.get();
        if (null == userid) {
            Result result = ResultGen.genResult(ResultCode.SESSION_LOGOUT);
            response.setBody(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
            response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.JSON.getType());
            response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getBody().length));
            response.setFlag(false);
        }
        return response;
    }
}
