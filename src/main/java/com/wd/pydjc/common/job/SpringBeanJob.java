package com.wd.pydjc.common.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.wd.pydjc.common.config.JobConfig;
import com.wd.pydjc.sys.service.JobService;

public class SpringBeanJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext()
					.get(JobConfig.KEY);
			JobService jobService = applicationContext.getBean(JobService.class);
			jobService.doJob(context.getJobDetail().getJobDataMap());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
