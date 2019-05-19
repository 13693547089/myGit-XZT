package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.StraMessage;

public interface DeliveryMapper {

	/**
	 * 查询送货单列表数据
	 * 
	 * @param map
	 * @return
	 */
	public List<Delivery> queryDeliveryByPage(Map<String, Object> map);

	/**
	 * 查询送货单列表数据条数
	 * 
	 * @param map
	 * @return
	 */
	public int queryDeliveryByPageCount(Map<String, Object> map);

	/**
	 * 删除送货单
	 * 
	 * @param deliIds
	 * @return
	 */
	public int deleteDeliveryBydeliId(String[] deliIds);

	/**
	 * 删除送货单下的物资
	 * 
	 * @param deliIds
	 * @return
	 */
	public int deleteDeliMateBydeliId(String[] deliIds);

	/**
	 * 添加预约送货
	 * 
	 * @return
	 */
	public int insertDelivery(Delivery deli);

	/**
	 * 添加预约送货单的物资信息
	 * 
	 * @param deliMate
	 * @return
	 */
	public int insertDeliMate(DeliMate deliMate);

	/**
	 * 根据送货单编号查询送货单详情
	 * 
	 * @param deliId
	 * @return
	 */
	public Delivery queryDeliveryByDeliId(String deliId);

	/**
	 * 根据送货单编号查询送货单下的物资
	 * 
	 * @param deliId
	 * @return
	 */
	public List<DeliMate> queryDeliMateByDeliId(String deliId);

	/**
	 * 根据送货编号修改送货单
	 * 
	 * @param deli
	 * @return
	 */
	public int updateDeliveryByDeliId(Delivery deli);

	/**
	 * 根据送货单号查询送货单的信息
	 * 
	 * @param deliCode
	 * @return
	 */
	public Delivery queryDeliveryByDeliCode(String deliCode);

	/**
	 * 根据送货单号查询送货单下物资的信息
	 * 
	 * @param deliCode
	 * @return
	 */
	public List<DeliMate> queryDeliMateByDeliCode(String deliCode);

	/**
	 * 根据送货单号修改送货单的状态
	 * 
	 * @param map
	 * @return
	 */
	public int updateDeliStatusByDeliCode(Map<String, Object> map);

	/**
	 * 送货签到列表数据
	 * 
	 * @param rb
	 * @param map
	 * @return
	 */
	public List<Delivery> findDeliveryByParams(RowBounds rb, Map<String, Object> map);

	/**
	 * 送货单签到的数量
	 * 
	 * @param map
	 * @return
	 */
	public int countDeliveryByParams(Map<String, Object> map);

	/**
	 * 根据查询供应商和物料编码查询相应状态的送货单物料
	 * 
	 * @param suppId
	 * @param mateCode
	 * @return
	 */
	public List<DeliMate> queryDeliMateBySuppIdAndMateCode(Map<String, Object> map);

	/**
	 * 根据查询供应商和物料编码查询相应状态的送货单
	 * 
	 * @param suppId
	 * @param mateCode
	 * @return
	 */
	public List<Delivery> queryDeliveryBySuppIdAndMateCode(Map<String, Object> map);

	/**
	 * 获取该采购订单，该物料的最新的送货单
	 * 
	 * 状态是已提交，已签到
	 * 
	 * @param deliMate
	 * @return
	 */
	public List<DeliMate> findDeliDetailsByParams(DeliMate deliMate);

	/**
	 * 修改送货单的预约日期和确认送货时间
	 * 
	 * @param appo
	 * @return
	 */
	public boolean updateDeliDate(Appoint appo);

	public boolean updateDeliDate2(StraMessage straMess);

	/**
	 * 根据送货单编码获取送货单信息
	 * 
	 * @param deliCode
	 * @return
	 */
	public Delivery findDeliveryByDeliCode(String deliCode);
	/**
	 * 查询采购订单在第几行
	 * @param suppId
	 * @param mateCode
	 * @param orderId
	 * @return
	 */
	public int queryNumOfOrderId(Map<String, Object> param);
	/**
	 * 根据送货单号查询送货单信息
	 * @param deliCode
	 * @return
	 */
	public Delivery queryOneDeliveryByDeliCode(String deliCode);
	/**
	 * 根据预约单号/直发通知单号获取送货单信息
	 * @param code
	 * @return
	 */
	public List<Delivery> getDeliveryListByAppoCode(String code);
	/**
	 * 根据收货单号查询送货单信息
	 * @param code
	 */
	public List<Delivery>  queryDeliveryListByReceCode(String code);

}
