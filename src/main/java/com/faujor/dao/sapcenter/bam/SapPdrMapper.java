package com.faujor.dao.sapcenter.bam;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;

/**
 * 产能上报
 * @author Vincent
 *
 */
public interface SapPdrMapper {
	
	/**
	 * 根据ID删除 产能上报  主表信息
	 * @param id
	 * @return
	 */
	public int delPdrById(String id);
	
	/**
	 * 保存 产能上报  主表信息
	 * @param pdr
	 * @return
	 */
	public int savePdr(Pdr pdr);
	
	/**
	 * 保存 产能上报 明细数据
	 * @param list
	 * @return
	 */
	public int savePdrDetailList(List<PdrDetail> list);
	
	/**
	 * 根据主表ID删除 产能上报 明细信息
	 * @param mainId
	 * @return
	 */
	public int delPdrDetailByMainId(String mainId);
	
	/**
	 * 保存 产能上报 项次表数据
	 * @param list
	 * @return
	 */
	public int savePdrItemList(List<PdrItem> list);
	
	/**
	 * 根据主表ID删除 产能上报 项次表数据
	 * @param mainId
	 * @return
	 */
	public int delPdrItemByMainId(String mainId);	
}
