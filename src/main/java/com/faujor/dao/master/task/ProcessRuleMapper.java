package com.faujor.dao.master.task;

import com.faujor.entity.task.ProcessRuleBackDO;
import com.faujor.entity.task.ProcessRuleDO;
import com.faujor.entity.task.RelationValuesParamsDO;
import com.faujor.entity.task.RelationValuesResultDO;
import com.faujor.entity.task.TaskParamsDO;

public interface ProcessRuleMapper {
	/**
	 * 关系
	 * 
	 * @param paramsDO
	 * @return
	 */
	RelationValuesResultDO relationValues(RelationValuesParamsDO paramsDO);

	/**
	 * 返回配置规则
	 * 
	 * @param ruleDO
	 * @return
	 */
	ProcessRuleDO findConfigRuleByRuleDO(ProcessRuleDO ruleDO);

	/**
	 * 保存配置规则
	 * 
	 * @param ruleDO
	 * @return
	 */
	int saveExecRule(ProcessRuleDO ruleDO);

	/**
	 * 更新配置规则
	 * 
	 * @param ruleDO
	 * @return
	 */
	int updateExecRule(ProcessRuleDO ruleDO);

	/**
	 * 获取回退规则信息
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
	 * 更新回退规则信息
	 * 
	 * @param ruleBackDO
	 * @return
	 */
	int updateBackRule(ProcessRuleBackDO ruleBackDO);

	/**
	 * 根据任务节点的信息获取回退规则
	 * 
	 * @param params
	 * @return
	 */
	ProcessRuleBackDO findBackRuleByTaskParamsDO(TaskParamsDO params);

	/**
	 * 删除执行规则
	 * 
	 * @param ruleDO
	 * @return
	 */
	int removeExecRule(ProcessRuleDO ruleDO);

	/**
	 * 通过配置信息的processCode, node获取执行规则
	 * 
	 * @param query
	 * @return
	 */
	ProcessRuleDO findConfigRuleByQuery(TaskParamsDO query);
}
