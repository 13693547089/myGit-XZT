package com.faujor.dao.sapcenter.sync;

import java.util.List;
import java.util.Map;

import com.faujor.entity.sapcenter.bam.OraCxjhEntity;

/**
 * 中间库数据同步
 * @author Vincent
 *
 */
public interface SyncMapper {
	
	/**
	 * 获取中间库T_ORA_CXJH表数据
	 * @param map
	 * @return
	 */
	public List<OraCxjhEntity> getMatSyncInfoByCondition(Map<String, Object> map);
}
