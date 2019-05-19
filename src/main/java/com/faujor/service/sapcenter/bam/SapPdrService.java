package com.faujor.service.sapcenter.bam;

import java.util.List;

import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;

/**
 * sap 产能上报服务类
 * @author Vincent
 *
 */
public interface SapPdrService {
	
	/**
	 * 保存 产能上报 信息
	 * @param pdr
	 * @param detailList
	 * @param itemList
	 * @return
	 */
	public int saveSapPdrInfo(Pdr pdr,List<PdrDetail> detailList,List<PdrItem> itemList) throws Exception;
	
	/**
	 * 删除产能上报信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void delSapPdrInfo(String id) throws Exception;
}
