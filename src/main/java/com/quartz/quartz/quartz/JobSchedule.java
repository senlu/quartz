package com.quartz.quartz.quartz;

import com.quartz.quartz.service.QuartzService;
import com.quartz.quartz.service.ScheduleJobService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author: lusen
 * @Date: 2019/12/24 14:44
 * @Description: 系统启动时初始定时任务
 */
@Component
public class JobSchedule implements CommandLineRunner
{
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * @Description 初始化定时任务
     * @Author lusen
     * @Date 2019/12/24 14:44
     * @Param
     * @Return
     * @Exception
     */
    @Override
    public void run(String... strings)
        throws Exception
    {
        System.out.println("==============开始初始化定时任务==============");
        scheduleJobService.initTask();
        System.out.println("==============初始化定时任务结束==============");
    }
}
