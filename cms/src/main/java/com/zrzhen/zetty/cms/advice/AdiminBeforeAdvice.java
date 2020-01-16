package com.zrzhen.zetty.cms.advice;

import com.zrzhen.zetty.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.Response;
import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.mvc.IBeforeAdvice;
import com.zrzhen.zetty.http.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.cms.pojo.result.Result;
import com.zrzhen.zetty.cms.pojo.result.ResultCode;
import com.zrzhen.zetty.cms.pojo.result.ResultGen;
import com.zrzhen.zetty.cms.service.UserService;
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
