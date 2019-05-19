package com.faujor.dao.master.mdm;

import java.util.List;

import com.faujor.entity.mdm.LatentPapers;

public interface LatentPapersMapper {

	/**
	 * 根据供应商编码删除供应商的相关证件
	 * @param suppId
	 * @return
	 */
	public int deleteLatentPapersBySuppId(String[] suppId);
	/**
	 * 根据供应商的编码查询供应商的相关证件
	 * @param suppId
	 * @return
	 */
	public List<LatentPapers> queryManyLatentPapersBySuppId(String suppId);
	
	
}
