package com.cpic.auto.controller;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cpic.auto.other.DataWorkContext;
import com.cpic.insurance.auap.base.quarz.ScheduleJobManager;
import com.cpic.insurance.auap.base.quarz.job.bean.ScheduleJob;
import com.cpic.insurance.auap.base.quarz.job.bean.ScheduleJobDetail;

@Controller
@RequestMapping("/quarz")
public class QuarzController {
	
	@Autowired
	private ScheduleJobManager scheduleJobManager;
	
	@RequestMapping(value = "/initializationScheduler.xml")
	public void initializationScheduler() {
		List<ScheduleJob> jobList = DataWorkContext.getAllJob();
		try {
			scheduleJobManager.initializationScheduler(jobList);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}
	
	@RequestMapping(value = "/pauseJob.xml")
	public void pauseJob() {
		ScheduleJob job = new ScheduleJob();
		job.setJobName("data_import_A");
		job.setJobGroup("dataWork_A");
		job.setCronExpression("0/5 * * * * ?");
		
		try {
			scheduleJobManager.pauseJob(job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/resumeJob.xml")
	public void resumeJob() {
		ScheduleJob job = new ScheduleJob();
		job.setJobName("data_import_A");
		job.setJobGroup("dataWork_A");
		job.setCronExpression("0/5 * * * * ?");
		
		try {
			scheduleJobManager.resumeJob(job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/deleteJob.xml")
	public void deleteJob() {
		ScheduleJob job = new ScheduleJob();
		job.setJobName("data_import_A");
		job.setJobGroup("dataWork_A");
		job.setCronExpression("0/5 * * * * ?");
		
		try {
			scheduleJobManager.deleteJob(job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/scheduleJob.xml")
	public void scheduleJob() {
		ScheduleJob job = new ScheduleJob();
		job.setJobName("data_import_A");
		job.setJobGroup("dataWork_A");
		job.setCronExpression("0/5 * * * * ?");
		job.setJobBeanName("quartzJobStandardSampleImpl");
		try {
			scheduleJobManager.scheduleJob(job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/rescheduleJob.xml")
	public void rescheduleJob() {
		ScheduleJob job = new ScheduleJob();
		job.setJobName("data_import_A");
		job.setJobGroup("dataWork_A");
		job.setCronExpression("0/7 * * * * ?");
		
		try {
			scheduleJobManager.rescheduleJob(job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/triggerJob.xml")
	public void triggerJob() {
		ScheduleJob job = new ScheduleJob();
		job.setJobName("data_import_A");
		job.setJobGroup("dataWork_A");
		job.setCronExpression("0/5 * * * * ?");
		
		try {
			scheduleJobManager.triggerJob(job);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/getTriggersOfJob.xml")
	public void getTriggersOfJob() {
		List<ScheduleJobDetail> list = null;
		try {
			list = scheduleJobManager.getTriggersOfJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		System.out.println("getTriggersOfJob" + list.size());
	}

	@RequestMapping(value = "/getExecutingOfJob.xml")
	public void getExecutingOfJob() {
		List<ScheduleJobDetail> list = null;
		try {
			list = scheduleJobManager.getExecutingOfJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		System.out.println("getExecutingOfJob" + list.size());
	}

}
