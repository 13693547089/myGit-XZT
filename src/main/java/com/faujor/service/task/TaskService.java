package com.faujor.service.task;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.TaskCenterDO;
import com.faujor.entity.task.TaskCheckResult;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;

public interface TaskService {

	/**
	 * 初始化任务数据（保存业务数据的时候同时保存一条给自己的任务数据）
	 * 
	 * @param sdata1
	 * @param processCode
	 * @param taskName
	 * @return
	 */
	int initialProcess(TaskParamsDO params);

	/**
	 * 根据参数获取待选择的人员列表
	 * 
	 * @param tpd
	 * @return
	 */
	List<SysUserDO> findExecutorsByParams(TaskParamsDO tpd);

	/**
	 * 根据参数获取任务数据
	 * 
	 * @param sdata1
	 * @param executor
	 * @return
	 */
	TaskDO findTaskByParams(TaskParamsDO tpd);

	/**
	 * 根据参数判断节点的任务是否完全走完
	 * 
	 * @param node
	 * @param processCode
	 * @return
	 */
	boolean nodeIsFinished(TaskParamsDO tpd);

	/**
	 * 处理当前任务
	 * 
	 * @param sdata1
	 * @return
	 */
	int doCurrentTask(String sdata1);

	/**
	 * 结束任务
	 * 
	 * @param sdata1
	 * @return
	 */
	int doOverTask(String sdata1);

	/**
	 * 流程开始流转
	 * 
	 * @param params
	 * @return
	 */
	int startProcess(TaskParamsDO params);

	/**
	 * 代办任务
	 * 
	 * @param executor
	 * @return
	 */
	Map<String, Object> findUndoTaskListByParams(RowBounds rb, String executor);

	/**
	 * 已办任务
	 * 
	 * @param rb
	 * @param executor
	 * @return
	 */
	Map<String, Object> findDoneTaskListByExecutor(RowBounds rb, String executor);

	/**
	 * 校验是否需要打开执行者弹出框
	 * 
	 * @param sdata1
	 * @param processCode
	 * 
	 * @return
	 */
	TaskCheckResult isSelectExecutor(String sdata1, String processCode);

	/**
	 * 任务流转
	 * 
	 * @param params
	 * @return
	 */
	int isProcess(TaskParamsDO params, String type);

	/**
	 * 任务回退
	 * 
	 * @param params
	 * @return
	 */
	int backProcess(TaskParamsDO params);

	/**
	 * 为潜在供应商创建任务
	 * 
	 * @param params
	 * @return
	 */
	int createTasksForReg(TaskParamsDO params);

	/**
	 * 获取任务
	 * 
	 * @param sdata1
	 * @return
	 */
	TaskDO getTask(String sdata1);

	/**
	 * 处理当前任务，并且将下一节点的执行者设置为直接领导
	 * 
	 * @param query
	 * @return
	 */
	int doTaskToLeader(TaskParamsDO query);

	/**
	 * 任务中心列表数据
	 * 
	 * @param rb
	 * 
	 * @param taskCenter
	 * @return
	 */
	Map<String, Object> taskCenterList(RowBounds rb, TaskCenterDO taskCenter);

	/**
	 * 删除业务的时候，级联删除任务
	 * 
	 * @param params
	 * @return
	 */
	int removeTaskBySdata1(TaskParamsDO params);
}
