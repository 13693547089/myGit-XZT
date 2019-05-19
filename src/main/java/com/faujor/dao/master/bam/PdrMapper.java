package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;
import com.faujor.entity.bam.psm.PdrStockReport;

/**
 * 产能上报
 * @author Vincent
 *
 */
public interface PdrMapper {
	/**
	 * 分页获取 产能上报 主表信息
	 * @param map
	 * @return
	 */
	public List<Pdr> getPdrByPage(Map<String, Object> map);
	

	/**
	 * 获取  产能上报 主表数量
	 * @param map
	 * @return
	 */
	public int getPdrCount(Map<String, Object> map);
	
	/**
	 * 根据条件获取产能上报 主表信息
	 * @param map
	 * @return
	 */
	public List<Pdr> getPdrByMap(Map<String, Object> map);
	
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
	 * 更新 产能上报  主表信息
	 * @param pdr
	 * @return
	 */
	public int updatePdr(Pdr pdr);
	
	/**
	 * 修改 产能上报  主表 的状态(多参数处理@Param)
	 * @param status
	 * @param id
	 * @return
	 */
	public int updatePdrStatus(@Param("status")String status,@Param("id")String id);
	
	/**
	 * 获取 产能上报 明细页面列表信息
	 * @param map
	 * @return
	 */
	public List<PdrDetail> getPdrDetailPage(Map<String, Object> map);
	
	/**
	 * 获取 产能上报 明细列表信息
	 * @param mainId
	 * @return
	 */
	public List<PdrDetail> getPdrDetailListByMainId(String mainId);
	

	/**
	 * 从供应商排产中获取 产能上报 明细数据
	 * @param suppCode 供应商编码
	 * @param productDate 生产日期
	 * @param firstDate 本月第一天
	 * @param preDate 生产日期前一天
	 * @param mainId 主表ID
	 * @return
	 */
	public List<PdrDetail> getPdrDetailListFromSuppProd(@Param("suppCode")String suppCode,@Param("productDate")String productDate
			,@Param("firstDate")String firstDate,@Param("preDate")String preDate,@Param("mainId")String mainId);
	
	/**
	 * 获取 产能上报 明细数量
	 * @param mainId
	 * @return
	 */
	public int getPdrDetailCount(String mainId);
	
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
	 * 根据物料供应商日期获取供应商物料库存
	 * @param map
	 * @return
	 */
	PdrDetail getPdrDetailBySuppMateDate(Map<String, Object> map);
	
	/**
	 * 获取 产能上报 项次表信息
	 * @param map
	 * @return
	 */
	public List<PdrItem> getPdrItemListByMainId(Map<String, Object> map);
	
	/**
	 * 获取 产能上报 项次表数量
	 * @param map
	 * @return
	 */
	public int getPdrItemCount(Map<String, Object> map);
	
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
	
	/**
	 * 根据item数据修改明细中物料的计算信息
	 * @param mainId
	 */
	public void updateDetailCalcInfoByItem(String mainId);
	
	/**
	 * 分页获取 产能上报查看 主表信息
	 * @param map
	 * @return
	 */
	public List<Pdr> getPdrViewByPage(Map<String, Object> map);
	
	/**
	 * 获取  产能上报查看 主表数量
	 * @param map
	 * @return
	 */
	public int getPdrViewCount(Map<String, Object> map);

	/**
	 * 根据物料编码和时间获取月底结账库存
	 * @param map
	 * @return
	 */
	public Double getSumInveNumByMateDate(Map<String, Object> map);
	
	/**
	 * 获取上一个日期Item中的批次和备注
	 * @param map
	 * @return
	 */
	public List<PdrItem> getPdrItemListFromPreItem(Map<String, Object> map);
	
	/**
	 * 修改同步标志
	 * @param status 1：已同步 0：未同步
	 * @param id
	 * @return
	 */
	public int updatePdrSyncFlag(@Param("syncFlag")String syncFlag,@Param("id")String id);
	
	/**
	 * 更新产能上报的计算数据
	 * @param map
	 * @return
	 */
	public int updatePdrCalcData(Map<String, Object> map);
	
	/*****************产能上报：供应商库存导出报表******************/
	
	/**
	 * 获取产能上报供应商库存信息
	 * @param map
	 * @return 
	 */
	public List<PdrStockReport> getPdrStockReportInfo(Map<String, Object> map);


	/**
	 * 获取上个月最后一天某个供应商某个物料的库存
	 * @param map
	 * @return
	 */
	public PdrDetail getPdrDetailBySapIdAndMonthAndMateCode(Map<String, Object> map);

	
	
	/**
	 * 分页获取 产能上报 主表信息（采购员）
	 * @param map
	 * @return
	 */
	public List<Pdr> getPdrSpecialByPage(Map<String, Object> map);
	

	/**
	 * 获取  产能上报 主表数量（采购员）
	 * @param map
	 * @return
	 */
	public int getPdrSpecialCount(Map<String, Object> map);

}
