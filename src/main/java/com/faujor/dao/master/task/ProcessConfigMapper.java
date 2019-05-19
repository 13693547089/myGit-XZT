package com.faujor.dao.master.task;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.task.ProcessConfig;
import com.faujor.entity.task.ProcessConfigDetails;
import com.faujor.entity.task.ProcessRuleDO;
import com.faujor.entity.task.RelationValuesParamsDO;
import com.faujor.entity.task.RelationValuesResultDO;

public interface ProcessConfigMapper {

	List<ProcessConfig> findProcessConfigList(RowBounds rb);

	int findProcessConfigCount();

	ProcessConfig findProcessConfigById(String id);

	List<ProcessConfigDetails> findConfigDetailsByProcessId(String processId);

	@Select("select count(*) from TM_PROCESS_DETAILS t where t.CONF_ID = #{processId}")
	int countConfigDetailsByProcessId(String processId);

	int save(ProcessConfig config);

	int update(ProcessConfig config);

	int saveDetailsInfo(List<ProcessConfigDetails> list);

	@Select("select t.id from TM_PROCESS_DETAILS t where t.conf_id =#{processId}")
	List<String> findConfigDetailsIdsByProcessId(String processId);

	int updateDetailsInfo(ProcessConfigDetails pcd);

	int removeDetailsInfo(List<ProcessConfigDetails> delList);

	ProcessConfig findProcessConfigByCode(String processCode);

	List<ProcessConfigDetails> findConfigDetailsByPIdANDNode(Map<String, Object> map);

	ProcessConfigDetails findConfigDetailsById(String detailsId);

	@Delete("delete from tm_process_details where conf_id = #{id}")
	int removeDetailsByConfigId(String id);

	int batchRemoveConfigByIDs(List<String> ids);

	ProcessConfigDetails findConfigDetailsByProcessCodeAndNode(String processCode, int node);

	List<ProcessConfigDetails> findConfigDetailsByProcessCode(String processCode);

	/**
	 * 根据流程主数据的编码或者id获取配置详情
	 * 
	 * @param config
	 * @return
	 */
	List<ProcessConfigDetails> findConfigDetailsListByProcess(ProcessConfig config);
}
