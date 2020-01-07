package com.quartz.quartz.service.impl;

import com.quartz.quartz.service.QuartzService;
import com.quartz.quartz.service.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author
 * @description
 * @data2019/12/20
 */
@Service("test")
public class TestImpl implements Test
{

    private final Logger log = LoggerFactory.getLogger(TestImpl.class);

    @Autowired
    private QuartzService quartzService;

    @Override
    public void test(String s)
    {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //System.out.println(Thread.currentThread().getName()+localDateTime.format(formatter)+s);
        log.info(s);
        try
        {
            Thread.sleep(5000L);
        }
        catch (InterruptedException e)
        {
            log.error("InterruptedException", e);
        }
    }

    @Override
    public void test()
    {
        log.info("无参定时任务执行");
    }
}
