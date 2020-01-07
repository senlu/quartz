package com.quartz.quartz.quartz;

import com.quartz.quartz.domain.ScheduleJob;
import com.quartz.quartz.utils.MethodUtil;
import com.quartz.quartz.utils.SpringContextUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @author: lusen
 * @Date: 2019/12/24 14:46
 * @Description: 定时任务执行工厂
 * DisallowConcurrentExecution 表示同一个定时任务，需要上次定时任务执行完成后，才会开始下次定时任务执行。
 */
@DisallowConcurrentExecution
public class QuartzFactory implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext)
    {
        //获取调度数据
        ScheduleJob scheduleJob =
            (ScheduleJob)jobExecutionContext.getMergedJobDataMap()
                .get("scheduleJob");

        try
        {
            //获取对应的Bean
            Object object =
                SpringContextUtil.getBean(scheduleJob.getBeanName());

            String[] parameters  = null;
            if(null == scheduleJob.getParameter() || "".equals(scheduleJob.getParameter()))
            {
                parameters = new String[]{};
            }
            else
            {
                parameters = scheduleJob.getParameter().split(",");
            }
            // 使用反射的方式执行定时任务
            MethodUtil.invokeMethod(object,
                object.getClass(),
                scheduleJob.getMethodName(),
                parameters);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
