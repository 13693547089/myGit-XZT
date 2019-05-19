package com.faujor.dao.sapcenter.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.OrderMate1;
import com.faujor.entity.bam.OrderRele1;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.bam.order.EkpoDO;
import com.faujor.entity.bam.receive.EkbeDO;

public interface OrderMapper1 {
	/**
	 * 获取托普数据库数据进行插入
	 *
	 * @param map
	 * @return
	 */
	public void insertReleaseList();

	/**
	 * 旧版获取托普数据
	 *
	 */
	public List<OrderRele1> SelectTopListFirst(String str);

	public List<String> SelectEkpoTypeFirst();

	// 新版获取托普数据
	public List<OrderRele1> SelectTopList(String format, String beforeFormat);

	public List<OrderRele1> findTopOrderByOrderNo(Map<String, Object> params);

	public List<OrderMate1> SelectTopDetail(String ContOrdeNumb);

	/**
	 *
	 *
	 * 根据行项目当前修改日期查询脱普EKPO表
	 *
	 */

	public List<String> SelectDateTopDetail(String modiDate);

	public List<String> SelectEkpoType();

	/**
	 * 根据合同单号查询托普数据
	 *
	 */
	public List<OrderRele1> condiSelectTopList(String format, String beforeFormat, String number);

	/**
	 * 查询出昨天和前天的数据
	 * 
	 * @param ydStr
	 * @param yydStr
	 * @return
	 */
	public List<OrderRele1> findPurchaseOrderSchedule(String ydStr, String yydStr);

	/**
	 * 根据采购订单号查出所有的行项目
	 * 
	 * @param contOrdeNumb
	 * @return
	 */
	public List<OrderMate1> findTopOrderDetailsByOrderNo(String orderNo);

	/**
	 * 根据订单类型获取EKPO数据，计算月初订单未交数量
	 * 
	 * @return
	 */
	public List<EkpoDO> findEkpoByOrderType();

	/**
	 * 通过 订单编码，项次，内向交货单号获取入库数据
	 * 
	 * @param receMate
	 * @return
	 */
	public List<EkbeDO> findEkbeByReceMate(ReceMate receMate);

}
