package com.zrzhen.zetty.cms.controller;

import com.zrzhen.zetty.cms.pojo.result.Result;
import com.zrzhen.zetty.cms.pojo.result.ResultCode;
import com.zrzhen.zetty.cms.pojo.result.ResultGen;
import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.mvc.anno.ContentType;
import com.zrzhen.zetty.http.mvc.anno.Controller;
import com.zrzhen.zetty.http.mvc.anno.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * @author chenanlian
 * <p>
 * 用于测试、学习的控制类
 */
@Controller("monitor/")
public class Monitoontroller {

    private static final Logger log = LoggerFactory.getLogger(Monitoontroller.class);


    /**
     * 测试json内容类型。@ContentType(ContentTypeEnum.JSON)注解该方法返回json字符串结果，如果返回结果为实体类，则框架会
     * 自动将实体转换为json字符串后再返回给调用方
     *
     * @return
     */
    @RequestMapping("json")
    @ContentType(ContentTypeEnum.JSON)
    public Result json() {

        try {
            System.out.println("maxMemoryValue:" + sun.misc.VM.maxDirectMemory());


            System.out.println("================================");

            ByteBuffer buffer = ByteBuffer.allocateDirect(0);
            Class<?> c = Class.forName("java.nio.Bits");
            Field maxMemory = c.getDeclaredField("maxMemory");
            maxMemory.setAccessible(true);
            synchronized (c) {
                Long maxMemoryValue = (Long) maxMemory.get(null);
                System.out.println("maxMemoryValue:" + maxMemoryValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ResultGen.genResult(ResultCode.SUCCESS, "测试数据");
    }


}
