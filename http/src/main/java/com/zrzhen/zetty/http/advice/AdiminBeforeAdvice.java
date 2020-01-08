package com.zrzhen.zetty.http.advice;

import com.zrzhen.zetty.http.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.http.Response;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.IBeforeAdvice;
import com.zrzhen.zetty.http.http.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.http.http.util.FileUtil;
import com.zrzhen.zetty.http.http.util.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenanlian
 * <p>
 * 非管理员拦截器，只有网站管理员才能通过
 */
@BeforeAdvice(id = "adiminBeforeAdvice")
public class AdiminBeforeAdvice implements IBeforeAdvice {

    private final static Logger log = LoggerFactory.getLogger(AdiminBeforeAdvice.class);


    @Override
    public Response before() {
        Long userid = UserService.getUserid();
        Response response = Response.get();
        if (null == userid || userid != 1001564102275036L) {
            Result result = ResultGen.genResult(ResultCode.USER_NOT_ADMIN);
            response.setContent(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
            response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.JSON.getType());
            response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getContent().length));
            response.setFlag(false);
        }
        return response;
    }
}
