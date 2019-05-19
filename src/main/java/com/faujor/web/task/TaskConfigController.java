package com.faujor.web.task;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.Tree;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.task.ProcessConfig;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.service.basic.BasicService;
import com.faujor.service.task.ProcessConfigService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;

@Controller
public class TaskConfigController {
	@Autowired
	private ProcessConfigService configService;
	@Autowired
	private BasicService basicService;

	/**
	 * 配置信息列表
	 * 
	 * @return
	 */
	@RequestMapping("/taskConfig")
	public String configIndex() {
		return "task/config/config";
	}

	@Log(value = "获取配置信息")
	@RequestMapping("/processConfig/configList")
	@ResponseBody
	public Map<String, Object> configList(Model model, Integer limit, Integer page) {
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return configService.findProcessConfigList(rb);
	}

	/**
	 * 配置信息详情
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/processConfigEdit")
	public String edit(Model model, @RequestParam("id") String id) {
		ProcessConfig pc = configService.findProcessConfigById(id);
		model.addAttribute("process", pc);
		List<Dic> statusList = basicService.findDicListByCategoryCode("usedStatus");
		model.addAttribute("statusList", statusList);
		return "task/config/edit";
	}

	@RequestMapping("/processConfig/findConfigDetailsByProcessId")
	@ResponseBody
	public Map<String, Object> configDetails(String processId) {
		return configService.findConfigDetailsByProcessId(processId);
	}

	@PostMapping("processConfig/remove")
	@ResponseBody
	public RestCode configRemove(String configIds) {
		int i = configService.removeConfigByIds(configIds);
		if (i > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error();
		}
	}

	@RequestMapping("/processConfig/processInfo")
	public String processInfo(Model model, String processId, String type) {
		ProcessConfig config = new ProcessConfig();
		if (!StringUtils.isEmpty(processId)) {
			config = configService.findProcessConfigById(processId);
		}
		model.addAttribute("process", config);
		// 处理方式
		List<Dic> handleList = basicService.findDicListByCategoryCode("TASK_HANDLE");
		model.addAttribute("handleList", handleList);
		return "task/config/edit";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/processConfig/processDetails")
	@ResponseBody
	public List<ProcessConfigDetails> processDetails(String processId) {
		Map<String, Object> map = configService.findConfigDetailsByProcessId(processId);
		return (List<ProcessConfigDetails>) map.get("rows");
	}

	@RequestMapping("/processConfig/executor")
	public String executor(Model model, String executor) {
		model.addAttribute("executor", executor);
		return "task/config/executor";
	}

	/**
	 * 选择审批人
	 * 
	 * @param executor
	 * @return
	 */
	@RequestMapping("/processConfig/executorSelect")
	@ResponseBody
	public List<Tree<OrgDo>> executorSelect(String executor) {
		List<Tree<OrgDo>> tree = configService.getExecutorSelectTree(executor);
		return tree;
	}

	/**
	 * 保存配置信息
	 * 
	 * @param config
	 * @param detailsData
	 * @return
	 */
	@PostMapping("/processConfig/save")
	@ResponseBody
	public RestCode save(ProcessConfig config, String detailsData) {
		List<ProcessConfigDetails> jsonToList = JsonUtils.jsonToList(detailsData, ProcessConfigDetails.class);
		int i = configService.saveConfigInfo(config, jsonToList);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 添加配置信息
	 * 
	 * @param model
	 * @param detailsId
	 * @param type
	 * @return
	 */
	@RequestMapping("/processConfig/add")
	public String addDetails(Model model, String detailsId, String type) {
		ProcessConfigDetails pcd = new ProcessConfigDetails();
		if (!"add".equals(type))
			pcd = configService.findConfigDetailsById(detailsId);
		model.addAttribute("pcd", pcd);
		return "task/config/add";
	}

	/**
	 * 节点类型
	 * 
	 * @param model
	 * @param nodeType
	 * @return
	 */
	@RequestMapping("/processConfig/nodeType")
	public String nodeType(Model model, String nodeType) {
		model.addAttribute("nodeType", nodeType);
		return "task/config/nodeType";
	}
}
