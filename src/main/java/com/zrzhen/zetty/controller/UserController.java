package com.zrzhen.zetty.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.constant.UserStatusEnum;
import com.zrzhen.zetty.core.mvc.ContentTypeEnum;
import com.zrzhen.zetty.core.mvc.Model;
import com.zrzhen.zetty.core.mvc.anno.*;
import com.zrzhen.zetty.core.util.JsonUtil;
import com.zrzhen.zetty.dao.UserDao;
import com.zrzhen.zetty.dao.UserExtDao;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
import com.zrzhen.zetty.service.SessionMapService;
import com.zrzhen.zetty.service.SessionService;
import com.zrzhen.zetty.service.UserService;
import com.zrzhen.zetty.util.HeaderUtil;
import com.zrzhen.zetty.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 * 用户路由控制类
 */

@Controller("/user/")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    /**
     * 返回Email的数量，为0则该Email尚未注册
     *
     * @param params
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("checkEmail")
    public Result checkEmail(@RequestJsonBody JsonNode params) {
        String email = JsonUtil.getString(params,"email");
        if (StringUtils.isBlank(email)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "email");
        } else {
            long count = UserDao.countByEmail(email);
            if (count == 0) {
                return ResultGen.genResult(ResultCode.SUCCESS, count);
            } else {
                return ResultGen.genResult(ResultCode.SESSION_EXIST_EMAIL, count);
            }
        }
    }

    /**
     * 检查是否登录
     *
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("checkLogin")
    public Result checkLogin() {
        return UserService.checkLogin();
    }

    /**
     * 登录
     *
     * @param params
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("login")
    public Result login(@RequestJsonBody JsonNode params) {
        String sessionid = HeaderUtil.getSession();
        String authCode = JsonUtil.getString(params,"authCode");
        if (StringUtils.isBlank(authCode)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "authCode");
        }

        if (StringUtils.isBlank(sessionid)) {
            log.warn(ResultCode.SESSION_NULL_CLIENT.getMessage() + " sessionid:" + sessionid);
            return ResultGen.genResult(ResultCode.SESSION_NULL_CLIENT);
        } else {
            HashMap session = SessionMapService.sessions.get(sessionid);
            if (session == null) {
                log.warn(ResultCode.SESSION_NULL_SERVER.getMessage() + " sessionid:" + sessionid);
                HeaderUtil.removeSession();
                return ResultGen.genResult(ResultCode.SESSION_NULL_SERVER);
            } else {
                String codeId = (String) session.get("codeId");

                if (!authCode.equalsIgnoreCase(codeId)) {
                    log.error("验证码不一致,authCode:{}；codeId:{}", authCode, codeId);
                    return ResultGen.genResult(ResultCode.SSO_FAIL, "验证码错误");
                } else {
                    /*检查sessionMap中是否有openid*/
                    Long openid = SessionService.getOpenidBySessionid(sessionid);
                    if (openid != null) {
                        /*根据openid和系统码查出用户信息*/
                        Long userid = SessionService.getUseridByOpenid(HeaderUtil.SYSCODE, openid);
                        if (userid != null) {
                            /*添加用户id*/
                            HeaderUtil.setUser( userid);
                            return ResultGen.genResult(ResultCode.SUCCESS, "已经登录，无需重复登录");
                        } else {
                            log.error("账户为空，openid:{}", openid);
                            return ResultGen.genResult(ResultCode.SSO_FAIL, "SSO单点登录账户为空");
                        }
                    } else {
                        return UserService.login(params);
                    }
                }
            }
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("logout")
    public Result logout() {
        String sessionid = HeaderUtil.getSession();
        if (StringUtils.isBlank(sessionid)) {
            log.warn(ResultCode.SESSION_NULL_CLIENT.getMessage() + " sessionid:" + sessionid);
            return ResultGen.genResult(ResultCode.SESSION_NULL_CLIENT);
        } else {
            return UserService.logout(sessionid);
        }
    }


    /**
     * 注册
     *
     * @param params
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("register")
    public Result register(@RequestJsonBody JsonNode params) {
        String sessionid = HeaderUtil.getSession();
        String authCode = JsonUtil.getString(params,"authCode");
        if (StringUtils.isBlank(authCode)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "authCode");
        }

        if (StringUtils.isBlank(sessionid)) {
            log.warn(ResultCode.SESSION_NULL_CLIENT.getMessage() + " sessionid:" + sessionid);
            return ResultGen.genResult(ResultCode.SESSION_NULL_CLIENT);
        } else {
            HashMap session = SessionMapService.sessions.get(sessionid);
            if (session == null) {
                log.warn(ResultCode.SESSION_NULL_SERVER.getMessage() + " sessionid:" + sessionid);
                HeaderUtil.removeSession();
                return ResultGen.genResult(ResultCode.SESSION_NULL_SERVER);
            } else {
                String codeId = (String) session.get("codeId");

                if (!authCode.equalsIgnoreCase(codeId)) {
                    log.error("验证码不一致,authCode:{}；codeId:{}", authCode, codeId);
                    return ResultGen.genResult(ResultCode.SSO_FAIL, "验证码错误");
                } else {

                    String email = JsonUtil.getString(params,"email");
                    String password = JsonUtil.getString(params,"password");
                    String name = JsonUtil.getString(params,"name");
                    if (StringUtils.isBlank(email)) {
                        return ResultGen.genResult(ResultCode.ARG_NEED, "email");
                    } else if (StringUtils.isBlank(password)) {
                        return ResultGen.genResult(ResultCode.ARG_NEED, "password");
                    } else if (StringUtils.isBlank(name)) {
                        return ResultGen.genResult(ResultCode.ARG_NEED, "name");
                    } else {
                        return UserService.register(email, name, password);

                    }
                }
            }
        }
    }

    @ContentType(ContentTypeEnum.HTML)
    @RequestMapping("emailCheck")
    public Model emailCheck(@RequestParam(name = "id", required = true) Long id,
                            @RequestParam(name = "token", required = true) String token) {
        Model model = new Model();
        Date tokenExptime = UserExtDao.tokenExptimeByUserid(id);
        Integer stauts = UserDao.statusById(id);

        Map map = new HashMap();
        if (tokenExptime == null || stauts == null) {
            map.put("status", UserStatusEnum.USER_NO_EXIST.getMessage());
        } else if (stauts == 0) {
            map.put("status", UserStatusEnum.TOKEN_SUCCEED.getMessage());
        } else if (!token.equalsIgnoreCase(token)) {
            map.put("status", UserStatusEnum.TOKEN_NO_RIGHT.getMessage());
        } else {
            //距离此刻几分钟
            Long distance = TimeUtil.timestampDistance(System.currentTimeMillis(), tokenExptime.getTime());
            //如果小于24小时
            if (distance < 60 * 24 && distance > 0) {
                Map<String, Object> valueMap = new HashMap<>();
                Map<String, Object> whereMap = new HashMap<>();
                valueMap.put("status", 0);
                whereMap.put("id", id);
                int row = UserDao.update(UserDao.tableName, valueMap, whereMap);
                if (row > 0) {
                    map.put("status", UserStatusEnum.SUCCESS.getMessage());

                } else {
                    map.put("status", UserStatusEnum.EXCEPTION.getMessage());
                }
            } else {
                //token过期
                try {
                    map.put("status", UserService.emailCheck(id).getMessage());
                } catch (Exception e) {
                    map.put("status", UserStatusEnum.EXCEPTION.getMessage());
                }
            }
        }

        model.setPath("emailCheck.html");
        model.setMap(map);
        return model;
    }


    /**
     * 清理session
     *
     * @return
     */
    @RequestMapping("cleanMap")
    public String cleanMap() {
        SessionMapService.cleanMap();
        return "清理成功";
    }

}
