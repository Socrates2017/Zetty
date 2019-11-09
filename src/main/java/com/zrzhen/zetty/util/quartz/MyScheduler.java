package com.zrzhen.zetty.util.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author chenanlian
 */
public class MyScheduler {

    public static void start() throws SchedulerException, InterruptedException {
        // 1、创建调度器Scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(CleanSessionMapJob.class)
                //.usingJobData("jobDetail1", "这个Job用来测试的")
                .withIdentity("job1", "group1").build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                //.usingJobData("trigger1", "这是jobDetail1的trigger")
                .startNow()//立即生效
                //.startAt(startDate)
                //.endAt(endDate)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 12 * * ?"))
                .build();
        //4、执行
        scheduler.scheduleJob(jobDetail, cronTrigger);
        scheduler.start();
    }
}
