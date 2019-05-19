package com.faujor.dao.master.sync;

import java.util.Map;

/**
 * 中间库数据同步
 * @author Vincent
 *
 */
public interface SyncMasterMapper {
	
	/**
	 * 中间库T_ORA_CXJH表数据插入至本库中
	 * @param map
	 * @return
	 */
	public void saveCxjhMatList(Map<String, Object> map);
	
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
}
