package com.faujor.test.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faujor.service.task.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {
	@Autowired
	private TaskService taskService;

//	@Test
//	public void task() {
//		taskService.doTask("42159b9268c3493c9a1e40acd4277598", "测试生成任务", "AuditCode");
//	}
}
