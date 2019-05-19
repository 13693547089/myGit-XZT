package com.faujor.service.rm;

import java.util.Map;

public interface DelieveOccupyService {
	/**
	 * 获取解除占用页面数据
	 * @param code
	 * @param codeDesc
	 * @return
	 */
	Map<String, Object> getAppoDeliData(String code, String codeDesc);
	/**
	 * 修改状态
	 * @param code
	 * @param status
	 * @param type
	 * @return
	 */
	boolean updateStatus(String code, String status, String type);

}
