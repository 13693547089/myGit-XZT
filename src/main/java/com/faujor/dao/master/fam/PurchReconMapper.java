package com.faujor.dao.master.fam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.fam.PurchRecon;
import com.faujor.entity.fam.PurchReconDebit;
import com.faujor.entity.fam.PurchReconInvoce;
import com.faujor.entity.fam.PurchReconMate;

public interface PurchReconMapper {
	/**
	 * 财务人员分页获取采购订单列表
	 * 
	 * @param map
	 * @return
	 */
	List<PurchRecon> getFinancePurchReconByPage(Map<String, Object> map);

	/**
	 * 财务人员获取分页的采购订单数量
	 * 
	 * @param map
	 * @return
	 */
	int getFinancePurchReconCount(Map<String, Object> map);

	/**
	 * 供应商分页获取采购订单列表
	 * 
	 * @param map
	 * @return
	 */
	List<PurchRecon> getSuppPurchReconByPage(Map<String, Object> map);

	/**
	 * 供应商获取分页的采购订单数量
	 * 
	 * @param map
	 * @return
	 */
	int getSuppPurchReconCount(Map<String, Object> map);

	/**
	 * 保存采购对账单
	 * 
	 * @param purchRecon
	 * @return
	 */
	int savePurchRecon(PurchRecon purchRecon);

	/**
	 * 更新采购对账单
	 * 
	 * @param purchRecon
	 * @return
	 */
	int updatePurchRecon(PurchRecon purchRecon);

	/**
	 * 根据对账单编码删除对账单
	 * 
	 * @param reconCode
	 * @return
	 */
	int deletePurchResonByCode(String reconCode);

	/**
	 * 根据编码获取采购对账单
	 * 
	 * @param reconCode
	 * @return
	 */
	PurchRecon getPurchReconByCode(String reconCode);

	/**
	 * 更新对账单状态
	 * 
	 * @param map
	 * @return
	 */
	int changeReconStatus(Map<String, Object> map);

	/**
	 * 保存采购对账单物资信息
	 * 
	 * @param mate
	 * @return
	 */
	int saveReconMate(PurchReconMate mate);

	/**
	 * 根据对账单编码删除物资信息
	 * 
	 * @param reconCode
	 * @return
	 */
	int delMateByReconCode(String reconCode);

	/**
	 * 获取对账单下的物资信息
	 * 
	 * @param reconCode
	 * @return
	 */
	List<PurchReconMate> getMateByReconCode(String reconCode);

	/**
	 * 保存对账单的扣款信息
	 * 
	 * @param debit
	 * @return
	 */
	int saveReonDebit(PurchReconDebit debit);

	/**
	 * 删掉对账信息根据对账单编码
	 * 
	 * @param reconCode
	 * @return
	 */
	int delDebitByReconCode(String reconCode);

	/**
	 * 根据对账单编码获取对账单信息
	 * 
	 * @param reconCode
	 * @return
	 */
	List<PurchReconDebit> getDebitByReconCode(String reconCode);

	/**
	 * 保存发票信息
	 * 
	 * @param invoce
	 * @return
	 */
	int saveInvoce(PurchReconInvoce invoce);

	/**
	 * 根据对账单号删除发票信息
	 * 
	 * @param reconCode
	 * @return
	 */
	int delInvoceByReconCode(String reconCode);

	/**
	 * 根据对账单号获取发票信息
	 * 
	 * @param reconCode
	 * @return
	 */
	List<PurchReconInvoce> getInvocesByReconCode(String reconCode);

	/**
	 * 获取发票信息
	 * 
	 * @param map
	 * @return
	 */
	List<PurchReconInvoce> getInvoiceByPage(Map<String, Object> map);

	/**
	 * 批量保存发票信息
	 * 
	 * @param list
	 * @return
	 */
	int batchSaveConfirmReconInfo(List<PurchReconInvoce> list);
	/**
	 * 根据供应商 工厂  出入库日期获取已存在的出入库物料
	 * @param map
	 * @return
	 */
	List<PurchReconMate> getPurchMateByMap(Map<String, Object> map);
	/**
	 * 根据主键id查询对账单
	 * @param id
	 * @return
	 */
	PurchRecon getReconById(String id);
}
