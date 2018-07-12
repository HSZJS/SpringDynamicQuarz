package com.cpic.insurance.auap.base.quarz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.cpic.insurance.auap.base.quarz.enums.JobDataMapEnum;
import com.cpic.insurance.auap.base.quarz.job.bean.ScheduleJob;
import com.cpic.insurance.auap.base.quarz.job.bean.ScheduleJobDetail;
import com.cpic.insurance.auap.base.quarz.job.factory.QuartzConcurrentJobFactory;
import com.cpic.insurance.auap.base.quarz.job.factory.QuartzJobFactory;

@Component
public class ScheduleJobManager {
	
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	/**
	 * 启动初始化Scheduler任务
	 * 
	 * @param jobList
	 * @throws SchedulerException
	 */
	public void initializationScheduler(List<ScheduleJob> jobList) throws SchedulerException {
		for (ScheduleJob scheduleJob : jobList) {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null == trigger) {
				// 没有就添加运行
				this.scheduleJob(scheduleJob);
			} else {
				// 有就重新设置时间运行
				this.rescheduleJob(scheduleJob);
			}
		}
	}

	/**
	 * 暂停一个任务
	 * 
	 * @param scheduleJob
	 */
	public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.pauseJob(jobKey);
	}
	
	/**
	 * 恢复一个任务(会补偿错过的任务)
	 * 
	 * @param scheduleJob
	 */
	public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.resumeJob(jobKey);
	}
	
	/**
	 * 删除一个任务
	 * 
	 * @param scheduleJob
	 */
	public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.pauseTrigger(triggerKey);// 停止触发器  
		scheduler.unscheduleJob(triggerKey);// 移除触发器 
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.deleteJob(jobKey);
	}
	
	/**
	 * 添加一个任务(按CronExpression时间自动运行)
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void scheduleJob(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		String jobName = scheduleJob.getJobName();
		String jobGroup = scheduleJob.getJobGroup();
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		Class<? extends Job> jobClass = QuartzJobFactory.class;//不支持并发
		if(scheduleJob.isJobConcurrent()) {
			//支持并发
			jobClass = QuartzConcurrentJobFactory.class;
		}
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
		jobDetail.getJobDataMap().put(JobDataMapEnum.SCHEDULE_JOB.toString(), scheduleJob);
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
				.withMisfireHandlingInstructionDoNothing();
		/**
		 * 不触发立即执行 等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
		 **/
		// scheduleBuilder.withMisfireHandlingInstructionDoNothing();
		/**
		 * 默认 以当前时间为触发频率立刻触发一次执行 然后按照Cron频率依次执行
		 */
		// scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
		/**
		 * 以错过的第一个频率时间立刻开始执行 重做错过的所有频率周期后 当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
		 **/
		// scheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
		
		trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, trigger);
	}
	
	/**
	 * 重新设置一个任务(按照CronExpression时间自动运行)
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void rescheduleJob(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		// 按新的trigger重新设置job执行
		scheduler.rescheduleJob(triggerKey, trigger);
	}
	
	/**
	 * 立刻执行一个任务(马上执行一次，仅一次，任务必须是存在的)
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void triggerJob(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.triggerJob(jobKey);
	}
	
	/**
	 * 获得计划中的所有任务(包含暂停的)
	 * 
	 * @return
	 */
	public List<ScheduleJobDetail> getTriggersOfJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		
		//返回对象
		List<ScheduleJobDetail> jobDetailList = new ArrayList<ScheduleJobDetail>();
		
		for (JobKey jobKey : jobKeys) {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			ScheduleJob scheduleJob = (ScheduleJob) jobDetail.getJobDataMap().get(JobDataMapEnum.SCHEDULE_JOB.toString());
			
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				
				ScheduleJobDetail scheduleJobDetail = new ScheduleJobDetail();
				
				//普通变量赋值
				scheduleJobDetail.setJobName(scheduleJob.getJobName());
				scheduleJobDetail.setJobGroup(scheduleJob.getJobGroup());
				scheduleJobDetail.setJobNameCN(scheduleJob.getJobNameCN());
				scheduleJobDetail.setCronExpression(scheduleJob.getCronExpression());
				scheduleJobDetail.setJobBeanName(scheduleJob.getJobBeanName());
				scheduleJobDetail.setJobParameter(scheduleJob.getJobParameter());
				scheduleJobDetail.setJobConcurrent(scheduleJob.isJobConcurrent());
				
				//运行状态属性
				scheduleJobDetail.setNextFireTime(trigger.getNextFireTime());
				scheduleJobDetail.setPreviousFireTime(trigger.getPreviousFireTime());
				scheduleJobDetail.setJobState(triggerState);
				
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					scheduleJobDetail.setCronExpression(cronTrigger.getCronExpression());
				}
				
				jobDetailList.add(scheduleJobDetail);
			}
		}
		//System.out.println(JSON.toJSONString(jobDetailList));
		return jobDetailList;
	}
	
	/**
	 * 获得当前运行中的所有任务
	 * 
	 * @return
	 */
	public List<ScheduleJobDetail> getExecutingOfJob() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		
		//返回对象
		List<ScheduleJobDetail> jobDetailList = new ArrayList<ScheduleJobDetail>(executingJobs.size());
		
		for (JobExecutionContext executingJob : executingJobs) {
			JobDetail jobDetail = executingJob.getJobDetail();
			ScheduleJob scheduleJob = (ScheduleJob) jobDetail.getJobDataMap().get(JobDataMapEnum.SCHEDULE_JOB.toString());
			
			Trigger trigger = executingJob.getTrigger();
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			
			ScheduleJobDetail scheduleJobDetail = new ScheduleJobDetail();
			
			//普通变量赋值
			scheduleJobDetail.setJobName(scheduleJob.getJobName());
			scheduleJobDetail.setJobGroup(scheduleJob.getJobGroup());
			scheduleJobDetail.setJobNameCN(scheduleJob.getJobNameCN());
			scheduleJobDetail.setCronExpression(scheduleJob.getCronExpression());
			scheduleJobDetail.setJobBeanName(scheduleJob.getJobBeanName());
			scheduleJobDetail.setJobParameter(scheduleJob.getJobParameter());
			scheduleJobDetail.setJobConcurrent(scheduleJob.isJobConcurrent());
			
			//运行状态属性
			scheduleJobDetail.setNextFireTime(trigger.getNextFireTime());
			scheduleJobDetail.setPreviousFireTime(trigger.getPreviousFireTime());
			scheduleJobDetail.setJobState(triggerState);
			
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				scheduleJobDetail.setCronExpression(cronExpression);
			}
			
			jobDetailList.add(scheduleJobDetail);
		}
		//System.out.println(JSON.toJSONString(jobDetailList));
		return jobDetailList;
	}

}
