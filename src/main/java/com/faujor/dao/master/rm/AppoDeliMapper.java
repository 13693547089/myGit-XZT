package com.faujor.dao.master.rm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.rm.AppoDeli;

public interface AppoDeliMapper {
	/**
	 * 预约送货一览表分页数据
	 * @param map
	 * @return
	 */
	public  List<AppoDeli> queryAppoDeliByPage(Map<String, Object> map);
	/**
	 * 预约送货一览表数据总条数
	 * @param map
	 * @return
	 */
	public  int queryAppoDeliByPageCount(Map<String, Object> map);
	/**
	 * 查询要导出的数据
	 * @param map
	 * @return
	 */
	public List<AppoDeli> queryAppoDeliListByMap(Map<String, Object> map);
	
}
