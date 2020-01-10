package com.zrzhen.zetty.http.advice;

import com.zrzhen.zetty.http.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.http.Response;
import com.zrzhen.zetty.http.http.mvc.*;
import com.zrzhen.zetty.http.http.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.service.UserService;


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
            response.setContent(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
            response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.JSON.getType());
            response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getContent().length));
            response.setFlag(false);
        }
        return response;
    }
}
