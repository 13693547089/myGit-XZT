package com.faujor.service.sync;

import java.util.List;
import java.util.Map;

import com.faujor.entity.sapcenter.bam.OraCxjhEntity;

/**
 * 中间库数据同步 服务类
 * @author Vincent
 *
 */
public interface SyncService {
	
	/**
	 * 获取中间库T_ORA_CXJH表数据
	 * @param map
	 * @return
	 */
	public List<OraCxjhEntity> getMatSyncInfoByCondition(Map<String,Object> map);
	
	/**
	 * 中间库T_ORA_CXJH表数据插入至本库中
	 * @param map
	 */
	public void saveCxjhMatList(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取同步的中间库表的数量
	 * @param map
	 * @return
	 */
	public int getCxjhMatCount(Map<String, Object> map);
	
	/**
	 * 根据条件删除中间库表的数据
	 * @param map
	 */
	public void delCxjhMatByCondition(Map<String, Object> map);
	
	/**
	 * 同步生产交货计划至FTP
	 */
	public void syncPadPlanReport();
	
	/**
	 * 同步产能上报信息
	 */
	public void syncPdrInfo();
	
	/**
	 * 同步生产交货计划至sap中间库
	 */
	public void syncPadPlanInfo();
}
