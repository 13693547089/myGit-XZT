package com.faujor.service.mdm;

import java.util.List;

import com.faujor.entity.mdm.LatentPapers;

public interface LatentPapersService {

	/**
	 * 跟据供应商编码查询供应商的证件信息
	 * @param suppId
	 * @return
	 */
	List<LatentPapers> queryManyLatentPapersBySuppId(String suppId);
	
}
