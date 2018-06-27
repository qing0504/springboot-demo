package com.springboot.demo.framework.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("quartz ===================== ");
        System.out.println("Hello quzrtz  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));

    }

}
