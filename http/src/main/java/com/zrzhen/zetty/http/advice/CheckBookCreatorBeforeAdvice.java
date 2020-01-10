package com.zrzhen.zetty.http.advice;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.http.Request;
import com.zrzhen.zetty.http.http.http.Response;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.IBeforeAdvice;
import com.zrzhen.zetty.http.http.mvc.anno.BeforeAdvice;
import com.zrzhen.zetty.common.FileUtil;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.service.BookService;
import org.apache.commons.lang3.StringUtils;


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
        String book = JsonUtil.getString(params, "bookId");

        Response response = Response.get();
        if (StringUtils.isBlank(book)) {
            Result result = ResultGen.genResult(ResultCode.FAIL, "book参数不能为空");
            response.setContent(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
            response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.JSON.getType());
            response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getContent().length));
            response.setFlag(false);
        } else {
            if (BookService.checkCreator(Long.valueOf(book))) {
              //do nothing
            } else {
                Result result = ResultGen.genResult(ResultCode.FAIL, "当前用户非本书作者");
                response.setContent(FileUtil.str2Byte(JsonUtil.obj2Json(result)));
                response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.JSON.getType());
                response.getHeaders().put(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(response.getContent().length));
                response.setFlag(false);
            }
        }
        return response;
    }
}
