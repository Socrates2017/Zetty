package com.zrzhen.zetty.http;

import com.zrzhen.zetty.net.ZettyServer;
import com.zrzhen.zetty.http.http.Constant;
import com.zrzhen.zetty.http.http.ReadHandler;
import com.zrzhen.zetty.http.http.mvc.ServerContext;
import com.zrzhen.zetty.http.http.util.ProUtil;
import com.zrzhen.zetty.http.http.util.ServerUtil;
import com.zrzhen.zetty.http.util.quartz.MyScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenanlian
 */
public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        try {

            /*初始化环境配置文件*/
            log.info("Environment properties init start......");
            Long envInitStart = System.currentTimeMillis();
            ServerUtil.initEnv(args);
            log.info("Environment properties init end, cost time:{}ms.", System.currentTimeMillis() - envInitStart);

            /**
             * 创建文件上传临时文件夹
             */
            File file = new File(Constant.UPLOAD_FILEPATH_TMP);
            if (!file.exists()) {
                file.mkdir();
            }

            /*初始化路由映射*/
            log.info("Server Context init start......");
            Long serverContextStart = System.currentTimeMillis();
            ServerContext.init();
            log.info("Server Context init end, cost time:{}ms.", System.currentTimeMillis() - serverContextStart);

            /*运行端口*/
            Integer port = ProUtil.getInteger("server.port", 8080);
            log.info("Aio server is going to start, the server port is:{}, environment is {}, whether use the environment properties in jar {}.", port, ProUtil.env, ProUtil.innerEnv);


            /*启动定时任务*/
            MyScheduler.start();

            ZettyServer.config()
                    .port(port)
                    .readHandlerClass(ReadHandler.class)
                    .buildServer()
                    .start();

            CountDownLatch countDownLatch = new CountDownLatch(1);
            countDownLatch.await();

            log.info("The application has been started successfully, cost time:{}ms.", System.currentTimeMillis() - envInitStart);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
