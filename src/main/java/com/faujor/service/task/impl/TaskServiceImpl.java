package com.faujor.service.task.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.task.ProcessConfigMapper;
import com.faujor.dao.master.task.ProcessRuleMapper;
import com.faujor.dao.master.task.TaskMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.task.ProcessConfig;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.entity.task.ProcessRuleBackDO;
import com.faujor.entity.task.ProcessRuleDO;
import com.faujor.entity.task.RelationValuesParamsDO;
import com.faujor.entity.task.RelationValuesResultDO;
import com.faujor.entity.task.TaskCenterDO;
import com.faujor.entity.task.TaskCheckResult;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.task.RuleService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private ProcessConfigMapper configMapper;
	@Autowired
	private ProcessRuleMapper ruleMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RuleService ruleService;

	@Override
	public int initialProcess(TaskParamsDO params) {
		SysUserDO user = UserCommon.getUser();
		Long executor = user.getUserId();
		String sdata1 = params.getSdata1();
		TaskParamsDO query = new TaskParamsDO();
		query.setSdata1(sdata1);
		query.setExecutor(executor);
		query.setStrExec(executor.toString());
		int i = 0;
		TaskDO task = taskMapper.findTaskByTaskParamsDO(query);
		if (task != null) {
			// 更新一下创建时间，其他的不用操作，如果任务名称变了之后再说
			task.setCreateTime(new Date());
			i = taskMapper.updateTask(task);
		} else {
			// 根据当前提交人为其生成任务
			ProcessConfig config = configMapper.findProcessConfigByCode(params.getProcessCode());
			if (config != null)
				params.setActionUrl(config.getActionUrl());
			params.setExecutor(executor);
			params.setExecutorName(user.getName());
			params.setNode(1);
			params.setTaskStatus("已保存");
			task = createSingleTask(params, user);
			i = taskMapper.saveTask(task);
		}
		return i;
	}

	@Override
	public List<SysUserDO> findExecutorsByParams(TaskParamsDO tpd) {
		SysUserDO user = UserCommon.getUser();
		// 判断是否需要初始化一条业务数据
		int node2 = tpd.getNode();
		if (node2 == 1) {
			TaskParamsDO query = new TaskParamsDO();
			query.setSdata1(tpd.getSdata1());
			query.setNode(node2);
			Long executor = user.getUserId();
			query.setExecutor(executor);
			query.setStrExec(executor.toString());
			TaskDO task = taskMapper.findTaskByTaskParamsDO(query);
			if (task == null) {
				initialProcess(tpd);
			}
		}
		List<SysUserDO> list = new ArrayList<SysUserDO>();
		String processCode = tpd.getProcessCode();
		int node = getNextNode(tpd);
		// 先获取当前节点
		ProcessConfigDetails pcd = configMapper.findConfigDetailsByProcessCodeAndNode(processCode, node);
		// 获取当前节点的执行规则
		String execRule = pcd.getExecRule();
		if (!StringUtils.isEmpty(execRule) && "Y".equals(execRule)) {
			list = getExecutorList(user, pcd.getId(), tpd.getSdata1());
		} else {
			// 获取下一个节点的执行者列表
			pcd = configMapper.findConfigDetailsByProcessCodeAndNode(processCode, node);
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
		}
		return list;
	}

	@Override
	public TaskDO findTaskByParams(TaskParamsDO tpd) {
		Long executor = tpd.getExecutor();
		if (executor != null)
			tpd.setStrExec(executor.toString());
		return taskMapper.findTaskByTaskParamsDO(tpd);
	}

	@Override
	public boolean nodeIsFinished(TaskParamsDO tpd) {
		boolean isFinished = true;
		String processCode = tpd.getProcessCode();
		int node = tpd.getNode();
		ProcessConfigDetails pcd = configMapper.findConfigDetailsByProcessCodeAndNode(processCode, node);
		if (pcd != null) {
			// 获取该级节点的处理方式
			String handWay = pcd.getHandWay();
			if ("AND".equals(handWay)) {
				// 该业务数据的所有未完成的任务数据
				TaskParamsDO query = new TaskParamsDO();
				query.setSdata1(tpd.getSdata1());
				query.setNode(node);
				List<TaskDO> list = taskMapper.findTaskListByTaskParamsDO(query);
				// 并处理，则需要该节点的所有任务都完成
				// 校验该节点的所有任务是否已完成
				String execu = String.valueOf(tpd.getExecutor());
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
		}
		return isFinished;
	}

	@Override
	@Transactional
	public int doCurrentTask(String sdata1) {
		int i = 0;
		SysUserDO user = UserCommon.getUser();
		Long executor = user.getUserId();
		TaskParamsDO query = new TaskParamsDO();
		query.setSdata1(sdata1);
		query.setExecutor(executor);
		query.setStrExec(executor.toString());
		TaskDO task = taskMapper.findTaskByTaskParamsDO(query);
		if (task != null) {
			task.setCompleteName(user.getName());
			task.setCompleteTime(new Date());
			task.setCompletor(executor);
			task.setStatus("已完成");
			taskMapper.removeTask(task);
			i = taskMapper.saveHisTask(task);
		}
		return i;
	}

	@Override
	@Transactional
	public int doOverTask(String sdata1) {
		int i = 0;
		SysUserDO user = UserCommon.getUser();
		Long executor = user.getUserId();
		TaskParamsDO query = new TaskParamsDO();
		query.setSdata1(sdata1);
		List<TaskDO> list = taskMapper.findTaskListByTaskParamsDO(query);
		for (TaskDO task : list) {
			task.setCompleteName(user.getName());
			task.setCompleteTime(new Date());
			task.setCompletor(executor);
			task.setStatus("已完成");
		}
		if (list.size() > 0) {
			taskMapper.batchRemoveTask(list);
			i = taskMapper.batchSaveHisTask(list);
		}
		return i;
	}

	@Override
	@Transactional
	public int doTaskToLeader(TaskParamsDO query) {
		int i = 0;
		SysUserDO user = UserCommon.getUser();
		Long executor = user.getUserId();
		query.setExecutor(executor);
		query.setStrExec(executor.toString());
		TaskDO task = taskMapper.findTaskByTaskParamsDO(query);
		if (task != null) {
			int node = task.getNode();
			query.setNode(node);
			List<TaskDO> list = taskMapper.findTaskListByTaskParamsDO(query);
			if (list.size() > 0) {
				for (TaskDO taskDO : list) {
					taskDO.setStatus("已完成");
					taskDO.setCompleteName(user.getName());
					taskDO.setCompleteTime(new Date());
					taskDO.setCompletor(user.getUserId());
				}
				taskMapper.batchRemoveTask(list);
				taskMapper.batchSaveHisTask(list);
			}
			query.setActionUrl(task.getActionUrl());
			query.setNode(node + 1);
		} else {
			ProcessConfig config = configMapper.findProcessConfigByCode(query.getProcessCode());
			if (config != null) {
				query.setActionUrl(config.getActionUrl());
			}
			// 先为自己生成一条任务数据
			query.setNode(1);
			query.setExecutor(user.getUserId());
			query.setExecutorName(user.getName());
			query.setTaskStatus("已完成");// 给自己的任务是已完成
			task = createSingleTask(query, user);
			task.setCompleteName(user.getName());
			task.setCompleteTime(new Date());
			task.setCompletor(user.getUserId());
			i += taskMapper.saveHisTask(task);
			// 为下一节点审批人生成代办
			query.setNode(2);
		}
		Long leader = user.getLeader();
		if (!leader.equals(0)) {
			query.setExecutor(user.getLeader());
			query.setExecutorName(user.getLeaderName());
			query.setTaskStatus("审批中");// 为领导生成的任务是审批中
			TaskDO task2 = createSingleTask(query, user);
			i += taskMapper.saveTask(task2);
		}
		return i;
	}

	@Override
	public int startProcess(TaskParamsDO params) {
		SysUserDO user = UserCommon.getUser();
		TaskParamsDO query = new TaskParamsDO();
		String sdata1 = params.getSdata1();
		Long executor = user.getUserId();
		query.setSdata1(sdata1);
		query.setStrExec(executor.toString());
		query.setExecutor(executor);
		TaskDO task = taskMapper.findTaskByTaskParamsDO(query);
		int i = 0;
		if (task == null) {
			params.setNode(1);
			ProcessConfig config = configMapper.findProcessConfigByCode(params.getProcessCode());
			if (config != null)
				params.setActionUrl(config.getActionUrl());
			params.setExecutor(user.getUserId());
			params.setTaskStatus("已完成");// 为自己生成的已办任务是已完成
			params.setExecutorName(user.getName());
			task = createSingleTask(params, user);
		}
		task.setCompleteName(user.getName());
		task.setCompleteTime(new Date());
		task.setCompletor(executor);
		taskMapper.removeTask(task);
		taskMapper.saveHisTask(task);

		// 生成下一节点的任务
		params.setTaskStatus("审批中");// 下一个节点的任务是审批中
		params.setNode(params.getNode() + 1);
		List<TaskDO> list = createTaskList(params, null);
		i = taskMapper.batchSaveTask(list);
		return i;
	}

	/**
	 * 生成单个任务
	 * 
	 * @param params
	 * @param user
	 * @return
	 */
	public TaskDO createSingleTask(TaskParamsDO params, SysUserDO user) {
		TaskDO task = new TaskDO();
		task.setActionUrl(params.getActionUrl());
		task.setSdata1(params.getSdata1());
		task.setTaskName(params.getTaskName());
		task.setProcessCode(params.getProcessCode());
		task.setNode(params.getNode());
		task.setExecutor(params.getExecutor().toString());
		task.setExecutorName(params.getExecutorName());
		task.setCreateTime(new Date());
		task.setCreator(user.getUserId());
		task.setCreatorName(user.getName());
		task.setStatus(params.getTaskStatus());
		task.setTaskType("processing");
		task.setTaskTypeDesc("正向流转任务");
		task.setId(UUIDUtil.getUUID());
		return task;
	}

	public List<TaskDO> createTaskList(TaskParamsDO params, List<SysUserDO> executors) {
		List<TaskDO> list = new ArrayList<TaskDO>();
		// 处理执行者列表数据，生产任务
		if (executors != null) {
			for (SysUserDO user : executors) {
				TaskDO task = new TaskDO();
				task.setActionUrl(params.getActionUrl());
				task.setTaskName(params.getTaskName());
				task.setSdata1(params.getSdata1());
				task.setProcessCode(params.getProcessCode());
				task.setNode(params.getNode());
				task.setId(UUIDUtil.getUUID());
				task.setCreateTime(new Date());
				task.setCreator(params.getExecutor());
				task.setCreatorName(params.getExecutorName());
				task.setExecutor(user.getUserId() + "");
				task.setExecutorName(user.getName());
				task.setStatus(params.getTaskStatus());
				task.setTaskType("processing");
				task.setTaskTypeDesc("正向流转任务");
				list.add(task);
			}
		} else {
			List<UserDO> executorList = params.getExecutorList();
			for (UserDO user : executorList) {
				TaskDO task = new TaskDO();
				task.setActionUrl(params.getActionUrl());
				task.setTaskName(params.getTaskName());
				task.setSdata1(params.getSdata1());
				task.setProcessCode(params.getProcessCode());
				task.setNode(params.getNode());
				task.setId(UUIDUtil.getUUID());
				task.setCreateTime(new Date());
				task.setCreator(params.getExecutor());
				task.setCreatorName(params.getExecutorName());
				task.setExecutor(user.getId() + "");
				task.setExecutorName(user.getName());
				task.setStatus(params.getTaskStatus());
				task.setTaskType("processing");
				task.setTaskTypeDesc("正向流转任务");
				list.add(task);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> findDoneTaskListByExecutor(RowBounds rb, String executor) {
		List<TaskDO> list = taskMapper.findDoneTaskListByExecutor(rb, executor);
		int count = taskMapper.countDoneTaskByExecutor(executor);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", count);
		map.put("msg", "");
		map.put("code", "0");
		return map;
	}

	@Override
	public Map<String, Object> findUndoTaskListByParams(RowBounds rb, String executor) {
		List<TaskDO> list = taskMapper.findUndoTaskListByExecutor(rb, executor);
		int count = taskMapper.countUndoTaskByExecutor(executor);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", count);
		map.put("msg", "");
		map.put("code", "0");
		return map;
	}

	@Override
	public TaskCheckResult isSelectExecutor(String sdata1, String processCode) {
		TaskCheckResult result = new TaskCheckResult();
		ProcessConfig config = configMapper.findProcessConfigByCode(processCode);
		if (config != null) {
			SysUserDO user = UserCommon.getUser();
			TaskParamsDO query = new TaskParamsDO();
			Long executor = user.getUserId();
			query.setExecutor(executor);
			query.setStrExec(executor.toString());
			query.setProcessCode(processCode);
			query.setSdata1(sdata1);
			TaskDO task = taskMapper.findTaskByTaskParamsDO(query);
			if (task == null) {
				// 任务为空，说明是首节点
				// 获取改节点要执行的下一节点是哪个
				query.setNode(1);
				int nextNode = getNextNode(query);
				// 获取下一个节点的执行规则
				query.setNode(nextNode);
				result = checkIsOpen(query);
			} else {
				int node = task.getNode();
				// 判断是不是末尾节点
				// 查询改流程的节点
				ProcessConfigDetails configDetails = configMapper.findConfigDetailsByProcessCodeAndNode(processCode,
						node + 1);
				if (configDetails != null) {
					// 查看当前节点是否还有其他的任务
					query.setNode(node);
					List<TaskDO> list = taskMapper.findTaskListByTaskParamsDO(query);
					if (list.size() == 1) {
						int nextNode = getNextNode(query);
						// 获取下一个节点的执行规则
						query.setNode(nextNode);
						result = checkIsOpen(query);
					} else {
						// 判断此节点的处理方式，判断是否处理完成
						ProcessConfigDetails details = configMapper.findConfigDetailsByProcessCodeAndNode(processCode,
								node);
						if (details != null) {
							String handWay = details.getHandWay();
							if ("AND".equals(handWay)) {
								// 并处理，并且任务数大于1，则说明还有其他的未完成
								result.setIsProcess("no");
							} else {
								int nextNode = getNextNode(query);
								query.setNode(nextNode);
								result = checkIsOpen(query);
							}
						} else {
							result.setIsProcess("error");
						}
					}
				} else {
					result.setIsProcess("yes");
					result.setIsOpen("over");
				}
			}
		} else {
			result.setIsProcess("error");
		}
		return result;
	}

	// 校验是否打开弹出框
	public TaskCheckResult checkIsOpen(TaskParamsDO query) {
		TaskCheckResult result = new TaskCheckResult();
		ProcessRuleDO rule = ruleMapper.findConfigRuleByQuery(query);
		if (rule != null) {
			result.setIsProcess("yes");
			Integer ocd = rule.getOpenChooseDialog();
			if (ocd != null && ocd == 1) {
				result.setIsOpen("Y");
			} else {
				result.setIsOpen("N");
			}
		} else {
			result.setIsProcess("no");
		}
		return result;
	}

	@Override
	@Transactional
	public int isProcess(TaskParamsDO params, String type) {
		SysUserDO user = UserCommon.getUser();
		Long executor = user.getUserId();
		String executorName = user.getName();
		TaskParamsDO query = new TaskParamsDO();
		query.setExecutor(executor);
		query.setStrExec(executor.toString());
		query.setSdata1(params.getSdata1());
		// 获取当前业务的所有任务
		int i = 0;
		List<TaskDO> list = taskMapper.findTaskListByTaskParamsDO(query);
		if (list.size() > 0) {
			String actionUrl = params.getActionUrl();
			int node = params.getNode();
			params.setExecutor(executor);
			params.setExecutorName(executorName);
			for (TaskDO task : list) {
				if (StringUtils.isEmpty(actionUrl))
					params.setActionUrl(task.getActionUrl());
				if (node == 0)
					params.setNode(task.getNode());
				String taskName = task.getTaskName();
				if (StringUtils.isEmpty(taskName))
					task.setTaskName(params.getTaskName());
				task.setCompleteName(executorName);
				task.setCompleteTime(new Date());
				task.setCompletor(executor);
			}
			// 保存到历史任务表中
			taskMapper.batchSaveHisTask(list);
			taskMapper.batchRemoveTask(list);
		} else {
			// 直接提交的任务
			params.setNode(1);
			ProcessConfig config = configMapper.findProcessConfigByCode(params.getProcessCode());
			if (config != null)
				params.setActionUrl(config.getActionUrl());
			params.setExecutor(user.getUserId());
			params.setExecutorName(user.getName());
			// 为该人生成提交任务
			TaskDO task = createSingleTask(params, user);
			task.setCompleteName(executorName);
			task.setCompleteTime(new Date());
			task.setCompletor(executor);
			taskMapper.saveHisTask(task);
		}
		// 根据选择的审批人生成任务信息,下一节点的任务数据
		// 此时判断是否有条件环节，如果有这判断条件，根据条件生产相应的任务
		// 获取下一节点的配置信息
		int nextNode = getNextNode(params);
		List<SysUserDO> executors = new ArrayList<SysUserDO>();
		if ("backstage".equals(type)) {
			TaskParamsDO query1 = new TaskParamsDO();
			// 处理当前节点的任务
			query1.setProcessCode(params.getProcessCode());
			query1.setNode(nextNode);
			ProcessRuleDO rule = ruleMapper.findConfigRuleByQuery(query1);
			if (rule != null) {
				// 获取所有的执行者
				executors = getExecutorList(user, rule.getMainId(), params.getSdata1());
			} else {
				executors = null;
			}
		} else {
			executors = null;
		}
		params.setNode(nextNode);
		List<TaskDO> taskList = createTaskList(params, executors);
		i = taskMapper.batchSaveTask(taskList);
		return i;
	}

	@Override
	@Transactional
	public int backProcess(TaskParamsDO params) {
		// 该业务数据的所有任务
		// 回退的节点
		int node = 0;
		List<TaskDO> list = taskMapper.findTaskListByTaskParamsDO(params);
		int i = 0;
		if (list.size() > 0) {
			SysUserDO user = UserCommon.getUser();
			String actionUrl = params.getActionUrl();
			for (TaskDO task : list) {
				params.setProcessCode(task.getProcessCode());
				String executor = task.getExecutor();
				if (executor.equals(user.getUserId().toString()))
					node = task.getNode();
				if (StringUtils.isEmpty(actionUrl))
					params.setActionUrl(task.getActionUrl());
				task.setCompleteName(user.getName());
				task.setCompleteTime(new Date());
				task.setCompletor(user.getUserId());
				task.setStatus("已回退");
			}
			taskMapper.batchSaveHisTask(list);
			taskMapper.batchRemoveTask(list);
			// 获取回退规则信息，生成回退任务
			params.setNode(node);
			ProcessRuleBackDO backDO = ruleMapper.findBackRuleByTaskParamsDO(params);
			if (backDO != null) {
				Integer backNode = backDO.getBackNode();
				if (backNode != null) {
					// 为首节点创建任务，通过his表获取首节点信息
					TaskParamsDO query = new TaskParamsDO();
					query.setSdata1(params.getSdata1());
					query.setNode(backNode);
					TaskDO taskHis = taskMapper.findTaskHisByParams(query);
					if (taskHis != null) {
						// 生成首节点信息
						TaskDO taskDO = new TaskDO();
						taskDO.setActionUrl(taskHis.getActionUrl());
						taskDO.setId(UUIDUtil.getUUID());
						taskDO.setNode(backNode);
						taskDO.setExecutor(taskHis.getExecutor());
						taskDO.setExecutorName(taskHis.getExecutorName());
						taskDO.setCreateTime(new Date());
						taskDO.setCreator(user.getUserId());
						taskDO.setCreatorName(user.getName());
						taskDO.setProcessCode(taskHis.getProcessCode());
						taskDO.setSdata1(taskHis.getSdata1());
						taskDO.setTaskName(taskHis.getTaskName());
						taskDO.setStatus("已回退");
						taskDO.setTaskType("back");
						taskDO.setTaskTypeDesc("回退任务");
						i = taskMapper.saveTask(taskDO);
					}
				}
			}
		}
		return i;
	}

	@Override
	public int createTasksForReg(TaskParamsDO params) {
		String processCode = params.getProcessCode();
		int node = params.getNode();
		int k = 0;
		ProcessConfig config = configMapper.findProcessConfigByCode(processCode);
		if (config != null) {
			String action = config.getActionUrl();
			ProcessConfigDetails details = configMapper.findConfigDetailsByProcessCodeAndNode(processCode, node + 1);
			if (details != null) {
				String executor = details.getExecutor();
				String[] ids = executor.split(",");
				// 为每一个执行者创建任务
				List<Long> IDs = new ArrayList<Long>();
				for (int i = 0; i < ids.length; i++) {
					long id = Long.parseLong(ids[i]);
					IDs.add(id);
				}
				List<SysUserDO> list = userMapper.findUserByIDs(IDs);
				List<TaskDO> taskList = new ArrayList<TaskDO>();
				for (SysUserDO user : list) {
					TaskDO task = new TaskDO();
					task.setActionUrl(action);
					task.setCreateTime(new Date());
					task.setCreatorName(params.getSubmit());
					task.setExecutor(user.getUserId().toString());
					task.setExecutorName(user.getName());
					task.setId(UUIDUtil.getUUID());
					task.setNode(node + 1);
					task.setProcessCode(processCode);
					task.setSdata1(params.getSdata1());
					task.setTaskName(params.getTaskName());
					task.setStatus("审批中");
					task.setTaskType("processing");
					task.setTaskTypeDesc("正向流转");
					taskList.add(task);
				}
				if (taskList.size() > 0)
					k = taskMapper.batchSaveTask(taskList);
			}
		}
		return k;
	}

	@Override
	public TaskDO getTask(String sdata1) {
		TaskParamsDO query = new TaskParamsDO();
		query.setSdata1(sdata1);
		SysUserDO user = UserCommon.getUser();
		Long executor = user.getUserId();
		query.setExecutor(executor);
		query.setStrExec(executor.toString());
		TaskDO task = taskMapper.findTaskByTaskParamsDO(query);
		if (task == null) {
			task = new TaskDO();
			task.setIsOwn(false);
		}
		return task;
	}

	@Override
	public Map<String, Object> taskCenterList(RowBounds rb, TaskCenterDO taskCenter) {
		Map<String, Object> map = new HashMap<String, Object>();
		String centerType = taskCenter.getCenterType();
		List<TaskCenterDO> list = new ArrayList<TaskCenterDO>();
		int count = 0;
		SysUserDO user = UserCommon.getUser();
		taskCenter.setExecutor(user.getUserId().toString());
		// 提交人
		String creatorName = taskCenter.getCreatorName();
		if (!StringUtils.isEmpty(creatorName)) {
			creatorName = "%" + creatorName + "%";
			taskCenter.setCreatorName(creatorName);
		}
		String dateStr = taskCenter.getSubmitDateStr();
		// 日期范围过滤
		if (!StringUtils.isEmpty(dateStr)) {
			String[] ds = dateStr.split("~");
			String start = ds[0];
			String end = ds[1];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate;
			Date endDate;
			try {
				startDate = sdf.parse(start);
				taskCenter.setStartDate(startDate);
				endDate = sdf.parse(end);
				taskCenter.setEndDate(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if ("done".equals(centerType)) {
			// 已办任务列表
			list = taskMapper.findDoneTaskListByTaskCenter(rb, taskCenter);
			count = taskMapper.countDoneTaskListByTaskCenter(taskCenter);

		} else if ("undo".equals(centerType)) {
			// 代办任务列表
			list = taskMapper.findUndoTaskListByTaskCenter(rb, taskCenter);
			count = taskMapper.countUndoTaskListByTaskCenter(taskCenter);
		}
		map.put("data", list);
		map.put("count", count);
		map.put("msg", "");
		map.put("code", "0");
		return map;
	}

	public int getNextNode(TaskParamsDO paramsDO) {
		int result = 0;
		// 当前环节的节点
		int node = paramsDO.getNode();
		// 获取下一节点的配置信息
		String processCode = paramsDO.getProcessCode();
		ProcessConfigDetails details = configMapper.findConfigDetailsByProcessCodeAndNode(processCode, node + 1);
		if (details != null) {
			String nodeType = details.getNodeType();
			// 02 是条件环节
			if ("02".equals(nodeType)) {
				// 获取条件环节的执行规则，如果是N或者为空，则流转到下一节点
				String execRule = details.getExecRule();
				if ("Y".equals(execRule)) {
					// 配置详情的id
					String detailsId = details.getId();
					// 条件环节的执行规则
					ProcessRuleDO ruleDO = new ProcessRuleDO();
					ruleDO.setMainId(detailsId);
					ProcessRuleDO rule = ruleMapper.findConfigRuleByRuleDO(ruleDO);
					if (rule != null) {
						// 条件环节的条件
						String condition = rule.getCondition();
						if (condition.contains("(") && condition.contains(")")) {
							String[] split = condition.split("\\(");
							String methodCode = split[0];
							RuleService rs = new RuleService();
							Class<? extends RuleService> clazz = rs.getClass();
							try {
								// 获取对应的方法
								Method method = clazz.getDeclaredMethod(methodCode, String.class);
								// 传入业务数据的id
								String sdata1 = paramsDO.getSdata1();
								boolean invoke = (boolean) method.invoke(rs, sdata1);
								if (invoke) {
									result = rule.gettBackNode();
								} else {
									result = rule.getfBackNode();
								}
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException
									| IllegalArgumentException | InvocationTargetException e) {
								e.printStackTrace();
								result = node + 2;
							}
						} else {
							result = node + 2;
						}
					}
				} else {
					result = node + 2;
				}
			} else {
				result = node + 1;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SysUserDO> getExecutorList(SysUserDO user, String confDetailsId, String sdata1) {
		List<SysUserDO> list = new ArrayList<SysUserDO>();
		// 获取执行规则详情
		ProcessRuleDO ruleDO = new ProcessRuleDO();
		ruleDO.setMainId(confDetailsId);
		ProcessRuleDO rule = ruleMapper.findConfigRuleByRuleDO(ruleDO);
		String defaultAudit = rule.getDefaultAudit();
		if (!StringUtils.isEmpty(defaultAudit) && "1".equals(defaultAudit)) {
			SysUserDO userDO = new SysUserDO();
			// 下一节点的审批人是直接上级
			Long leader = user.getLeader();
			if (leader != null) {
				userDO.setUserId(user.getLeader());
				list = userMapper.findUsersByParams(userDO);
			}
		}
		// 执行者表达式
		String execStr = rule.getExecStr();
		if (!StringUtils.isEmpty(execStr)) {
			if (execStr.startsWith("relationValue")) {
				String post = execStr.replace("relationValue(", "");
				String pre = post.replace(")", "");
				String[] params = pre.split(",");
				RelationValuesParamsDO paramsDO = new RelationValuesParamsDO();
				paramsDO.setTableName(params[0]);
				paramsDO.setIdName(params[1]);
				if (!StringUtils.isEmpty(sdata1))
					sdata1 = "'" + sdata1 + "'";
				paramsDO.setIdValue(sdata1);
				paramsDO.setCondition(params[3]);
				paramsDO.setOrderRelation(params[4]);
				paramsDO.setReturnRelation(params[5]);
				RelationValuesResultDO result1 = ruleMapper.relationValues(paramsDO);
				String username = result1.getResult();
				Map<String, Object> map = new HashMap<String, Object>();
				String pType = params[6];
				if ("userid".equals(pType)) {
					try {
						Long l = Long.parseLong(username);
						map.put("userId", l);
					} catch (Exception e) {
						map.put("username", username);
					}
				} else {
					map.put("username", username);
				}
				SysUserDO user1 = userMapper.findUserForTaskByMap(map);
				if (user != null)
					list.add(user1);
			} else {
				String[] split = execStr.split("\\(");
				String methodCode = split[0];
				String methodParams = sdata1;
				Class<? extends RuleService> clazz = ruleService.getClass();
				try {
					if (!StringUtils.isEmpty(methodParams)) {
						Method method = clazz.getDeclaredMethod(methodCode, String.class);
						list = (List<SysUserDO>) method.invoke(ruleService, methodParams);
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		// 执行者范围表达式
		String execRange = rule.getExecRange();
		if (!StringUtils.isEmpty(execRange)) {

		}
		// 待选执行者
		String executor = rule.getExecutor();
		if (!StringUtils.isEmpty(executor)) {
			String[] ids = executor.split(",");
			List<Long> IDs = new ArrayList<Long>();
			for (int i = 0; i < ids.length; i++) {
				long id = Long.parseLong(ids[i]);
				IDs.add(id);
			}
			list = userMapper.findUserByIDs(IDs);
		}
		return list;
	}

	@Override
	public int removeTaskBySdata1(TaskParamsDO params) {
		int i = 0;
		String sdata1 = params.getSdata1();
		if (!StringUtils.isEmpty(sdata1)) {
			i += taskMapper.removeTaskByTaskParams(params);
			i += taskMapper.removeTaskHisByTaskParams(params);
		}
		return i;
	}

}
