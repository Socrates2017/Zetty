package com.zrzhen.zetty.http.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.dao.ReplyDao;
import com.zrzhen.zetty.http.dao.UserDao;
import com.zrzhen.zetty.http.http.http.Request;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.anno.*;
import com.zrzhen.zetty.common.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import com.zrzhen.zetty.http.service.UserService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/reply/")
public class ReplyController {


    /**
     * 查询回复
     *
     * @param id
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("article/{id}")
    public Result article(@PathVariable Integer id) {
        List<Map<String, Object>> list = ReplyDao.getList2(id);
        if (list != null) {
            list = ReplyDao.getList2(id);
            return ResultGen.genResult(ResultCode.SUCCESS, list);
        } else {
            return ResultGen.genResult(ResultCode.FAIL);
        }
    }

    /**
     * @param params
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("add")
    public Result add(@RequestJsonBody JsonNode params) {

        Long userid = UserService.getUserid();
        String articleIdStr = JsonUtil.getString(params, "articleId").trim();
        String content = JsonUtil.getString(params, "content");

        if (StringUtils.isBlank(articleIdStr)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "articleId");
        } else if (StringUtils.isBlank(content)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "content");
        } else {

            Integer articleId = Integer.valueOf(articleIdStr);

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("article_id", articleId);
            valueMap.put("content", content);
            if (userid != null) {
                valueMap.put("user_id", userid);
                String name = UserDao.nameById(userid);
                valueMap.put("user_name", name);
            } else {
                Request request = Request.get();
                valueMap.put("user_name", request.getHost());
            }
            int row = ReplyDao.insert(ReplyDao.tableName, valueMap);
            if (row == 1) {
                return ResultGen.genResult(ResultCode.SUCCESS, row);
            } else {
                return ResultGen.genResult(ResultCode.FAIL, row);
            }

        }
    }
}
