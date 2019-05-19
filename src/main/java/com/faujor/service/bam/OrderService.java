package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.OrderDO;
import com.faujor.entity.bam.OrderEnclosure;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.OrderReleDO;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.mdm.QualSupp;

public interface OrderService {
	/**
	 * 分页展示订单列表
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryReleaseListByPage(Map<String, Object> map);

	public Map<String, Object> queryReleaseListByPageSupp(Map<String, Object> map);

	/**
	 * 查询订单详情
	 *
	 * @param mateId
	 * @return
	 */
	OrderRele queryOrderReleByFid(String fid);

	/**
	 * 修改订单状态
	 *
	 * @param type
	 */
	void updateLatentSuppBySuppId(List<OrderEnclosure> list, String type, OrderRele orderRele);

	/**
	 * 定时同步采购订单数据
	 * 
	 * @param al
	 * @return
	 */
	public int asyncPurchaseOrderSchedule(AsyncLog al);

	public void setDeleteEncl(final String docId);

	/**
	 * 根据供应商的sapId查询这个供应商的所有调拨单
	 *
	 * @param sapId
	 * @return
	 */
	public List<OrderRele> queryOrderReleOfQualSupp(String sapId);

	/**
	 * 获取某个供应商的调拨单
	 *
	 * @param contOrdeNumb
	 * @return
	 */
	public List<OrderMate> queryOrderMateByContOrdeNumb(String contOrdeNumb);

	/**
	 * 获取采购订单号
	 *
	 * @param sapId
	 * @param mateCode
	 * @return
	 */
	public OrderRele querycontOrdeNumbOfOrderReleBySapIdAndMateCode(String sapId, String mateCode, Integer num);

	/**
	 * 获取这个物料对应的所有采购订单
	 *
	 * @param sapId
	 * @param mateCode
	 * @return
	 */
	public List<OrderDO> queryAllOrderOfMate(QualSupp supp, String mateCode);

	Double getUnpaidNum(Map<String, Object> map);

	/**
	 * 根据供应商的sapId查询这个供应商的所有调拨单分页
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryOrderReleOfQualSuppByPage(Map<String, Object> map);

	/**
	 *
	 * 修改订单状态
	 *
	 */
	public void setPaperDataFallback(String fid);

	/**
	 *
	 * 订单确认
	 *
	 */
	public void setPaperDataConfirm(String fid,String limitThan);

	/**
	 * 根据订单主键查询订单详情
	 *
	 * @param fid
	 * @return
	 */
	public OrderRele queryOrderReleByFid2(String fid);

	/**
	 * 根据采购订单主键修改采购订单的状态
	 *
	 * @param map
	 * @return
	 */
	public boolean updateOrderStatusByfid(Map<String, Object> map);

	/**
	 *
	 *
	 * 修改已交未交数量
	 *
	 */
	public boolean updateLineProject(List<ReceMate> receMate);

	/**
	 * 采购订单同步
	 * 
	 * @param orderNo
	 * @param asyncDate
	 * @param al
	 * @return
	 */
	public int asyncPurchaseOrder(String orderNo, String asyncDate, AsyncLog al);

	/**
	 * 采购订单编码获取采购订单
	 * 
	 * @param fid
	 * @return
	 */
	public OrderRele findOrderReleByOrderNo(String orderNo);

	/**
	 * 采购订单编码获取采购订单详情
	 * 
	 * @param fid
	 * @return
	 */
	public List<OrderMate> findOrderMateByOrderNo(String orderNo);

	/**
	 * 获取附件信息
	 * 
	 * @param fid
	 * @return
	 */
	public List<OrderEnclosure> findOrderEnclosureByOrderNo(String orderNo);

	/**
	 * 根据id获取采购订单
	 * 
	 * @param sdata1
	 * @return
	 */
	public OrderRele findOrderReleByID(String id);

	/**
	 * 导出采购订单
	 * 
	 * @param order
	 * @return
	 */
	public List<OrderRele> exportOrderInfo(OrderReleDO order);
	/**
	 * 根据供应商编号查询NB类型的采购订单编码集合
	 * @param string
	 * @param sapId
	 * @return
	 */
	public List<String> queryOrderMessListByOrderTypeAndSapId(String string, String sapId);
	/**
	 * 只修改采购订单的下单限比
	 * @param orderRele
	 * @return
	 */
	public boolean updateLimitThanOfOrderByFid(OrderRele orderRele);

}
