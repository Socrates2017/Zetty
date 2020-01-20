package com.zrzhen.sqlgraph.controller;

import com.zrzhen.sqlgraph.dao.JobDao;
import com.zrzhen.sqlgraph.pojo.result.Result;
import com.zrzhen.sqlgraph.pojo.result.ResultCode;
import com.zrzhen.sqlgraph.pojo.result.ResultGen;
import com.zrzhen.zetty.http.mvc.ContentTypeEnum;
import com.zrzhen.zetty.http.mvc.anno.ContentType;
import com.zrzhen.zetty.http.mvc.anno.Controller;
import com.zrzhen.zetty.http.mvc.anno.PathVariable;
import com.zrzhen.zetty.http.mvc.anno.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author chenanlian
 */

@Controller
public class JobController {


    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/data/getAllJobs")
    public Result getAllJobs() {
        List<Map<String, Object>> jobs= JobDao.allJob();
        return ResultGen.genResult(ResultCode.SUCCESS,jobs);
    }

    @ContentType(ContentTypeEnum.JSON)
    @RequestMapping("/data/getAllDbs")
    public Result getAllDbs() {
        List<Map<String, Object>> jobs= JobDao.allJob();
        return ResultGen.genResult(ResultCode.SUCCESS,jobs);
    }

    @ContentType(ContentTypeEnum.HTML)
    @RequestMapping("/jobs/{id}")
    public String job(@PathVariable Integer id) {

        return "jobDetail.html";

    }

}
