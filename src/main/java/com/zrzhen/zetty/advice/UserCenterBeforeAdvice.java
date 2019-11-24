package com.zrzhen.zetty.advice;

import com.zrzhen.zetty.core.http.HttpHeaders;
import com.zrzhen.zetty.core.http.Request;
import com.zrzhen.zetty.core.http.Response;
import com.zrzhen.zetty.core.mvc.IBeforeAdvice;
import com.zrzhen.zetty.core.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.core.util.FileUtil;
import com.zrzhen.zetty.core.util.JsonUtil;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
import com.zrzhen.zetty.service.UserService;
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
            response.setBody(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
            response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getBody().length));
            response.setFlag(false);
        }
        return response;
    }
}
