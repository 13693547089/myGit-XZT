package com.faujor.service.rm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.rm.CutMatePack;
import com.faujor.entity.rm.PackOrderVO;
import com.faujor.entity.rm.SemiMatePack;

public interface PackInfoService {
	/**
	 * 获取半成品包材统计表
	 * @param map
	 * @return
	 */
	Map<String, Object> getSemiMatePackList(Map<String, Object> map);
	/**
	 * 获取半成品包材导出数据
	 * @param map
	 * @return
	 */
	List<SemiMatePack> getSemimatePacks(Map<String, Object> map);
	/**
	 * 获取打切包材统计表
	 * @param map
	 * @return
	 */
	Map<String, Object> getCutMatePackListByPage(Map<String, Object> map);
	/**
	 * 获取抬头字段信息
	 * @param cutMonth
	 * @return
	 */
	String getCutMatePackListFields(String cutMonth);
	/**
	 * 获取打切包材导出数据
	 * @param map
	 * @return
	 */
	Map<String, Object> getCutMatePacks(Map<String, Object> map);
	/**
	 * 包材订单执行情况统计表
	 * @param map
	 * @return
	 */
	Map<String, Object> getPackOrderListByPage(Map<String, Object> map);
	/**
	 * 获取导出包材订单执行情况统计表数据
	 * @param map
	 * @return
	 */
	List<PackOrderVO> getPackOrderList(Map<String, Object> map);

}
