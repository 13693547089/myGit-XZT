package com.faujor.service.rm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.rm.AppoDeli;

public interface AppoDeliService {

	/**
	 * 预约送货一览列表数据
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryAppoDeliByPage(Map<String, Object> map);
	

	/**
	 * 获取导出预约送货一览表的数据集
	 * @param map
	 * @return
	 */
	public List<AppoDeli> queryAppoDeliListByMap(Map<String, Object> map);

	/**
	 * 处理时间
	 * @param appoDeli2
	 * @return
	 */
	public AppoDeli proceTime(AppoDeli appoDeli2);
}
