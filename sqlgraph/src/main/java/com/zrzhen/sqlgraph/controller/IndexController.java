package com.zrzhen.sqlgraph.controller;

import com.zrzhen.zetty.common.CaptchaUtil;
import com.zrzhen.zetty.http.http.HttpHeaders;
import com.zrzhen.zetty.http.http.HttpResponseStatus;
import com.zrzhen.zetty.http.http.Response;
import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.mvc.anno.ContentType;
import com.zrzhen.zetty.http.mvc.anno.Controller;
import com.zrzhen.zetty.http.mvc.anno.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author chenanlian
 */
@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);


    /**
     * 首页
     * @return
     */
    @RequestMapping
    @ContentType(ContentTypeEnum.HTML)
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/redirect")
    public Response redirect() {
        Response response = Response.get();
        response.getHeaders().put(HttpHeaders.Names.LOCATION, "http://www.baidu.com");
        response.setStatus(HttpResponseStatus.MOVED_PERMANENTLY);
        return response;
    }

}
