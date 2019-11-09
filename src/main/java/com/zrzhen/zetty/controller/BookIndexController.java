package com.zrzhen.zetty.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.core.mvc.ContentTypeEnum;
import com.zrzhen.zetty.core.mvc.anno.*;
import com.zrzhen.zetty.core.util.JsonUtil;
import com.zrzhen.zetty.dao.BookIndexDao;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
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

        String parentId = JsonUtil.getString(params, "parentId").trim();
        String url = JsonUtil.getString(params, "url").trim();
        String childTitle = JsonUtil.getString(params, "childTitle").trim();
        String book = JsonUtil.getString(params, "bookId").trim();
        String indexOrder = JsonUtil.getString(params, "indexOrder").trim();


        if (StringUtils.isBlank(parentId)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "parentId");
        } else if (StringUtils.isBlank(url)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "childId");
        } else if (StringUtils.isBlank(childTitle)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "childTitle");
        } else if (StringUtils.isBlank(book)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "book");
        } else {
            int order = StringUtils.isBlank(indexOrder)?0:Integer.valueOf(indexOrder);

            Integer parent = Integer.valueOf(parentId);
            Long bookId = Long.valueOf(book);

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("parent", parent);
            valueMap.put("url", url);
            valueMap.put("name", childTitle);
            valueMap.put("book", bookId);
            valueMap.put("index_order", order);
            return ResultGen.genResult(ResultCode.SUCCESS, BookIndexDao.insert(BookIndexDao.tableName, valueMap));

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
    @RequestMapping("/update")
    public Result update(@RequestJsonBody JsonNode params) {

        String parentId = JsonUtil.getString(params, "id").trim();
        String url = JsonUtil.getString(params, "url").trim();
        String name = JsonUtil.getString(params, "name").trim();
        String indexOrder = JsonUtil.getString(params, "indexOrder").trim();

        if (StringUtils.isBlank(parentId)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "parentId");
        } else if (StringUtils.isBlank(url)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "childId");
        } else if (StringUtils.isBlank(name)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "name");
        } else {
            int order = StringUtils.isBlank(indexOrder)?0:Integer.valueOf(indexOrder);

            Integer parent = Integer.valueOf(parentId);
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("url", url);
            valueMap.put("name", name);
            valueMap.put("index_order", order);

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

        String id = JsonUtil.getString(params, "id").trim();
        String pid = JsonUtil.getString(params, "pid").trim();

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
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/delete")
    public Result delete(@RequestJsonBody JsonNode params) {
        String id = JsonUtil.getString(params, "id").trim();

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
