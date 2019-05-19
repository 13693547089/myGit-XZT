package com.faujor.dao.master.mdm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.mdm.LatentPapers;
import com.faujor.entity.mdm.LatentSupp;

public interface LatentSuppMapper {

	/**
	 * 潜在供应商的登记
	 * @param latentSupp
	 * @return
	 */
	public int insertLatentSupp(LatentSupp latentSupp);
	
	/**
	 * 添加潜在供应商对应的证件
	 * @param latentPapers
	 * @return
	 */
	public int insertLatentPapers(LatentPapers latentPapers);
	/**
	 * 潜在供应商列表分页
	 * @param map
	 * @return
	 */
	public List<LatentSupp> queryLatentSuppByPage(Map<String,Object> map);
	/**
	 * 查询潜在供应商数量
	 * @param map
	 * @return
	 */
	public int queryLatentSuppCount(Map<String,Object> map);
	
	/**
	 * 删除不合格的供应商信息
	 * @param suppId
	 * @return
	 */
	public int deleteLatentSuppBySuppId(String[] suppId);
	/**
	 * 根据供应商编码查询供应商信息
	 * @param suppId
	 * @return
	 */
	public LatentSupp queryOneLatentSuppBySuppId(String suppId);
	/**
	 * 修改潜在供应商的信息
	 * @param latentSupp
	 * @return
	 */
	public int updateLatentSuppBySuppId(LatentSupp latentSupp);
	
	/**
	 * 在审核页面的修改潜在供应商信息
	 * @param latentSupp
	 * @return
	 */
	public int updateLatentSuppAtAudit(LatentSupp latentSupp);
	
	/**
	 * 采购部长要审核潜在供应商
	 * @param map
	 * @return
	 */
	public List<LatentSupp> queryLatentSuppToMini(Map<String,Object> map);
	
	/**采购部长要审核潜在供应商
	 * 供应商的数量
	 * @param map
	 * @return
	 */
	public int queryLatentSuppToMiniCount(Map<String,Object> map);
	/**
	 * 部长提交OA
	 * @param latentSupp
	 * @return
	 */
	public int subLatentSuppToOA(LatentSupp latentSupp);
	/**
	 * 采购员退回潜在供应商
	 * @param latentSupp
	 * @return
	 */
	public int buyerSendBackLatentSupp(LatentSupp latentSupp);
	
	
}
