package com.faujor.web.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.ProcessRuleBackDO;
import com.faujor.entity.task.ProcessRuleDO;
import com.faujor.entity.task.RelationValuesResultDO;
import com.faujor.service.basic.BasicService;
import com.faujor.service.task.ProcessRuleService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;

@RequestMapping("/rule")
@Controller
public class TaskRuleController {
	@Autowired
	private ProcessRuleService ruleService;
	@Autowired
	private BasicService basicService;

	/**
	 * 设置执行规则
	 * 
	 * @param execRule
	 * @return
	 */
	@RequestMapping("/setExecRule")
	public String setExecRule(Model model, ProcessRuleDO ruleDO) {
		ProcessRuleDO rule = ruleService.findConfigRuleByRuleDO(ruleDO);
		if (rule == null) {
			rule = new ProcessRuleDO();
			rule.setMainId(ruleDO.getMainId());
			rule.setNode(ruleDO.getNode());
		}
		model.addAttribute("rule", rule);
		List<Dic> dicList = basicService.findDicListByCategoryCode("SYMBOL");
		model.addAttribute("dicList", dicList);
		String url = "task/rule/execRule";
		if ("02".equals(ruleDO.getNodeType())) {
			url = "task/rule/condExecRule";
		}
		return url;
	}

	/**
	 * 校验任务名称
	 * 
	 * @param taskName
	 * @return
	 */
	@RequestMapping("/checkTaskName")
	@ResponseBody
	public RestCode checkTaskName(String taskName) {
		RelationValuesResultDO result = ruleService.checkTaskName(taskName);
		if (result != null)
			return RestCode.ok(JsonUtils.beanToJson(result));
		return RestCode.error();
	}

	/**
	 * 校验执行者
	 * 
	 * @param execStr
	 * @return
	 */
	@RequestMapping("/checkExecStr")
	@ResponseBody
	public RestCode checkExecStr(String execStr) {
		SysUserDO result = ruleService.checkExecStr(execStr);
		if (result != null)
			return RestCode.ok(JsonUtils.beanToJson(result));
		return RestCode.error();
	}

	/**
	 * 保存执行规则
	 * 
	 * @param ruleDO
	 * @return
	 */
	@RequestMapping("/saveExecRule")
	@ResponseBody
	public RestCode saveExecRule(ProcessRuleDO ruleDO) {
		int i = ruleService.saveExecRule(ruleDO);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 
	 * @param ruleDO
	 * @return
	 */
	@RequestMapping("/removeExecRule")
	@ResponseBody
	public RestCode removeExecRule(ProcessRuleDO ruleDO) {
		int i = ruleService.removeExecRule(ruleDO);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	// 回退规则
	@RequestMapping("/setBackRule")
	public String setBackRule(Model model, ProcessRuleBackDO ruleBackDO) {
		ProcessRuleBackDO rule = ruleService.findBackRuleByRuleBackDO(ruleBackDO);
		if (rule == null) {
			rule = new ProcessRuleBackDO();
			rule.setMainId(ruleBackDO.getMainId());
			rule.setNode(ruleBackDO.getNode());
		}
		model.addAttribute("rule", rule);
		return "task/rule/backRule";
	}

	/**
	 * 保存回退规则
	 * 
	 * @param ruleBackDO
	 * @return
	 */
	@RequestMapping("/saveBackRule")
	@ResponseBody
	public RestCode saveBackRule(ProcessRuleBackDO ruleBackDO) {
		int i = ruleService.saveBackRule(ruleBackDO);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 校验条件
	 * 
	 * @param preCond
	 * @return
	 */
	@RequestMapping("/checkCondition")
	@ResponseBody
	public RestCode checkPreCond(String condition) {
		String cond = ruleService.checkPreCond(condition);
		if ("true".contentEquals(cond) || "false".equals(cond))
			return RestCode.ok(cond);
		return RestCode.error("error");
	}
}
