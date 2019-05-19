package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.OrderDO;
import com.faujor.entity.mdm.QualSupp;

public interface DeliveryService {

	/**
	 * 送货单列表数据
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryDeliveryByPage(Map<String, Object> map);

	/**
	 * 删除送货单
	 * 
	 * @param deliIds
	 * @return
	 */
	boolean deleteDeliveryBydeliId(String[] deliIds);

	/**
	 * 保存预约送货
	 * 
	 * @param deli
	 * @param list
	 * @return
	 */
	boolean addDelivery(Delivery deli, List<DeliMate> list);

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
	 * 根据送货单编号修改送货单
	 * 
	 * @return
	 */
	boolean updateDeliveryByDeliId(Delivery deli, List<DeliMate> deliMate);

	/**
	 * 根据送货单号修改送货单状态
	 * 
	 * @param map
	 * @return
	 */
	boolean updateDeliStatusByDeliCode(Map<String, Object> map);

	/**
	 * 在可以创建送货单的前提下，判断采购订单中物料的未交数量是否要做修改
	 * @param suppId
	 * @param mateCode
	 * @return
	 */
	public Map<String,Object> judgeUpdateMateUnpaNumber(String suppId,String mateCode,String orderId,int unpa);
	
	
	public Map<String, Object> queryOrderBySuppIdAndMateCode(String suppId, String mateCode, Integer num,
			boolean oldCodeJudge,String orderId,String suppRange);
	/**
	 * type=add :保存 type=sub :提交 新建直发送货页面 保存/提交直发送货
	 * @param deli
	 * @param deliMateData
	 * @param type
	 * @return
	 */
	boolean addDeliveryTwo(Delivery deli, String deliMateData, String type);
	/**
	 * 特殊送货弹窗数据
	 * @param supp
	 * @param mateCode
	 * @return
	 */
	List<OrderDO> queryAllOrderOfMate(QualSupp supp, String mateCode,String suppRange);
	/**
	 * 作废（已保存，已发货，已签到）送货单
	 * @param list
	 * @return
	 */
	boolean cancellDeliveryByDeliIds(List<String> list);
	/**
	 * 查询采购订单在第几行
	 * @param suppId
	 * @param mateCode
	 * @param orderId
	 * @return
	 */
	int queryNumOfOrderId(String suppId, String mateCode, String orderId);
}
