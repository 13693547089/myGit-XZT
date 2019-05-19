package com.faujor.dao.master.mdm;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.faujor.entity.cluster.mdm.SuppPurcDO;
import com.faujor.entity.mdm.QualPapers;
import com.faujor.entity.mdm.QualProc;

public interface QualPapersMapper {

	/**
	 * 添加潜在供应商对应的证件
	 * 
	 * @param latentPapers
	 * @return
	 */
	public int insertQualPapers(QualPapers qualPapers);

	/**
	 * 根据供应商的编码查询供应商的相关证件信息
	 * 
	 * @param suppId
	 * @return
	 */
	public List<QualPapers> queryQualPapersBySuppId(String suppId);

	/**
	 * 根据供应商的编码查询供应商的采购数据
	 * 
	 * @param suppId
	 * @return
	 */
	public List<QualProc> queryQualProcBySuppId(String suppId);
	/**
	 * 根据SAP的编码获取范围列表
	 * @param sapId
	 * @return
	 */
	public List<QualProc> queryQualProcBySapId(String sapId);
	

	/**
	 * 根据供应商id获取采购组织和子范围组合ids
	 * 
	 * @param suppId
	 * @return
	 */
	@Select("select concat(purc_orga, supp_range) as orgRanges from mdm_proc where supp_id = #{suppId}")
	public List<String> findOrgAndRangeBySuppId(String suppId);

	public int updateSuppPurcFromEDI(SuppPurcDO purc);

	public int batchSaveSuppPurcFromEDI(List<SuppPurcDO> addPurcList);

	public QualPapers findPapersBySuppIdAndCode(String suppId, String pmc12);

	public int updateSuppPapersFromEDI(QualPapers qp);
	/**
	 *  删除合格供应商的附件
	 * @param suppId
	 * @return
	 */
	public int deletePaperOfQualSuppBySuppId(String suppId);
	/**
	 * 获取供应商子范围集合
	 * @param sapId
	 * @return
	 */
	public List<QualProc> queryQualProcBySapId2(String sapId);
	
	

}
