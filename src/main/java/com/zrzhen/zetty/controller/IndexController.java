package com.zrzhen.zetty.controller;

import com.zrzhen.zetty.core.mvc.ContentTypeEnum;
import com.zrzhen.zetty.core.mvc.anno.ContentType;
import com.zrzhen.zetty.core.mvc.anno.Controller;
import com.zrzhen.zetty.core.mvc.anno.RequestMapping;
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

    /**
     *
     * 加密解密工具
     * @return
     */
    @RequestMapping("codec")
    @ContentType(ContentTypeEnum.HTML)
    public String codec() {
        return "codec.html";
    }

    @RequestMapping("love/20190613")
    @ContentType(ContentTypeEnum.HTML)
    public String huiyan20190613() {
        return "huiyan-20190613.html";
    }

}
