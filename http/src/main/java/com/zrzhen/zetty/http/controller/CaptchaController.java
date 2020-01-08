package com.zrzhen.zetty.http.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.http.Response;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.anno.ContentType;
import com.zrzhen.zetty.http.http.mvc.anno.Controller;
import com.zrzhen.zetty.http.http.mvc.anno.RequestJsonBody;
import com.zrzhen.zetty.http.http.mvc.anno.RequestMapping;
import com.zrzhen.zetty.http.http.util.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.service.SessionMapService;
import com.zrzhen.zetty.http.util.CaptchaUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * 验证码生成
 *
 * @author ChenAnlian
 */
@Controller
public class CaptchaController {

    private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);

    @RequestMapping("/pcrimg")
    public Response pcrimg() throws IOException {
        String sessionid = SessionMapService.getSession();
        HashMap map = SessionMapService.getHashMap(sessionid);
        String token = CaptchaUtil.createToken();

        Response response = Response.get();
        response.setContent(CaptchaUtil.createCode(token));

        response.getHeaders().put(HttpHeaders.Names.CACHE_CONTROL, "no-cache, no-store");
        response.getHeaders().put(HttpHeaders.Names.PRAGMA, "no-cache");
        long time = System.currentTimeMillis();
        response.getHeaders().put(HttpHeaders.Names.LAST_MODIFIED, String.valueOf(time));
        response.getHeaders().put(HttpHeaders.Names.DATE, String.valueOf(time));
        response.getHeaders().put(HttpHeaders.Names.EXPIRES, String.valueOf(time));
        response.getHeaders().put(HttpHeaders.Names.CONTENT_TYPE, ContentTypeEnum.PNG.getType());

        map.put("codeId", token);
        log.debug("生成验证码，codeId:{}", token);
        SessionMapService.sessions.put(sessionid, map);
        return response;
    }


    /**
     * 验证码是否一致
     *
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/checkAuthCode")
    public Result checkAuthCode(@RequestJsonBody JsonNode params) {
        String authCode = JsonUtil.getString(params,"authCode");
        if (StringUtils.isBlank(authCode)) {
            log.debug("验证码参数缺失");
            return ResultGen.genResult(ResultCode.ARG_NEED, "authCode");
        }

        String sessionid = SessionMapService.getSession();
        HashMap map = SessionMapService.getHashMap(sessionid);
        String codeId = (String) map.get("codeId");

        if (StringUtils.isBlank(codeId)) {
            log.error("服务端codeId缺失,authCode:{}", authCode);
            return ResultGen.genResult(ResultCode.CAPTCHA_NULL_CODEID, "服务端codeId为空");
        }

        if (codeId.equalsIgnoreCase(authCode)) {
            /*一致*/
            return ResultGen.genResult(ResultCode.SUCCESS, true);
        } else {
            /*不一致*/
            log.debug("验证码不一致,authCode:{}；codeId:{}", authCode, codeId);
            return ResultGen.genResult(ResultCode.CAPTCHA_WRONG, false);
        }

    }
}