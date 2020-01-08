package com.zrzhen.zetty.http.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.http.dao.BookIndexDao;
import com.zrzhen.zetty.http.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.http.mvc.Model;
import com.zrzhen.zetty.http.http.mvc.anno.*;
import com.zrzhen.zetty.http.http.util.JsonUtil;
import com.zrzhen.zetty.http.pojo.result.Result;
import com.zrzhen.zetty.http.pojo.result.ResultCode;
import com.zrzhen.zetty.http.pojo.result.ResultGen;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */

@Controller("/bookIndex")
public class BookIndexController {


    /**
     * 根据节点id查找子节点
     *
     * @param parent
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/children")
    public Result index(@RequestParam(name = "book", required = true) Long book,
                        @RequestParam(name = "parent", defaultValue = "0") Integer parent) {
        List<Map<String, Object>> list;
        if (parent == 0) {
            list = BookIndexDao.listByParent(book, parent);
        } else {
            list = BookIndexDao.listByParent(parent);
        }

        return ResultGen.genResult(ResultCode.SUCCESS, list);
    }


    /**
     * 新增章节
     *
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/add")
    public Result add(@RequestJsonBody JsonNode params) {

        String parentId = JsonUtil.getString(params, "parentId");
        String url = JsonUtil.getString(params, "url");
        String childTitle = JsonUtil.getString(params, "childTitle");
        String book = JsonUtil.getString(params, "bookId");
        String indexOrder = JsonUtil.getString(params, "indexOrder");
        String isLeaf = JsonUtil.getString(params, "isLeaf");


        if (StringUtils.isBlank(parentId)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "parentId");
        } else if (StringUtils.isBlank(url)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "childId");
        } else if (StringUtils.isBlank(childTitle)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "childTitle");
        } else if (StringUtils.isBlank(book)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "book");
        } else {
            float order = StringUtils.isBlank(indexOrder) ? 0 : Float.valueOf(indexOrder);
            int isLeafInt = StringUtils.isBlank(isLeaf) ? 0 : Integer.valueOf(isLeaf);

            Integer parent = Integer.valueOf(parentId);
            Long bookId = Long.valueOf(book);

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("parent", parent);
            valueMap.put("url", url);
            valueMap.put("name", childTitle);
            valueMap.put("book", bookId);
            valueMap.put("index_order", order);
            valueMap.put("is_leaf", isLeafInt);

            return ResultGen.genResult(ResultCode.SUCCESS, BookIndexDao.insert(BookIndexDao.tableName, valueMap));

        }
    }

    /**
     * 返回新增文章页面，并添加到书籍中
     *
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.HTML)
    @RequestMapping("/addByAddArticle")
    public Model addByAddArticle(@RequestJsonBody JsonNode params) {
        Model model = new Model();
        model.setPath("articleAdd.html");
        model.setMap(JsonUtil.jsonNode2Map(params));
        return model;
    }

    /**
     * 更新章节
     *
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/update")
    public Result update(@RequestJsonBody JsonNode params) {

        String parentId = JsonUtil.getString(params, "id");
        String url = JsonUtil.getString(params, "url");
        String name = JsonUtil.getString(params, "name");
        String indexOrder = JsonUtil.getString(params, "indexOrder");
        String isLeaf = JsonUtil.getString(params, "isLeaf");

        if (StringUtils.isBlank(parentId)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "parentId");
        } else if (StringUtils.isBlank(url)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "childId");
        } else if (StringUtils.isBlank(name)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "name");
        } else {
            float order = StringUtils.isBlank(indexOrder) ? 0 : Float.valueOf(indexOrder);
            int isLeafInt = StringUtils.isBlank(isLeaf) ? 0 : Integer.valueOf(isLeaf);

            Integer parent = Integer.valueOf(parentId);
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("url", url);
            valueMap.put("name", name);
            valueMap.put("index_order", order);
            valueMap.put("is_leaf", isLeafInt);

            Map<String, Object> whereMap = new HashMap<>();
            whereMap.put("id", parent);
            return ResultGen.genResult(ResultCode.SUCCESS, BookIndexDao.update(BookIndexDao.tableName, valueMap, whereMap));
        }
    }

    /**
     * 更新章节
     *
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/updatePid")
    public Result updatePid(@RequestJsonBody JsonNode params) {

        String id = JsonUtil.getString(params, "id");
        String pid = JsonUtil.getString(params, "pid");

        if (StringUtils.isBlank(id)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "id");
        } else if (StringUtils.isBlank(pid)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "pid");
        } else {
            Integer idInt = Integer.valueOf(id);
            Integer pidInt = Integer.valueOf(pid);
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("parent", pidInt);

            Map<String, Object> whereMap = new HashMap<>();
            whereMap.put("id", idInt);
            return ResultGen.genResult(ResultCode.SUCCESS, BookIndexDao.update(BookIndexDao.tableName, valueMap, whereMap));
        }
    }

    /**
     * 删除章节
     *
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/delete")
    public Result delete(@RequestJsonBody JsonNode params) {
        String id = JsonUtil.getString(params, "id");

        if (StringUtils.isBlank(id)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "id");
        } else {
            Integer idInt = Integer.valueOf(id);
            List<Map<String, Object>> list = BookIndexDao.listByParent(idInt);
            if (list != null && list.size() > 0) {
                return ResultGen.genResult(ResultCode.FAIL, "请先删除子章节！");
            } else {

                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("id", idInt);
                return ResultGen.genResult(ResultCode.SUCCESS, BookIndexDao.delete(BookIndexDao.tableName, valueMap));
            }
        }
    }
}
