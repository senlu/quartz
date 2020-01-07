package com.quartz.quartz.domain;

/**
 *@Description
 *@Author lusen
 *@Date 2019/12/26 11:40 
 */
public class ScheduleJob
{
    /**
     * id
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 服务名称
     */
    private String beanName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法参数
     */
    private String parameter;

    /**
     * cron表达式
     */
    private String cexp;

    /**
     * 状态
     */
    private int status;

    /**
     * 定是任务启动个数
     */
    private int schedulerNum;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getJobDesc()
    {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc)
    {
        this.jobDesc = jobDesc;
    }

    public String getBeanName()
    {
        return beanName;
    }

    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public String getParameter()
    {
        return parameter;
    }

    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

    public String getCexp()
    {
        return cexp;
    }

    public void setCexp(String cexp)
    {
        this.cexp = cexp;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getSchedulerNum()
    {
        return schedulerNum;
    }

    public void setSchedulerNum(int schedulerNum)
    {
        this.schedulerNum = schedulerNum;
    }
}
