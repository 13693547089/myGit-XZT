package com.faujor.dao.master.bam;

import com.faujor.entity.bam.OrderMonth;
import com.faujor.entity.bam.order.EkpoDO;

import java.util.List;
import java.util.Map;

/**
 * 月初未交货数量
 * 
 * @author hql
 *
 */
public interface OrderMonthMapper {

	/**
	 * 创建
	 * 
	 * @param record
	 * @return
	 */
	int insert(OrderMonth record);

	/**
	 * 获取供应商或者物料额未交货数量
	 * 
	 * @param map
	 * @return
	 */
	Double selectUndeliveredNumByMap(Map<String, Object> map);

	/**
	 * 月初批量保存订单数据
	 * 
	 * @return
	 */
	int ScheduledAsybcMonthOrder();

	/**
	 * 批量保存数据
	 * 
	 * @param addList
	 * @return
	 */
	// int batchSaveByEkpoList(List<EkpoDO> addList);

}