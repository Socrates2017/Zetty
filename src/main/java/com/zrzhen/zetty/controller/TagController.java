package com.zrzhen.zetty.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.core.util.JsonUtil;
import com.zrzhen.zetty.dao.ArticleDao;
import com.zrzhen.zetty.dao.ArticleTagDao;
import com.zrzhen.zetty.dao.TagDao;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
import com.zrzhen.zetty.core.mvc.ContentTypeEnum;
import com.zrzhen.zetty.core.mvc.anno.*;
import com.zrzhen.zetty.service.UserService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 * 标签路由控制类
 */
@Controller("/tag/")
public class TagController {

    /**
     * 查询所有标签
     *
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("all")
    public Result all() {
        List<String> list = TagDao.allTags();
        return ResultGen.genResult(ResultCode.SUCCESS, list);
    }


    /**
     * 查询某篇文章添加的标签
     *
     * @param id
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("tagForArticleUpdate/{id}")
    public Result tagForArticleUpdate(@PathVariable Integer id) {
        Long userid = UserService.getUserid();
        if (null == userid) {
            return ResultGen.genResult(ResultCode.SESSION_LOGOUT);
        }

        Map<String, Object> articleInDb = ArticleDao.oneByKey(id);

        if (articleInDb == null) {
            return ResultGen.genResult(ResultCode.FAIL, "文章不存在");
        } else if (!articleInDb.get("userid").equals(userid)) {
            return ResultGen.genResult(ResultCode.FAIL, "权限不足");
        } else {

            List<String> tagsNotInArticleList = TagDao.tagsNotInArticleList(id);
            List<String> tagsInArticleList = TagDao.tagsInArticleList(id);

            HashMap<String, List<String>> data = new HashMap();
            data.put("tagsNotInArticleList", tagsNotInArticleList);
            data.put("tagsInArticleList", tagsInArticleList);

            return ResultGen.genResult(ResultCode.SUCCESS, data);
        }
    }


    /**
     * 为某篇文章添加标签
     *
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("add")
    public Result add(@RequestJsonBody JsonNode params) {

        Long userid = UserService.getUserid();
        String id = JsonUtil.getString(params,"id").trim();
        String tag = JsonUtil.getString(params,"tag");

        if (StringUtils.isBlank(id)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "id");
        } else if (StringUtils.isBlank(tag)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "tag");
        } else {

            Integer articleId = Integer.valueOf(id);
            Map<String, Object> articleInDb = ArticleDao.oneByKey(articleId);

            if (articleInDb == null) {
                return ResultGen.genResult(ResultCode.FAIL, "文章不存在");

            } else if (!articleInDb.get("userid").equals(userid)) {
                return ResultGen.genResult(ResultCode.FAIL, "权限不足");
            } else {
                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("tag", tag);
                valueMap.put("article", articleId);
                valueMap.put("uuser", userid);
                return ResultGen.genResult(ResultCode.SUCCESS, ArticleTagDao.insert(ArticleTagDao.tableName, valueMap));
            }
        }
    }

    /**
     * 为某篇文章删除某标签
     *
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("delete")
    public Result delete(@RequestJsonBody JsonNode params) {

        Long userid = UserService.getUserid();

        String id = JsonUtil.getString(params,"id").trim();
        String tag = JsonUtil.getString(params,"tag");

        if (StringUtils.isBlank(id)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "id");
        } else if (StringUtils.isBlank(tag)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "tag");
        } else {

            Integer articleId = Integer.valueOf(id);
            Map<String, Object> articleInDb = ArticleDao.oneByKey(articleId);

            if (articleInDb == null) {
                return ResultGen.genResult(ResultCode.FAIL, "文章不存在");

            } else if (!articleInDb.get("userid").equals(userid)) {
                return ResultGen.genResult(ResultCode.FAIL, "权限不足");
            } else {
                Map<String, Object> whereMap = new HashMap<>();
                whereMap.put("tag", tag);
                whereMap.put("article", articleId);
                return ResultGen.genResult(ResultCode.SUCCESS, ArticleTagDao.delete(ArticleTagDao.tableName, whereMap));
            }
        }
    }

}
