package com.faujor.web.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.task.ProcessConfigService;
import com.faujor.service.task.TaskService2;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@Controller
//@RequestMapping("/task")
public class TaskDealController {
	@Autowired
	private ProcessConfigService configService;
	@Autowired
	private TaskService2 taskService;

	@GetMapping("/undoList")
	@ResponseBody
	public Map<String, Object> undoList() {
		SysUserDO user = UserCommon.getUser();
		String exectorId = user.getUserId().toString();
		return taskService.undoTaskList(exectorId);
	}

	@GetMapping("doneList")
	@ResponseBody
	public Map<String, Object> doneList() {
		SysUserDO user = UserCommon.getUser();

		String exectorId = user.getUserId().toString();
		return taskService.doneTaskList(exectorId);
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
	public String getSelecteExecutor(Model model, String sdata1, String processCode) {
		TaskParamsDO tpd = new TaskParamsDO();
		tpd.setSdata1(sdata1);
		tpd.setProcessCode(processCode);
		Map<String, Object> map = taskService.getSelecteExecutor(tpd);
		boolean isNode = (boolean) map.get("node");
		boolean isFinished = (boolean) map.get("isFinished");
		if (isNode && isFinished) {
			@SuppressWarnings("unchecked")
			List<SysUserDO> list = (List<SysUserDO>) map.get("list");
			String userJson = JsonUtils.beanToJson(list);
			model.addAttribute("userJson", userJson);
			return "task/task/selectExecutor";
		} else {
			if (!isNode) {
				model.addAttribute("msg", "没有编码为(" + processCode + ")任务流程的配置信息，请检查配置信息!");
			} else if (!isFinished) {
				model.addAttribute("msg", "等待其他执行者执行任务！");
			}
			model.addAttribute("isNode", isNode);
			model.addAttribute("isFinished", isFinished);
			return "task/task/processMsg";
		}
	}

	/**
	 * 校验是否有配置信息
	 * 
	 * @param processCode
	 * @return
	 */
	@RequestMapping("/checkConfig")
	@ResponseBody
	public boolean checkConfig(String processCode) {
		List<ProcessConfigDetails> list = configService.findConfigDetailsByProcessCode(processCode);
		if (list.size() > 0)
			return true;
		return false;
	}

	@RequestMapping("/startProcess")
	@ResponseBody
	public RestCode startProcess(TaskParamsDO tpd) {
		SysUserDO user = UserCommon.getUser();
		tpd.setSubmit(user.getUserId().toString());
		tpd.setSubmitName(user.getName());
		int k = taskService.createTask(tpd);
		if (k > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 选择完审批人流转审批信息
	 * 
	 * @param tpd
	 * @return
	 */
	@RequestMapping("/isProcess")
	@ResponseBody
	public RestCode isProcess(TaskParamsDO tpd) {
		RestCode rc = taskService.isProcess(tpd);
		return rc;
	}

	/**
	 * 保存业务数据的时候，生成一条任务数据
	 * 
	 * @param tpd
	 * @return
	 */
	@RequestMapping("/process")
	@ResponseBody
	public RestCode process(TaskParamsDO tpd) {
		int i = taskService.createTask(tpd);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 并处理
	 * 
	 * @param tpd
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/andProcess")
	public RestCode andProcess(TaskParamsDO tpd) {
		int i = taskService.andProcess(tpd);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}
}
