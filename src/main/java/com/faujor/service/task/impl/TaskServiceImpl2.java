package com.faujor.service.task.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.task.ProcessConfigMapper;
import com.faujor.dao.master.task.TaskMapper2;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.task.ProcessConfig;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.task.TaskService2;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service("taskService2")
public class TaskServiceImpl2 implements TaskService2 {
	@Autowired
	private ProcessConfigMapper configMapper;
	@Autowired
	private TaskMapper2 taskMapper;
	@Autowired
	private UserMapper userMapper;

	@Override
	public Map<String, Object> getSelecteExecutor(TaskParamsDO tpd) {
		SysUserDO user = UserCommon.getUser();
		String sdata1 = tpd.getSdata1();
		Long userId = user.getUserId();
		TaskDO task = taskMapper.findTaskByStada1AndExecutor(sdata1, userId);
		int node = 1;
		if (task != null) {
			node = task.getNode();
		}
		tpd.setNode(node);
		Map<String, Object> map = isFinished(tpd, userId);
		// 两个字段node(是否该节点)， isFinished(是否需要流转)
		Boolean isNode = (Boolean) map.get("node");
		Boolean isFinished = (Boolean) map.get("isFinished");
		if (isNode && isFinished) {
			List<SysUserDO> list = new ArrayList<SysUserDO>();
			ProcessConfigDetails pcd = configMapper.findConfigDetailsByProcessCodeAndNode(tpd.getProcessCode(),
					tpd.getNode() + 1);
			String executorIDs = pcd.getExecutor();
			if (!StringUtils.isEmpty(executorIDs)) {
				String[] ids = executorIDs.split(",");
				List<Long> IDs = new ArrayList<Long>();
				for (int i = 0; i < ids.length; i++) {
					long id = Long.parseLong(ids[i]);
					IDs.add(id);
				}
				list = userMapper.findUserByIDs(IDs);
			}
			map.put("list", list);
		}
		return map;
	}

	@Override
	public int doTask(String sdata1, String sName, String processCode) {
		// 任务信息
		List<TaskDO> taskList = taskMapper.findTaskBySdata1(sdata1);
		// 流程信息
		ProcessConfig config = configMapper.findProcessConfigByCode(processCode);
		int node = 0;
		List<TaskDO> newTaskList = new ArrayList<TaskDO>();
		if (taskList.size() == 0) {
			// 之前没有任务。
			// 流程节点信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("processId", config.getId());
			params.put("node", node);
			List<ProcessConfigDetails> list = configMapper.findConfigDetailsByPIdANDNode(params);
			// 下一个节点的信息
			ProcessConfigDetails cd = list.get(0);
			String executorType = cd.getExecType();
			// 执行者为人员
			if ("psn".equals(executorType)) {
				TaskDO t = new TaskDO();
				t.setId(UUIDUtil.getUUID());
				String taskName = cd.getNodeName() + ":" + sName;
				t.setTaskName(taskName);
				t.setSdata1(sdata1);
				t.setProcessCode(processCode);
				t.setCreateTime(new Date());
				t.setNode(cd.getNode());
				t.setExecutor(cd.getExecutor());
				t.setExecutorName(cd.getExecName());
				newTaskList.add(t);
			}
		} else {
			// 之前有任务
			SysUserDO user = UserCommon.getUser();
			String userId = String.valueOf(user.getUserId());
			// 循环已经存在的任务执行当人登录人的任务
			for (TaskDO taskDO : taskList) {
				if (userId.equals(taskDO.getExecutor())) {
					taskDO.setCompleteTime(new Date());
					break;
				}
			}
			// 获取节点
			node = taskList.get(0).getNode();
			// 流程节点信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("processId", config.getId());
			params.put("node", node);
			List<ProcessConfigDetails> list = configMapper.findConfigDetailsByPIdANDNode(params);
			// 下一个节点的信息
			ProcessConfigDetails cd = list.get(0);
			String executorType = cd.getExecType();
			// 执行者为人员
			if ("psn".equals(executorType)) {
				TaskDO t = new TaskDO();
				t.setId(UUIDUtil.getUUID());
				String taskName = cd.getNodeName() + ":" + sName;
				t.setTaskName(taskName);
				t.setSdata1(sdata1);
				t.setProcessCode(processCode);
				t.setCreateTime(new Date());
				t.setNode(cd.getNode());
				t.setExecutor(cd.getExecutor());
				t.setExecutorName(cd.getExecName());
				newTaskList.add(t);
			}
			taskMapper.batchSaveTaskHis(taskList);
			taskMapper.batchRemoveTask(taskList);
		}
		int k = taskMapper.batchSaveTask(newTaskList);
		return k;
	}

	@Override
	public Map<String, Object> undoTaskList(String exectorId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TaskDO> list = taskMapper.findUndoTaskList(exectorId);
		map.put("data", list);
		map.put("count", list.size());
		map.put("msg", "");
		map.put("code", "0");
		return map;
	}

	@Override
	public Map<String, Object> doneTaskList(String exectorId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<TaskDO> list = taskMapper.findDoneTaskList(exectorId);
		map.put("data", list);
		map.put("count", list.size());
		map.put("msg", "");
		map.put("code", "0");
		return map;
	}

	@Override
	public int createTask(TaskParamsDO tpd) {
		String sdata1 = tpd.getSdata1();
		Long executor = tpd.getExecutor();
		TaskDO task = taskMapper.findTaskByStada1AndExecutor(sdata1, executor);
		int i = 0;
		if (task != null) {
			task.setCreateTime(new Date());
			i = taskMapper.updateTaskInfo(task);
		} else {
			task = new TaskDO();
			task.setTaskName(tpd.getTaskName());
			task.setSdata1(tpd.getSdata1());
			task.setProcessCode(tpd.getProcessCode());
			task.setNode(tpd.getNode());
			task.setId(UUIDUtil.getUUID());
			task.setCreateTime(new Date());
			task.setCreator(tpd.getExecutor());
			task.setActionUrl(tpd.getActionUrl());
			task.setCreatorName(tpd.getExecutorName());
			task.setExecutor(tpd.getExecutor() + "");
			task.setExecutorName(tpd.getExecutorName());
			i = taskMapper.saveTaskInfo(task);
		}
		return i;
	}

	@Override
	public RestCode isProcess(TaskParamsDO tpd) {
		SysUserDO user = UserCommon.getUser();
		tpd.setExecutor(user.getUserId());
		tpd.setExecutorName(user.getName());
		Long executor = user.getUserId();
		String executorName = user.getName();
		// 获取当前执行者的任务
		TaskDO task = taskMapper.findTaskByStada1AndExecutor(tpd.getSdata1(), executor);
		int node = 1;
		if (task != null) {
			node = task.getNode();
			tpd.setActionUrl(task.getActionUrl());
			tpd.setProcessCode(tpd.getProcessCode());
		} else {
			// ProcessConfig pc =
			// configMapper.findConfigByProcessCode(tpd.getProcessCode());
		}
		tpd.setNode(node);
		RestCode rc = dealTask(tpd, task, executor, executorName);
		int code = (int) rc.get("code");
		if (code >= 100) {
			// 获取审批页面的路径
			if (code == 110) {
				String object = (String) rc.get("msg");
				List<TaskDO> list = JsonUtils.jsonToList(object, TaskDO.class);
				taskMapper.batchSaveTaskHis(list);
				taskMapper.batchRemoveTask(list);
			}
			// 生成新的任务数据
			tpd.setNode(tpd.getNode() + 1);
			List<TaskDO> list = taskParamsDOToTaskDo(tpd);
			int i = taskMapper.batchSaveTask(list);
			if (i > 0)
				return RestCode.ok();
			return RestCode.error();
		}
		return rc;
	}

	/**
	 * 返回值 如果处理过程中出现异常则
	 * 
	 * 0-10代表异常，100-110代表成功
	 * 
	 * @param tpd
	 * @param executor
	 * @param executorName
	 * @return
	 */
	RestCode dealTask(TaskParamsDO tpd, TaskDO task, long executor, String executorName) {
		// 获取配置信息
		ProcessConfigDetails pcd = configMapper.findConfigDetailsByProcessCodeAndNode(tpd.getProcessCode(),
				tpd.getNode());
		if (pcd != null) {
			// 获取该级节点的处理方式
			String handWay = pcd.getHandWay();
			// 该业务数据的所有未完成的任务数据
			List<TaskDO> list = taskMapper.findTaskBySdata1(tpd.getSdata1());
			if (list.size() > 0) {
				if ("AND".equals(handWay)) {
					// 并处理，则需要该节点的所有任务都完成
					// 校验该节点的所有任务是否已完成
					boolean isFinished = true;
					for (TaskDO taskDO : list) {
						if (taskDO.getId() != task.getId()) {
							if (StringUtils.isEmpty(taskDO.getCompleteName()))
								isFinished = false;
						} else {
							// 当前数据的完成时间和完成人
							// 先把数据填充进去，以防所有任务已完成，反正没什么影响。
							taskDO.setCompleteTime(new Date());
							taskDO.setCompletor(executor);
							taskDO.setCompleteName(executorName);
						}
					}
					if (isFinished) {
						String json = JsonUtils.beanToJson(list);
						return RestCode.ok(110, json);
						// true为除了本执行者的任务均已完成
						// if (taskMapper.batchSaveTaskHis(list) > 0) {
						// if (taskMapper.batchRemoveTask(list) > 0)
						// return RestCode.ok(110, "本节点处理完成!");
						// return RestCode.error(0, "保存不成功！");
						// } else {
						// return RestCode.error(0, "保存不成功！");
						// }
					} else {
						// 除了本执行者的任务，还有其他执行者的任务未完成。
						task.setCompleteTime(new Date());
						task.setCompletor(executor);
						task.setCompleteName(executorName);
						taskMapper.updateTaskInfo(task);
						return RestCode.ok(101, "本节点未处理完成！");
					}
				} else {
					// 或处理，则该节点只要有人完成任务，则任务完成
					// 处理该任务，并将所有任务设置为已完成
					for (TaskDO taskDO : list) {
						taskDO.setCompleteTime(new Date());
						taskDO.setCompletor(executor);
						taskDO.setCompleteName(executorName);
					}
					String json = JsonUtils.beanToJson(list);
					return RestCode.ok(110, json);
					// if (taskMapper.batchSaveTaskHis(list) > 0) {
					// if (taskMapper.batchRemoveTask(list) > 0)
					// return RestCode.ok(100, "当前节点处理完成！");
					// return RestCode.error(0, "保存不成功！");
					// } else {
					// return RestCode.error(0, "保存不成功！");
					// }
				}
			} else {
				return RestCode.ok(110, "本节点处理完成！");
			}
		} else {
			return RestCode.error(3, "该节点无配置信息！");
		}
	}

	/**
	 * 根据选出的下一节点执行者生成任务数据
	 * 
	 * @param tpd
	 * @return
	 */
	List<TaskDO> taskParamsDOToTaskDo(TaskParamsDO tpd) {
		List<TaskDO> list = new ArrayList<TaskDO>();
		// 处理执行者列表数据，生产任务
		List<UserDO> json = tpd.getExecutorList();
		for (UserDO user : json) {
			TaskDO task = new TaskDO();
			task.setTaskName(tpd.getTaskName());
			task.setSdata1(tpd.getSdata1());
			task.setProcessCode(tpd.getProcessCode());
			task.setNode(tpd.getNode());
			task.setId(UUIDUtil.getUUID());
			task.setCreateTime(new Date());
			task.setCreator(tpd.getExecutor());
			task.setCreatorName(tpd.getExecutorName());
			task.setExecutor(user.getId() + "");
			task.setExecutorName(user.getName());
			list.add(task);
		}
		return list;
	}

	/**
	 * 判断是否完成
	 * 
	 * @param tpd
	 * @param executor
	 * @return
	 */
	Map<String, Object> isFinished(TaskParamsDO tpd, long executor) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isFinished = true;
		ProcessConfigDetails pcd = configMapper.findConfigDetailsByProcessCodeAndNode(tpd.getProcessCode(),
				tpd.getNode());
		if (pcd != null) {
			// 获取该级节点的处理方式
			String handWay = pcd.getHandWay();
			// 该业务数据的所有未完成的任务数据
			List<TaskDO> list = taskMapper.findTaskBySdata1(tpd.getSdata1());
			if ("AND".equals(handWay)) {
				// 并处理，则需要该节点的所有任务都完成
				// 校验该节点的所有任务是否已完成
				String execu = String.valueOf(executor);
				for (TaskDO taskDO : list) {
					if (taskDO.getExecutor() != execu && StringUtils.isEmpty(taskDO.getCompleteName())) {
						isFinished = false;
					}
				}
			} else {
				// 或处理，则该节点只要有人完成任务，则任务完成
				// 处理该任务，并将所有任务设置为已完成
				isFinished = true;
			}
			map.put("node", true);
		} else {
			isFinished = false;
			map.put("node", false);
		}
		map.put("isFinished", isFinished);
		return map;
	}

	@Override
	public int firstProcess(TaskParamsDO tpd) {
		// 首先判断是否有任务，如果有则不变，没有则创建任务
		List<TaskDO> list = taskMapper.findTaskBySdata1(tpd.getSdata1());
		if (list.size() == 0) {
			TaskDO task = new TaskDO();
			task.setTaskName(tpd.getTaskName());
			task.setSdata1(tpd.getSdata1());
			task.setProcessCode(tpd.getProcessCode());
			task.setNode(tpd.getNode());
			task.setId(UUIDUtil.getUUID());
			task.setCreateTime(new Date());
			task.setCreator(tpd.getExecutor());
			task.setCreatorName(tpd.getExecutorName());
			task.setExecutor(tpd.getExecutor().toString());
			task.setExecutorName(tpd.getExecutorName());
			taskMapper.saveTaskInfo(task);
		}
		return 0;
	}

	@Override
	public int andProcess(TaskParamsDO tpd) {
		int i = 0;
		SysUserDO user = UserCommon.getUser();
		TaskDO task = taskMapper.findTaskByStada1AndExecutor(tpd.getSdata1(), user.getUserId());
		if (task != null) {
			List<TaskDO> list = new ArrayList<TaskDO>();
			task.setCompleteName(user.getName());
			task.setCompleteTime(new Date());
			task.setCompletor(user.getUserId());
			list.add(task);
			i = taskMapper.batchSaveTaskHis(list);
			taskMapper.batchRemoveTask(list);
		}
		return i;
	}
}
