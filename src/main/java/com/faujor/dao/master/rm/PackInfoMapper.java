package com.faujor.dao.master.rm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.rm.CutMatePack;
import com.faujor.entity.rm.PackOrderVO;
import com.faujor.entity.rm.SemiMatePack;

public interface PackInfoMapper {
	/**
	 * 获取半成品包材统计表的数据
	 * @param map
	 * @return
	 */
	List<SemiMatePack> getSemiMatePackList(Map<String, Object> map);
	/**
	 * 获取半成品包材统计表数据的数量
	 * @param map
	 * @return
	 */
	int getSemiMatePackListCount(Map<String, Object> map);
	/**
	 * 获取导出数据
	 * @param map
	 * @return
	 */
	List<SemiMatePack> getSemimatePacks(Map<String, Object> map);
	/**
	 * 获取打切包材统计表数据
	 * @param map
	 * @return
	 */
	List<CutMatePack> getCutMatePackListByPage(Map<String, Object> map);
	/**
	 * 获取打切包材统计表数据的数量
	 * @param map
	 * @return
	 */
	int getCutMatePackListByPageCount(Map<String, Object> map);
	/**
	 * 获取打切包材导出数据
	 * @param map
	 * @return
	 */
	List<CutMatePack> getCutMatePackList(Map<String, Object> map);
	/**
	 * 包材采购订单执行情况统计表数据
	 * @param map
	 * @return
	 */
	List<PackOrderVO> getPackOrderListByPage(Map<String, Object> map);
	/**
	 * 包材采购订单执行情况统计表数据的数据
	 * @param map
	 * @return
	 */
	int getPackOrderListByPageCount(Map<String, Object> map);
	/**
	 * 获取导出包材订单执行情况统计表
	 * @param map
	 * @return
	 */
	List<PackOrderVO> getPackOrderList(Map<String, Object> map);

}
