package com.zrzhen.zetty.cms.util.quartz;

import com.zrzhen.zetty.cms.controller.UserController;
import com.zrzhen.zetty.cms.service.SessionMapService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenanlian
 */
public class CleanSessionMapJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("session 清理 定时任务开始...");
        SessionMapService.cleanMap();
        log.info("session 清理 定时任务结束...");


    }
}
