package com.faujor.service.task;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.ProcessRuleBackDO;
import com.faujor.entity.task.ProcessRuleDO;
import com.faujor.entity.task.RelationValuesResultDO;

public interface ProcessRuleService {
	/**
	 * 校验任务名称规则
	 * 
	 * @param taskName
	 * @return
	 */
	RelationValuesResultDO checkTaskName(String taskName);

	/**
	 * 获取规则信息
	 * 
	 * @param ruleDO
	 * @return
	 */
	ProcessRuleDO findConfigRuleByRuleDO(ProcessRuleDO ruleDO);

	/**
	 * 保存规则信息
	 * 
	 * @param ruleDO
	 * @return
	 */
	int saveExecRule(ProcessRuleDO ruleDO);

	/**
	 * 校验执行者规则
	 * 
	 * @param execStr
	 * @return
	 */
	SysUserDO checkExecStr(String execStr);

	/**
	 * 获取回退信息
	 * 
	 * @param ruleBackDO
	 * @return
	 */
	ProcessRuleBackDO findBackRuleByRuleBackDO(ProcessRuleBackDO ruleBackDO);

	/**
	 * 保存回退规则信息
	 * 
	 * @param ruleBackDO
	 * @return
	 */
	int saveBackRule(ProcessRuleBackDO ruleBackDO);

	/**
	 * 校验比较值
	 * 
	 * @param preCond
	 * @return
	 */
	String checkPreCond(String preCond);

	/**
	 * 移除执行规则
	 * 
	 * @param ruleDO
	 * @return
	 */
	int removeExecRule(ProcessRuleDO ruleDO);
}
