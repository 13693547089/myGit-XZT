package com.faujor.service.task.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.common.UserMapper;
import com.faujor.dao.master.task.ProcessRuleMapper;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.task.ProcessRuleBackDO;
import com.faujor.entity.task.ProcessRuleDO;
import com.faujor.entity.task.RelationValuesParamsDO;
import com.faujor.entity.task.RelationValuesResultDO;
import com.faujor.service.task.ProcessRuleService;
import com.faujor.service.task.RuleService;
import com.faujor.utils.UUIDUtil;

@Service("processRuleService")
public class ProcessRuleServiceImpl implements ProcessRuleService {
	@Autowired
	private ProcessRuleMapper ruleMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RuleService ruleService;

	@Override
	public RelationValuesResultDO checkTaskName(String taskName) {
		RelationValuesResultDO result = new RelationValuesResultDO();
		String[] names = taskName.split("&&");
		for (String name : names) {
			String preResult = result.getResult();
			preResult = StringUtils.isEmpty(preResult) ? "" : preResult;
			if (name.contains("relationValue")) {
				String post = name.replace("relationValue(", "");
				String pre = post.replace(")", "");
				String[] params = pre.split(",");
				RelationValuesParamsDO paramsDO = new RelationValuesParamsDO();
				paramsDO.setTableName(params[0]);
				paramsDO.setIdName(params[1]);
				paramsDO.setIdValue(params[2]);
				paramsDO.setCondition(params[3]);
				paramsDO.setOrderRelation(params[4]);
				paramsDO.setReturnRelation(params[5]);
				RelationValuesResultDO result1 = ruleMapper.relationValues(paramsDO);
				if (result1 != null)
					preResult += result1.getResult();
			} else {
				preResult += name;
			}
			result.setResult(preResult);
		}
		return result;
	}

	@Override
	public ProcessRuleDO findConfigRuleByRuleDO(ProcessRuleDO ruleDO) {

		return ruleMapper.findConfigRuleByRuleDO(ruleDO);
	}

	@Override
	public int saveExecRule(ProcessRuleDO ruleDO) {
		String id = ruleDO.getId();
		if (StringUtils.isEmpty(id)) {
			id = UUIDUtil.getUUID();
			ruleDO.setId(id);
			return ruleMapper.saveExecRule(ruleDO);
		}
		return ruleMapper.updateExecRule(ruleDO);
	}

	@Override
	public SysUserDO checkExecStr(String execStr) {
		if (execStr.startsWith("relationValue")) {
			String post = execStr.replace("relationValue(", "");
			String pre = post.replace(")", "");
			String[] params = pre.split(",");
			RelationValuesParamsDO paramsDO = new RelationValuesParamsDO();
			paramsDO.setTableName(params[0]);
			paramsDO.setIdName(params[1]);
			paramsDO.setIdValue(params[2]);
			paramsDO.setCondition(params[3]);
			paramsDO.setOrderRelation(params[4]);
			paramsDO.setReturnRelation(params[5]);
			RelationValuesResultDO result1 = ruleMapper.relationValues(paramsDO);
			if (result1 != null) {
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
				SysUserDO user = userMapper.findUserForTaskByMap(map);
				if (user != null)
					return user;
			}
		} else if (execStr.contains("(") && execStr.contains(")")) {
			String[] split = execStr.split("\\(");
			String methodCode = split[0];
			String suf = split[1];
			String[] split2 = suf.split("\\)");
			String methodParams = split2[0];
			Class<? extends RuleService> clazz = ruleService.getClass();
			try {
				if (!StringUtils.isEmpty(methodParams)) {
					Method method = clazz.getDeclaredMethod(methodCode, String.class);
					List<SysUserDO> invoke = (List<SysUserDO>) method.invoke(ruleService, methodParams);
					return invoke.get(0);
				} else {
					return null;
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ProcessRuleBackDO findBackRuleByRuleBackDO(ProcessRuleBackDO ruleBackDO) {
		return ruleMapper.findBackRuleByRuleBackDO(ruleBackDO);
	}

	@Override
	public int saveBackRule(ProcessRuleBackDO ruleBackDO) {
		String id = ruleBackDO.getId();
		int i = 0;
		if (StringUtils.isEmpty(id)) {
			id = UUIDUtil.getUUID();
			ruleBackDO.setId(id);
			i = ruleMapper.saveBackRule(ruleBackDO);
		} else {
			i = ruleMapper.updateBackRule(ruleBackDO);
		}
		return i;
	}

	@Override
	public String checkPreCond(String preCond) {
		if (preCond.contains("(") && preCond.contains(")")) {
			String result = "false";
			String[] split = preCond.split("\\(");
			String methodCode = split[0];
			String suf = split[1];
			String[] split2 = suf.split("\\)");
			String methodParams = split2[0];
			RuleService rs = new RuleService();
			Class<? extends RuleService> clazz = rs.getClass();
			try {
				if (!StringUtils.isEmpty(methodParams)) {
					Method method = clazz.getDeclaredMethod(methodCode, String.class);
					boolean invoke = (boolean) method.invoke(rs, methodParams);
					if (invoke)
						result = "true";
				} else {
					result = "error";
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			return result;
		} else {
			return "error";
		}
	}

	@Override
	public int removeExecRule(ProcessRuleDO ruleDO) {
		String id = ruleDO.getId();
		int i = 0;
		if (!StringUtils.isEmpty(id))
			i = ruleMapper.removeExecRule(ruleDO);
		return i;
	}
}
