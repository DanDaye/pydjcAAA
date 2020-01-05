package com.wd.pydjc.sys.service;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

import com.wd.pydjc.sys.model.JobModel;

public interface JobService {

	void saveJob(JobModel jobModel);

	void doJob(JobDataMap jobDataMap);

	void deleteJob(Long id) throws SchedulerException;
}
