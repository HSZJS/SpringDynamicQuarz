package com.cpic.insurance.auap.base.quarz.job.factory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpic.insurance.auap.base.quarz.common.SpringApplicationContext;
import com.cpic.insurance.auap.base.quarz.enums.JobDataMapEnum;
import com.cpic.insurance.auap.base.quarz.job.bean.ScheduleJob;
import com.cpic.insurance.auap.base.quarz.job.standard.QuartzJobStandard;

/**
 * 统一调度任务类(支持并发)
 * 所有的定时任务都是调用该类，由该类实现调用真正的定时任务实现
 * @author wuzelin
 * 
 */
public class QuartzConcurrentJobFactory implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(QuartzConcurrentJobFactory.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get(JobDataMapEnum.SCHEDULE_JOB.toString());
        
        QuartzJobStandard quartzJobStandard = (QuartzJobStandard) SpringApplicationContext.getBean(scheduleJob.getJobBeanName());
        
        logger.debug("[定时任务][开始]JobName:{},JobNameCN:{},JobGroup:{},CronExpression:{},JobBeanName:{},JobParameter:{}",
        		scheduleJob.getJobName(),
        		scheduleJob.getJobNameCN(),
        		scheduleJob.getJobGroup(),
        		scheduleJob.getCronExpression(),
        		scheduleJob.getJobBeanName(),
        		scheduleJob.getJobParameter());
        //调用真正的执行类
        quartzJobStandard.executeJob(scheduleJob.getJobParameter());
        
        logger.debug("[定时任务][结束]JobName:{},JobNameCN:{},JobGroup:{},CronExpression:{},JobBeanName:{},JobParameter:{}",
        		scheduleJob.getJobName(),
        		scheduleJob.getJobNameCN(),
        		scheduleJob.getJobGroup(),
        		scheduleJob.getCronExpression(),
        		scheduleJob.getJobBeanName(),
        		scheduleJob.getJobParameter());
        
	}
	
}
