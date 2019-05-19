package com.faujor.service.bam.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.master.bam.TranPlanMapper;
import com.faujor.entity.bam.psm.TranPlan;
import com.faujor.entity.bam.psm.TranPlanDetail;
import com.faujor.entity.common.LayuiPage;
import com.faujor.service.bam.TranPlanService;

/**
 * 调拨计划 实现类
 * @author Vincent
 *
 */
@Service
public class TranPlanServiceImpl implements TranPlanService {

	@Autowired
	private TranPlanMapper tranPlanMapper;
	
	/**
	 * 分页获取 调拨计划 主表信息
	 * @param map
	 * @return
	 */
	@Override
	public LayuiPage<TranPlan> getTranPlanByPage(Map<String, Object> map) {
		LayuiPage<TranPlan> page = new LayuiPage<TranPlan>();
		int count = 0;
		List<TranPlan> list = tranPlanMapper.getTranPlanByPage(map);
		count = tranPlanMapper.getTranPlanCount(map);
		page.setCount(count);
		page.setData(list);
		
		return page;
	}
	
	/**
	 * 获取分页获取 调拨计划 主表数量
	 * @param map
	 * @return
	 */
	@Override
	public int getTranPlanCount(Map<String, Object> map) {
		return tranPlanMapper.getTranPlanCount(map);
	}
	
	/**
	 * 根据ID获取单条 调拨计划 主表信息
	 * @param id
	 * @return
	 */
	@Override
	public TranPlan getTranPlanById(String id) {
		return tranPlanMapper.getTranPlanById(id);
	}

	/**
	 * 根据ID删除 调拨计划 信息
	 * 主表加明细数据
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int delTranPlanInfoById(String id) {
		tranPlanMapper.delTranPlanDetailByMainId(id);
		
		return tranPlanMapper.delTranPlanById(id);
	}

	/**
	 * 根据IDs批量删除 调拨计划 信息
	 * 主表加明细数据
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional
	public int delBatchTranPlanInfoByIds(List<String> ids) {
		int rs = 0;
		for(int i=0;i<ids.size();i++){
			tranPlanMapper.delTranPlanDetailByMainId(ids.get(i));
			tranPlanMapper.delTranPlanById(ids.get(i));
			
			rs++;
		}
		return rs;
	}
	
	/**
	 * 保存 调拨计划 信息
	 * @param tranPlan
	 * @param detailList
	 * @return
	 */
	@Override
	@Transactional
	public int saveTranPlanInfo(TranPlan tranPlan,List<TranPlanDetail> detailList) {
		
		// 删除明细
		tranPlanMapper.delTranPlanDetailByMainId(tranPlan.getId());
		
		if(detailList.size()>0){
			// 保存明细
			tranPlanMapper.saveTranPlanDetailList(detailList);
		}
		
		// 更新或插入主表
		TranPlan tranPlanE = tranPlanMapper.getTranPlanById(tranPlan.getId());
		int rs = 0;
		if(tranPlanE != null){
			// 存在
			rs = tranPlanMapper.updateTranPlan(tranPlan);
		}else{
			// 不存在
			rs = tranPlanMapper.saveTranPlan(tranPlan);
		}
		
		return rs;
	}

	/**
	 * 更新 调拨计划 信息
	 * 暂时未用到 。。。。。。。。
	 * @param TranPlan
	 * @param detailList
	 * @return
	 */
	@Override
	@Transactional
	public int updateTranPlanInfo(TranPlan tranPlan,List<TranPlanDetail> detailList) {
		String id = tranPlan.getId();
		// 删除原有明细
		tranPlanMapper.delTranPlanDetailByMainId(id);
		
		tranPlanMapper.saveTranPlanDetailList(detailList);
		
		return tranPlanMapper.saveTranPlan(tranPlan);
	}
	
	/**
	 * 获取 调拨计划 明细列表信息
	 * @param mainId
	 * @return
	 */
	@Override
	public LayuiPage<TranPlanDetail> getTranPlanDetailPage(Map<String, Object> map) {
		LayuiPage<TranPlanDetail> page = new LayuiPage<TranPlanDetail>();
		int count = 0;
		List<TranPlanDetail> list = tranPlanMapper.getTranPlanDetailPage(map);
		count = tranPlanMapper.getTranPlanDetailCount(map.get("mainId").toString());
		page.setCount(count);
		page.setData(list);
		
		return page;
	}
	
	/**
	 * 获取 调拨计划 明细列表信息
	 * @param mainId
	 * @return
	 */
	@Override
	public List<TranPlanDetail> getTranPlanDetailListByMainId(String mainId) {
		List<TranPlanDetail> list = tranPlanMapper.getTranPlanDetailListByMainId(mainId);
		
		return list;
	}

	/**
	 * 获取 调拨计划 明细数量
	 * @param mainId
	 * @return
	 */
	@Override
	public int getTranPlanDetailCount(String mainId) {
		return tranPlanMapper.getTranPlanDetailCount(mainId);
	}

	/**
	 * 修改 调拨计划 主表 的状态
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int updateTranPlanStatus(String status, String id) {
		return tranPlanMapper.updateTranPlanStatus(status, id);
	}
	
	/**
	 * 从 生产/交货计划 中获取物料数据
	 * @param ym 年月
	 * @return
	 */
	@Override
	public List<TranPlanDetail> getMatInfoFromPadPlan(Map<String, Object> map){
		return tranPlanMapper.getMatInfoFromPadPlan(map);
	}
	
	/**
	 * 从 生产/交货计划 中获取调拨的明细数据
	 * @param map
	 * @return
	 */
	@Override
	public List<TranPlanDetail> getTranPlanDetailFromPadPlan(Map<String, Object> map){
		return tranPlanMapper.getTranPlanDetailFromPadPlan(map);
	}
	
	/**
	 * 根据 生产/交货计划 最新数据修改调拨的数据
	 * @param map
	 * @return
	 */
	@Override
	@Transactional
	public int updateTranPlanDetailFromPadPlan(Map<String, Object> map) throws Exception{
		return tranPlanMapper.updateTranPlanDetailFromPadPlan(map);
	}
}
