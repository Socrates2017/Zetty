package com.zrzhen.sqlgraph.controller;

import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.mvc.anno.ContentType;
import com.zrzhen.zetty.http.mvc.anno.Controller;
import com.zrzhen.zetty.http.mvc.anno.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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



}
