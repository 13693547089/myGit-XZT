package com.faujor.service.task;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.Tree;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.task.ProcessConfig;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.entity.task.ProcessRuleDO;
import com.faujor.entity.task.RelationValuesResultDO;

public interface ProcessConfigService {
	Map<String, Object> findProcessConfigList(RowBounds rb);

	ProcessConfig findProcessConfigById(String id);

	Map<String, Object> findConfigDetailsByProcessId(String processId);

	int removeConfigByIds(String configIds);

	List<Tree<OrgDo>> getExecutorSelectTree(String executor);

	int saveConfigInfo(ProcessConfig config, List<ProcessConfigDetails> jsonToList);

	ProcessConfigDetails findConfigDetailsById(String detailsId);

	List<ProcessConfigDetails> findConfigDetailsByProcessCode(String processCode);

	/**
	 * 根据流程主表数据（id或者code）获取配置详情
	 * 
	 * @param config
	 * @return
	 */
	List<ProcessConfigDetails> findConfigDetailsListByProcess(ProcessConfig config);
}
