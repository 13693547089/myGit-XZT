package com.faujor.service.bam.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.master.bam.PdrMapper;
import com.faujor.entity.bam.psm.Pdr;
import com.faujor.entity.bam.psm.PdrDetail;
import com.faujor.entity.bam.psm.PdrItem;
import com.faujor.entity.bam.psm.PdrStockReport;
import com.faujor.entity.common.LayuiPage;
import com.faujor.service.bam.PdrService;
import com.faujor.utils.DateUtils;

/**
 * 产能上报 实现类
 * @author Vincent
 *
 */
@Service
public class PdrServiceImpl implements PdrService {

	@Autowired
	private PdrMapper pdrMapper;
	
	/**
	 * 分页获取 产能上报 主表信息
	 * @param map
	 * @return
	 */
	@Override
	public LayuiPage<Pdr> getPdrByPage(Map<String, Object> map) {
		LayuiPage<Pdr> page = new LayuiPage<Pdr>();
		int count = 0;
		List<Pdr> list = pdrMapper.getPdrByPage(map);
		count = pdrMapper.getPdrCount(map);
		page.setCount(count);
		page.setData(list);
		
		return page;
	}
	
	/**
	 * 获取分页获取 产能上报 主表数量
	 * @param map
	 * @return
	 */
	@Override
	public int getPdrCount(Map<String, Object> map) {
		return pdrMapper.getPdrCount(map);
	}
	
	/**
	 * 根据条件获取产能上报 主表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Pdr> getPdrByMap(Map<String, Object> map) {
		return pdrMapper.getPdrByMap(map);
	}

	/**
	 * 根据ID删除 产能上报 信息
	 * 主表加明细数据
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int delPdrInfoById(String id) {
		pdrMapper.delPdrDetailByMainId(id);
		
		return pdrMapper.delPdrById(id);
	}

	/**
	 * 根据IDs批量删除 产能上报 信息
	 * 主表加明细数据
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional
	public int delBatchPdrInfoByIds(List<String> ids) {
		int rs = 0;
		for(int i=0;i<ids.size();i++){
			pdrMapper.delPdrDetailByMainId(ids.get(i));
			pdrMapper.delPdrById(ids.get(i));
			
			rs++;
		}
		return rs;
	}
	
	/**
	 * 保存 产能上报 信息
	 * @param Pdr
	 * @param detailList
	 * @return
	 */
	@Override
	@Transactional
	public int savePdrInfo(Pdr pdr,List<PdrDetail> detailList,List<PdrItem> itemList) {
		
		// 删除明细
		pdrMapper.delPdrDetailByMainId(pdr.getId());
		pdrMapper.delPdrItemByMainId(pdr.getId());
		
		if(detailList.size()>0){
			// 保存明细
			pdrMapper.savePdrDetailList(detailList);
		}
		
		if(itemList.size()>0){
			pdrMapper.savePdrItemList(itemList);
		}
		
		// 修改 detail 中的计算信息
		pdrMapper.updateDetailCalcInfoByItem(pdr.getId());
		
		// 更新或插入主表
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", pdr.getId());
		List<Pdr> pdrList = pdrMapper.getPdrByMap(map);
		int rs = 0;
		if(pdrList.size() == 1){
			// 存在
			rs = pdrMapper.updatePdr(pdr);
		}else{
			// 不存在
			rs = pdrMapper.savePdr(pdr);
		}
		
		String status = pdr.getStatus();
		if(status.equals("已提交")){
			// 更新后续产能上报的计算数据
			String suppCode = pdr.getSuppCode();
			String productDate = DateUtils.format(pdr.getProduceDate(),DateUtils.DATE_PATTERN);
			updateCalcData(suppCode,productDate);
		}
		
		return rs;
	}

	/**
	 * 更新 产能上报 信息
	 * 暂时未用到 。。。。。。。。
	 * @param Pdr
	 * @param detailList
	 * @return
	 */
	@Override
	@Transactional
	public int updatePdrInfo(Pdr pdr,List<PdrDetail> detailList) {
		String id = pdr.getId();
		// 删除原有明细
		pdrMapper.delPdrDetailByMainId(id);
		
		pdrMapper.savePdrDetailList(detailList);
		
		return pdrMapper.savePdr(pdr);
	}
	
	/**
	 * 获取 产能上报 明细列表信息
	 * @param mainId
	 * @return
	 */
	@Override
	public LayuiPage<PdrDetail> getPdrDetailPage(Map<String, Object> map) {
		LayuiPage<PdrDetail> page = new LayuiPage<PdrDetail>();
		int count = 0;
		List<PdrDetail> list = pdrMapper.getPdrDetailPage(map);
		count = pdrMapper.getPdrDetailCount(map.get("mainId").toString());
		page.setCount(count);
		page.setData(list);
		
		return page;
	}
	
	/**
	 * 获取 产能上报 明细列表信息
	 * @param mainId
	 * @return
	 */
	@Override
	public List<PdrDetail> getPdrDetailListByMainId(String mainId) {
		List<PdrDetail> list = pdrMapper.getPdrDetailListByMainId(mainId);
		
		return list;
	}

	/**
	 * 获取 产能上报 明细数量
	 * @param mainId
	 * @return
	 */
	@Override
	public int getPdrDetailCount(String mainId) {
		return pdrMapper.getPdrDetailCount(mainId);
	}

	/**
	 * 修改 产能上报 主表 的状态
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int updatePdrStatus(String status, String id) {
		return pdrMapper.updatePdrStatus(status, id);
	}
	
	/**
	 * 从供应商排产中获取 产能上报 明细数据
	 * @param suppCode 供应商编码
	 * @param productDate 生产日期
	 * @param firstDate 本月第一天
	 * @param preDate 生产日期前一天
	 * @param mainId 主表ID
	 * @return
	 */
	@Override
	public List<PdrDetail> getPdrDetailListFromSuppProd(String suppCode, String productDate,String firstDate,String preDate,String mainId) {
		return pdrMapper.getPdrDetailListFromSuppProd(suppCode, productDate,firstDate,preDate,mainId);
	}
	

	@Override
	public PdrDetail getPdrDetailBySuppMateDate(Map<String, Object> map) {
		return pdrMapper.getPdrDetailBySuppMateDate(map);
	}
	
	/**
	 * 获取 产能上报 项次表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<PdrItem> getPdrItemListByMainId(Map<String, Object> map){
		return pdrMapper.getPdrItemListByMainId(map);
	}
	
	/**
	 * 获取 产能上报 项次表数量
	 * @param map
	 * @return
	 */
	@Override
	public int getPdrItemCount(Map<String, Object> map){
		return pdrMapper.getPdrItemCount(map);
	}
	
	/**
	 * 保存 产能上报 项次表数据
	 * @param list
	 * @return
	 */
	@Override
	@Transactional
	public int savePdrItemList(List<PdrItem> list){
		return pdrMapper.savePdrItemList(list);
	}
	
	/**
	 * 根据主表ID删除 产能上报 项次表数据
	 * @param mainId
	 * @return
	 */
	@Override
	@Transactional
	public int delPdrItemByMainId(String mainId){
		return pdrMapper.delPdrItemByMainId(mainId);
	}
	
	/**
	 * 更新产能上报计算数据
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int updatePdrCalcData(Map<String, Object> map){
		String status = map.get("status").toString();
		String id = map.get("id").toString();

		String suppCode = map.get("suppCode").toString();
		String productDate = map.get("productDate").toString();
		
		// 修改状 为已保存
		pdrMapper.updatePdrStatus(status, id);
		
		// 更新后续产能上报的计算数据
		updateCalcData(suppCode,productDate);
		
		return 1;
	}
	
	public void updateCalcData(String suppCode,String productDate){
		// 获取后续日期的产能上报数据
		Map<String, Object> pdrMap = new HashMap<String, Object>();
		pdrMap.put("suppCode", suppCode);
		pdrMap.put("currPdcDate", productDate);
		pdrMap.put("isPdcOrder", 1);
		List<Pdr> pdrList = pdrMapper.getPdrByMap(pdrMap);
		for(int i=0;i<pdrList.size();i++){
			String currDate = DateUtils.format(pdrList.get(i).getProduceDate(),DateUtils.DATE_PATTERN);

			String[] tempDate = currDate.split("-");
			// 本月第一天
			String firstDate = tempDate[0] + "-" + tempDate[1] + "-" + "01";
			// 当前id
			String currId= pdrList.get(i).getId();
			
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("firstDate", firstDate);
			updateMap.put("currDate", currDate);
			updateMap.put("suppCode", suppCode);
			updateMap.put("mainId", currId);
			// 修改计算数据
			pdrMapper.updatePdrCalcData(updateMap);
		}
	}
	
	/******************** 产能上报查看 ******************/
	/**
	 * 分页获取 产能上报查看 主表信息
	 * @param map
	 * @return
	 */
	@Override
	public LayuiPage<Pdr> getPdrViewByPage(Map<String, Object> map) {
		LayuiPage<Pdr> page = new LayuiPage<Pdr>();
		int count = 0;
		List<Pdr> list = pdrMapper.getPdrViewByPage(map);
		count = pdrMapper.getPdrViewCount(map);
		page.setCount(count);
		page.setData(list);
		
		return page;
	}

	@Override
	public Double getSumInveNumByMateDate(Map<String, Object> map) {
		return pdrMapper.getSumInveNumByMateDate(map);
	}

	/**
	 * 获取上一个日期Item中的批次和备注
	 * @param map
	 * @return
	 */
	@Override
	public List<PdrItem> getPdrItemListFromPreItem(Map<String, Object> map) {
		return pdrMapper.getPdrItemListFromPreItem(map);
	}

	/**
	 * 修改同步状态
	 * @param syncFlag
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int updatePdrSyncFlag(String syncFlag, String id) throws Exception {
		return pdrMapper.updatePdrSyncFlag(syncFlag, id);
	}


	/*****************产能上报：供应商库存导出报表******************/
	
	/**
	 * 获取产能上报供应商库存信息
	 * @param map
	 * @return 
	 */
	@Override
	public List<PdrStockReport> getPdrStockReportInfo(Map<String, Object> map) {
		return pdrMapper.getPdrStockReportInfo(map);
	}


	@Override
	public PdrDetail getPdrDetailBySapIdAndMonthAndMateCode(Map<String, Object> map) {
		return pdrMapper.getPdrDetailBySapIdAndMonthAndMateCode(map);
	}

	
	/**
	 * 分页获取 产能上报 主表信息 (采购员)
	 * @param map
	 * @return
	 */
	@Override
	public LayuiPage<Pdr> getPdrSpecialByPage(Map<String, Object> map) {
		LayuiPage<Pdr> page = new LayuiPage<Pdr>();
		int count = 0;
		List<Pdr> list = pdrMapper.getPdrSpecialByPage(map);
		count = pdrMapper.getPdrSpecialCount(map);
		page.setCount(count);
		page.setData(list);
		
		return page;
	}

}
