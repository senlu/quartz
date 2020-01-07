package com.quartz.quartz.service.impl;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author
 * @description 另一种执行定时任的方式，直接继承QuartzJobBean，重写executeInternal
 * 方法，该方法内添加业务逻辑
 * @data2019/12/26
 */
public class TestJob extends QuartzJobBean
{
    private final Logger log = LoggerFactory.getLogger(TestJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext)
    {
        // 获取参数
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        // 业务逻辑 ...
        log.info("获取参数t1:"+jobDataMap.get("t1")+",参数t2:"+jobDataMap.get("t2"));
    }
}
