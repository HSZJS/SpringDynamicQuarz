package com.cpic.insurance.auap.base.quarz.job.standard;

/**
 * 
 * 定时任务接口类
 * @author wuzelin
 *
 */
public interface QuartzJobStandard {
	
	/**
	 * Job任务具体业务实现
	 * @param scheduleJob
	 */
	public void executeJob(String jobParameter);
	
}
