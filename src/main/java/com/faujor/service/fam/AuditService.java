package com.faujor.service.fam;

import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.fam.AuditMate;
import com.faujor.entity.fam.AuditOrder;

public interface AuditService {

	Map<String, Object> findAuditListByParams(AuditOrder ao, RowBounds rb);

	AuditOrder findAuditById(String auditId);

	Map<String, Object> getMateData(String suppId);

	Map<String, Object> getMouldData(String auditId);

	int saveAuditData(AuditOrder ao, String mateList, String mouldList);

	int removeAuditOrder(String id);

	int batchRemoveAuditOrder(String rows);

	int auditConfirm(String auditIds, String type);

	Map<String, Object> auditConfirmData(AuditOrder ao, RowBounds rb);

	/**
	 * 更新稽核单
	 * 
	 * @param order
	 * @return
	 */
	int updateAuditOrder(AuditOrder order);
	/**
	 * 添加物料时，查询上月月末库存
	 * @param param
	 * @return
	 */
	AuditMate queryLastMonthBala(Map<String, Object> param);
	/**
	 * 根据财务稽核的主键修改财务稽核的状态
	 * @param map
	 * @return
	 */
	boolean updateAuditSatusByAuditId(Map<String, Object> map);

}
