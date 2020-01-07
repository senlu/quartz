package com.quartz.quartz.service;

import com.quartz.quartz.domain.ScheduleJob;

/**
 * @author
 * @description
 * @data2019/12/20
 **/
public interface ScheduleJobService
{

    /**
     *  @author: lusen
     *  @Date: 2019/12/24 15:25
     *  @Description: 初始化定时任
     */
    void initTask();
    /**
     * 新增定时任务
     *
     * @author lanjerry
     * @date 2019/1/28 15:37
     * @param job 任务
     */
    void add(ScheduleJob job);

    /**
     * 启动定时任务
     *
     * @author lanjerry
     * @date 2019/1/28 16:49
     * @param id 任务id
     */
    void start(int id);

    /**
     * 暂停定时任务
     *
     * @author lanjerry
     * @date 2019/1/28 16:49
     * @param id 任务id
     */
    void pause(int id);

    /**
     * 删除定时任务
     *
     * @author lanjerry
     * @date 2019/1/28 16:49
     * @param id 任务id
     */
    void delete(int id);

    /**
     * 启动所有定时任务
     *
     * @author lanjerry
     * @date 2019/1/28 16:49
     */
    void startAllJob();

    /**
     * 暂停所有定时任务
     *
     * @author lanjerry
     * @date 2019/1/28 16:49
     */
    void pauseAllJob();
}
