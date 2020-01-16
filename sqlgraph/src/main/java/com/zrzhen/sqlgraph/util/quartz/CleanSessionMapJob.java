package com.zrzhen.sqlgraph.util.quartz;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenanlian
 */
public class CleanSessionMapJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(CleanSessionMapJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("session 清理 定时任务开始...");

        log.info("session 清理 定时任务结束...");


    }
}
