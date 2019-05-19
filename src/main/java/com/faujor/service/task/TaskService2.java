package com.faujor.service.task;

import java.util.Map;

import com.faujor.entity.task.TaskParamsDO;
import com.faujor.utils.RestCode;

public interface TaskService2 {

	/**
	 * 创建和处理任务
	 * 
	 * @param sdata1,业务ID
	 * @param sName，业务内容简介
	 * @param processCode,使用的流程编码
	 * @return =0,任务完成，>0任务流转成功，<0任务流转失败
	 */
	public int doTask(String sdata1, String sName, String processCode);

	public Map<String, Object> undoTaskList(String exectorId);

	public Map<String, Object> doneTaskList(String exectorId);

	/**
	 * 参数必须有sdata1， node， processCode
	 * 
	 * @param tpd
	 * @return isNode(是否有节点的配置信息),isFinished(当前节点的任务是否完成),list(待选的执行者列表)
	 */
	public Map<String, Object> getSelecteExecutor(TaskParamsDO tpd);

	/**
	 * 业务保存的时候给自己生成一条任务
	 * 
	 * @param tpd
	 * @return
	 */
	public int createTask(TaskParamsDO tpd);

	/**
	 * 任务首节点保存业务数据的时候会给自己产生一条代办任务
	 * 
	 * @param tpd
	 * @return
	 */
	public int firstProcess(TaskParamsDO tpd);

	/**
	 * 流程流转
	 * 
	 * @param tpd
	 * @return
	 */
	public RestCode isProcess(TaskParamsDO tpd);

	/**
	 * 并处理
	 * 
	 * @param tpd
	 * @return
	 */
	public int andProcess(TaskParamsDO tpd);

}
