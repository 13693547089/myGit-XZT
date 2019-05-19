package com.faujor.service.demo;

import com.faujor.entity.task.ProcTestDO;
import com.faujor.entity.task.TestDO;

public interface ProcessTestService {

	int saveProcessTest(TestDO td, String executors);

	ProcTestDO findTestById(String sdata1);

	int saveTestInfo(TestDO td);

}
