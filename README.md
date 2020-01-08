### 展示网站

  以本框架开发的系统示例：[哲人镇](http://www.zrzhen.com)

### 项目结构

  code包：AIO网络通信封装  
  http包：轻量级web服务开发框架示例  
  im包：即时通信服务开发示例  
  
### 网络通信包（code包）使用说明

  实例化ZettyServer或ZettyClient，配置读取消息后的处理类，该类必须实现com.zrzhen.zetty.core.SocketReadHandler。可以在该类中进行
  协议解析和业务处理。ZettyClient可返回com.zrzhen.zetty.core.SocketSession，利用它可以进行灵活的、全双工的、长连接的网络通信。示例：
        
        ZettyServer.config()
                .port(8080)
                .readHandlerClass(ImReadHandler.class)
                .socketReadTimeout(Long.MAX_VALUE)
                .buildServer()
                .start();
  
  
### http服务示例部署

  直接jar包启动，或者执行启动脚本（在doc文件夹下有参考的shell脚本）：sh dev.sh start  
  jar包启动命令：java -jar E:\github\zetty\target\zetty-0.0.1.jar server.profiles.active=dev。其中server.profiles.active=后面
  的dev表示将以dev环境启动，加载后缀为_dev的properties文件（类似springboot）。

#### web框架设计说明

  基于java原生的Aio搭建socket通信。  
  基于jdbc进行数据库操作。   
  自己解析http协议，放弃servlet规范。  
  模仿springboot，实现了基于注解的编程，实现了AOP、请求静态页面、请求json数据、文件上传下载等功能。   
    
    
#### web框架路由说明

  js、css、img等静态文件需要放在resources下的static文件夹下，引用路径需以“/static/”开头，此类请求不经由controller处理。  
  html文件需要放在resources下的html文件夹下，在返回结果中返回文件的路径，如“index.html”，且要在方法头加@ContentType(ContentTypeEnum.HTML)。     
  Controller 中方法的每一个参数必须加注解。  
  其他使用要点可以参考测试例子（代码中包含了一个完整的论坛网站功能）。文档说明后续会进一步补充。  


####  web框架控制类编码示例

        package com.zrzhen.zetty.controller;
        
        import com.alibaba.fastjson.JSON;
        import com.alibaba.fastjson.JSONObject;
        import com.zrzhen.zetty.pojo.result.Result;
        import com.zrzhen.zetty.pojo.result.ResultCode;
        import com.zrzhen.zetty.pojo.result.ResultGen;
        import com.zrzhen.zetty.server.ContentTypeEnum;
        import com.zrzhen.zetty.server.ZettyRequest;
        import com.zrzhen.zetty.server.ZettyResponse;
        import com.zrzhen.zetty.server.anno.*;
        import com.zrzhen.zetty.util.HeaderUtil;
        import io.netty.handler.codec.http.HttpHeaders;
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
                return "defaultType";
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
        
            /**
             * 测试json内容类型。@ContentType(ContentTypeEnum.JSON)注解该方法返回json字符串结果，如果返回结果为实体类，则框架会
             * 自动将实体转换为json字符串后再返回给调用方
             *
             * @return
             */
            @RequestMapping("json")
            @ContentType(ContentTypeEnum.JSON)
            public Result json() {
                return ResultGen.genResult(ResultCode.SUCCESS);
            }
        
            /**
             * 测试添加session
             *
             * @param Request
             * @param response
             * @return
             */
            @RequestMapping("setSession")
            @ContentType(ContentTypeEnum.JSON)
            public Map setSession(@Request ZettyRequest Request, @Response ZettyResponse response) {
                Map out = new HashMap();
                String value = "sessionValue";
                HttpHeaders headers = response.headers();
                HeaderUtil.setCookie(headers, HeaderUtil.SESSION, value);
                out.put(HeaderUtil.SESSION, value);
                return out;
            }
        
            /**
             * 测试删除session
             *
             * @param response
             * @return
             */
            @RequestMapping("resetSession")
            public String resetSession(@Response ZettyResponse response) {
                HeaderUtil.removeSession(response.headers());
                return "resetSession";
            }
        
            /**
             * 测试获取session
             *
             * @param request
             * @return
             */
            @RequestMapping("getSession")
            @ContentType(ContentTypeEnum.JSON)
            public Map getSession(@Request ZettyRequest request) {
                HttpHeaders headers = request.headers();
                String value = HeaderUtil.getCookie(headers, HeaderUtil.SESSION);
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
        
            /**
             * 测试后置增强器
             *
             * @param jsonObject
             * @return
             */
            @AfterAdviceAction(id = "jsonpAdvice")
            @RequestMapping("jsonpAdvice")
            public String jsonpAdvice(@RequestJsonBody JSONObject jsonObject) {
        
                if (null != jsonObject) {
                    for (Map.Entry<String, Object> item : jsonObject.entrySet()) {
                        log.info(item.getKey() + "=" + item.getValue().toString());
                    }
                }
                return JSON.toJSONString(jsonObject);
            }
        
            /**
             * 测试uri传参和jsonbody传参
             *
             * @param name
             * @param age
             * @param body
             * @return
             */
            @RequestMapping("params")
            public String params(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "age") Integer age,
                                 @RequestJsonBody(required = true) JSONObject body) {
        
                if (null != body) {
                    for (Map.Entry<String, Object> item : body.entrySet()) {
                        log.info(item.getKey() + "=" + item.getValue().toString());
                    }
                }
                return "name:" + name + "\nage:" + age + "\nbody:" + body;
            }
        
        
        }





