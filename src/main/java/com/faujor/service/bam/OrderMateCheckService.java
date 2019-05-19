package com.faujor.service.bam;

import java.util.List;
import java.util.Map;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.MessMate;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.StraMessAndMateDO;

public interface OrderMateCheckService {
	/**
	 * 计算订单的实际在外量，把在送货过程中的也要减去
	 * 
	 * @param orderNo
	 * @param mateCode
	 * @param orderNumber
	 * @return
	 */
	StraMessAndMateDO calculateActualOrderNumber(String orderNo, String mateCode ,String suppRange, Double orderNumber);

	/**
	 * 为送货单推荐采购订单
	 * 
	 * @param mapgCode
	 * @return
	 */
	Map<String, Object> recommendPurchaseOrder(String mapgCode);

	/**
	 * 在新建送货单选择预约单时，校验预约单中的物料，看是否可以创建送货单
	 * 
	 * @param appo
	 * @param appoMates
	 * @return
	 */
	public Map<String, Object> checkOutAppoMate(String suppId, List<String> mates, String id);

	/**
	 * 为直发通知推荐采购订单
	 * 
	 * @param result
	 * 
	 * @param alloOrder
	 * @param list
	 * @return
	 */
	Map<String, Object> recommendPurchaseOrderForStraMates(Map<String, Object> result, List<MessMate> list,String suppRange);

	/**
	 * 锁定占用
	 * 
	 * @param params
	 * @return
	 */
	List<StraMessAndMateDO> findOccupyNumberByParams(Map<String, Object> params);
}
