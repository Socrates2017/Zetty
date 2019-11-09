package com.zrzhen.zetty.advice;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.core.http.HttpHeaders;
import com.zrzhen.zetty.core.http.Request;
import com.zrzhen.zetty.core.http.Response;
import com.zrzhen.zetty.core.mvc.ContentTypeEnum;
import com.zrzhen.zetty.core.mvc.IBeforeAdvice;
import com.zrzhen.zetty.core.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.core.util.FileUtil;
import com.zrzhen.zetty.core.util.JsonUtil;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
import com.zrzhen.zetty.service.BookService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


/**
 * @author chenanlian
 * <p>
 * 未登录拦截器
 */
@BeforeAdvice(id = "checkBookCreatorBeforeAdvice")
public class CheckBookCreatorBeforeAdvice implements IBeforeAdvice {

    @Override
    public Response before() {

        Request request = Request.get();
        JsonNode params = request.getJsonBody();
        String book = JsonUtil.getString(params, "bookId").trim();

        Response response = Response.get();
        if (StringUtils.isBlank(book)) {
            Result result = ResultGen.genResult(ResultCode.FAIL, "book参数不能为空");
            response.setBody(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
            response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.JSON.getType());
            response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getBody().length));
            response.setFlag(false);
        } else {
            if (BookService.checkCreator(Long.valueOf(book))) {
              //do nothing
            } else {
                Result result = ResultGen.genResult(ResultCode.FAIL, "当前用户非本书作者");
                response.setBody(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
                response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.JSON.getType());
                response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getBody().length));
                response.setFlag(false);
            }
        }
        return response;
    }
}
