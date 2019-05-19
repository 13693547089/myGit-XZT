package com.faujor.dao.master.task;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.task.ProcTestDO;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TestDO;

public interface TaskMapper2 {

	/**
	 * 通过业务id获取该业务的当前要执行的任务列表
	 * 
	 * @param sdata1
	 * @return
	 */
	List<TaskDO> findTaskBySdata1(String sdata1);

	/**
	 * 批量保存任务
	 * 
	 * @param newTaskList
	 * @return
	 */
	int batchSaveTask(List<TaskDO> newTaskList);

	/**
	 * 将已经执行完的任务放置到任务历史表中
	 * 
	 * @param taskList
	 * @return
	 */
	int batchSaveTaskHis(List<TaskDO> taskList);

	/**
	 * 删除已经执行完的任务
	 * 
	 * @param taskList
	 * @return
	 */
	int batchRemoveTask(List<TaskDO> taskList);

	List<TaskDO> findUndoTaskList(String exectorId);

	List<TaskDO> findDoneTaskList(String exectorId);

	TaskDO findTaskByStada1AndExecutor(@Param("sdata1") String sdata1, @Param("userId") Long userId);

	int updateTaskInfo(TaskDO td);

	ProcTestDO findTestById(String sdata1);

	/**
	 * 插入测试的业务数据
	 * 
	 * @param td
	 * @return
	 */
	int saveTestInfo(TestDO td);

	/**
	 * 更新一条测试数据
	 * 
	 * @param td
	 * @return
	 */
	int updateTestInfo(TestDO td);

	/**
	 * 保存一条任务数据
	 * 
	 * @param task
	 * @return
	 */
	int saveTaskInfo(TaskDO task);

}
