package com.zrzhen.zetty.cms.advice;

import com.zrzhen.zetty.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.Request;
import com.zrzhen.zetty.http.http.Response;
import com.zrzhen.zetty.http.mvc.IBeforeAdvice;
import com.zrzhen.zetty.http.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.cms.pojo.result.Result;
import com.zrzhen.zetty.cms.pojo.result.ResultCode;
import com.zrzhen.zetty.cms.pojo.result.ResultGen;
import com.zrzhen.zetty.cms.service.UserService;
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
