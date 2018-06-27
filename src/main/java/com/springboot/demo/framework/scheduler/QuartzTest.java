package com.springboot.demo.framework.scheduler;

import java.text.ParseException;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p><b>QuartzTest Description:</b>简单的quartz之mysql数据库配置实现</p>
 *
 * @author wanchongyang
 * <b>DATE</b> 2016年6月28日 下午4:17:07
 */
public class QuartzTest {
    private static String JOB_GROUP_NAME = "jmcs_job_group";
    private static String TRIGGER_GROUP_NAME = "jmcs_trigger_group";

    public static void main(String[] args) throws SchedulerException, ParseException {
        startSchedule();
//		resumeJob();
    }

    /**
     * 开始一个simpleSchedule()调度
     *
     * @throws ParseException
     */
    public static void startSchedule() throws ParseException {
        try {
            // 1、创建一个JobDetail实例，指定Quartz
            JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                    // 任务执行类
                    .withIdentity(JOB_GROUP_NAME, TRIGGER_GROUP_NAME)
                    // 任务名，任务组
                    .build();
            // 2、创建Trigger
            // 设置执行次数
//			SimpleScheduleBuilder.simpleSchedule();
//			SimpleScheduleBuilder builder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(100);
//			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1_1", "tGroup1").startNow().withSchedule(builder).build();

            // 创建CronTrigger
            // 每隔5秒执行一次
            CronExpression cronExpression = new CronExpression("*/5 * * * * ?");
            CronScheduleBuilder cronBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cron_trigger1_1", "cron_tGroup1").withSchedule(cronBuilder).build();

            // 3、创建Scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            scheduler.start();
            // 4、调度执行
//			scheduler.scheduleJob(jobDetail, trigger);
            scheduler.scheduleJob(jobDetail, cronTrigger);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中找到已经存在的job，并重新开户调度
     */
    public static void resumeJob() {
        try {

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            // ①获取调度器中所有的触发器组
            List<String> triggerGroups = scheduler.getTriggerGroupNames();
            // ②重新恢复在tgroup1组中，名为trigger1_1触发器的运行
            for (int i = 0; i < triggerGroups.size(); i++) {
                List<String> triggers = scheduler.getTriggerGroupNames();
                for (int j = 0; j < triggers.size(); j++) {
                    Trigger tg = scheduler.getTrigger(new TriggerKey(triggers.get(j), triggerGroups.get(i)));
                    // ②-1:根据名称判断
//					if (tg instanceof SimpleTrigger && tg.getDescription().equals("tgroup1.trigger1_1")) {
                    if (tg instanceof SimpleTrigger && tg.getDescription().equals("cron_tGroup1.cron_trigger1_1")) {
                        // ②-1:恢复运行
                        scheduler.resumeJob(new JobKey(triggers.get(j), triggerGroups.get(i)));
                    }
                }

            }
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
