package com.cpic.insurance.auap.base.quarz.job.bean;

import java.io.Serializable;
import java.util.Date;

import org.quartz.Trigger.TriggerState;

public class ScheduleJobDetail extends ScheduleJob implements Serializable {

	private static final long serialVersionUID = -5174918547527488172L;

	private TriggerState jobState;
	private Date previousFireTime;
	private Date nextFireTime;

	public TriggerState getJobState() {
		return jobState;
	}

	public void setJobState(TriggerState jobState) {
		this.jobState = jobState;
	}

	public Date getPreviousFireTime() {
		return previousFireTime;
	}

	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

}
