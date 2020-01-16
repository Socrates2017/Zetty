package com.zrzhen.sqlgraph.controller;

import com.zrzhen.sqlgraph.dao.JobDao;
import com.zrzhen.sqlgraph.pojo.result.Result;
import com.zrzhen.sqlgraph.pojo.result.ResultCode;
import com.zrzhen.sqlgraph.pojo.result.ResultGen;
import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.mvc.anno.ContentType;
import com.zrzhen.zetty.http.mvc.anno.Controller;
import com.zrzhen.zetty.http.mvc.anno.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */

@Controller
public class UserController {


    /**
     * 加密
     *
     * @return
     */
    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/user/get/name")
    public Result getAllJobs() {

        List<Map<String, Object>> jobs= JobDao.allJob();
        return ResultGen.genResult(ResultCode.SUCCESS,jobs);

    }


}
