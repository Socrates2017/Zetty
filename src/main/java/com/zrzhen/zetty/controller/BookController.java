package com.zrzhen.zetty.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zrzhen.zetty.core.mvc.ContentTypeEnum;
import com.zrzhen.zetty.core.mvc.anno.*;
import com.zrzhen.zetty.core.util.JsonUtil;
import com.zrzhen.zetty.dao.BookDao;
import com.zrzhen.zetty.dao.BookIndexDao;
import com.zrzhen.zetty.dao.jdbc.MysqlFirst;
import com.zrzhen.zetty.pojo.result.Result;
import com.zrzhen.zetty.pojo.result.ResultCode;
import com.zrzhen.zetty.pojo.result.ResultGen;
import com.zrzhen.zetty.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */

@Controller("/book")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);


    /**
     * 返回书籍页面
     *
     * @return
     */
    @RequestMapping("/{id}")
    @ContentType(ContentTypeEnum.HTML)
    public String index() {
        return "book.html";
    }

    /**
     * 获取一本书
     *
     * @return
     */
    @RequestMapping("/info/{id}")
    @ContentType(ContentTypeEnum.JSON)
    public Result book(@PathVariable Long id) {
        Map<String, Object> book = BookDao.oneByKey(id);
        return ResultGen.genResult(ResultCode.SUCCESS, book);
    }

    /**
     * 获取书籍列表
     *
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/infoList")
    public Result infoList(@RequestParam(name = "userId", required = false) Long userId) {
        List<Map<String, Object>> list;
        if (userId == null) {
            list = BookDao.bookList();
        } else {
            list = BookDao.bookList(userId);
        }
        return ResultGen.genResult(ResultCode.SUCCESS, list);
    }

    /**
     * 新增书籍
     *
     * @param params
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/add")
    public Result add(@RequestJsonBody JsonNode params) {

        String description = JsonUtil.getString(params, "description").trim();
        String name = JsonUtil.getString(params, "name").trim();

        if (StringUtils.isBlank(name)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "name");
        } else if (StringUtils.isBlank(description)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "description");
        } else {
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("description", description);
            valueMap.put("name", name);
            Long userid = UserService.getUserid();
            valueMap.put("creator", userid);
            valueMap.put("id", System.currentTimeMillis());
            return ResultGen.genResult(ResultCode.SUCCESS, BookDao.insert(BookDao.tableName, valueMap));
        }
    }

    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/delete")
    public Result delete(@RequestJsonBody JsonNode params) {
        String id = JsonUtil.getString(params, "bookId").trim();

        if (StringUtils.isBlank(id)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "id");
        } else {
            Long idInt = Long.valueOf(id);
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("id", idInt);
            Map<String, Object> indexValueMap = new HashMap<>();
            indexValueMap.put("book", idInt);

            try {
                MysqlFirst.instance().begin();
                BookDao.delete(BookDao.tableName, valueMap, false);
                BookIndexDao.delete(BookIndexDao.tableName, indexValueMap);
                MysqlFirst.instance().commit();
                return ResultGen.genResult(ResultCode.SUCCESS);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                MysqlFirst.instance().rollback();
                return ResultGen.genResult(ResultCode.FAIL);
            } finally {
                MysqlFirst.closeConnectionInThread();
            }
        }
    }

    @BeforeAdviceAction(id = "loginBeforeAdvice,checkBookCreatorBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/update")
    public Result update(@RequestJsonBody JsonNode params) {
        String id = JsonUtil.getString(params, "bookId").trim();
        String description = JsonUtil.getString(params, "description").trim();
        String name = JsonUtil.getString(params, "name").trim();

        if (StringUtils.isBlank(id)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "id");
        } else if (StringUtils.isBlank(name)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "name");
        } else if (StringUtils.isBlank(description)) {
            return ResultGen.genResult(ResultCode.ARG_NEED, "description");
        } else {
            Long idInt = Long.valueOf(id);
            Map<String, Object> whereMap = new HashMap<>();
            whereMap.put("id", idInt);

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("description", description);
            valueMap.put("name", name);

            return ResultGen.genResult(ResultCode.SUCCESS, BookDao.update(BookDao.tableName, valueMap, whereMap));

        }

    }

    /**
     * 检查当前登陆用户是否为书籍作者
     *
     * @param book
     * @return
     */
    @BeforeAdviceAction(id = "loginBeforeAdvice")
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/checkUser")
    public Result checkUser(@RequestParam(name = "bookId", required = true) Long book) {
        Long userid = UserService.getUserid();
        long count = BookDao.checkUser(book, userid);
        if (count > 0) {
            return ResultGen.genResult(ResultCode.SUCCESS, "登陆用户是书籍创建者");
        } else {
            return ResultGen.genResult(ResultCode.FAIL, "登陆用户不是书籍创建者");
        }

    }


}
