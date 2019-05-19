package com.faujor.dao.master.task;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.task.TaskCenterDO;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;

public interface TaskMapper {

	/**
	 * 根据任务的查询参数获取任务数据
	 * 
	 * @param query
	 * @param sdata1
	 * @param executor
	 * @return
	 */
	TaskDO findTaskByTaskParamsDO(TaskParamsDO query);

	/**
	 * 根据参数，查询任务数据
	 * 
	 * @param query
	 * @return
	 */
	List<TaskDO> findTaskListByTaskParamsDO(TaskParamsDO query);

	/**
	 * 更新单一任务信息
	 * 
	 * @param task
	 * @return
	 */
	int updateTask(TaskDO task);

	/**
	 * 保存单一任务信息
	 * 
	 * @param task
	 * @return
	 */
	int saveTask(TaskDO task);

	/**
	 * 删除任务数据
	 * 
	 * @param task
	 * @return
	 */
	int removeTask(TaskDO task);

	/**
	 * 保存任务历史数据
	 * 
	 * @param task
	 * @return
	 */
	int saveHisTask(TaskDO task);

	/**
	 * 批量插入任务数据
	 * 
	 * @param list
	 * @return
	 */
	int batchSaveTask(List<TaskDO> list);

	/**
	 * 批量删除任务数据
	 * 
	 * @param list
	 * @return
	 */
	int batchRemoveTask(List<TaskDO> list);

	/**
	 * 批量插入已完成任务数据
	 * 
	 * @param list
	 * @return
	 */
	int batchSaveHisTask(List<TaskDO> list);

	/**
	 * 代办任务
	 * 
	 * @param rb
	 * 
	 * @param executor
	 * @return
	 */
	List<TaskDO> findUndoTaskListByExecutor(RowBounds rb, String executor);

	int countUndoTaskByExecutor(String executor);

	/**
	 * 已办任务
	 * 
	 * @param rb
	 * 
	 * @param executor
	 * @return
	 */
	List<TaskDO> findDoneTaskListByExecutor(RowBounds rb, String executor);

	int countDoneTaskByExecutor(String executor);

	/**
	 * 查询历史的任务信息
	 * 
	 * @param query
	 * @return
	 */
	TaskDO findTaskHisByParams(TaskParamsDO query);

	/**
	 * 已办任务列表（任务中心）
	 * 
	 * @param rb
	 * 
	 * @param taskCenter
	 * @return
	 */
	List<TaskCenterDO> findDoneTaskListByTaskCenter(RowBounds rb, TaskCenterDO taskCenter);

	/**
	 * 已办任务列表(任务中心)计数
	 * 
	 * @param taskCenter
	 * @return
	 */
	int countDoneTaskListByTaskCenter(TaskCenterDO taskCenter);

	/**
	 * 代办任务列表（任务中心）
	 * 
	 * @param rb
	 * @param taskCenter
	 * @return
	 */
	List<TaskCenterDO> findUndoTaskListByTaskCenter(RowBounds rb, TaskCenterDO taskCenter);

	/**
	 * 代办任务列表(任务中心)计数
	 * 
	 * @param taskCenter
	 * @return
	 */
	int countUndoTaskListByTaskCenter(TaskCenterDO taskCenter);

	/**
	 * 删除任务数据
	 * 
	 * @param params
	 * @return
	 */
	int removeTaskByTaskParams(TaskParamsDO params);

	/**
	 * 删除任务历史数据
	 * 
	 * @param params
	 * @return
	 */
	int removeTaskHisByTaskParams(TaskParamsDO params);

	/**
	 * 查询得到首节点的创建人
	 * 
	 * @param sdata1
	 * @return
	 */
	List<TaskDO> findTaskHisBySdata1(String sdata1);

}
