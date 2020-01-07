package com.quartz.quartz.service;

import com.quartz.quartz.domain.ScheduleJob;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Map;

/**
 *  @author: lusen
 *  @Date: 2019/12/24 14:50
 *  @Description: 定时任务操作接口
 */
public interface QuartzService
{

    /**
     *  @author: lusen
     *  @Date: 2019/12/24 15:12
     *  @Description: 初始化定时任务
     */
    void initTask(List<ScheduleJob> scheduleJobList);

    /**
     *  @author: lusen
     *  @Date: 2019/12/24 15:12
     *  @Description: 新增定时任务
     */
    void addJob(ScheduleJob job);

    /**
     *  @author: lusen
     *  @Date: 2019/12/24 15:13
     *  @Description: 暂停、启动、删除定时任务
     *  status:pause暂停，start启动，del删除
     */
    void operateJob(String status, ScheduleJob job);

    /**
     *  @author: lusen
     *  @Date: 2019/12/24 15:15
     *  @Description: 启动所有定时任务
     */
    void startAllJob();

    /**
     *  @author: lusen
     *  @Date: 2019/12/24 15:15
     *  @Description: 暂停所有定时任
     */
    void pauseAllJob();

    /**
     *  @author: lusen
     *  @Date: 2019/12/24 15:16
     *  @Description: 更新定时任务
     */
    void updateJob(ScheduleJob job);


    /**
     *  @author: lusen
     *  @Date: 2019/12/25 16:08
     *  @Description: c查询所有定时任务
     */
    List<Map<String, Object>> queryAllJob();


    /**
     *  @author: lusen
     *  @Date: 2019/12/25 16:09
     *  @Description: 查询所有运行中的定时任务
     */
    List<Map<String, Object>> queryRunJob();

    void runAJobNow(String jobName, String jobGroupName);

    
    /**
     *  @author: lusen
     *  @Date: 2019/12/26 10:26
     *  @Description:
     * @param jobClass
     *            任务实现类
     * @param jobName
     *            任务名称(建议唯一)
     * @param jobGroupName
     *            任务组名
     * @param jobTime
     *            时间表达式 （如：0/5 * * * * ? ）
     * @param jobData
     *            参数
     */
    void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobTime, Map jobData);

}
