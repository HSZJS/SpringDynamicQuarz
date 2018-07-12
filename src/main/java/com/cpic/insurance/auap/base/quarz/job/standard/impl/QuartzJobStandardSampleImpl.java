package com.cpic.insurance.auap.base.quarz.job.standard.impl;

import com.cpic.insurance.auap.base.quarz.job.standard.QuartzJobStandard;

public class QuartzJobStandardSampleImpl implements QuartzJobStandard {
	
	@Override
	public void executeJob(String jobParameter) {
		System.out.println("QuartzJobStandardSampleImpl  -- 具体业务逻辑处理,jobParameter:"+jobParameter);
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
