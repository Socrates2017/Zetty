package com.zrzhen.zetty.cms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.common.ShiftLeft;
import com.zrzhen.zetty.common.TimeUtil;
import com.zrzhen.zetty.cms.constant.UserStatusEnum;
import com.zrzhen.zetty.cms.dao.OpenidDao;
import com.zrzhen.zetty.cms.dao.UserDao;
import com.zrzhen.zetty.cms.dao.UserExtDao;
import com.zrzhen.zetty.cms.dao.UserOpenidDao;
import com.zrzhen.zetty.cms.dao.jdbc.MysqlFirst;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.cms.pojo.result.Result;
import com.zrzhen.zetty.cms.pojo.result.ResultCode;
import com.zrzhen.zetty.cms.pojo.result.ResultGen;
import com.zrzhen.zetty.cms.thread.ThreadPoolUtil;
import com.zrzhen.zetty.cms.thread.UserLoginThread;
import com.zrzhen.zetty.cms.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenanlian
 */
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    /**
     * 检查是否登录
     *
     * @return
     */
    public static Result checkLogin() {
        String sessionid = HeaderUtil.getSession();
        if (StringUtils.isBlank(sessionid)) {
            log.warn(ResultCode.SESSION_NULL_CLIENT.getMessage() + " sessionid:" + sessionid);
            return ResultGen.genResult(ResultCode.SESSION_NULL_CLIENT);
        } else {
            /*检查sessionMap中是否有openid*/
            HashMap map = SessionMapService.sessions.get(sessionid);
            if (map != null) {
                Long openid = (Long) map.get("openid");
                if (openid != null) {
                    /*根据openid和系统码查出用户信息*/
                    Long userid = SessionService.getUseridByOpenid(HeaderUtil.SYSCODE, openid);
                    if (userid != null) {
                        /*添加用户id*/
                        HeaderUtil.setUser(userid);
                        Map<String, Object> user = UserDao.selectByPrimaryKey(userid);
                        return ResultGen.genResult(ResultCode.SUCCESS, user);
                    } else {
                        log.error("用户sso账户为空,openid:{}", openid);
                        HeaderUtil.removeUser();
                        return ResultGen.genResult(ResultCode.SESSION_NULL_USER);
                    }
                } else {
                    /*未登录状态*/
                    HeaderUtil.removeUser();
                    return ResultGen.genResult(ResultCode.SESSION_LOGOUT);
                }
            } else {
                /*服务端session缺失，需要重新生成*/
                log.warn(ResultCode.SESSION_NULL_SERVER.getMessage() + " sessionid:" + sessionid);
                HeaderUtil.removeSession();
                HeaderUtil.removeUser();
                return ResultGen.genResult(ResultCode.SESSION_NULL_SERVER);
            }
        }
    }

    /**
     * 登录
     *
     * @param params
     * @return
     */
    public static Result login(JsonNode params) {

        boolean out = false;
        String sessionid = SessionUtil.genSessionid();
        String email = JsonUtil.getString(params, "email");
        String password = JsonUtil.getString(params, "password");

        if (StringUtils.isBlank(email)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "email");
        } else if (StringUtils.isBlank(password)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "password");
        } else {
            Map<String, Object> user = UserDao.idByEmailAndPwd(email, password);
            if (user != null) {
                Long id = ((BigInteger) user.get("id")).longValue();
                /*获得openid*/
                Long openid = UserOpenidDao.selectOpenidByKey(HeaderUtil.SYSCODE, String.valueOf(id));
                if (openid != null) {
                    out = SessionMapService.addSession(sessionid, openid);
                }

                if (out) {
                    /*添加sessionid给cookie*/
                    HeaderUtil.setSession(sessionid);
                    /*添加用户id*/
                    HeaderUtil.setUser(id);

                    ThreadPoolUtil.logExecutor.execute(new UserLoginThread(id));

                    return ResultGen.genResult(ResultCode.SUCCESS, "登录成功");
                } else {
                    return ResultGen.genResult(ResultCode.SSO_FAIL, "SSO登录失败");
                }

            } else {
                user = UserDao.statusbyEmail(email);
                if (user == null) {
                    return ResultGen.genResult(ResultCode.SSO_FAIL, "邮箱未注册");
                } else if (1 == (Integer) user.get("status")) {
                    return ResultGen.genResult(ResultCode.SSO_FAIL, "账户未激活");
                } else if (1 < (Integer) user.get("status")) {
                    return ResultGen.genResult(ResultCode.SSO_FAIL, "账户状态异常，请联系管理员");
                } else {
                    return ResultGen.genResult(ResultCode.SSO_FAIL, "密码错误");
                }
            }
        }
    }

    /**
     * 退出登录
     *
     * @param sessionid
     * @return
     */
    public static Result logout(String sessionid) {
        /*检查sessionMap是否有sessionid*/
        boolean checkSessionidResultcode = SessionService.checkLogin(sessionid);
        if (checkSessionidResultcode) {
            //删除服务端sessionid
            boolean rmSessionidResultcode = SessionMapService.rmSession(sessionid);
            if (rmSessionidResultcode) {
                HeaderUtil.removeCookie(HeaderUtil.SESSION);
                HeaderUtil.removeCookie(HeaderUtil.SYSCODE_STRING);
                return ResultGen.genResult(ResultCode.SUCCESS, "登出成功");
            } else {
                return ResultGen.genResult(ResultCode.FAIL, "登出失败,请重试");
            }

        } else {
            return ResultGen.genResult(ResultCode.SUCCESS, "账户未在登录状态");
        }

    }

    /**
     * 注册
     *
     * @param email
     * @param name
     * @param password
     * @return
     */
    public static Result register(String email, String name, String password) {

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("password", password);
        user.put("email", email);
        user.put("id", UserUtil.userid());
        user.put("status", (short) 1);

        long time = System.currentTimeMillis();
        String token = DigestUtils.md5Hex(ShiftLeft.shift(email + password + time, -5));
        // 创建激活码
        long tokenExptime = time + 1000 * 60 * 60 * 24;
        // 激活有效时间为24小时内

        Map<String, Object> userExt = new HashMap<>(6);
        userExt.put("userid", user.get("id"));
        userExt.put("token", token);
        userExt.put("token_exptime", TimeUtil.timestamp2Date(tokenExptime));

        Map<String, Object> openid = new HashMap<>(3);
        openid.put("num", 1);

        Map<String, Object> userOpenid = new HashMap<>(3);
        userOpenid.put("sys_code", HeaderUtil.SYSCODE);
        userOpenid.put("userid", String.valueOf(user.get("id")));


        try {
            MysqlFirst.instance().begin();

            UserDao.insert(UserDao.tableName, user, false);
            UserExtDao.insert(UserExtDao.tableName, userExt, false);
            Integer openidKey = OpenidDao.insertAndGetKey(OpenidDao.tableName, openid, false);
            if (openidKey == null) {
                throw new SQLException("OpenidDao insert fail,OpenidKey is null");
            }
            userOpenid.put("openid", openidKey);
            UserOpenidDao.insert(UserOpenidDao.tableName, userOpenid, false);
            MysqlFirst.instance().commit();

            String subject = "哲人镇注册验证";
            StringBuffer content = new StringBuffer("<div ><div style=\"padding:24px 20px;\">您好，" + email
                    + "<br/><br/>哲人镇是一个学习、互助平台，欢迎您的加入！<br/><br/>请点击下面链接激活账号，24小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
            content.append("<a href=\"http://www.zrzhen.com/user/emailCheck?id=");
            content.append(user.get("id"));
            content.append("&token=");
            content.append(token);
            content.append("\">http://www.zrzhen.com/user/emailCheck?id=");
            content.append(user.get("id"));
            content.append("&token=");
            content.append(token);
            content.append(
                    "</a><br/>如果以上链接无法点击，请把上面网页地址复制到浏览器地址栏中打开。<br/><br/><br/>哲人镇，智者聚焉<br/></div></div>");

            EmailUtil.sendEmail(email, subject, content.toString());

            return ResultGen.genResult(ResultCode.SUCCESS, user.get("id"));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MysqlFirst.instance().rollback();
        } finally {
            MysqlFirst.closeConnectionInThread();
        }
        return ResultGen.genResult(ResultCode.FAIL, user.get("id"));
    }


    /**
     * 如果处于非登录状态则返回空,否则返回userid
     * 如果前端sessioncid存在，而后端sessionid不存在，则返回空
     *
     * @return
     */
    public static Long getUserid() {
        String sessionid = HeaderUtil.getSession();
        if (StringUtils.isBlank(sessionid)) {
            //log.warn(ResultCode.SESSION_NULL_CLIENT.getMessage() + " sessionid:" + sessionid);
            return null;
        } else {
            return SessionService.getUseridBySessionid(HeaderUtil.SYSCODE, sessionid);
        }
    }

    public static UserStatusEnum emailCheck(Long id) {

        try {
            MysqlFirst.instance().begin();
            Map<String, Object> whereMap = new HashMap<>(3);
            whereMap.put("id", id);
            UserDao.delete(UserDao.tableName, whereMap, false);

            Map<String, Object> whereMapUserOpenid = new HashMap<>(5);
            whereMapUserOpenid.put("sys_code", HeaderUtil.SYSCODE);
            whereMapUserOpenid.put("userid", String.valueOf(id));
            UserOpenidDao.delete(UserOpenidDao.tableName, whereMapUserOpenid, false);
            MysqlFirst.instance().commit();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MysqlFirst.instance().rollback();
        } finally {
            MysqlFirst.closeConnectionInThread();
        }

        return UserStatusEnum.TOKEN_EXPIRED;
    }
}
