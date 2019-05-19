package com.faujor.service.mdm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.mdm.LatentPapers;
import com.faujor.entity.mdm.LatentSupp;

public interface LatentSuppService {

	/**
	 * 潜在供应商登记
	 * @param latentSupp
	 * @return
	 */
	boolean insertLatentSupp(LatentSupp latentSupp);
	/**
	 * 潜在供应商列表分页展示
	 * @param map
	 * @return
	 */
	Map<String,Object> queryLatentSuppByPage(Map<String,Object> map);
	/**
	 * 潜在供应商证件的添加
	 * @param latentPapers
	 * @return
	 */
	boolean  insertLatentPapers(LatentPapers latentPapers);
	
	/**
	 * 根据供应商编码删除不合格的潜在供应商
	 * @param suppId
	 * @return
	 */
	boolean deleteLatentSuppBySuppId(String[] suppId);
	/**
	 * 根据供应商编码查询供应商的信息
	 * @param suppId
	 * @return
	 */
	LatentSupp queryOneLatentSuppBySuppId(String suppId);
	/**
	 * 编辑潜在供应商的信息
	 * @param latentSupp
	 * @return
	 */
	boolean updateLatentSupp(LatentSupp latentSupp,List<LatentPapers> list);
	/**
	 * 采购员审核页面批准潜在供应商
	 * @return
	 */
	boolean approveLatentSupp(LatentSupp latentSupp);
	
	/**
	 * 采购部长审核列表
	 * @param map
	 * @return
	 */
	Map<String,Object> queryLatentSuppToMini(Map<String,Object> map);
	/**
	 * 部长提交OA
	 * @param latentSupp
	 * @return
	 */
	boolean subLatentSuppToOA(LatentSupp latentSupp);
	/**
	 * 采购员退回潜在供应商
	 * @param latentSupp
	 * @return
	 */
	boolean buyerSendBackLatentSupp(LatentSupp latentSupp);
	
	
}
