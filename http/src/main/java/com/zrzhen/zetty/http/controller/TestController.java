package com.zrzhen.zetty.http.controller;

import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.anno.*;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 * <p>
 * 用于测试、学习的控制类
 */
@Controller("test/")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    /**
     * 测试默认的内容类型
     *
     * @return
     */
    @RequestMapping("defaultType")
    public String text() {
        return "test数据";
    }

    /**
     * 测试html内容类型。@ContentType(ContentTypeEnum.HTML)注解表示该方法返回html文件，返回结果即html文件的路径。路径的表示为以resources
     * 下的html文件为基路径的相对路径
     *
     * @return
     */
    @RequestMapping("html")
    @ContentType(ContentTypeEnum.HTML)
    public String html() {
        return "index.html";
    }

    @RequestMapping("comet")
    @ContentType(ContentTypeEnum.HTML)
    public String comet() {
        return "cometTest.html";
    }

    /**
     * 测试json内容类型。@ContentType(ContentTypeEnum.JSON)注解该方法返回json字符串结果，如果返回结果为实体类，则框架会
     * 自动将实体转换为json字符串后再返回给调用方
     *
     * @return
     */
    @RequestMapping("json")
    @ContentType(ContentTypeEnum.JSON)
    public Result json() {
        return ResultGen.genResult(ResultCode.SUCCESS,"测试数据");
    }

    /**
     * 测试添加session
     *
     * @return
     */
    @RequestMapping("setSession")
    @ContentType(ContentTypeEnum.JSON)
    public Map setSession() {
        Map out = new HashMap();
        String value = "sessionValue";
        HeaderUtil.setCookie(HeaderUtil.SESSION, value);
        out.put(HeaderUtil.SESSION, value);
        return out;
    }

    /**
     * 测试删除session
     *
     * @return
     */
    @RequestMapping("resetSession")
    public String resetSession() {
        HeaderUtil.removeSession();
        return "resetSession";
    }

    /**
     * 测试获取session
     *
     * @return
     */
    @RequestMapping("getSession")
    @ContentType(ContentTypeEnum.JSON)
    public Map getSession() {
        String value = HeaderUtil.getCookie(HeaderUtil.SESSION);
        Map out = new HashMap();
        out.put(HeaderUtil.SESSION, value);
        return out;
    }

    /**
     * 测试前置增强器，即拦截器
     *
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice,adiminBeforeAdvice")
    @RequestMapping("beforeAdvice")
    public String beforeAdvice() {

        return "允许访问";
    }




}
