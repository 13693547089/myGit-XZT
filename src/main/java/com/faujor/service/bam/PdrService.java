package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;
import com.faujor.entity.bam.psm.PdrStockReport;
import com.faujor.entity.common.LayuiPage;

/**
 * 产能上报 服务类
 * @author Vincent
 *
 */
public interface PdrService {
	/**
	 * 分页获取 产能上报 主表信息
	 * @param map
	 * @return
	 */
	public LayuiPage<Pdr> getPdrByPage(Map<String, Object> map);
	
	/**
	 * 获取分页获取 产能上报 主表数量
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
	 * 根据ID删除 产能上报 信息
	 * @param id
	 * @return
	 */
	public int delPdrInfoById(String id);
	

	/**
	 * 根据IDs批量删除 产能上报 信息
	 * @param ids
	 * @return
	 */
	public int delBatchPdrInfoByIds(List<String> ids);
	
	/**
	 * 保存 产能上报 信息
	 * @param pdr
	 * @param detailList
	 * @return
	 */
	public int savePdrInfo(Pdr pdr,List<PdrDetail> detailList,List<PdrItem> itemList);
	
	/**
	 * 更新 产能上报 信息
	 * @param pdr
	 * @param detailList
	 * @return
	 */
	public int updatePdrInfo(Pdr pdr,List<PdrDetail> detailList);
	
	/**
	 * 获取 产能上报 明细列表页面数据
	 * @param map
	 * @return
	 */
	public LayuiPage<PdrDetail> getPdrDetailPage(Map<String, Object> map);
	
	
	/**
	 * 获取 产能上报 明细列表信息
	 * @param mainId
	 * @return
	 */
	public List<PdrDetail> getPdrDetailListByMainId(String mainId);
	
	/**
	 * 获取 产能上报 明细数量
	 * @param mainId
	 * @return
	 */
	public int getPdrDetailCount(String mainId);
	
	/**
	 * 修改 产能上报 主表 的状态
	 * @param status
	 * @param id
	 * @return
	 */
	public int updatePdrStatus(String status,String id);
	
	/**
	 * 从供应商排产中获取 产能上报 明细数据
	 * @param suppCode 供应商编码
	 * @param productDate 生产日期
	 * @param firstDate 本月第一天
	 * @param preDate 生产日期前一天
	 * @param mainId 主表ID
	 * @return
	 */
	public List<PdrDetail> getPdrDetailListFromSuppProd(String suppCode,String productDate,
			String firstDate,String preDate,String mainId);
	
	/**
	 * 获取供应商物料库存
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
	 * 分页获取 产能上报查看 主表信息
	 * @param map
	 * @return
	 */
	public LayuiPage<Pdr> getPdrViewByPage(Map<String, Object> map);
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
	 * 修改同步状态
	 * @param syncFlag
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updatePdrSyncFlag(String syncFlag,String id) throws Exception;
	
	/**
	 * 更新产能上报计算数据
	 * @param id
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
	 * 分页获取 产能上报 主表信息 (采购员)
	 * @param map
	 * @return
	 */
	public LayuiPage<Pdr> getPdrSpecialByPage(Map<String, Object> map);

}
