package com.cpic.auto.other;

import java.util.List;

import org.quartz.SchedulerException;

import com.cpic.insurance.auap.base.quarz.ScheduleJobManager;
import com.cpic.insurance.auap.base.quarz.job.bean.ScheduleJob;

public class ScheduleJobLoad {
	
	private ScheduleJobManager scheduleJobManager;

	public ScheduleJobManager getScheduleJobManager() {
		return scheduleJobManager;
	}

	public void setScheduleJobManager(ScheduleJobManager scheduleJobManager) {
		this.scheduleJobManager = scheduleJobManager;
	}
	
	public void LoadScheduleJob() {
		//得到所有要执行的定时任务
		List<ScheduleJob> jobList = DataWorkContext.getAllJob();
		try {
			scheduleJobManager.initializationScheduler(jobList);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
