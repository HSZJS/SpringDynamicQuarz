package com.cpic.insurance.auap.base.quarz.job.bean;

import java.io.Serializable;

public class ScheduleJob implements Serializable {

	private static final long serialVersionUID = 7537497523650417391L;

	private String jobName;
	private String jobGroup;
	private String jobNameCN;
	private String cronExpression;
	private String jobBeanName;
	private String jobParameter;
	private boolean jobConcurrent;
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobNameCN() {
		return jobNameCN;
	}

	public void setJobNameCN(String jobNameCN) {
		this.jobNameCN = jobNameCN;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getJobBeanName() {
		return jobBeanName;
	}

	public void setJobBeanName(String jobBeanName) {
		this.jobBeanName = jobBeanName;
	}

	public String getJobParameter() {
		return jobParameter;
	}

	public void setJobParameter(String jobParameter) {
		this.jobParameter = jobParameter;
	}

	public boolean isJobConcurrent() {
		return jobConcurrent;
	}

	public void setJobConcurrent(boolean jobConcurrent) {
		this.jobConcurrent = jobConcurrent;
	}
	
}
