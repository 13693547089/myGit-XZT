package com.faujor.service.mdm;

import java.util.List;

import com.faujor.entity.mdm.QualPapers;
import com.faujor.entity.mdm.QualProc;

public interface QualPapersService {

	/**
	 * 查询合格供应商的证件信息
	 * @param suppId
	 * @return
	 */
	List<QualPapers> queryQualPapersBySuppId(String suppId);
	/**
	 * 查询合格供应商的采购数据信息
	 * @param suppId
	 * @return
	 */
	List<QualProc> queryQualProcBySuppId(String suppId);
	/**
	 * 查询合格供应商的采购数据信息
	 * @param suppId
	 * @return
	 */
	List<QualProc> queryQualProcBySapId(String sapId);
	/**
	 * 获取供应商子范围的集合
	 * @param suppNo
	 * @return
	 */
	List<QualProc> queryQualProcBySapId2(String suppNo);
}
