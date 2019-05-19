package com.faujor.service.bam;

import java.util.Map;

public interface DeliverySignService {

	/**
	 * 获取送货单列表信息
	 * 
	 * @param offset
	 * @param limit
	 * @param params
	 * @return
	 */
	Map<String, Object> getSignData(int offset, int limit, Map<String, Object> params);

	/**
	 * 保存签到信息
	 * 
	 * @param dsd
	 * @return
	 */
	String saveSignInfo(String data);

	/**
	 * 通过送货单号查询得到送货单信息
	 * 
	 * @param deliCode
	 * @return
	 */
	String findDeliveryByDeliCode(String data);
}
