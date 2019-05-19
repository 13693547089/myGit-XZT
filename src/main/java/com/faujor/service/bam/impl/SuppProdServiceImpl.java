package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.bam.InvenPlanMapper;
import com.faujor.dao.master.bam.SuppProdMapper;
import com.faujor.entity.bam.psm.InvenPlanDetail;
import com.faujor.entity.bam.psm.SuppProd;
import com.faujor.entity.bam.psm.SuppProdPlan;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.bam.SuppProdService;
import com.faujor.utils.BigDecimalUtil;
import com.faujor.utils.DateUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;
@Service
public class SuppProdServiceImpl implements SuppProdService{
	@Autowired
	private SuppProdMapper suppProdMapper;
	@Autowired
	private InvenPlanMapper invenPlanMapper;
	
	@Override
	public LayuiPage<SuppProd> getSuppProdByPage(Map<String, Object> map) {
		LayuiPage<SuppProd> page=new LayuiPage<SuppProd>();
		List<SuppProd> data = suppProdMapper.getSuppProdByPage(map);
		int count = suppProdMapper.getSuppProdNum(map);
		page.setData(data);
		page.setCount(count);
		return page;
	}

	@Override
	@Transactional
	public void saveSuppProds(List<SuppProd> suppProds,String mainId,String safeScale,BigDecimal prodPlan) {
		InvenPlanDetail planDetail = invenPlanMapper.getPlanDetailById(mainId);
		Date planMonth = planDetail.getPlanMonth();
		Calendar cal=Calendar.getInstance();
		cal.setTime(planMonth);
		cal.add(Calendar.MONTH,-1);
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		//上个月时间
		Date beforeDate = new Date(cal.getTimeInMillis());
		//保存排查计划（先删除）改版之后不能删除 只能做更新和插入操作
//		suppProdMapper.delSuppProdByPlanDetailId(mainId);
		String itemCode = planDetail.getItemCode();
		String itemName = planDetail.getItemName();
		String prodSeriesCode = planDetail.getProdSeriesCode();
		String prodSeriesDesc = planDetail.getProdSeriesDesc();
		String ranking = planDetail.getRanking();
		for (SuppProd suppProd : suppProds) {
//			判断该排产是否存在
			String id = suppProd.getId();
			SuppProd oldSuppProd = suppProdMapper.getSuppProdById(id);
			if(oldSuppProd==null){
				suppProd.setCreator(userId);
				suppProd.setItemCode(itemCode);
				suppProd.setStatus("未排产");
				suppProd.setItemName(itemName);
				suppProd.setProdSeriesCode(prodSeriesCode);
				suppProd.setProdSeriesDesc(prodSeriesDesc);
				suppProd.setRanking(ranking);
				suppProdMapper.saveSuppProd(suppProd);
			}else{
				//更新剩余未分配数量
				String status = suppProd.getStatus();
				if(status!=null && !"未排产".equals(status)){
					BigDecimal dateilProdPlan = suppProd.getProdPlan();
					BigDecimal actDistNum = suppProdMapper.getActDistNum(id);
					BigDecimal remainNum = BigDecimalUtil.subtract(dateilProdPlan, actDistNum);
					suppProd.setRemainNum(remainNum.toString());
				}
				suppProdMapper.updateSuppProd(suppProd);
			}
		}
		//suppProdMapper.saveSuppProds(suppProds);
		//更新上个月的安全库存率
		Map<String, Object> params=new HashMap<>();
		params.put("planMonth", beforeDate);
		for (SuppProd suppProd : suppProds) {
			params.put("mateCode",suppProd.getMateCode() );
			params.put("suppNo",suppProd.getSuppNo() );
			List<SuppProd> beforeProds = suppProdMapper.getSuppProdByMap(params);
			for (SuppProd beforeProd : beforeProds) {
				//当月的交货计划
				BigDecimal deliveryPlan = suppProd.getDeliveryPlan();
				beforeProd.setNextDeliveryNum(deliveryPlan);
				BigDecimal beforeEndStock = beforeProd.getEndStock();
				String beforeSafeScale="";
				if(deliveryPlan!=null && !deliveryPlan.equals(0)){
					beforeSafeScale=BigDecimalUtil.getPercentage(beforeEndStock, deliveryPlan);
				}
				params.put("safeScale",beforeSafeScale);
				params.put("nextDeliveryNum",deliveryPlan);
				suppProdMapper.changeSafeScale(params);
			}
		}
		//修改备货计划详情的状态
		Map<String, Object> map=new HashMap<String,Object>();
		BigDecimal beginStock = planDetail.getBeginStock();
		BigDecimal deliveryPlan = planDetail.getDeliveryPlan();
		if(beginStock==null){
			beginStock=new BigDecimal(0);
		}
		if(deliveryPlan==null){
			deliveryPlan=new BigDecimal(0);
		}
		if(prodPlan==null){
			prodPlan=new BigDecimal(0);
		}
		BigDecimal endStock = beginStock.add(prodPlan).subtract(deliveryPlan);
		map.put("id", mainId);
		map.put("status", "已分解");
		map.put("safeScale", safeScale);
		map.put("prodPlan", prodPlan);
		map.put("endStock", endStock);
		invenPlanMapper.updatePlanDetail(map);
	}

	@Override
	@Transactional
	public void changeSuppProdStatus(List<String> ids, String status) {
		suppProdMapper.changeSuppProdStatus(ids, status);
	}

	@Override
	public List<SuppProd> getSuppProdByInvenId(String id) {
		List<SuppProd> suppProds = suppProdMapper.getSuppProdByInvenId(id);
		return suppProds;
	}

	@Override
	public List<SuppProdPlan> getSuppProdPlanByMainId(String mainId) {
		List<SuppProdPlan> list = suppProdMapper.getSuppProdPlanByMainId(mainId);
		return list;
	}

	@Override
	public SuppProd getSuppProdById(String id) {
		SuppProd suppProd = suppProdMapper.getSuppProdById(id);
		return suppProd;
	}

	@Override
	@Transactional
	public void saveSuppProdPlan(List<SuppProdPlan> suppPlans,String mainId,String status,BigDecimal remainNum) {
		Map<String, Object> map=new HashMap<>();
		map.put("id", mainId);
		map.put("status", status);
		map.put("remainNum", remainNum);
		suppProdMapper.changeStatusAndRemainNum(map);
		suppProdMapper.delSuppProdPlanByMainId(mainId);
		for (SuppProdPlan suppProdPlan : suppPlans) {
//			SuppProdPlan suppPlan = suppProdMapper.getSuppProdPlanByPlan(suppProdPlan);
//			if(suppPlan==null){
				suppProdPlan.setId(UUIDUtil.getUUID());
				suppProdMapper.saveProdPlan(suppProdPlan);
//			}else{
//				suppProdMapper.updateProdPlan(suppProdPlan);
//			}
		}
	}

	@Override
	@Transactional
	public void avgAllSuppProdPlan(List<String> ids) {
		for (String id : ids) {
			SuppProd suppProd = suppProdMapper.getSuppProdById(id);
			suppProdMapper.delSuppProdPlanByMainId(id);
			Date planMonth = suppProd.getPlanMonth();
			List<Date> monthDates = DateUtils.getMonthDates(planMonth);
			int size = monthDates.size();
			BigDecimal prodPlan = suppProd.getProdPlan();
			BigDecimal[] divideAndRemainder = prodPlan.divideAndRemainder(new BigDecimal(size));
			BigDecimal divideVal=divideAndRemainder[0];
			BigDecimal remainVal=divideAndRemainder[1];
			for (int i=0;i<size;i++) {
				Date date = monthDates.get(i);
				SuppProdPlan plan=new SuppProdPlan();
				plan.setId(UUIDUtil.getUUID());
				plan.setMainId(id);
				plan.setPlanDate(date);
				if(i==size-1){
					plan.setPlanNum(divideVal.add(remainVal).setScale(2, BigDecimal.ROUND_HALF_UP));	
				}else{
					plan.setPlanNum(divideVal.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				suppProdMapper.saveProdPlan(plan);
			}
			//修改
			Map<String, Object> map=new HashMap<>();
			map.put("id", id);
			map.put("status", "已排产");
			map.put("remainNum", new BigDecimal(0));
			suppProdMapper.changeStatusAndRemainNum(map);
	   }
	}

	@Override
	public List<SuppProd> getSuppProdByMainId(String mainId) {
		return suppProdMapper.getSuppProdByMainId(mainId);
	}
	@Override
	public List<SuppProd> getSuppProdByPlanDetailId(String planDetailId) {
		return suppProdMapper.getSuppProdByPlanDetailId(planDetailId);
	}
	@Override
	public List<SuppProd> getSuppProdByMap(Map<String, Object> map) {
		List<SuppProd> suppProds = suppProdMapper.getSuppProdByMap(map);
		return suppProds;
	}
}
