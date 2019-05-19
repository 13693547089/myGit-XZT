package com.faujor.service.fam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.fam.PurchRecon;
import com.faujor.entity.fam.PurchReconDebit;
import com.faujor.entity.fam.PurchReconInvoce;
import com.faujor.entity.fam.PurchReconMate;

public interface PurchReconService {
	/**
	 * 财务人员分页获取采购对账单列表
	 * 
	 * @param map
	 * @return
	 */
	LayuiPage<PurchRecon> getFinancePurchReconByPage(Map<String, Object> map);

	/**
	 * 供应商分页获取采购对账单列表
	 * 
	 * @param map
	 * @return
	 */
	LayuiPage<PurchRecon> getSuppPurchReconByPage(Map<String, Object> map);

	/**
	 * 根据对账单编码获取对账单
	 * 
	 * @param reconCode
	 * @return
	 */
	PurchRecon getReconByCode(String reconCode);

	/**
	 * 保存对账单信息
	 * 
	 * @param recon
	 * @param mates
	 * @param debits
	 */
	void saveRecon(PurchRecon recon, List<PurchReconMate> mates, List<PurchReconDebit> debits);

	/**
	 * 更新对账单信息
	 * 
	 * @param recon
	 * @param mates
	 * @param debits
	 */
	void updateRecon(PurchRecon recon, List<PurchReconMate> mates, List<PurchReconDebit> debits);

	/**
	 * 根据对账单编码删除对账单信息
	 * 
	 * @param reconCodes
	 */
	void delRecon(List<String> reconCodes);

	/**
	 * 更改对账单的状态信息
	 * 
	 * @param reconCodes
	 * @param status
	 */
	void changeReconState(List<String> reconCodes, String status);

	/**
	 * 根据对账单编码 获取对账单物资信息
	 * 
	 * @param reconCode
	 * @return
	 */
	List<PurchReconMate> getMatesByReconCode(String reconCode);

	/**
	 * 根据对账单编码获取扣款信息
	 * 
	 * @param reconCode
	 * @return
	 */
	List<PurchReconDebit> getDebitsByReconCode(String reconCode);

	/**
	 * 从SAP同步数据
	 * 
	 * @param map
	 * @return
	 */
	List<PurchReconMate> getSapPurchReconMatesByMap(Map<String, Object> map);

	/**
	 * 发票的数据
	 * 
	 * @param params
	 * @return
	 */
	LayuiPage<PurchReconInvoce> getInvoiceByPage(Map<String, Object> params);

	/**
	 * 获取发票信息
	 * 
	 * @param reconCode
	 * @return
	 */
	List<PurchReconInvoce> getInvocesByReconCode(String reconCode);

	/**
	 * 保存对账单发票信息
	 * 
	 * @param reconCode
	 * @param list
	 * @return
	 */
	int saveConfirmReconInfo(String reconCode, List<PurchReconInvoce> list);
	/**
	 * 根据供应商工厂 开始结束日期获取采购对账的物料
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
