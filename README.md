## zetty使用指南

  [zetty使用指南](http://www.zrzhen.com/book/1582792865146)

## 项目结构

  common：通用工具  
  net：AIO网络通信封装  
  http：轻量级web服务开发框架  
  cms：基于http模块开发的一个网站  
  im：即时通信服务开发示例  
  p2p：p2p网络示例
  
## 网络通信模块（net）使用说明

  实例化ZettyServer或ZettyClient，配置读取消息后的处理类，该类必须实现com.zrzhen.zetty.net.SocketReadHandler。可以在该类中进行
  协议解析和业务处理。ZettyClient可返回com.zrzhen.zetty.net.SocketSession，利用它可以进行灵活的、全双工的、长连接的网络通信。示例：
        
  Http服务示例：
        
        ZettyServer.config()
                      .port(port)
                      .socketType(SocketEnum.AIO)
                      .decode(new HttpDecode())
                      .processor(new HttpProcessor())
                      .writeHandler(new HttpWriteHandler())
                      .buildServer()
                      .start();      
        
  全双工长连接示例，（服务端）：
        
        ZettyServer.config()
                .port(8080)
                .socketType(SocketEnum.AIO)
                .socketReadTimeout(Integer.MAX_VALUE)
                .decode(new FixedDecode())
                .encode(new FixedEncode())
                .processor(new Processor<ImMessage>() {
                    @Override
                    public boolean process(SocketSession socketSession, ImMessage message) {

                        HashMap<String, Object> map = Manager.getMapBySession(socketSession);
                        if (map == null) {
                            map = new HashMap();
                            map.put("session", socketSession);
                            Manager.sessions.put(socketSession.getRemoteAddress(), map);
                        }

                        String msg = FileUtil.byte2Str(message.getMsg());

                        message.setMsg(null);
                        message.setMsgIndex(0);
                        socketSession.read();

                        if (msg.startsWith(">>login:")) {
                            String userName = msg.substring(8);
                            Manager.loginUser.put(userName, socketSession.getRemoteAddress());
                            socketSession.write("登录成功，名字为：" + msg);
                        } else if (msg.startsWith(">>send to:")) {
                            String targetUser = msg.substring(10);
                            HashMap map2 = Manager.getMapByUserName(targetUser);

                            String response = null;
                            if (map2 == null) {
                                response = "用户不存在，设置失败：" + targetUser;
                            } else {
                                map2.put("sentTo", targetUser);
                                response = "已设置消息接收对象：" + targetUser;
                            }
                            socketSession.write(response);

                        } else {

                            String sendTo = (String) Manager.getMapBySession(socketSession).get("sentTo");
                            if (sendTo == null) {
                                socketSession.write("请设置消息接收的对象，设置命令：>>send to:userName");
                            } else {

                                SocketSession sessionSendTo = Manager.getSocketSessionByUserName(sendTo);

                                if (sessionSendTo == null) {
                                    socketSession.write("消息发送失败，用户已下线：" + sendTo);
                                } else {
                                    socketSession.write(msg);
                                }
                            }
                        }
                        return false;
                    }
                })
                .writeHandler(new ImWriteHandler())
                .buildServer()
                .start();
                                
  全双工长连接示例，（客户端）：
  
        SocketSession socketSession = ZettyClient.config()
                        .port(8080)
                        .socketType(SocketEnum.AIO)
                        .socketReadTimeout(Integer.MAX_VALUE)
                        .decode(new FixedDecode())
                        .encode(new FixedEncode())
                        .processor(new Processor<ImMessage>() {
                            @Override
                            public boolean process(SocketSession socketSession, ImMessage message) {
                                System.out.println("receive the message:"+FileUtil.byte2Str(message.getMsg()));
                                message.setMsg(null);
                                message.setMsgIndex(0);
                                socketSession.read();
                                return true;
                            }
                        })
                        .writeHandler(new WriteHandler<Integer, SocketSession>() {
                            @Override
                            public void completed(Integer result, SocketSession socketSession) {
                                AsynchronousSocketChannel channel = socketSession.getSocketChannel();
                                ByteBuffer buffer = socketSession.getWriteBuffer();
                                if (buffer.hasRemaining()) {
                                    channel.write(buffer, socketSession, this);
                                }
                                //长连接，所以，写完毕后不关闭通道
                            }
        
                            @Override
                            public void failed(Throwable exc, SocketSession socketSession) {
                                exc.getStackTrace();
                                socketSession.destroy();
                            }
                        })
                        .buildClient()
                        .connect();
                
  
  
## web框架使用说明
  
  web框架需要引入http模块，具体请参照cms示例。
### http服务示例部署

  直接jar包启动，或者执行启动脚本（在doc文件夹下有参考的shell脚本）：sh dev.sh start  
  jar包启动命令：java -jar E:\github\zetty\target\zetty-0.0.1.jar server.profiles.active=dev。其中server.profiles.active=后面
  的dev表示将以dev环境启动，加载后缀为_dev的properties文件（类似springboot）。

### web框架设计说明

  基于java原生的Aio搭建socket通信，即本项目中的net模块。  
  基于jdbc进行数据库操作，可替换为其他ORM框架。   
  自己解析http协议，放弃servlet规范。  
  模仿springboot，实现了基于注解的编程，实现了AOP、请求静态页面、请求json数据、文件上传下载等功能。   
    
    
### web框架路由说明

  js、css、img等静态文件需要放在resources下的static文件夹下，引用路径需以“/static/”开头，此类请求不经由controller处理。  
  html文件需要放在resources下的html文件夹下，在返回结果中返回文件的路径，如“index.html”，且要在方法头加@ContentType(ContentTypeEnum.HTML)。     
  Controller 中方法的每一个参数必须加注解。  
  其他使用要点可以参考测试例子（代码中包含了一个完整的论坛网站功能）。文档说明后续会进一步补充。  


###  web框架控制类编码示例

        package com.zrzhen.zetty.cms.controller;
        
        import com.zrzhen.zetty.http.http.HttpHeaders;
        import com.zrzhen.zetty.http.http.HttpResponseStatus;
        import com.zrzhen.zetty.http.http.Response;
        import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
        import com.zrzhen.zetty.http.mvc.anno.*;
        import com.zrzhen.zetty.cms.pojo.result.Result;
        import com.zrzhen.zetty.cms.pojo.result.ResultCode;
        import com.zrzhen.zetty.cms.pojo.result.ResultGen;
        import com.zrzhen.zetty.cms.util.HeaderUtil;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        
        import java.io.IOException;
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
        
        
            /**
             * 测试重定向
             * @return
             */
            @RequestMapping(value = "redirect")
            public Response redirect(){
                Response response = Response.get();
                response.getHeaders().put(HttpHeaders.Names.LOCATION, "http://www.baidu.com");
                response.setStatus(HttpResponseStatus.MOVED_PERMANENTLY);
                return response;
            }
        
        }






