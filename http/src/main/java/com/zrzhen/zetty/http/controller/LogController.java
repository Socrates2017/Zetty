package com.zrzhen.zetty.http.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.http.http.Request;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.anno.*;
import com.zrzhen.zetty.http.http.util.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.service.UserService;
import com.zrzhen.zetty.http.thread.ThreadPoolUtil;
import com.zrzhen.zetty.http.thread.UserPositionLogThread;
import com.zrzhen.zetty.http.thread.WebLogThread;
import com.zrzhen.zetty.http.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/log")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    /**
     * 记录页面访问时长
     *
     * @param uri
     * @param duration
     * @return
     */
    @RequestMapping("/duration")
    @ContentType(ContentTypeEnum.JSON)
    public Result duration(@RequestParam(name = "uri", required = true) String uri,
                           @RequestParam(name = "duration", required = true) Long duration) {

        Long userid = UserService.getUserid();
        Request request = Request.get();
        String host = request.getHost();
        String userAgent = request.getHeaders().get("User-Agent");
        String sessionid = HeaderUtil.getSession();

        ThreadPoolUtil.logExecutor.execute(new WebLogThread(userid, sessionid, duration, uri, host, userAgent));
        return ResultGen.genResult(ResultCode.SUCCESS);
    }

    /**
     * 记录用户访问位置
     *
     * @param params
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/position")
    public Result position(@RequestJsonBody JsonNode params) {
        Request request = Request.get();

        Long userid = UserService.getUserid();
        String host = request.getHost();
        String userAgent = request.getHeaders().get("User-Agent");
        String uri = JsonUtil.getString(params, "uri");

        String sessionid = HeaderUtil.getSession();

        String latitude = JsonUtil.getString(params, "latitude");
        String longitude = JsonUtil.getString(params, "longitude");
        String altitude = JsonUtil.getString(params, "altitude");
        String accuracy = JsonUtil.getString(params, "accuracy");
        String altitudeAccuracy = JsonUtil.getString(params, "altitudeAccuracy");
        String heading = JsonUtil.getString(params, "heading");
        String speed = JsonUtil.getString(params, "speed");
        String timestamp = JsonUtil.getString(params, "timestamp");

        log.info("position log:{}", latitude);
        ThreadPoolUtil.logExecutor.execute(new UserPositionLogThread(userid, host, uri, userAgent, sessionid, latitude, longitude, altitude, accuracy,
                altitudeAccuracy, heading, speed, timestamp));
        return ResultGen.genResult(ResultCode.SUCCESS);

    }
}
