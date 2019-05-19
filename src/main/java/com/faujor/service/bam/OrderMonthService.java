package com.faujor.service.bam;

import java.util.Map;

public interface OrderMonthService {
	/**
	 * 获取供应商或者物料额未交货数量
	 * 
	 * @param map
	 * @return
	 */
	Double selectUndeliveredNumByMap(Map<String, Object> map);

	int ScheduledAsybcMonthOrder();
}
