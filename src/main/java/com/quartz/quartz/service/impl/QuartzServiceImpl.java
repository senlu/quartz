package com.quartz.quartz.service.impl;

import com.quartz.quartz.domain.ScheduleJob;
import com.quartz.quartz.quartz.QuartzFactory;
import com.quartz.quartz.service.QuartzService;
import com.quartz.quartz.service.ScheduleJobService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: lusen
 * @Date: 2019/12/24 14:49
 * @Description: 定时任务操作
 */
@Service
public class QuartzServiceImpl implements QuartzService
{
    /**
     * 调度器
     */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobService jobService;

    @Override
    public void initTask(List<ScheduleJob> scheduleJobList)
    {
        if (scheduleJobList != null)
        {
            scheduleJobList.forEach(this::addJob);
        }
    }

    @Override
    public void addJob(ScheduleJob job)
    {
        try
        {
            for (int i = 0; i < job.getSchedulerNum(); i++)
            {
                String jobName = job.getJobName() + (i + 1);
                //创建触发器
                Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCexp()))
                    //.startNow()
                    .build();
                //创建任务
                JobDetail jobDetail = JobBuilder.newJob(QuartzFactory.class)
                    .withIdentity(jobName)
                    .build();

                //传入调度的数据，在QuartzFactory中需要使用
                jobDetail.getJobDataMap().put("scheduleJob", job);

                //调度作业
                scheduler.scheduleJob(jobDetail, trigger);
                if (job.getStatus() == 0)
                {
                    JobKey jobKey = new JobKey(jobName);
                    scheduler.pauseJob(jobKey);
                }
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void operateJob(String status, ScheduleJob job)
    {
        try
        {
            for (int i = 0; i < job.getSchedulerNum(); i++)
            {
                String jobName = job.getJobName() + (i + 1);
                JobKey jobKey = new JobKey(jobName);
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                if (jobDetail == null)
                {
                    //抛异常
                }
                switch (status)
                {
                    case "start":
                        scheduler.resumeJob(jobKey);
                        break;
                    case "pause":
                        scheduler.pauseJob(jobKey);
                        break;
                    case "del":
                        scheduler.deleteJob(jobKey);
                        break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void startAllJob()
    {
        try
        {
            scheduler.start();
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseAllJob()
    {
        try
        {
            scheduler.standby();
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void updateJob(ScheduleJob job)
    {
        try
        {
            for (int i = 0; i < job.getSchedulerNum(); i++)
            {
                String jobName = job.getJobName() + (i + 1);
                TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
                CronTrigger trigger =
                    (CronTrigger)scheduler.getTrigger(triggerKey);
                trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCexp()))
                    .build();
                // 重启触发器
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void runAJobNow(String jobName, String jobGroupName)
    {
        try
        {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.triggerJob(jobKey);
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> queryAllJob()
    {
        List<Map<String, Object>> jobList = null;
        try
        {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<Map<String, Object>>();
            for (JobKey jobKey : jobKeys)
            {
                List<? extends Trigger> triggers =
                    scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers)
                {
                    Map<String, Object> map = new HashMap<>();
                    map.put("jobName", jobKey.getName());
                    map.put("jobGroupName", jobKey.getGroup());
                    map.put("description", "触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState =
                        scheduler.getTriggerState(trigger.getKey());
                    map.put("jobStatus", triggerState.name());
                    if (trigger instanceof CronTrigger)
                    {
                        CronTrigger cronTrigger = (CronTrigger)trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        map.put("jobTime", cronExpression);
                    }
                    jobList.add(map);
                }
            }
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
        return jobList;
    }

    @Override
    public List<Map<String, Object>> queryRunJob()
    {
        List<Map<String, Object>> jobList = null;
        try
        {
            List<JobExecutionContext> executingJobs =
                scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<Map<String, Object>>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                map.put("jobName", jobKey.getName());
                map.put("jobGroupName", jobKey.getGroup());
                map.put("description", "触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState =
                    scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus", triggerState.name());
                if (trigger instanceof CronTrigger)
                {
                    CronTrigger cronTrigger = (CronTrigger)trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("jobTime", cronExpression);
                }
                jobList.add(map);
            }
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
        return jobList;
    }

    @Override
    public void addJob(Class<? extends QuartzJobBean> jobClass, String jobName,
        String jobGroupName, String jobTime, Map jobData)
    {
        try
        {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroupName)
                .build();
            // 设置job参数
            if (jobData != null && jobData.size() > 0)
            {
                jobDetail.getJobDataMap().putAll(jobData);
            }
            // 定义调度触发规则
            // 使用cornTrigger规则
            // 触发器key
            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroupName)
                .startAt(DateBuilder.futureDate(1,
                    DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(jobTime))
                .startNow()
                .build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
