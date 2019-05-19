package com.faujor.web.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.ProcessConfig;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.entity.task.TaskCenterDO;
import com.faujor.entity.task.TaskCheckResult;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.task.ProcessConfigService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessConfigService configService;

	/**
	 * 代办列表
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/undoList")
	@ResponseBody
	public Map<String, Object> undoList(Integer page, Integer limit) {
		SysUserDO user = UserCommon.getUser();
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		String executor = user.getUserId().toString();
		return taskService.findUndoTaskListByParams(rb, executor);
	}

	/**
	 * 已办列表
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("/doneList")
	@ResponseBody
	public Map<String, Object> doneList(Integer page, Integer limit) {
		SysUserDO user = UserCommon.getUser();
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		String executor = user.getUserId().toString();
		return taskService.findDoneTaskListByExecutor(rb, executor);
	}

	/**
	 * 校验是否有配置信息
	 * 
	 * @param processCode
	 * @return
	 */
	@RequestMapping("/checkConfig")
	@ResponseBody
	public Boolean checkConfig(String processCode) {
		ProcessConfig config = new ProcessConfig();
		config.setProcessCode(processCode);
		List<ProcessConfigDetails> list = configService.findConfigDetailsListByProcess(config);
		if (list.size() > 0)
			return true;
		return false;
	}

	/**
	 * 校验是否可以打开选择人员弹出框
	 * 
	 * @return
	 */
	@RequestMapping("/isSelectExecutor")
	@ResponseBody
	public TaskCheckResult isSelectExecutor(String sdata1, String processCode) {

		return taskService.isSelectExecutor(sdata1, processCode);
	}

	/**
	 * 保存业务的时候生成一条代办任务
	 * 
	 * @param sdata1
	 * @param processCode
	 * @param taskName
	 * @return
	 */
	@RequestMapping("/initialProcess")
	@ResponseBody
	public RestCode initialProcess(TaskParamsDO params) {
		int i = taskService.initialProcess(params);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 选择候选人员
	 * 
	 * @param model
	 * @param sdata1
	 * @param node
	 * @param processCode
	 * @return
	 */
	@RequestMapping("/getSelectExecutor")
	public String getSelecteExecutor(Model model, TaskParamsDO tpd) {
		// 初始化一个返回页面路径（默认是选择候选人页面）
		String url = "task/task/selectExecutor";
		SysUserDO user = UserCommon.getUser();
		tpd.setExecutor(user.getUserId());
		TaskDO task = taskService.findTaskByParams(tpd);
		if (task != null) {
			tpd.setNode(task.getNode());
		} else {
			tpd.setNode(1);
		}
		List<SysUserDO> list = taskService.findExecutorsByParams(tpd);
		String userJson = JsonUtils.beanToJson(list);
		model.addAttribute("userJson", userJson);
		return url;
	}

	/**
	 * 开始流转
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/startProcess")
	public RestCode startProcess(TaskParamsDO params) {
		int i = taskService.startProcess(params);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 完成当前人的任务（因为当前节点还有其他人的任务未完成）
	 * 
	 * @param sdata1
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doCurrentTask")
	public RestCode doCurrentTask(String sdata1) {
		int i = taskService.doCurrentTask(sdata1);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@RequestMapping("/doOverTask")
	@ResponseBody
	public RestCode doOverTask(String sdata1) {
		int i = taskService.doOverTask(sdata1);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 处理当前任务，并且将下一节点的执行者设置为直接领导
	 * 
	 * @param sdata1
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doTaskToLeader")
	public RestCode doTaskToLeader(TaskParamsDO query) {
		int i = taskService.doTaskToLeader(query);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 任务流转
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/isProcess")
	public RestCode isProcess(TaskParamsDO params, String type) {
		int i = taskService.isProcess(params, type);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 任务回退
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/backProcess")
	public RestCode backProcess(TaskParamsDO params) {
		int i = taskService.backProcess(params);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 获取任务数据
	 * 
	 * @param sdata1
	 * @return
	 */
	@RequestMapping("/getTask")
	@ResponseBody
	public TaskDO getTask(String sdata1) {

		return taskService.getTask(sdata1);
	}

	/**
	 * 进入任务中心
	 * 
	 * @param model
	 * @param centerType
	 * @return
	 */
	@RequestMapping("/taskCenter")
	public String taskCenter(Model model, String centerType) {
		model.addAttribute("centerType", centerType);
		// undo/已保存，审批中，已回退;done/已完成，已回退
		List<String> statusList = new ArrayList<String>();
		if ("undo".equals(centerType)) {
			statusList.add("已保存");
			statusList.add("审批中");
			statusList.add("已回退");
		} else {
			statusList.add("已完成");
			statusList.add("已回退");
		}
		model.addAttribute("statusList", statusList);
		return "task/task/taskCenter";
	}

	/**
	 * 任务中心列表
	 * 
	 * @param taskCenter
	 * @return
	 */
	@RequestMapping("/taskCenterList")
	@ResponseBody
	public Map<String, Object> taskCenterList(Integer page, Integer limit, TaskCenterDO taskCenter) {
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return taskService.taskCenterList(rb, taskCenter);
	}

	/**
	 * 删除业务的时候级联删除任务
	 * 
	 * @param sdata1
	 * @return
	 */
	@RequestMapping("/removeTaskBySdata1")
	@ResponseBody
	public RestCode removeTaskBySdata1(TaskParamsDO params) {
		int i = taskService.removeTaskBySdata1(params);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}
}
