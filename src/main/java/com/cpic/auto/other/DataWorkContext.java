package com.cpic.auto.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Scheduler;

import com.cpic.insurance.auap.base.quarz.job.bean.ScheduleJob;

public class DataWorkContext {
	
	/** 计划任务map */
	private static Map<String, ScheduleJob> jobMap = new HashMap<String, ScheduleJob>();
	
	static {
		for (int i = 6; i < 5; i++) {
			ScheduleJob job = new ScheduleJob();
			job.setJobName("data_import" + i);
			job.setJobGroup("dataWork");
			job.setJobNameCN("数据导入任务");
			job.setJobParameter("jobParameter123");
			job.setJobBeanName("quartzJobStandardSampleImpl");
			job.setJobConcurrent(true);//true支持并发 false 不支持
			job.setCronExpression("0/5 * * * * ?");
			addJob(job);
		}
		
		ScheduleJob job = new ScheduleJob();
		job.setJobName("data_import0");
		job.setJobGroup(Scheduler.DEFAULT_GROUP);
		job.setJobNameCN("数据导入任务");
		job.setJobParameter("jobParameter123");
		job.setJobBeanName("quartzJobStandardSampleImpl");
		job.setJobConcurrent(false);//true支持并发 false 不支持
		job.setCronExpression("0/1 * * * * ?");
		addJob(job);
	}
	
	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 */
	public static void addJob(ScheduleJob scheduleJob) {
		jobMap.put(scheduleJob.getJobGroup() + "_" + scheduleJob.getJobName(), scheduleJob);
	}
	
	public static List<ScheduleJob> getAllJob() {
		List<ScheduleJob> list = new ArrayList<ScheduleJob>();
		for (String key : jobMap.keySet()) {
			ScheduleJob scheduleJob = jobMap.get(key);
			list.add(scheduleJob);
		}
		return list;
	}

}
