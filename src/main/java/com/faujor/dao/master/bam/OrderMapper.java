package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.bam.OrderDO;
import com.faujor.entity.bam.OrderEnclosure;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderMate1;
import com.faujor.entity.bam.OrderPackVO;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.OrderRele1;
import com.faujor.entity.bam.OrderReleDO;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.mdm.MateDO;

public interface OrderMapper {
	/**
	 * 分页展示订单列表
	 *
	 * @param map
	 * @return
	 */
	public List<OrderRele> queryReleaseListByPage(Map<String, Object> map);

	public List<OrderRele> queryReleaseListByPageSupp(Map<String, Object> map);

	/**
	 * 分页展示订单列表数据条数
	 *
	 * @param map
	 * @return
	 */
	public int queryReleaseListByPageCount(Map<String, Object> map);

	public int queryReleaseListByPageCountSupp(Map<String, Object> map);

	/**
	 * 查询订单信息
	 *
	 * @param mateId
	 * @return
	 */
	public OrderRele queryOrderReleByFid(String fid);

	/**
	 * 修改订单状态
	 */
	OrderRele updateLatentSuppBySuppId(String fid);

	OrderRele updatePurchaseOrderId(OrderRele orderRele);

	void insertOrder(OrderEnclosure orderE);

	void setDeleteEncl(String docId);

	/**
	 * 把脱普数据插入到数据库中
	 *
	 */
	int setTopList(OrderRele1 orderRele1);

	void setTopDetail(OrderMate1 mate1);

	/**
	 *
	 * 查询采购订单从表数据
	 *
	 * @param OrdeNumb
	 * @return
	 */
	List<OrderMate> queryOrderMate(String OrdeNumb);

	/**
	 *
	 * 查询是否有合同单号
	 *
	 */
	String queryContOrdeNumb(String OrdeNumb);

	/**
	 *
	 * 删除订单字表
	 *
	 */
	void deleteMateList(String ContOrdeNumb);

	/**
	 * 根据供应商的sapId查询这个供应商的所有调拨单
	 *
	 * @param sapId
	 * @return
	 */
	public List<OrderRele> queryOrderReleOfQualSupp(String sapId);

	/**
	 * 获取某个调拨单的物料
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
	public OrderRele querycontOrdeNumbOfOrderReleBySapIdAndMateCode(Map<String, Object> param);

	/**
	 * 获取这个物料对应的所有采购订单
	 *
	 * @param sapId
	 * @param mateCode
	 * @return
	 */
	public List<OrderDO> queryAllOrderOfMate(String sapId, String mateCode);

	/**
	 * 查看供应商的某个物料 或者某个物料的未交货数量
	 *
	 * @param map
	 * @return
	 */
	Double getUnpaidNum(Map<String, Object> map);

	/**
	 * 根据供应商的sapId查询这个供应商的所有调拨单分页
	 *
	 * @param map
	 * @return
	 */
	public List<OrderRele> queryOrderReleOfQualSuppByPage(Map<String, Object> map);

	/**
	 * 根据供应商的sapId查询这个供应商的所有调拨单总数量
	 *
	 * @param map
	 * @return
	 */
	public int queryOrderReleOfQualSuppByPageCount(Map<String, Object> map);

	/**
	 *
	 *
	 * 查询主表FID
	 *
	 */
	public String getOrderFid(String fid);

	/**
	 *
	 * 修改订单状态为已退回
	 *
	 */
	public void setPaperDataFallback(String fid);

	/**
	 *
	 * 订单确认
	 *
	 */
	public void setPaperDataConfirm(@Param("fid")String fid,@Param("limitThan")String limitThan);

	/**
	 * 根据采购订单主键修改采购订单的状态
	 *
	 * @param map
	 * @return
	 */
	public int updateOrderStatusByfid(Map<String, Object> map);

	/**
	 *
	 * 修改行项目已交未交数量
	 *
	 */
	public int updateLineProject(Map<String, Object> map);

	/**
	 *
	 * 查询行项目已交未交数量
	 *
	 */
	public OrderMate selectLineProject(ReceMate r);

	/**
	 * 批量插入采购订单从表数据
	 *
	 * @param selectTopDetail
	 * @return
	 */
	public int batchSaveTopDetails(List<OrderMate1> selectTopDetail);

	/**
	 * 更新采购订单数据
	 * 
	 * @param orderRele1
	 * @return
	 */
	public int updateTopOrderRele(OrderRele1 orderRele1);

	/**
	 * 查找出没有采购订单行项目的数据
	 * 
	 * @return
	 */
	public List<String> findNoMateOrder();

	/**
	 * 
	 * 根据供应商编码和物料编码获取采购订单
	 * 
	 * @param suppId
	 * @param mapgCode
	 * @return
	 */
	public List<OrderRele> findOrderListBySapIdAndMateCode(Map<String, Object> param);

	public OrderRele queryOrderReleBySapIdAndMateCode(Map<String, Object> param);

	/**
	 * 根据条件查询唯一采购订单
	 * 
	 * @param order
	 * @return
	 */
	public OrderRele findOrderReleByOrderRele(OrderRele order);

	/**
	 * 修改调拨单的状态为allo或者置为cancel
	 * 
	 * @param string
	 * @return
	 */
	public int updateAlloOrderStatus(Map<String, Object> map);

	/**
	 * 获取调拨单行项目（订单号和物料号）
	 * 
	 * @param mateCode
	 * @param orderId
	 * 
	 * @return
	 */
	public OrderMate findOrderMateByOrderNoAndMateCode(String orderId, String mateCode);

	/**
	 * 通过采购订单号删除采购订单
	 * 
	 * @param orderNo
	 * @return
	 */
	public int removeOrderByOrderNO(String orderNo);

	/**
	 * 根据采购订单号获取详情数据
	 * 
	 * @param orderNo
	 * @return
	 */
	public List<OrderMate> findOrderMateByOrderNo(String orderNo);

	/**
	 * 根据采购订单号获取附件
	 * 
	 * @param orderNo
	 * @return
	 */
	public List<OrderEnclosure> findOrderEnclosureByOrderNo(String orderNo);

	public List<OrderRele> findOrderReleByOrderReleDO(OrderReleDO order);
	
	public OrderMate findOrderMateByOrderNoAndMateCodeAndFrequence(String orderId, String mateCode, String frequency);
	/**
	 * 根据供应商编号查询NB类型的采购订单编码集合
	 * @param orderType
	 * @param sapId
	 * @return
	 */
	public List<String> queryOrderMessListByOrderTypeAndSapId(@Param("orderType")String orderType, @Param("sapId")String sapId);
	/**
	 * 根据订单编号查询采购订信息
	 * @param oemOrderCode
	 * @return
	 */
	public OrderPackVO queryOrderMessageByOEMOrderCode(String oemOrderCode);
	/**
	 * 根据fid主键编号修改下单限比
	 * @param orderRele
	 */
	public int updateLimitThanOfOrder(OrderRele orderRele);
	/**
	 * 查询没有物料的订单
	 * @return
	 */
	public List<OrderRele> findNoMateOrder2();

}
