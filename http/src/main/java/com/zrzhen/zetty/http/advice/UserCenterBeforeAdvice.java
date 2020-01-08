package com.zrzhen.zetty.http.advice;

import com.zrzhen.zetty.http.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.http.Request;
import com.zrzhen.zetty.http.http.http.Response;
import com.zrzhen.zetty.http.http.mvc.IBeforeAdvice;
import com.zrzhen.zetty.http.http.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.http.http.util.FileUtil;
import com.zrzhen.zetty.http.http.util.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.service.UserService;
import org.apache.commons.lang3.StringUtils;


/**
 * @author chenanlian
 * <p>
 * 未登录拦截器
 */
@BeforeAdvice(id = "userCenterBeforeAdvice")
public class UserCenterBeforeAdvice implements IBeforeAdvice {

    @Override
    public Response before() {

        Request request = Request.get();
        String userId = request.getParameters().get("userId");
        Long useridInSession = UserService.getUserid();
        Response response = Response.get();
        if (StringUtils.isBlank(userId) || useridInSession == null || !userId.equals(String.valueOf(useridInSession))) {

            Result result = ResultGen.genResult(ResultCode.FAIL, "非当前用户");
            response.setContent(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
            response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getContent().length));
            response.setFlag(false);
        }
        return response;
    }
}
