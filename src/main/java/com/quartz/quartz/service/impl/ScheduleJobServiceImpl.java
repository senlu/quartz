package com.quartz.quartz.service.impl;

import com.quartz.quartz.domain.ScheduleJob;
import com.quartz.quartz.service.QuartzService;
import com.quartz.quartz.service.ScheduleJobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lusen
 * @Date: 2019/12/24 15:22
 * @Description: 定时任务操作业务实现
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService
{
    @Autowired
    private QuartzService quartzService;

    @Override
    public void initTask()
    {
        //查询数据库是否存在需要定时的任务,此处模拟改为手动添加
        List<ScheduleJob> scheduleJobs = new ArrayList<>();
       for(int i =1;i<=3;i++)
       {
           ScheduleJob job = new ScheduleJob();
           job.setId(1);
           job.setBeanName("test");
           job.setMethodName("test");
           job.setCexp("0/1 * * * * ?");

           //job.setParameter("测试定时任务"+i);
           job.setParameter("");
           job.setStatus(1);
           job.setJobName("测试"+i);
           job.setSchedulerNum(1);
           scheduleJobs.add(job);
       }
        quartzService.initTask(scheduleJobs);

       Map<String,Object> data = new HashMap();
        data.put("t1","测试t1");
        data.put("t2","测试t2");
       // 测试第二种添加job方式
        quartzService.addJob(TestJob.class,"第二种方式jobname",null,"0/1 * * * * ?",data);

    }

    @Override
    public void add(ScheduleJob job)
    {

        //此处省去数据验证
        //持久化定时任务
        //save(job);

        //加入job
        try
        {
            quartzService.addJob(job);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void start(int id)
    {
        //此处省去数据验证
        // 根据id查找定时任务
        //ScheduleJob job = this.getById(id);
        ScheduleJob job = new ScheduleJob();
        job.setStatus(1);
        //更新状态为执行
        //this.updateById(job);

        //执行job

        quartzService.operateJob("start", job);

    }

    @Override
    public void pause(int id)
    {
        //此处省去数据验证
        ScheduleJob job = new ScheduleJob();
        job.setStatus(2);
        //this.updateById(job);

        quartzService.operateJob("pause", job);

    }

    @Override
    public void delete(int id)
    {
        //此处省去数据验证
        ScheduleJob job = new ScheduleJob();
        //this.removeById(id);

        quartzService.operateJob("del", job);

    }

    @Override
    public void startAllJob()
    {
        //此处省去数据验证
        ScheduleJob job = new ScheduleJob();
        job.setStatus(1);
        //this.update(job, new QueryWrapper<>());

        quartzService.startAllJob();

    }

    @Override
    public void pauseAllJob()
    {
        //此处省去数据验证
        ScheduleJob job = new ScheduleJob();
        job.setStatus(2);
        //this.update(job, new QueryWrapper<>());

        quartzService.pauseAllJob();

    }
}
