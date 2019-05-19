package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.faujor.dao.master.bam.ActReachMapper;
import com.faujor.dao.master.bam.PadPlanMapper;
import com.faujor.dao.master.mdm.QualSuppMapper;
import com.faujor.dao.sapcenter.fam.SapPurchReconMapper;
import com.faujor.entity.bam.psm.ActuallyReach;
import com.faujor.entity.bam.psm.BusyStock;
import com.faujor.entity.bam.psm.InnerControl;
import com.faujor.entity.bam.psm.PdrDetailVo;
import com.faujor.entity.bam.psm.PrdDlvInfo;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.ActReachService;
import com.faujor.utils.BigDecimalUtil;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.StringUtil;
@Service
public class ActReachServiceImpl implements ActReachService{
	@Autowired
	private ActReachMapper actReachMapper;
/*	@Autowired
	private QualSuppMapper qualSuppMapper;
	@Autowired
	private PadPlanMapper padPlanMapper;*/
	@Autowired
	private SapPurchReconMapper  sapMapper;
	//--------------------------内部管控页面报表开始------------------
	@Override
	public LayuiPage<ActuallyReach> getActReachByPage(Map<String, Object> map) {
		BigDecimal bg0=new BigDecimal(0);
		BigDecimal bg100=new BigDecimal(100);
		LayuiPage< ActuallyReach> page=new LayuiPage<>();
		//String planMonth = map.get("planMonth").toString();
		Date nextMonth =(Date) map.get("nextMonth");
		Date startDate =(Date) map.get("startDate");
		List<ActuallyReach> data = actReachMapper.getActReachByPage(map);
		Integer count = actReachMapper.getActReachCount(map);
		for (ActuallyReach actReach : data) {
			//获取预计的生产数量
			map.put("planMonth", startDate);
			map.put("startDate", startDate);
			String mateCode=actReach.getMateCode();
			map.put("mateCode", mateCode);
			//--------------
			BigDecimal planPrdNum = actReach.getPlanPrdNum();
			planPrdNum=planPrdNum==null?bg0:planPrdNum;
			actReach.setPlanPrdNum(planPrdNum);
			//获取实际生产交货数量(实际生产来自产能上报 实际交货来自中间表)
			ActuallyReach actuallyReach = getActPrdDlvNum(map);
			BigDecimal actPrdNum = actuallyReach.getActPrdNum();
			actPrdNum=actPrdNum==null?bg0:actPrdNum;
			actReach.setActPrdNum(actPrdNum);
			BigDecimal devNum = sapMapper.getDevNum(map);
			devNum=devNum==null?bg0:devNum;
			actReach.setActDlvNum(devNum);
			//获取实际库存（先获取物料的供应商 然后获取每个供应商该物料的库存）
//			 List<QualSupp> supps = qualSuppMapper.queryQualSuppOfMateByMateCode(mateCode);
			List<QualSupp> supps = actReachMapper.getSuppByMateMonth(map);
			 BigDecimal actStoreNum=new BigDecimal(0);
			 for (QualSupp qualSupp : supps) {
				 String sapId = qualSupp.getSapId();
				 map.put("suppNo", sapId);
				 BigDecimal suppActStoredNum = getActStoreNum(map);
				 actStoreNum= actStoreNum.add(suppActStoredNum);
			 }
			 actReach.setSuppActNum(actStoreNum);
			 //获取下个月的交货计划
			 
			 //获取下个月的交货计划
			 map.put("planMonth", nextMonth);
			 map.remove("suppNo");
			 ActuallyReach nextPlanActReach = getPlanPrdDevNum(map);
			 BigDecimal nextDevNum = nextPlanActReach.getPlanDlvNum();
			 nextDevNum=nextDevNum==null?bg0:nextDevNum;
			 actReach.setNextDevNum(nextDevNum);
			 
			 //计算生产达成 交货达成  库存安全率
			 String pudReachScale="";
			 if(planPrdNum!=null && planPrdNum.compareTo(bg0)!=0){
				 pudReachScale=actPrdNum.divide(planPrdNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 actReach.setPudReachScale(pudReachScale);
			 
			 String dlvReachScale="";
			 BigDecimal planDlvNum = actReach.getPlanDlvNum();
			 if(planDlvNum.compareTo(bg0)!=0){
				 dlvReachScale=devNum.divide(planDlvNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 actReach.setDlvReachScale(dlvReachScale);
			 
			 // 计算物料的安全库存率
			 String safeScale="";
			 if(nextDevNum.compareTo(bg0)!=0){
				 safeScale=actStoreNum.divide(nextDevNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 actReach.setSafeScale(safeScale);
		}
		
		page.setData(data);
		page.setCount(count);
		return page;
	}
	
	@Override
	public List<ActuallyReach> getExportActReach(Map<String, Object> map) {
		BigDecimal bg0=new BigDecimal(0);
		Date nextMonth =(Date) map.get("nextMonth");
		Date startDate =(Date) map.get("startDate");
		List<ActuallyReach> list = actReachMapper.getExportActData(map);
		for (ActuallyReach actReach : list) {
			//获取预计的生产数量   交货数量
			map.put("planMonth", startDate);
			map.put("suppNo", actReach.getSuppNo());
			map.put("mateCode", actReach.getMateCode());
			//获取实际生产交货数量(实际生产来自产能上报 实际交货来自中间表)
			ActuallyReach actuallyReach = getActPrdDlvNum(map);
			BigDecimal actPrdNum = actuallyReach.getActPrdNum();
			actPrdNum=actPrdNum==null?bg0:actPrdNum;
			actReach.setActPrdNum(actPrdNum);
			
			BigDecimal devNum = sapMapper.getDevNum(map);
			devNum=devNum==null?bg0:devNum;
			actReach.setActDlvNum(devNum);
			//获取实际库存（先获取物料的供应商 然后获取每个供应商该物料的库存）
			 BigDecimal suppActStoredNum = getActStoreNum(map);
			 suppActStoredNum=suppActStoredNum==null?bg0:suppActStoredNum;
			 actReach.setSuppActNum(suppActStoredNum);
			 //获取下个月的交货计划
			 map.put("planMonth", nextMonth);
			 ActuallyReach nextPlanActReach = getPlanPrdDevNum(map);
			 BigDecimal nextDevNum = nextPlanActReach.getPlanDlvNum();
			 nextDevNum=nextDevNum==null?bg0:nextDevNum;
			 actReach.setNextDevNum(nextDevNum);
			 //计算生产达成 交货达成  库存安全率
			 String pudReachScale=BigDecimalUtil.getPercentage(actPrdNum, actReach.getPlanPrdNum());
			 actReach.setPudReachScale(pudReachScale);
			 String dlvReachScale=BigDecimalUtil.getPercentage(devNum, actReach.getPlanDlvNum());
			 actReach.setDlvReachScale(dlvReachScale);
			 // 计算物料的安全库存率
			 String safeScale=BigDecimalUtil.getPercentage(suppActStoredNum, nextDevNum);
			 actReach.setSafeScale(safeScale);
		}
		return list;
	}
	
	
	/**
	 * 根据物料 供应商 获取该供应商该物料的 生产计划和交货计划
	 * @param map
	 * @return
	 */
	public ActuallyReach getPlanPrdDevNum(Map<String, Object> map){
		ActuallyReach planActReach = actReachMapper.getPlanPrdDevNum(map);
		return planActReach;
	}
	
	/**
	 * 获取实际的生产和交货数量
	 * @param map
	 * @return
	 */
	public ActuallyReach getActPrdDlvNum(Map<String, Object> map){
		ActuallyReach planActReach = actReachMapper.getActPrdNum(map);
		return planActReach;
	}
	
	
	/**
	 * 获取物料或者供应商的实际生产汇总
	 * @param map
	 * @return
	 */
	public BigDecimal getActStoreNum(Map<String, Object> map){
		BigDecimal actStoreNum = actReachMapper.getActStoreNum(map);
		if(actStoreNum==null){
			actStoreNum=new BigDecimal(0);
		}
		return actStoreNum;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ActuallyReach> getMateSuppActReach(Map<String, Object> map) {
		BigDecimal bg0=new BigDecimal(0);
		BigDecimal bg100=new BigDecimal(100);
		List<ActuallyReach> list = (List<ActuallyReach> )map.get("list");
		Date startDate = (Date)map.get("startDate");
		Date nextMonth = (Date)map.get("nextMonth");
		for (ActuallyReach actReach : list) {
			//获取预计的生产数量   交货数量
			map.put("planMonth", startDate);
			map.put("suppNo", actReach.getSuppNo());
			ActuallyReach planActReach = getPlanPrdDevNum(map);
			BigDecimal planPrdNum = planActReach.getPlanPrdNum();
			planPrdNum=planPrdNum==null?bg0:planPrdNum;
			actReach.setPlanPrdNum(planPrdNum);
			
			BigDecimal planDlvNum = planActReach.getPlanDlvNum();
			planDlvNum=planDlvNum==null?bg0:planDlvNum;
			actReach.setPlanDlvNum(planDlvNum);
			//获取实际生产交货数量(实际生产来自产能上报 实际交货来自中间表)
			ActuallyReach actuallyReach = getActPrdDlvNum(map);
			BigDecimal actPrdNum = actuallyReach.getActPrdNum();
			actPrdNum=actPrdNum==null?bg0:actPrdNum;
			actReach.setActPrdNum(actPrdNum);
			
			
			BigDecimal devNum = sapMapper.getDevNum(map);
			devNum=devNum==null?bg0:devNum;
			actReach.setActDlvNum(devNum);
			//获取实际库存（先获取物料的供应商 然后获取每个供应商该物料的库存）
			 BigDecimal suppActStoredNum = getActStoreNum(map);
			 suppActStoredNum=suppActStoredNum==null?bg0:suppActStoredNum;
			 actReach.setSuppActNum(suppActStoredNum);
			 //获取下个月的交货计划
			 map.put("planMonth", nextMonth);
			 ActuallyReach nextPlanActReach = getPlanPrdDevNum(map);
			 BigDecimal nextDevNum = nextPlanActReach.getPlanDlvNum();
			 nextDevNum=nextDevNum==null?bg0:nextDevNum;
			 actReach.setNextDevNum(nextDevNum);
			 //计算生产达成 交货达成  库存安全率
			 String pudReachScale="";
			 if(planPrdNum!=null && planPrdNum.compareTo(bg0)!=0){
				 pudReachScale=actPrdNum.divide(planPrdNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 actReach.setPudReachScale(pudReachScale);
			 
			 String dlvReachScale="";
			 if(planDlvNum.compareTo(bg0)!=0){
				 dlvReachScale=devNum.divide(planDlvNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 actReach.setDlvReachScale(dlvReachScale);
			 
			 // 计算物料的安全库存率
			 String safeScale="";
			 if(nextDevNum.compareTo(bg0)!=0){
				 safeScale=suppActStoredNum.divide(nextDevNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 actReach.setSafeScale(safeScale);
		}
		return list;
	}

	//--------------------------内部管控页面报表结束------------------
	@Override
	public List<InnerControl> getSuppInnerControl(Map<String, Object> map) {
		//计算相关操作
		BigDecimal bg0=new BigDecimal(0);
		BigDecimal bg100=new BigDecimal(100);
		int maxDay=31;
		//获取相关日期
		Date planMonth = (Date)map.get("planMonth");
		Date startDate = (Date)map.get("startDate");
		Date endDate = (Date)map.get("endDate");
	
		//获取内部管控的物料供应Date商对应列表以及他们的生产计划于交货计划
		List<InnerControl> suppInnerControl = actReachMapper.getSuppInnerControl(map);
		//获取当月实际生产 交货  以及预测数据
		for (InnerControl con : suppInnerControl) {
			Calendar cal=Calendar.getInstance();
			cal.setTime(planMonth);
			//获取预计的生产数量   交货数量
			map.put("planMonth", planMonth);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("suppNo", con.getSuppNo());
			map.put("mateCode", con.getMateCode());
			//获取实际生产交货数量(实际生产来自产能上报 实际交货来自中间表)
			ActuallyReach actuallyReach = getActPrdDlvNum(map);
			BigDecimal actPrdNum = actuallyReach.getActPrdNum();
			BigDecimal suppDlvNum = actuallyReach.getActDlvNum();
			actPrdNum=actPrdNum==null?bg0:actPrdNum;
			suppDlvNum=suppDlvNum==null?bg0:suppDlvNum;
			con.setActPrdNum(actPrdNum);
			con.setSuppDlvNum(suppDlvNum);
			BigDecimal devNum = sapMapper.getDevNum(map);
			devNum=devNum==null?bg0:devNum;
			con.setActDlvNum(devNum);
			//获取实际库存（先获取物料的供应商 然后获取每个供应商该物料的库存）
			 BigDecimal suppActStoredNum = getActStoreNum(map);
			 suppActStoredNum=suppActStoredNum==null?bg0:suppActStoredNum;
			 con.setSuppActNum(suppActStoredNum);
			 //计算生产达成 交货达成
			 BigDecimal  planPrdNum =con.getPlanPrdNum();
			 BigDecimal  planDlvNum =con.getPlanDlvNum();
			 String pudReachScale="";
			 if(planPrdNum!=null && planPrdNum.compareTo(bg0)!=0){
				 pudReachScale=actPrdNum.divide(planPrdNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 con.setPudReachScale(pudReachScale);
			 String dlvReachScale="";
			 if(planDlvNum!=null && planDlvNum.compareTo(bg0)!=0){
				 dlvReachScale=devNum.divide(planDlvNum,4, BigDecimal.ROUND_HALF_DOWN).multiply(bg100).setScale(2)+"%";
			 }
			 con.setDlvReachScale(dlvReachScale);			
			//推迟1个月的生产交货计划与安全库存率(本月的实际安全库存率)
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addOnePrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addOnePrdDlvInfo!=null){
				BigDecimal addOnePlanDlvNum = addOnePrdDlvInfo.getPlanDlvNum();
				con.setAddOnePlanPrdNum(addOnePrdDlvInfo.getPlanPrdNum());
				con.setAddOnePlanDlvNum(addOnePlanDlvNum);
				con.setAddOnePlanEndStock(addOnePrdDlvInfo.getEndStock());
				con.setAddOneSafeScale(addOnePrdDlvInfo.getSafeScale());
				//上个月的实际安全库存率与预计安全库存率
				con.setSafeScale(BigDecimalUtil.getPercentage(suppActStoredNum, addOnePlanDlvNum));
				con.setPlanSafeScale(BigDecimalUtil.getPercentage(con.getEndStock(), addOnePlanDlvNum));
			}
			//推迟2个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwoPrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addTwoPrdDlvInfo!=null){
				con.setAddTwoPlanPrdNum(addTwoPrdDlvInfo.getPlanPrdNum());
				con.setAddTwoPlanDlvNum(addTwoPrdDlvInfo.getPlanDlvNum());
				con.setAddTwoPlanEndStock(addTwoPrdDlvInfo.getEndStock());
				con.setAddTwoSafeScale(addTwoPrdDlvInfo.getSafeScale());
			}
			//推迟3个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addThreePrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addThreePrdDlvInfo!=null){
				con.setAddThreePlanPrdNum(addThreePrdDlvInfo.getPlanPrdNum());
				con.setAddThreePlanDlvNum(addThreePrdDlvInfo.getPlanDlvNum());
				con.setAddThreePlanEndStock(addThreePrdDlvInfo.getEndStock());
				con.setAddThreeSafeScale(addThreePrdDlvInfo.getSafeScale());
			}
			//推迟4个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFourPrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addFourPrdDlvInfo!=null){
				con.setAddFourPlanPrdNum(addFourPrdDlvInfo.getPlanPrdNum());
				con.setAddFourPlanDlvNum(addFourPrdDlvInfo.getPlanDlvNum());
				con.setAddFourPlanEndStock(addFourPrdDlvInfo.getEndStock());
				con.setAddFourSafeScale(addFourPrdDlvInfo.getSafeScale());
			}
			//推迟5个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFivePrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addFivePrdDlvInfo!=null){
				con.setAddFivePlanPrdNum(addFivePrdDlvInfo.getPlanPrdNum());
				con.setAddFivePlanDlvNum(addFivePrdDlvInfo.getPlanDlvNum());
				con.setAddFivePlanEndStock(addFivePrdDlvInfo.getEndStock());
				con.setAddFiveSafeScale(addFivePrdDlvInfo.getSafeScale());
			}
			//推迟6个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSixPrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addSixPrdDlvInfo!=null){
				con.setAddSixPlanPrdNum(addSixPrdDlvInfo.getPlanPrdNum());
				con.setAddSixPlanDlvNum(addSixPrdDlvInfo.getPlanDlvNum());
				con.setAddSixPlanEndStock(addSixPrdDlvInfo.getEndStock());
				con.setAddSixSafeScale(addSixPrdDlvInfo.getSafeScale());
			}
			//推迟7个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSevenPrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addSevenPrdDlvInfo!=null){
				con.setAddSevenPlanPrdNum(addSevenPrdDlvInfo.getPlanPrdNum());
				con.setAddSevenPlanDlvNum(addSevenPrdDlvInfo.getPlanDlvNum());
				con.setAddSevenPlanEndStock(addSevenPrdDlvInfo.getEndStock());
				con.setAddSevenSafeScale(addSevenPrdDlvInfo.getSafeScale());
			}
			//推迟8个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addEightPrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addEightPrdDlvInfo!=null){
				con.setAddEightPlanPrdNum(addEightPrdDlvInfo.getPlanPrdNum());
				con.setAddEightPlanDlvNum(addEightPrdDlvInfo.getPlanDlvNum());
				con.setAddEightPlanEndStock(addEightPrdDlvInfo.getEndStock());
				con.setAddEightSafeScale(addEightPrdDlvInfo.getSafeScale());
			}
			//推迟9个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addNinePrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addNinePrdDlvInfo!=null){
				con.setAddNinePlanPrdNum(addNinePrdDlvInfo.getPlanPrdNum());
				con.setAddNinePlanDlvNum(addNinePrdDlvInfo.getPlanDlvNum());
				con.setAddNinePlanEndStock(addNinePrdDlvInfo.getEndStock());
				con.setAddNineSafeScale(addNinePrdDlvInfo.getSafeScale());
			}
			//推迟10个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTenPrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addTenPrdDlvInfo!=null){
				con.setAddTenPlanPrdNum(addTenPrdDlvInfo.getPlanPrdNum());
				con.setAddTenPlanDlvNum(addTenPrdDlvInfo.getPlanDlvNum());
				con.setAddTenPlanEndStock(addTenPrdDlvInfo.getEndStock());
				con.setAddTenSafeScale(addTenPrdDlvInfo.getSafeScale());
			}
			//推迟11个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addElevenPrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addElevenPrdDlvInfo!=null){
				con.setAddElevenPlanPrdNum(addElevenPrdDlvInfo.getPlanPrdNum());
				con.setAddElevenPlanDlvNum(addElevenPrdDlvInfo.getPlanDlvNum());
				con.setAddElevenPlanEndStock(addElevenPrdDlvInfo.getEndStock());
				con.setAddElevenSafeScale(addElevenPrdDlvInfo.getSafeScale());
			}
			//推迟12个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwelvePrdDlvInfo = actReachMapper.getSuppPrdDlvInfo(map);
			if(addTwelvePrdDlvInfo!=null){
				con.setAddTwelvePlanPrdNum(addTwelvePrdDlvInfo.getPlanPrdNum());
				con.setAddTwelvePlanDlvNum(addTwelvePrdDlvInfo.getPlanDlvNum());
				con.setAddTwelvePlanEndStock(addTwelvePrdDlvInfo.getEndStock());
				con.setAddTwelveSafeScale(addTwelvePrdDlvInfo.getSafeScale());
			}
		}
		return suppInnerControl;
	}
	
	@Override
	public List<InnerControl> getMateInnerControl(Map<String, Object> map) {
		//计算相关操作
		BigDecimal bg0=new BigDecimal(0);
		BigDecimal bg100=new BigDecimal(100);
		int maxDay=31;
		//获取相关日期
		Date planMonth = (Date)map.get("planMonth");
		Date startDate = (Date)map.get("startDate");
		Date endDate = (Date)map.get("endDate");
	
		//获取内部管控的物料供应Date商对应列表以及他们的生产计划于交货计划
		List<InnerControl> mateInnerControl = actReachMapper.getMateInnerControl(map);
		//获取当月实际生产 交货  以及预测数据
		for (InnerControl con : mateInnerControl) {
			Calendar cal=Calendar.getInstance();
			cal.setTime(planMonth);
			//获取预计的生产数量   交货数量
			map.put("planMonth", planMonth);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("mateCode", con.getMateCode());
			map.remove("suppNo");
			//获取实际生产交货数量(实际生产来自产能上报 实际交货来自中间表)
			ActuallyReach actuallyReach = getActPrdDlvNum(map);
			BigDecimal actPrdNum = actuallyReach.getActPrdNum();
			BigDecimal suppDlvNum = actuallyReach.getActDlvNum();
			actPrdNum=actPrdNum==null?bg0:actPrdNum;
			actPrdNum=actPrdNum==null?bg0:actPrdNum;
			suppDlvNum=suppDlvNum==null?bg0:suppDlvNum;
			con.setActPrdNum(actPrdNum);
			con.setSuppDlvNum(suppDlvNum);
			BigDecimal devNum = sapMapper.getDevNum(map);
			devNum=devNum==null?bg0:devNum;
			con.setActDlvNum(devNum);
			//获取实际库存（先获取物料的供应商 然后获取每个供应商该物料的库存）
			List<QualSupp> supps = actReachMapper.getSuppByMateMonth(map);
			 BigDecimal actStoreNum=new BigDecimal(0);
			 for (QualSupp qualSupp : supps) {
				 String sapId = qualSupp.getSapId();
				 map.put("suppNo", sapId);
				 BigDecimal suppActStoredNum = getActStoreNum(map);
				 actStoreNum= actStoreNum.add(suppActStoredNum);
			 }
			 con.setSuppActNum(actStoreNum);
			//推迟1个月的生产交货计划与安全库存率(本月的实际安全库存率)
			map.remove("suppNo");
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addOnePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addOnePrdDlvInfo!=null){
				BigDecimal addOnePlanDlvNum = addOnePrdDlvInfo.getPlanDlvNum();
				con.setAddOnePlanPrdNum(addOnePrdDlvInfo.getPlanPrdNum());
				con.setAddOnePlanDlvNum(addOnePlanDlvNum);
				con.setAddOnePlanEndStock(addOnePrdDlvInfo.getEndStock());
			}
			//推迟2个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwoPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTwoPrdDlvInfo!=null){
				con.setAddTwoPlanPrdNum(addTwoPrdDlvInfo.getPlanPrdNum());
				con.setAddTwoPlanDlvNum(addTwoPrdDlvInfo.getPlanDlvNum());
				con.setAddTwoPlanEndStock(addTwoPrdDlvInfo.getEndStock());
			}
			//推迟3个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addThreePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addThreePrdDlvInfo!=null){
				con.setAddThreePlanPrdNum(addThreePrdDlvInfo.getPlanPrdNum());
				con.setAddThreePlanDlvNum(addThreePrdDlvInfo.getPlanDlvNum());
				con.setAddThreePlanEndStock(addThreePrdDlvInfo.getEndStock());
			}
			//推迟4个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFourPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addFourPrdDlvInfo!=null){
				con.setAddFourPlanPrdNum(addFourPrdDlvInfo.getPlanPrdNum());
				con.setAddFourPlanDlvNum(addFourPrdDlvInfo.getPlanDlvNum());
				con.setAddFourPlanEndStock(addFourPrdDlvInfo.getEndStock());
			}
			//推迟5个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFivePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addFivePrdDlvInfo!=null){
				con.setAddFivePlanPrdNum(addFivePrdDlvInfo.getPlanPrdNum());
				con.setAddFivePlanDlvNum(addFivePrdDlvInfo.getPlanDlvNum());
				con.setAddFivePlanEndStock(addFivePrdDlvInfo.getEndStock());
			}
			//推迟6个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSixPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addSixPrdDlvInfo!=null){
				con.setAddSixPlanPrdNum(addSixPrdDlvInfo.getPlanPrdNum());
				con.setAddSixPlanDlvNum(addSixPrdDlvInfo.getPlanDlvNum());
				con.setAddSixPlanEndStock(addSixPrdDlvInfo.getEndStock());
			}
			//推迟7个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSevenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addSevenPrdDlvInfo!=null){
				con.setAddSevenPlanPrdNum(addSevenPrdDlvInfo.getPlanPrdNum());
				con.setAddSevenPlanDlvNum(addSevenPrdDlvInfo.getPlanDlvNum());
				con.setAddSevenPlanEndStock(addSevenPrdDlvInfo.getEndStock());
			}
			//推迟8个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addEightPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addEightPrdDlvInfo!=null){
				con.setAddEightPlanPrdNum(addEightPrdDlvInfo.getPlanPrdNum());
				con.setAddEightPlanDlvNum(addEightPrdDlvInfo.getPlanDlvNum());
				con.setAddEightPlanEndStock(addEightPrdDlvInfo.getEndStock());
			}
			//推迟9个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addNinePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addNinePrdDlvInfo!=null){
				con.setAddNinePlanPrdNum(addNinePrdDlvInfo.getPlanPrdNum());
				con.setAddNinePlanDlvNum(addNinePrdDlvInfo.getPlanDlvNum());
				con.setAddNinePlanEndStock(addNinePrdDlvInfo.getEndStock());
			}
			//推迟10个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTenPrdDlvInfo!=null){
				con.setAddTenPlanPrdNum(addTenPrdDlvInfo.getPlanPrdNum());
				con.setAddTenPlanDlvNum(addTenPrdDlvInfo.getPlanDlvNum());
				con.setAddTenPlanEndStock(addTenPrdDlvInfo.getEndStock());
			}
			//推迟11个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addElevenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addElevenPrdDlvInfo!=null){
				con.setAddElevenPlanPrdNum(addElevenPrdDlvInfo.getPlanPrdNum());
				con.setAddElevenPlanDlvNum(addElevenPrdDlvInfo.getPlanDlvNum());
				con.setAddElevenPlanEndStock(addElevenPrdDlvInfo.getEndStock());
			}
			//推迟12个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwelvePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTwelvePrdDlvInfo!=null){
				con.setAddTwelvePlanPrdNum(addTwelvePrdDlvInfo.getPlanPrdNum());
				con.setAddTwelvePlanDlvNum(addTwelvePrdDlvInfo.getPlanDlvNum());
				con.setAddTwelvePlanEndStock(addTwelvePrdDlvInfo.getEndStock());
			}
			//推迟12个月的生产交货计划与安全库存率
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addThirteenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTwelvePrdDlvInfo!=null){
				con.setAddThirteenPlanDlvNum(addThirteenPrdDlvInfo.getPlanDlvNum());
			}
		}
		mateInnerControl=calMateTotalInnerControl(mateInnerControl);
		//计算实际生产实际交货安全库存率
		calInnerControlSafeScale(mateInnerControl);
		return mateInnerControl;
	}
	
	
	public void calInnerControlSafeScale(List<InnerControl> innerControls){
		for (InnerControl con : innerControls) {
			 BigDecimal planPrdNum =con.getPlanPrdNum();
			 BigDecimal planDlvNum =con.getPlanDlvNum();
			 BigDecimal actPrdNum = con.getActPrdNum();
			 BigDecimal actDlvNum = con.getActDlvNum();
			 BigDecimal suppActNum = con.getSuppActNum();
			 BigDecimal endStock = con.getEndStock();
			 
			 BigDecimal addOnePlanDlvNum = con.getAddOnePlanDlvNum();
			 BigDecimal addOnePlanEndStock = con.getAddOnePlanEndStock();
			 
			 BigDecimal addTwoPlanDlvNum = con.getAddTwoPlanDlvNum();
			 BigDecimal addTwoPlanEndStock = con.getAddTwoPlanEndStock();
			 
			 BigDecimal addThreePlanDlvNum = con.getAddThreePlanDlvNum();
			 BigDecimal addThreePlanEndStock = con.getAddThreePlanEndStock();
			 
			 BigDecimal addFourPlanDlvNum = con.getAddFourPlanDlvNum();
			 BigDecimal addFourPlanEndStock = con.getAddFourPlanEndStock();
			 
			 BigDecimal addFivePlanDlvNum = con.getAddFivePlanDlvNum();
			 BigDecimal addFivePlanEndStock = con.getAddFivePlanEndStock();
			 
			 BigDecimal addSixPlanDlvNum = con.getAddSixPlanDlvNum();
			 BigDecimal addSixPlanEndStock = con.getAddSixPlanEndStock();
			 
			 BigDecimal addSevenPlanDlvNum = con.getAddSevenPlanDlvNum();
			 BigDecimal addSevenPlanEndStock = con.getAddSevenPlanEndStock();
			 
			 BigDecimal addEightPlanDlvNum = con.getAddEightPlanDlvNum();
			 BigDecimal addEightPlanEndStock = con.getAddEightPlanEndStock();
			 
			 BigDecimal addNinePlanDlvNum = con.getAddNinePlanDlvNum();
			 BigDecimal addNinePlanEndStock = con.getAddNinePlanEndStock();
			 
			 BigDecimal addTenPlanDlvNum = con.getAddTenPlanDlvNum();
			 BigDecimal addTenPlanEndStock = con.getAddTenPlanEndStock();
			 
			 BigDecimal addElevenPlanDlvNum = con.getAddElevenPlanDlvNum();
			 BigDecimal addElevenPlanEndStock = con.getAddElevenPlanEndStock();
			 
			 BigDecimal addTwelvePlanDlvNum = con.getAddTwelvePlanDlvNum();
			 BigDecimal addTwelvePlanEndStock = con.getAddTwelvePlanEndStock();
			 
			 BigDecimal addThirteenPlanDlvNum = con.getAddThirteenPlanDlvNum();
			 //实际生产交货达成 实际安全库存率  预计安全库存率
			 con.setPudReachScale(BigDecimalUtil.getPercentage(actPrdNum, planPrdNum));
			 con.setDlvReachScale(BigDecimalUtil.getPercentage(actDlvNum, planDlvNum));
			 con.setSafeScale(BigDecimalUtil.getPercentage(suppActNum, addOnePlanDlvNum));
			 con.setPlanSafeScale(BigDecimalUtil.getPercentage(endStock, addOnePlanDlvNum));
			 //	预算的第1个月的期末预计库存
			 con.setAddOneSafeScale(BigDecimalUtil.getPercentage(addOnePlanEndStock, addTwoPlanDlvNum));
			 //	预算的第2个月的期末预计库存
			 con.setAddTwoSafeScale(BigDecimalUtil.getPercentage(addTwoPlanEndStock, addThreePlanDlvNum));
			 //	预算的第3个月的期末预计库存
			 con.setAddThreeSafeScale(BigDecimalUtil.getPercentage(addThreePlanEndStock, addFourPlanDlvNum));
			 //	预算的第4个月的期末预计库存
			 con.setAddFourSafeScale(BigDecimalUtil.getPercentage(addFourPlanEndStock, addFivePlanDlvNum));
			 //	预算的第5个月的期末预计库存
			 con.setAddFiveSafeScale(BigDecimalUtil.getPercentage(addFivePlanEndStock, addSixPlanDlvNum));
			 //	预算的第6个月的期末预计库存
			 con.setAddSixSafeScale(BigDecimalUtil.getPercentage(addSixPlanEndStock, addSevenPlanDlvNum));
			 //	预算的第7个月的期末预计库存
			 con.setAddSevenSafeScale(BigDecimalUtil.getPercentage(addSevenPlanEndStock, addEightPlanDlvNum));
			 //	预算的第8个月的期末预计库存
			 con.setAddEightSafeScale(BigDecimalUtil.getPercentage(addEightPlanEndStock, addNinePlanDlvNum));
			 //	预算的第9个月的期末预计库存
			 con.setAddNineSafeScale(BigDecimalUtil.getPercentage(addNinePlanEndStock, addTenPlanDlvNum));
			 //	预算的第10个月的期末预计库存
			 con.setAddTenSafeScale(BigDecimalUtil.getPercentage(addTenPlanEndStock, addElevenPlanDlvNum));
			 //	预算的第11个月的期末预计库存
			 con.setAddElevenSafeScale(BigDecimalUtil.getPercentage(addElevenPlanEndStock, addTwelvePlanDlvNum));
			 //	预算的第12个月的期末预计库存
			 con.setAddTwelveSafeScale(BigDecimalUtil.getPercentage(addTwelvePlanEndStock, addThirteenPlanDlvNum));			 
		}
	}
	/**
	 * 8月9号去除箱入数包入数
	 * 计算旺季备货表按照类别物料的汇总信息
	 * @param busyStocks
	 */
	public List<InnerControl> calMateTotalInnerControl(List<InnerControl> innerControls){

		List<InnerControl> list=new LinkedList<>();
		BigDecimal bg0 = new BigDecimal(0);
		String tempItemCode="";
		String tempSeriesCode="";
		String tempSeriesName="";
		String tempItemName="";
		String tempRank="";
		
		BigDecimal subBeginOrder = bg0;
		BigDecimal subBeginStock = bg0;
		BigDecimal subBeginEnableOrder = bg0;
		
		BigDecimal subPlanPrdNum = bg0;
		BigDecimal subActPrdNum = bg0;
		BigDecimal subPlanDlvNum = bg0;
		BigDecimal subActDlvNum = bg0;
		BigDecimal subSuppDlvNum = bg0;
		BigDecimal subSuppActNum=bg0;
		
		BigDecimal subEndStock=bg0;
		
		BigDecimal subAddOnePlanPrdNum=bg0;
		BigDecimal subAddOnePlanDlvNum=bg0;
		BigDecimal subAddOnePlanEndStock=bg0;
		
		
		BigDecimal subAddTwoPlanPrdNum=bg0;
		BigDecimal subAddTwoPlanDlvNum=bg0;
		BigDecimal subAddTwoPlanEndStock=bg0;
	
		
		BigDecimal subAddThreePlanPrdNum=bg0;
		BigDecimal subAddThreePlanDlvNum=bg0;
		BigDecimal subAddThreePlanEndStock=bg0;
			
		BigDecimal subAddFourPlanPrdNum=bg0;
		BigDecimal subAddFourPlanDlvNum=bg0;
		BigDecimal subAddFourPlanEndStock=bg0;
	
		
		BigDecimal subAddFivePlanPrdNum=bg0;
		BigDecimal subAddFivePlanDlvNum=bg0;
		BigDecimal subAddFivePlanEndStock=bg0;
		
		BigDecimal subAddSixPlanPrdNum=bg0;
		BigDecimal subAddSixPlanDlvNum=bg0;
	    BigDecimal subAddSixPlanEndStock=bg0;
		
		BigDecimal subAddSevenPlanPrdNum=bg0;
		BigDecimal subAddSevenPlanDlvNum=bg0;
		BigDecimal subAddSevenPlanEndStock=bg0;
		
		BigDecimal subAddEightPlanPrdNum=bg0;
		BigDecimal subAddEightPlanDlvNum=bg0;
		BigDecimal subAddEightPlanEndStock=bg0;
		
		
		BigDecimal subAddNinePlanPrdNum=bg0;
		BigDecimal subAddNinePlanDlvNum=bg0;
		BigDecimal subAddNinePlanEndStock=bg0;
		
		BigDecimal subAddTenPlanPrdNum=bg0;
		BigDecimal subAddTenPlanDlvNum=bg0;
		BigDecimal subAddTenPlanEndStock=bg0;
		
		BigDecimal subAddElevenPlanPrdNum=bg0;
		BigDecimal subAddElevenPlanDlvNum=bg0;
		BigDecimal subAddElevenPlanEndStock=bg0;
		
		BigDecimal subAddTwelvePlanPrdNum=bg0;
		BigDecimal subAddTwelvePlanDlvNum=bg0;
		BigDecimal subAddTwelvePlanEndStock=bg0;
		
		BigDecimal subAddThirteenPlanDlvNum=bg0;

		
		int size = innerControls.size();
		if(innerControls==null ||size==0){
			return innerControls;
		}
		InnerControl innerControl0 = innerControls.get(0);
		tempItemCode=innerControl0.getItemCode();
		tempSeriesCode=innerControl0.getProdSeriesCode();
		tempSeriesName=innerControl0.getProdSeriesDesc();

		tempItemName=innerControl0.getItemName();
		tempRank=innerControl0.getRank();
		for (int i=0;i<size;i++) {
			InnerControl innerControl = innerControls.get(i);
			
			String itemCode = innerControl.getItemCode();
			
			String prodSeriesCode=innerControl.getProdSeriesCode();
			String prodSeriesDesc = innerControl.getProdSeriesDesc();
			
			
			String itemName = innerControl.getItemName();
			if(itemName==null || "".equals(itemName)){
				itemName="未知类别";
			}
			String rank = innerControl.getRank();
			BigDecimal beginOrder = innerControl.getBeginOrder();
			BigDecimal beginStock = innerControl.getBeginStock();
			BigDecimal beginEnableOrder = innerControl.getBeginEnableOrder();
			
			BigDecimal planPrdNum = innerControl.getPlanPrdNum();
			BigDecimal actPrdNum = innerControl.getActPrdNum();
			BigDecimal planDlvNum = innerControl.getPlanDlvNum();
			BigDecimal actDlvNum = innerControl.getActDlvNum();
			BigDecimal suppDlvNum = innerControl.getSuppDlvNum();
			BigDecimal suppActNum = innerControl.getSuppActNum();
			BigDecimal endStock = innerControl.getEndStock();
			
			BigDecimal addOnePlanPrdNum = innerControl.getAddOnePlanPrdNum();
			BigDecimal addOnePlanDlvNum = innerControl.getAddOnePlanDlvNum();
			BigDecimal addOnePlanEndStock = innerControl.getAddOnePlanEndStock();
			
			BigDecimal addTwoPlanPrdNum = innerControl.getAddTwoPlanPrdNum();
			BigDecimal addTwoPlanDlvNum = innerControl.getAddTwoPlanDlvNum();
			BigDecimal addTwoPlanEndStock = innerControl.getAddTwoPlanEndStock();
			
			BigDecimal addThreePlanPrdNum = innerControl.getAddThreePlanPrdNum();
			BigDecimal addThreePlanDlvNum = innerControl.getAddThreePlanDlvNum();
			BigDecimal addThreePlanEndStock = innerControl.getAddThreePlanEndStock();
			
			BigDecimal addFourPlanPrdNum = innerControl.getAddFourPlanPrdNum();
			BigDecimal addFourPlanDlvNum = innerControl.getAddFourPlanDlvNum();
			BigDecimal addFourPlanEndStock = innerControl.getAddFourPlanEndStock();
			
			BigDecimal addFivePlanPrdNum = innerControl.getAddFivePlanPrdNum();
			BigDecimal addFivePlanDlvNum = innerControl.getAddFivePlanDlvNum();
			BigDecimal addFivePlanEndStock = innerControl.getAddFivePlanEndStock();
			
			BigDecimal addSixPlanPrdNum = innerControl.getAddSixPlanPrdNum();
			BigDecimal addSixPlanDlvNum = innerControl.getAddSixPlanDlvNum();
			BigDecimal addSixPlanEndStock = innerControl.getAddSixPlanEndStock();
			
			BigDecimal addSevenPlanPrdNum = innerControl.getAddSevenPlanPrdNum();
			BigDecimal addSevenPlanDlvNum = innerControl.getAddSevenPlanDlvNum();
			BigDecimal addSevenPlanEndStock = innerControl.getAddSevenPlanEndStock();
			
			BigDecimal addEightPlanPrdNum = innerControl.getAddEightPlanPrdNum();
			BigDecimal addEightPlanDlvNum = innerControl.getAddEightPlanDlvNum();
			BigDecimal addEightPlanEndStock = innerControl.getAddEightPlanEndStock();
			
			BigDecimal addNinePlanPrdNum = innerControl.getAddNinePlanPrdNum();
			BigDecimal addNinePlanDlvNum = innerControl.getAddNinePlanDlvNum();
			BigDecimal addNinePlanEndStock = innerControl.getAddNinePlanEndStock();
			
			BigDecimal addTenPlanPrdNum = innerControl.getAddTenPlanPrdNum();
			BigDecimal addTenPlanDlvNum = innerControl.getAddTenPlanDlvNum();
			BigDecimal addTenPlanEndStock = innerControl.getAddTenPlanEndStock();
			
			BigDecimal addElevenPlanPrdNum = innerControl.getAddElevenPlanPrdNum();
			BigDecimal addElevenPlanDlvNum = innerControl.getAddElevenPlanDlvNum();
			BigDecimal addElevenPlanEndStock = innerControl.getAddElevenPlanEndStock();
			
			BigDecimal addTwelvePlanPrdNum = innerControl.getAddTwelvePlanPrdNum();
			BigDecimal addTwelvePlanDlvNum = innerControl.getAddTwelvePlanDlvNum();
			BigDecimal addTwelvePlanEndStock = innerControl.getAddTwelvePlanEndStock();
			
			BigDecimal addThirteenPlanDlvNum = innerControl.getAddThirteenPlanDlvNum();

			if(!StringUtil.equals(itemCode, tempItemCode) || !StringUtil.equals(prodSeriesCode, tempSeriesCode)){
				InnerControl subInnerControl=new InnerControl();
				subInnerControl.setItemCode(tempItemCode);
				subInnerControl.setProdSeriesCode(tempSeriesCode);
				subInnerControl.setProdSeriesDesc(tempSeriesName);
				if(StringUtils.isEmpty(tempItemName)){
					subInnerControl.setMateDesc("未知类别合计"); 
					subInnerControl.setItemName("未知类别");

				}else{
					subInnerControl.setMateDesc(tempItemName+"合计"); 
					subInnerControl.setItemName(tempItemName);
				}
				subInnerControl.setRank(tempRank);
				subInnerControl.setBeginOrder(subBeginOrder);
				subInnerControl.setBeginStock(subBeginStock);
				subInnerControl.setBeginEnableOrder(subBeginEnableOrder);
				subInnerControl.setPlanPrdNum(subPlanPrdNum);
				subInnerControl.setActPrdNum(subActPrdNum);
				
				subInnerControl.setPlanDlvNum(subPlanDlvNum);
				subInnerControl.setActDlvNum(subActDlvNum);
				subInnerControl.setSuppDlvNum(subSuppDlvNum);
				
				subInnerControl.setSuppActNum(subSuppActNum);
				subInnerControl.setEndStock(subEndStock);
				
				subInnerControl.setAddOnePlanPrdNum(subAddOnePlanPrdNum);
				subInnerControl.setAddOnePlanDlvNum(subAddOnePlanDlvNum);
				subInnerControl.setAddOnePlanEndStock(subAddOnePlanEndStock);
				
				subInnerControl.setAddTwoPlanPrdNum(subAddTwoPlanPrdNum);
				subInnerControl.setAddTwoPlanDlvNum(subAddTwoPlanDlvNum);
				subInnerControl.setAddTwoPlanEndStock(subAddTwoPlanEndStock);
				
				subInnerControl.setAddThreePlanPrdNum(subAddThreePlanPrdNum);
				subInnerControl.setAddThreePlanDlvNum(subAddThreePlanDlvNum);
				subInnerControl.setAddThreePlanEndStock(subAddThreePlanEndStock);
				
				subInnerControl.setAddFourPlanPrdNum(subAddFourPlanPrdNum);
				subInnerControl.setAddFourPlanDlvNum(subAddFourPlanDlvNum);
				subInnerControl.setAddFourPlanEndStock(subAddFourPlanEndStock);
				
				subInnerControl.setAddFivePlanPrdNum(subAddFivePlanPrdNum);
				subInnerControl.setAddFivePlanDlvNum(subAddFivePlanDlvNum);
				subInnerControl.setAddFivePlanEndStock(subAddFivePlanEndStock);
				
				subInnerControl.setAddSixPlanPrdNum(subAddSixPlanPrdNum);
				subInnerControl.setAddSixPlanDlvNum(subAddSixPlanDlvNum);
				subInnerControl.setAddSixPlanEndStock(subAddSixPlanEndStock);
				
				subInnerControl.setAddSevenPlanPrdNum(subAddSevenPlanPrdNum);
				subInnerControl.setAddSevenPlanDlvNum(subAddSevenPlanDlvNum);
				subInnerControl.setAddSevenPlanEndStock(subAddSevenPlanEndStock);
				
				subInnerControl.setAddEightPlanPrdNum(subAddEightPlanPrdNum);
				subInnerControl.setAddEightPlanDlvNum(subAddEightPlanDlvNum);
				subInnerControl.setAddEightPlanEndStock(subAddEightPlanEndStock);
				
				subInnerControl.setAddNinePlanPrdNum(subAddNinePlanPrdNum);
				subInnerControl.setAddNinePlanDlvNum(subAddNinePlanDlvNum);
				subInnerControl.setAddNinePlanEndStock(subAddNinePlanEndStock);
				
				subInnerControl.setAddTenPlanPrdNum(subAddTenPlanPrdNum);
				subInnerControl.setAddTenPlanDlvNum(subAddTenPlanDlvNum);
				subInnerControl.setAddTenPlanEndStock(subAddTenPlanEndStock);
				
				subInnerControl.setAddElevenPlanPrdNum(subAddElevenPlanPrdNum);
				subInnerControl.setAddElevenPlanDlvNum(subAddElevenPlanDlvNum);
				subInnerControl.setAddElevenPlanEndStock(subAddElevenPlanEndStock);
				
				subInnerControl.setAddTwelvePlanPrdNum(subAddTwelvePlanPrdNum);
				subInnerControl.setAddTwelvePlanDlvNum(subAddTwelvePlanDlvNum);
				subInnerControl.setAddTwelvePlanEndStock(subAddTwelvePlanEndStock);
				
				subInnerControl.setAddThirteenPlanDlvNum(subAddThirteenPlanDlvNum);

				list.add(subInnerControl);
				//从新设置值
				 tempItemCode=itemCode;
				 tempItemName=itemName;
				 tempSeriesCode=prodSeriesCode;
				 tempSeriesName=prodSeriesDesc;
				 tempRank=rank;
				 subBeginOrder=bg0;
				 subBeginStock=bg0;
				 subBeginEnableOrder=bg0;
				 subPlanPrdNum = bg0;
				 subActPrdNum = bg0;
				 subPlanDlvNum = bg0;
				 subActDlvNum = bg0;
				 subSuppDlvNum = bg0;
				 subSuppActNum=bg0;
				 subEndStock=bg0;
					
				 subAddOnePlanPrdNum=bg0;
				 subAddOnePlanDlvNum=bg0;
				 subAddOnePlanEndStock=bg0;
				
				
				 subAddTwoPlanPrdNum=bg0;
				 subAddTwoPlanDlvNum=bg0;
				 subAddTwoPlanEndStock=bg0;
			
				
				 subAddThreePlanPrdNum=bg0;
				 subAddThreePlanDlvNum=bg0;
				 subAddThreePlanEndStock=bg0;
					
				 subAddFourPlanPrdNum=bg0;
				 subAddFourPlanDlvNum=bg0;
				 subAddFourPlanEndStock=bg0;
			
				
				 subAddFivePlanPrdNum=bg0;
				 subAddFivePlanDlvNum=bg0;
				 subAddFivePlanEndStock=bg0;
				
				 subAddSixPlanPrdNum=bg0;
				 subAddSixPlanDlvNum=bg0;
			     subAddSixPlanEndStock=bg0;
				
				 subAddSevenPlanPrdNum=bg0;
				 subAddSevenPlanDlvNum=bg0;
				 subAddSevenPlanEndStock=bg0;
				
				 subAddEightPlanPrdNum=bg0;
				 subAddEightPlanDlvNum=bg0;
				 subAddEightPlanEndStock=bg0;
				
				
				 subAddNinePlanPrdNum=bg0;
				 subAddNinePlanDlvNum=bg0;
				 subAddNinePlanEndStock=bg0;
				
				 subAddTenPlanPrdNum=bg0;
				 subAddTenPlanDlvNum=bg0;
				 subAddTenPlanEndStock=bg0;
				
				 subAddElevenPlanPrdNum=bg0;
				 subAddElevenPlanDlvNum=bg0;
				 subAddElevenPlanEndStock=bg0;
				
				 subAddTwelvePlanPrdNum=bg0;
				 subAddTwelvePlanDlvNum=bg0;
				 subAddTwelvePlanEndStock=bg0;
				
				 subAddThirteenPlanDlvNum=bg0;

			}
			list.add(innerControl);
			
			subBeginOrder=BigDecimalUtil.add(subBeginOrder, beginOrder);
			subBeginStock=BigDecimalUtil.add(subBeginStock, beginStock);
			subBeginEnableOrder=BigDecimalUtil.add(subBeginEnableOrder, beginEnableOrder);
			subPlanPrdNum=BigDecimalUtil.add(subPlanPrdNum, planPrdNum);
			subActPrdNum=BigDecimalUtil.add(subActPrdNum, actPrdNum);
			subPlanDlvNum=BigDecimalUtil.add(subPlanDlvNum, planDlvNum);
			subActDlvNum=BigDecimalUtil.add(subActDlvNum, actDlvNum);
			subSuppDlvNum=BigDecimalUtil.add(subSuppDlvNum, suppDlvNum);
			
			subSuppActNum=BigDecimalUtil.add(subSuppActNum, suppActNum);
			subEndStock=BigDecimalUtil.add(subEndStock, endStock);

			subAddOnePlanPrdNum=BigDecimalUtil.add(subAddOnePlanPrdNum, addOnePlanPrdNum);
			subAddOnePlanDlvNum=BigDecimalUtil.add(subAddOnePlanDlvNum, addOnePlanDlvNum);
			subAddOnePlanEndStock=BigDecimalUtil.add(subAddOnePlanEndStock, addOnePlanEndStock);

			subAddTwoPlanPrdNum=BigDecimalUtil.add(subAddTwoPlanPrdNum, addTwoPlanPrdNum);
			subAddTwoPlanDlvNum=BigDecimalUtil.add(subAddTwoPlanDlvNum, addTwoPlanDlvNum);
			subAddTwoPlanEndStock=BigDecimalUtil.add(subAddTwoPlanEndStock, addTwoPlanEndStock);

			subAddThreePlanPrdNum=BigDecimalUtil.add(subAddThreePlanPrdNum, addThreePlanPrdNum);
			subAddThreePlanDlvNum=BigDecimalUtil.add(subAddThreePlanDlvNum, addThreePlanDlvNum);
			subAddThreePlanEndStock=BigDecimalUtil.add(subAddThreePlanEndStock, addThreePlanEndStock);

			subAddFourPlanPrdNum=BigDecimalUtil.add(subAddFourPlanPrdNum, addFourPlanPrdNum);
			subAddFourPlanDlvNum=BigDecimalUtil.add(subAddFourPlanDlvNum, addFourPlanDlvNum);
			subAddFourPlanEndStock=BigDecimalUtil.add(subAddFourPlanEndStock, addFourPlanEndStock);

			subAddFivePlanPrdNum=BigDecimalUtil.add(subAddFivePlanPrdNum, addFivePlanPrdNum);
			subAddFivePlanDlvNum=BigDecimalUtil.add(subAddFivePlanDlvNum, addFivePlanDlvNum);
			subAddFivePlanEndStock=BigDecimalUtil.add(subAddFivePlanEndStock, addFivePlanEndStock);

			subAddSixPlanPrdNum=BigDecimalUtil.add(subAddSixPlanPrdNum, addSixPlanPrdNum);
			subAddSixPlanDlvNum=BigDecimalUtil.add(subAddSixPlanDlvNum, addSixPlanDlvNum);
			subAddSixPlanEndStock=BigDecimalUtil.add(subAddSixPlanEndStock, addSixPlanEndStock);

			subAddSevenPlanPrdNum=BigDecimalUtil.add(subAddSevenPlanPrdNum, addSevenPlanPrdNum);
			subAddSevenPlanDlvNum=BigDecimalUtil.add(subAddSevenPlanDlvNum, addSevenPlanDlvNum);
			subAddSevenPlanEndStock=BigDecimalUtil.add(subAddSevenPlanEndStock, addSevenPlanEndStock);

			subAddEightPlanPrdNum=BigDecimalUtil.add(subAddEightPlanPrdNum, addEightPlanPrdNum);
			subAddEightPlanDlvNum=BigDecimalUtil.add(subAddEightPlanDlvNum, addEightPlanDlvNum);
			subAddEightPlanEndStock=BigDecimalUtil.add(subAddEightPlanEndStock, addEightPlanEndStock);

			subAddNinePlanPrdNum=BigDecimalUtil.add(subAddNinePlanPrdNum, addNinePlanPrdNum);
			subAddNinePlanDlvNum=BigDecimalUtil.add(subAddNinePlanDlvNum, addNinePlanDlvNum);
			subAddNinePlanEndStock=BigDecimalUtil.add(subAddNinePlanEndStock, addNinePlanEndStock);

			subAddTenPlanPrdNum=BigDecimalUtil.add(subAddTenPlanPrdNum, addTenPlanPrdNum);
			subAddTenPlanDlvNum=BigDecimalUtil.add(subAddTenPlanDlvNum, addTenPlanDlvNum);
			subAddTenPlanEndStock=BigDecimalUtil.add(subAddTenPlanEndStock, addTenPlanEndStock);

			subAddElevenPlanPrdNum=BigDecimalUtil.add(subAddElevenPlanPrdNum, addElevenPlanPrdNum);
			subAddElevenPlanDlvNum=BigDecimalUtil.add(subAddElevenPlanDlvNum, addElevenPlanDlvNum);
			subAddElevenPlanEndStock=BigDecimalUtil.add(subAddElevenPlanEndStock, addElevenPlanEndStock);

			subAddTwelvePlanPrdNum=BigDecimalUtil.add(subAddTwelvePlanPrdNum, addTwelvePlanPrdNum);
			subAddTwelvePlanDlvNum=BigDecimalUtil.add(subAddTwelvePlanDlvNum, addTwelvePlanDlvNum);
			subAddTwelvePlanEndStock=BigDecimalUtil.add(subAddTwelvePlanEndStock, addTwelvePlanEndStock);

			subAddThirteenPlanDlvNum=BigDecimalUtil.add(subAddThirteenPlanDlvNum, addThirteenPlanDlvNum);
			
			if(i==size-1){
				InnerControl subInnerControl=new InnerControl();
				subInnerControl.setItemCode(itemCode);
				if(StringUtils.isEmpty(itemName)){
					subInnerControl.setMateDesc("未知类别合计"); 
					subInnerControl.setItemName("未知类别");

				}else{
					subInnerControl.setMateDesc(itemName+"合计"); 
					subInnerControl.setItemName(itemName);
				}
				subInnerControl.setProdSeriesCode(prodSeriesCode);
				subInnerControl.setProdSeriesDesc(prodSeriesDesc);
				subInnerControl.setRank(tempRank);
				subInnerControl.setBeginOrder(subBeginOrder);
				subInnerControl.setBeginStock(subBeginStock);
				subInnerControl.setBeginEnableOrder(subBeginEnableOrder);
				subInnerControl.setPlanPrdNum(subPlanPrdNum);
				subInnerControl.setActPrdNum(subActPrdNum);
				
				subInnerControl.setPlanDlvNum(subPlanDlvNum);
				subInnerControl.setActDlvNum(subActDlvNum);
				subInnerControl.setSuppDlvNum(subSuppDlvNum);
				
				subInnerControl.setSuppActNum(subSuppActNum);
				subInnerControl.setEndStock(subEndStock);
				
				subInnerControl.setAddOnePlanPrdNum(subAddOnePlanPrdNum);
				subInnerControl.setAddOnePlanDlvNum(subAddOnePlanDlvNum);
				subInnerControl.setAddOnePlanEndStock(subAddOnePlanEndStock);
				
				subInnerControl.setAddTwoPlanPrdNum(subAddTwoPlanPrdNum);
				subInnerControl.setAddTwoPlanDlvNum(subAddTwoPlanDlvNum);
				subInnerControl.setAddTwoPlanEndStock(subAddTwoPlanEndStock);
				
				subInnerControl.setAddThreePlanPrdNum(subAddThreePlanPrdNum);
				subInnerControl.setAddThreePlanDlvNum(subAddThreePlanDlvNum);
				subInnerControl.setAddThreePlanEndStock(subAddThreePlanEndStock);
				
				subInnerControl.setAddFourPlanPrdNum(subAddFourPlanPrdNum);
				subInnerControl.setAddFourPlanDlvNum(subAddFourPlanDlvNum);
				subInnerControl.setAddFourPlanEndStock(subAddFourPlanEndStock);
				
				subInnerControl.setAddFivePlanPrdNum(subAddFivePlanPrdNum);
				subInnerControl.setAddFivePlanDlvNum(subAddFivePlanDlvNum);
				subInnerControl.setAddFivePlanEndStock(subAddFivePlanEndStock);
				
				subInnerControl.setAddSixPlanPrdNum(subAddSixPlanPrdNum);
				subInnerControl.setAddSixPlanDlvNum(subAddSixPlanDlvNum);
				subInnerControl.setAddSixPlanEndStock(subAddSixPlanEndStock);
				
				subInnerControl.setAddSevenPlanPrdNum(subAddSevenPlanPrdNum);
				subInnerControl.setAddSevenPlanDlvNum(subAddSevenPlanDlvNum);
				subInnerControl.setAddSevenPlanEndStock(subAddSevenPlanEndStock);
				
				subInnerControl.setAddEightPlanPrdNum(subAddEightPlanPrdNum);
				subInnerControl.setAddEightPlanDlvNum(subAddEightPlanDlvNum);
				subInnerControl.setAddEightPlanEndStock(subAddEightPlanEndStock);
				
				subInnerControl.setAddNinePlanPrdNum(subAddNinePlanPrdNum);
				subInnerControl.setAddNinePlanDlvNum(subAddNinePlanDlvNum);
				subInnerControl.setAddNinePlanEndStock(subAddNinePlanEndStock);
				
				subInnerControl.setAddTenPlanPrdNum(subAddTenPlanPrdNum);
				subInnerControl.setAddTenPlanDlvNum(subAddTenPlanDlvNum);
				subInnerControl.setAddTenPlanEndStock(subAddTenPlanEndStock);
				
				subInnerControl.setAddElevenPlanPrdNum(subAddElevenPlanPrdNum);
				subInnerControl.setAddElevenPlanDlvNum(subAddElevenPlanDlvNum);
				subInnerControl.setAddElevenPlanEndStock(subAddElevenPlanEndStock);
				
				subInnerControl.setAddTwelvePlanPrdNum(subAddTwelvePlanPrdNum);
				subInnerControl.setAddTwelvePlanDlvNum(subAddTwelvePlanDlvNum);
				subInnerControl.setAddTwelvePlanEndStock(subAddTwelvePlanEndStock);
				
				subInnerControl.setAddThirteenPlanDlvNum(subAddThirteenPlanDlvNum);

				list.add(subInnerControl);
			}
		}
		return list;
	}
	@Override
	public List<BusyStock> getSuppBusyStock(Map<String, Object> map) {
		Date planMonth = (Date)map.get("planMonth");
		//获取当月的旺季备货信息
		List<BusyStock> busyStocks = actReachMapper.getSuppBusyStock(map);
		//循环获取以后12个月份的生产备货 交货信息
		for (BusyStock busyStock : busyStocks) {
			Calendar cal= Calendar.getInstance();
			cal.setTime(planMonth);
			String mateCode = busyStock.getMateCode();
			String suppNo = busyStock.getSuppNo();
			map.put("mateCode", mateCode);
			map.put("suppNo", suppNo);
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addOnePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addOnePrdDlvInfo!=null){
				busyStock.setAddOnePlanPrdNum(addOnePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddOnePlanDlvNum(addOnePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddOnePlanEndStock(addOnePrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwoPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTwoPrdDlvInfo!=null){
				busyStock.setAddTwoPlanPrdNum(addTwoPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddTwoPlanDlvNum(addTwoPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddTwoPlanEndStock(addTwoPrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addThreePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addThreePrdDlvInfo!=null){
				busyStock.setAddThreePlanPrdNum(addThreePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddThreePlanDlvNum(addThreePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddThreePlanEndStock(addThreePrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFourPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addFourPrdDlvInfo!=null){
				busyStock.setAddFourPlanPrdNum(addFourPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddFourPlanDlvNum(addFourPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddFourPlanEndStock(addFourPrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFivePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addFivePrdDlvInfo!=null){
				busyStock.setAddFivePlanPrdNum(addFivePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddFivePlanDlvNum(addFivePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddFivePlanEndStock(addFivePrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSixPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addSixPrdDlvInfo!=null){
				busyStock.setAddSixPlanPrdNum(addSixPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddSixPlanDlvNum(addSixPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddSixPlanEndStock(addSixPrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSevenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addSevenPrdDlvInfo!=null){
				busyStock.setAddSevenPlanPrdNum(addSevenPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddSevenPlanDlvNum(addSevenPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddSevenPlanEndStock(addSevenPrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addEightPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addEightPrdDlvInfo!=null){
				busyStock.setAddEightPlanPrdNum(addEightPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddEightPlanDlvNum(addEightPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddEightPlanEndStock(addEightPrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addNinePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addNinePrdDlvInfo!=null){
				busyStock.setAddNinePlanPrdNum(addNinePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddNinePlanDlvNum(addNinePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddNinePlanEndStock(addNinePrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTenPrdDlvInfo!=null){
				busyStock.setAddTenPlanPrdNum(addTenPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddTenPlanDlvNum(addTenPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddTenPlanEndStock(addTenPrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addElevenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addElevenPrdDlvInfo!=null){
				busyStock.setAddElevenPlanPrdNum(addElevenPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddElevenPlanDlvNum(addElevenPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddElevenPlanEndStock(addElevenPrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwelvePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTwelvePrdDlvInfo!=null){
				busyStock.setAddTwelvePlanPrdNum(addTwelvePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddTwelvePlanDlvNum(addTwelvePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddTwelvePlanEndStock(addTwelvePrdDlvInfo.getEndStock());
			}
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addThirteenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addThirteenPrdDlvInfo!=null){
				busyStock.setAddThirteenPlanDlvNum(addThirteenPrdDlvInfo.getPlanDlvNum());
			}
		};
		calSumAndMax(busyStocks);
		calSafeScale(busyStocks);
		return busyStocks;
	}
	/**
	 * 计算生产交货计划的总和以及期末预计库存的最大值
	 * @param busyStocks
	 */
	public void  calSumAndMax( List<BusyStock> busyStocks){
		BigDecimal bg0=new BigDecimal(0);
		for (BusyStock busyStock : busyStocks) {
			BigDecimal planPrdNum = busyStock.getPlanPrdNum();
			BigDecimal planDlvNum = busyStock.getPlanDlvNum();
			BigDecimal endStock = busyStock.getEndStock();
			
			BigDecimal addOnePlanPrdNum = busyStock.getAddOnePlanPrdNum();
			BigDecimal addOnePlanDlvNum = busyStock.getAddOnePlanDlvNum();
			BigDecimal addOnePlanEndStock = busyStock.getAddOnePlanEndStock();
			
			BigDecimal addTwoPlanPrdNum = busyStock.getAddTwoPlanPrdNum();
			BigDecimal addTwoPlanDlvNum = busyStock.getAddTwoPlanDlvNum();
			BigDecimal addTwoPlanEndStock = busyStock.getAddTwoPlanEndStock();
			
			BigDecimal addThreePlanPrdNum = busyStock.getAddThreePlanPrdNum();
			BigDecimal addThreePlanDlvNum = busyStock.getAddThreePlanDlvNum();
			BigDecimal addThreePlanEndStock = busyStock.getAddThreePlanEndStock();
			
			BigDecimal addFourPlanPrdNum = busyStock.getAddFourPlanPrdNum();
			BigDecimal addFourPlanDlvNum = busyStock.getAddFourPlanDlvNum();
			BigDecimal addFourPlanEndStock = busyStock.getAddFourPlanEndStock();
			
			BigDecimal addFivePlanPrdNum = busyStock.getAddFivePlanPrdNum();
			BigDecimal addFivePlanDlvNum = busyStock.getAddFivePlanDlvNum();
			BigDecimal addFivePlanEndStock = busyStock.getAddFivePlanEndStock();
			
			BigDecimal addSixPlanPrdNum = busyStock.getAddSixPlanPrdNum();
			BigDecimal addSixPlanDlvNum = busyStock.getAddSixPlanDlvNum();
			BigDecimal addSixPlanEndStock = busyStock.getAddSixPlanEndStock();
			
			BigDecimal addSevenPlanPrdNum = busyStock.getAddSevenPlanPrdNum();
			BigDecimal addSevenPlanDlvNum = busyStock.getAddSevenPlanDlvNum();
			BigDecimal addSevenPlanEndStock = busyStock.getAddSevenPlanEndStock();
			
			BigDecimal addEightPlanPrdNum = busyStock.getAddEightPlanPrdNum();
			BigDecimal addEightPlanDlvNum = busyStock.getAddEightPlanDlvNum();
			BigDecimal addEightPlanEndStock = busyStock.getAddEightPlanEndStock();
			
			BigDecimal addNinePlanPrdNum = busyStock.getAddNinePlanPrdNum();
			BigDecimal addNinePlanDlvNum = busyStock.getAddNinePlanDlvNum();
			BigDecimal addNinePlanEndStock = busyStock.getAddNinePlanEndStock();
			
			BigDecimal addTenPlanPrdNum = busyStock.getAddTenPlanPrdNum();
			BigDecimal addTenPlanDlvNum = busyStock.getAddTenPlanDlvNum();
			BigDecimal addTenPlanEndStock = busyStock.getAddTenPlanEndStock();
			
			BigDecimal addElevenPlanPrdNum = busyStock.getAddElevenPlanPrdNum();
			BigDecimal addElevenPlanDlvNum = busyStock.getAddElevenPlanDlvNum();
			BigDecimal addElevenPlanEndStock = busyStock.getAddElevenPlanEndStock();
			
			BigDecimal addTwelvePlanPrdNum =busyStock.getAddTwelvePlanPrdNum();
			BigDecimal addTwelvePlanDlvNum = busyStock.getAddTwelvePlanDlvNum();
			BigDecimal addTwelvePlanEndStock = busyStock.getAddTwelvePlanEndStock();
			
			BigDecimal sumPlanPrdNum=bg0;
			BigDecimal sumPlanDlvNum=bg0;
			BigDecimal maxPlanEndStock=bg0;
			sumPlanPrdNum=BigDecimalUtil.getSum(planPrdNum,addOnePlanPrdNum,addTwoPlanPrdNum,addThreePlanPrdNum,addFourPlanPrdNum,addFivePlanPrdNum,addSixPlanPrdNum,addSevenPlanPrdNum,addEightPlanPrdNum,addNinePlanPrdNum,addTenPlanPrdNum,addElevenPlanPrdNum,addTwelvePlanPrdNum);
			sumPlanDlvNum=BigDecimalUtil.getSum(planDlvNum,addOnePlanDlvNum,addTwoPlanDlvNum,addThreePlanDlvNum,addFourPlanDlvNum,addFivePlanDlvNum,addSixPlanDlvNum,addSevenPlanDlvNum,addEightPlanDlvNum,addNinePlanDlvNum,addTenPlanDlvNum,addElevenPlanDlvNum,addTwelvePlanDlvNum);
			maxPlanEndStock=BigDecimalUtil.getMax(endStock,addOnePlanEndStock,addTwoPlanEndStock,addThreePlanEndStock,addFourPlanEndStock,addFivePlanEndStock,addSixPlanEndStock,addSevenPlanEndStock,addEightPlanEndStock,addNinePlanEndStock,addTenPlanEndStock,addElevenPlanEndStock,addTwelvePlanEndStock);
			busyStock.setSumPlanPrdNum(sumPlanPrdNum);
			busyStock.setSumPlanDlvNum(sumPlanDlvNum);
			busyStock.setMaxPlanEndStock(maxPlanEndStock);
		}
	}
	
	
	/**
	 * 计算安全库存率
	 * @param busyStocks
	 */
	public void  calSafeScale( List<BusyStock> busyStocks){
		for (BusyStock busyStock : busyStocks) {
			BigDecimal endStock = busyStock.getEndStock();
			BigDecimal addOnePlanDlvNum = busyStock.getAddOnePlanDlvNum();
			BigDecimal addOnePlanEndStock = busyStock.getAddOnePlanEndStock();
			
			BigDecimal addTwoPlanDlvNum = busyStock.getAddTwoPlanDlvNum();
			BigDecimal addTwoPlanEndStock = busyStock.getAddTwoPlanEndStock();
			
			BigDecimal addThreePlanDlvNum = busyStock.getAddThreePlanDlvNum();
			BigDecimal addThreePlanEndStock = busyStock.getAddThreePlanEndStock();
			
			BigDecimal addFourPlanDlvNum = busyStock.getAddFourPlanDlvNum();
			BigDecimal addFourPlanEndStock = busyStock.getAddFourPlanEndStock();
			
			BigDecimal addFivePlanDlvNum = busyStock.getAddFivePlanDlvNum();
			BigDecimal addFivePlanEndStock = busyStock.getAddFivePlanEndStock();
			
			BigDecimal addSixPlanDlvNum = busyStock.getAddSixPlanDlvNum();
			BigDecimal addSixPlanEndStock = busyStock.getAddSixPlanEndStock();
			
			BigDecimal addSevenPlanDlvNum = busyStock.getAddSevenPlanDlvNum();
			BigDecimal addSevenPlanEndStock = busyStock.getAddSevenPlanEndStock();
			
			BigDecimal addEightPlanDlvNum = busyStock.getAddEightPlanDlvNum();
			BigDecimal addEightPlanEndStock = busyStock.getAddEightPlanEndStock();
			
			BigDecimal addNinePlanDlvNum = busyStock.getAddNinePlanDlvNum();
			BigDecimal addNinePlanEndStock = busyStock.getAddNinePlanEndStock();
			
			BigDecimal addTenPlanDlvNum = busyStock.getAddTenPlanDlvNum();
			BigDecimal addTenPlanEndStock = busyStock.getAddTenPlanEndStock();
			
			BigDecimal addElevenPlanDlvNum = busyStock.getAddElevenPlanDlvNum();
			BigDecimal addElevenPlanEndStock = busyStock.getAddElevenPlanEndStock();
			
			BigDecimal addTwelvePlanDlvNum = busyStock.getAddTwelvePlanDlvNum();
			BigDecimal addTwelvePlanEndStock = busyStock.getAddTwelvePlanEndStock();
			
			BigDecimal addThirteenPlanDlvNum = busyStock.getAddThirteenPlanDlvNum();			
			//计算安全库存率
			busyStock.setSafeScale(BigDecimalUtil.getPercentage(endStock, addOnePlanDlvNum));
			busyStock.setAddOneSafeScale(BigDecimalUtil.getPercentage(addOnePlanEndStock, addTwoPlanDlvNum));
			busyStock.setAddTwoSafeScale(BigDecimalUtil.getPercentage(addTwoPlanEndStock, addThreePlanDlvNum));
			busyStock.setAddThreeSafeScale(BigDecimalUtil.getPercentage(addThreePlanEndStock, addFourPlanDlvNum));
			busyStock.setAddFourSafeScale(BigDecimalUtil.getPercentage(addFourPlanEndStock, addFivePlanDlvNum));
			busyStock.setAddFiveSafeScale(BigDecimalUtil.getPercentage(addFivePlanEndStock, addSixPlanDlvNum));
			busyStock.setAddSixSafeScale(BigDecimalUtil.getPercentage(addSixPlanEndStock, addSevenPlanDlvNum));
			busyStock.setAddSevenSafeScale(BigDecimalUtil.getPercentage(addSevenPlanEndStock, addEightPlanDlvNum));
			busyStock.setAddEightSafeScale(BigDecimalUtil.getPercentage(addEightPlanEndStock, addNinePlanDlvNum));
			busyStock.setAddNineSafeScale(BigDecimalUtil.getPercentage(addNinePlanEndStock, addTenPlanDlvNum));
			busyStock.setAddTenSafeScale(BigDecimalUtil.getPercentage(addTenPlanEndStock, addElevenPlanDlvNum));
			busyStock.setAddElevenSafeScale(BigDecimalUtil.getPercentage(addElevenPlanEndStock, addTwelvePlanDlvNum));
			busyStock.setAddTwelveSafeScale(BigDecimalUtil.getPercentage(addTwelvePlanEndStock,addThirteenPlanDlvNum ));
		}
	}
	
	
	
	
	@Override
	public List<BusyStock> getMateBusyStock(Map<String, Object> map) {
		map.remove("suppNo");
		Date planMonth = (Date)map.get("planMonth");
		//获取当月的旺季备货信息
		List<BusyStock> busyStocks = actReachMapper.getMateBusyStock(map);
		//循环获取以后12个月份的生产备货 交货信息
		for (BusyStock busyStock : busyStocks) {
			Calendar cal= Calendar.getInstance();
			cal.setTime(planMonth);
			String mateCode = busyStock.getMateCode();
			map.put("mateCode", mateCode);
			//第一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addOnePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addOnePrdDlvInfo!=null){
				busyStock.setAddOnePlanPrdNum(addOnePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddOnePlanDlvNum(addOnePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddOnePlanEndStock(addOnePrdDlvInfo.getEndStock());
			}
			//第二个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwoPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTwoPrdDlvInfo!=null){
				busyStock.setAddTwoPlanPrdNum(addTwoPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddTwoPlanDlvNum(addTwoPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddTwoPlanEndStock(addTwoPrdDlvInfo.getEndStock());
			}
			//第三个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addThreePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addThreePrdDlvInfo!=null){
				busyStock.setAddThreePlanPrdNum(addThreePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddThreePlanDlvNum(addThreePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddThreePlanEndStock(addThreePrdDlvInfo.getEndStock());
			}
			//第四个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFourPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addFourPrdDlvInfo!=null){
				busyStock.setAddFourPlanPrdNum(addFourPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddFourPlanDlvNum(addFourPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddFourPlanEndStock(addFourPrdDlvInfo.getEndStock());
			}
			//第五个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addFivePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addFivePrdDlvInfo!=null){
				busyStock.setAddFivePlanPrdNum(addFivePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddFivePlanDlvNum(addFivePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddFivePlanEndStock(addFivePrdDlvInfo.getEndStock());
			}
			//第六个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSixPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addSixPrdDlvInfo!=null){
				busyStock.setAddSixPlanPrdNum(addSixPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddSixPlanDlvNum(addSixPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddSixPlanEndStock(addSixPrdDlvInfo.getEndStock());
			}
			//第七个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addSevenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addSevenPrdDlvInfo!=null){
				busyStock.setAddSevenPlanPrdNum(addSevenPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddSevenPlanDlvNum(addSevenPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddSevenPlanEndStock(addSevenPrdDlvInfo.getEndStock());
			}
			//第八个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addEightPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addEightPrdDlvInfo!=null){
				busyStock.setAddEightPlanPrdNum(addEightPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddEightPlanDlvNum(addEightPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddEightPlanEndStock(addEightPrdDlvInfo.getEndStock());
			}
			//第九个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addNinePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addNinePrdDlvInfo!=null){
				busyStock.setAddNinePlanPrdNum(addNinePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddNinePlanDlvNum(addNinePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddNinePlanEndStock(addNinePrdDlvInfo.getEndStock());
			}
			//第十个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTenPrdDlvInfo!=null){
				busyStock.setAddTenPlanPrdNum(addTenPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddTenPlanDlvNum(addTenPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddTenPlanEndStock(addTenPrdDlvInfo.getEndStock());
			}
			//第十一个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addElevenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addElevenPrdDlvInfo!=null){
				busyStock.setAddElevenPlanPrdNum(addElevenPrdDlvInfo.getPlanPrdNum());
				busyStock.setAddElevenPlanDlvNum(addElevenPrdDlvInfo.getPlanDlvNum());
				busyStock.setAddElevenPlanEndStock(addElevenPrdDlvInfo.getEndStock());
			}
			//第十二个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addTwelvePrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addTwelvePrdDlvInfo!=null){
				busyStock.setAddTwelvePlanPrdNum(addTwelvePrdDlvInfo.getPlanPrdNum());
				busyStock.setAddTwelvePlanDlvNum(addTwelvePrdDlvInfo.getPlanDlvNum());
				busyStock.setAddTwelvePlanEndStock(addTwelvePrdDlvInfo.getEndStock());
			}
			//第13个月
			cal.add(Calendar.MONTH, 1);
			map.put("planMonth", cal.getTime());
			PrdDlvInfo addThirteenPrdDlvInfo = actReachMapper.getPrdDlvInfo(map);
			if(addThirteenPrdDlvInfo!=null){
				busyStock.setAddThirteenPlanDlvNum(addThirteenPrdDlvInfo.getPlanDlvNum());
			}
		};
		busyStocks=calMateTotalBusyStock(busyStocks);
		calSafeScale(busyStocks);
		return busyStocks;
	}
	/**
	 * 计算旺季备货表按照类别物料的汇总信息
	 * @param busyStocks
	 */
	public List<BusyStock> calMateTotalBusyStock(List<BusyStock> busyStocks){
		for (BusyStock busyStock : busyStocks) {
			
			Integer boxNumber = busyStock.getBoxNumber();
			Integer packNumber = busyStock.getPackNumber();
			BigDecimal boxNum=new BigDecimal(boxNumber);
			BigDecimal packNum=new BigDecimal(packNumber);
			
			BigDecimal beginStock = busyStock.getBeginStock();
			BigDecimal beginOrder = busyStock.getBeginOrder();
			BigDecimal beginEnableOrder = busyStock.getBeginEnableOrder();
			BigDecimal planPrdNum = busyStock.getPlanPrdNum();
			BigDecimal planDlvNum = busyStock.getPlanDlvNum();
			BigDecimal endStock = busyStock.getEndStock();
			
			BigDecimal addOnePlanPrdNum = busyStock.getAddOnePlanPrdNum();
			BigDecimal addOnePlanDlvNum = busyStock.getAddOnePlanDlvNum();
			BigDecimal addOnePlanEndStock = busyStock.getAddOnePlanEndStock();
			
			BigDecimal addTwoPlanPrdNum = busyStock.getAddTwoPlanPrdNum();
			BigDecimal addTwoPlanDlvNum = busyStock.getAddTwoPlanDlvNum();
			BigDecimal addTwoPlanEndStock = busyStock.getAddTwoPlanEndStock();
			
			BigDecimal addThreePlanPrdNum = busyStock.getAddThreePlanPrdNum();
			BigDecimal addThreePlanDlvNum = busyStock.getAddThreePlanDlvNum();
			BigDecimal addThreePlanEndStock = busyStock.getAddThreePlanEndStock();
			
			BigDecimal addFourPlanPrdNum = busyStock.getAddFourPlanPrdNum();
			BigDecimal addFourPlanDlvNum = busyStock.getAddFourPlanDlvNum();
			BigDecimal addFourPlanEndStock = busyStock.getAddFourPlanEndStock();
			
			BigDecimal addFivePlanPrdNum = busyStock.getAddFivePlanPrdNum();
			BigDecimal addFivePlanDlvNum = busyStock.getAddFivePlanDlvNum();
			BigDecimal addFivePlanEndStock = busyStock.getAddFivePlanEndStock();
			
			BigDecimal addSixPlanPrdNum = busyStock.getAddSixPlanPrdNum();
			BigDecimal addSixPlanDlvNum = busyStock.getAddSixPlanDlvNum();
			BigDecimal addSixPlanEndStock = busyStock.getAddSixPlanEndStock();
			
			BigDecimal addSevenPlanPrdNum = busyStock.getAddSevenPlanPrdNum();
			BigDecimal addSevenPlanDlvNum = busyStock.getAddSevenPlanDlvNum();
			BigDecimal addSevenPlanEndStock = busyStock.getAddSevenPlanEndStock();
			
			BigDecimal addEightPlanPrdNum = busyStock.getAddEightPlanPrdNum();
			BigDecimal addEightPlanDlvNum = busyStock.getAddEightPlanDlvNum();
			BigDecimal addEightPlanEndStock = busyStock.getAddEightPlanEndStock();
			
			BigDecimal addNinePlanPrdNum = busyStock.getAddNinePlanPrdNum();
			BigDecimal addNinePlanDlvNum = busyStock.getAddNinePlanDlvNum();
			BigDecimal addNinePlanEndStock = busyStock.getAddNinePlanEndStock();
			
			BigDecimal addTenPlanPrdNum = busyStock.getAddTenPlanPrdNum();
			BigDecimal addTenPlanDlvNum = busyStock.getAddTenPlanDlvNum();
			BigDecimal addTenPlanEndStock = busyStock.getAddTenPlanEndStock();
			
			BigDecimal addElevenPlanPrdNum = busyStock.getAddElevenPlanPrdNum();
			BigDecimal addElevenPlanDlvNum = busyStock.getAddElevenPlanDlvNum();
			BigDecimal addElevenPlanEndStock = busyStock.getAddElevenPlanEndStock();
			
			BigDecimal addTwelvePlanPrdNum =busyStock.getAddTwelvePlanPrdNum();
			BigDecimal addTwelvePlanDlvNum = busyStock.getAddTwelvePlanDlvNum();
			BigDecimal addTwelvePlanEndStock = busyStock.getAddTwelvePlanEndStock();
						
			busyStock.setBeginStock(BigDecimalUtil.multiply(beginStock,boxNum,packNum));
			busyStock.setBeginOrder(BigDecimalUtil.multiply(beginOrder,boxNum,packNum));
			busyStock.setBeginEnableOrder(BigDecimalUtil.multiply(beginEnableOrder,boxNum,packNum));
			busyStock.setPlanPrdNum(BigDecimalUtil.multiply(planPrdNum,boxNum,packNum));
			busyStock.setPlanDlvNum(BigDecimalUtil.multiply(planDlvNum,boxNum,packNum));
			busyStock.setEndStock(BigDecimalUtil.multiply(endStock,boxNum,packNum));
			
			busyStock.setAddOnePlanPrdNum(BigDecimalUtil.multiply(addOnePlanPrdNum,boxNum,packNum));
			busyStock.setAddOnePlanDlvNum(BigDecimalUtil.multiply(addOnePlanDlvNum,boxNum,packNum));
			busyStock.setAddOnePlanEndStock(BigDecimalUtil.multiply(addOnePlanEndStock,boxNum,packNum));
			
			busyStock.setAddTwoPlanPrdNum(BigDecimalUtil.multiply(addTwoPlanPrdNum,boxNum,packNum));
			busyStock.setAddTwoPlanDlvNum(BigDecimalUtil.multiply(addTwoPlanDlvNum,boxNum,packNum));
			busyStock.setAddTwoPlanEndStock(BigDecimalUtil.multiply(addTwoPlanEndStock,boxNum,packNum));
			
			busyStock.setAddThreePlanPrdNum(BigDecimalUtil.multiply(addThreePlanPrdNum,boxNum,packNum));
			busyStock.setAddThreePlanDlvNum(BigDecimalUtil.multiply(addThreePlanDlvNum,boxNum,packNum));
			busyStock.setAddThreePlanEndStock(BigDecimalUtil.multiply(addThreePlanEndStock,boxNum,packNum));
			
			busyStock.setAddFourPlanPrdNum(BigDecimalUtil.multiply(addFourPlanPrdNum,boxNum,packNum));
			busyStock.setAddFourPlanDlvNum(BigDecimalUtil.multiply(addFourPlanDlvNum,boxNum,packNum));
			busyStock.setAddFourPlanEndStock(BigDecimalUtil.multiply(addFourPlanEndStock,boxNum,packNum));
			
			busyStock.setAddFivePlanPrdNum(BigDecimalUtil.multiply(addFivePlanPrdNum,boxNum,packNum));
			busyStock.setAddFivePlanDlvNum(BigDecimalUtil.multiply(addFivePlanDlvNum,boxNum,packNum));
			busyStock.setAddFivePlanEndStock(BigDecimalUtil.multiply(addFivePlanEndStock,boxNum,packNum));
			
			busyStock.setAddSixPlanPrdNum(BigDecimalUtil.multiply(addSixPlanPrdNum,boxNum,packNum));
			busyStock.setAddSixPlanDlvNum(BigDecimalUtil.multiply(addSixPlanDlvNum,boxNum,packNum));
			busyStock.setAddSixPlanEndStock(BigDecimalUtil.multiply(addSixPlanEndStock,boxNum,packNum));
			
			busyStock.setAddSevenPlanPrdNum(BigDecimalUtil.multiply(addSevenPlanPrdNum,boxNum,packNum));
			busyStock.setAddSevenPlanDlvNum(BigDecimalUtil.multiply(addSevenPlanDlvNum,boxNum,packNum));
			busyStock.setAddSevenPlanEndStock(BigDecimalUtil.multiply(addSevenPlanEndStock,boxNum,packNum));
			
			busyStock.setAddEightPlanPrdNum(BigDecimalUtil.multiply(addEightPlanPrdNum,boxNum,packNum));
			busyStock.setAddEightPlanDlvNum(BigDecimalUtil.multiply(addEightPlanDlvNum,boxNum,packNum));
			busyStock.setAddEightPlanEndStock(BigDecimalUtil.multiply(addEightPlanEndStock,boxNum,packNum));
			
			busyStock.setAddNinePlanPrdNum(BigDecimalUtil.multiply(addNinePlanPrdNum,boxNum,packNum));
			busyStock.setAddNinePlanDlvNum(BigDecimalUtil.multiply(addNinePlanDlvNum,boxNum,packNum));
			busyStock.setAddNinePlanEndStock(BigDecimalUtil.multiply(addNinePlanEndStock,boxNum,packNum));
			
			busyStock.setAddTenPlanPrdNum(BigDecimalUtil.multiply(addTenPlanPrdNum,boxNum,packNum));
			busyStock.setAddTenPlanDlvNum(BigDecimalUtil.multiply(addTenPlanDlvNum,boxNum,packNum));
			busyStock.setAddTenPlanEndStock(BigDecimalUtil.multiply(addTenPlanEndStock,boxNum,packNum));
			
			busyStock.setAddElevenPlanPrdNum(BigDecimalUtil.multiply(addElevenPlanPrdNum,boxNum,packNum));
			busyStock.setAddElevenPlanDlvNum(BigDecimalUtil.multiply(addElevenPlanDlvNum,boxNum,packNum));
			busyStock.setAddElevenPlanEndStock(BigDecimalUtil.multiply(addElevenPlanEndStock,boxNum,packNum));
			
			busyStock.setAddTwelvePlanPrdNum(BigDecimalUtil.multiply(addTwelvePlanPrdNum,boxNum,packNum));
			busyStock.setAddTwelvePlanDlvNum(BigDecimalUtil.multiply(addTwelvePlanDlvNum,boxNum,packNum));
			busyStock.setAddTwelvePlanEndStock(BigDecimalUtil.multiply(addTwelvePlanEndStock,boxNum,packNum));
			
				
		}
		List<BusyStock> list=new LinkedList<>();
		BigDecimal bg0 = new BigDecimal(0);
		String tempItemCode="";
		String tempItemName="";
		BigDecimal subPlanPrdNum = bg0;
		BigDecimal subPlanDlvNum = bg0;
		BigDecimal subBeginOrder = bg0;
		BigDecimal subBeginStock = bg0;
		BigDecimal subBeginEnableOrder = bg0;
		BigDecimal subEndStock = bg0;
		BigDecimal subAddOnePlanPrdNum = bg0;
		BigDecimal subAddOnePlanDlvNum = bg0;
		BigDecimal subAddOnePlanEndStock = bg0;
		BigDecimal subAddTwoPlanPrdNum = bg0;
		BigDecimal subAddTwoPlanDlvNum = bg0;
		BigDecimal subAddTwoPlanEndStock = bg0;
		BigDecimal subAddThreePlanPrdNum = bg0;
		BigDecimal subAddThreePlanDlvNum = bg0;
		BigDecimal subAddThreePlanEndStock = bg0;
		BigDecimal subAddFourPlanPrdNum = bg0;
		BigDecimal subAddFourPlanDlvNum = bg0;
		BigDecimal subAddFourPlanEndStock = bg0;
		BigDecimal subAddFivePlanPrdNum = bg0;
		BigDecimal subAddFivePlanDlvNum = bg0;
		BigDecimal subAddFivePlanEndStock = bg0;
		BigDecimal subAddSixPlanPrdNum = bg0;
		BigDecimal subAddSixPlanDlvNum = bg0;
		BigDecimal subAddSixPlanEndStock = bg0;
		BigDecimal subAddSevenPlanPrdNum = bg0;
		BigDecimal subAddSevenPlanDlvNum = bg0;
		BigDecimal subAddSevenPlanEndStock = bg0;
		BigDecimal subAddEightPlanPrdNum = bg0;
		BigDecimal subAddEightPlanDlvNum = bg0;
		BigDecimal subAddEightPlanEndStock = bg0;
		BigDecimal subAddNinePlanPrdNum = bg0;
		BigDecimal subAddNinePlanDlvNum = bg0;
		BigDecimal subAddNinePlanEndStock = bg0;
		BigDecimal subAddTenPlanPrdNum = bg0;
		BigDecimal subAddTenPlanDlvNum = bg0;
		BigDecimal subAddTenPlanEndStock = bg0;
		BigDecimal subAddElevenPlanPrdNum = bg0;
		BigDecimal subAddElevenPlanDlvNum = bg0;
		BigDecimal subAddElevenPlanEndStock = bg0;
		BigDecimal subAddTwelvePlanPrdNum = bg0;
		BigDecimal subAddTwelvePlanDlvNum = bg0;
		BigDecimal subAddTwelvePlanEndStock = bg0;
		int size = busyStocks.size();
		if(busyStocks==null ||size==0){
			return busyStocks;
		}
		BusyStock busyStock0 = busyStocks.get(0);
		tempItemCode=busyStock0.getItemCode();
		if(tempItemCode==null){
			tempItemCode="";
		}
		tempItemName=busyStock0.getItemName();
	
		for (int i=0;i<size;i++) {
			BusyStock busyStock = busyStocks.get(i);
			
			String itemCode = busyStock.getItemCode();
			if(itemCode==null){
				itemCode="";
			}
			String itemName = busyStock.getItemName();
			if(itemName==null || "".equals(itemName)){
				itemName="未知类别";
			}
			BigDecimal beginStock = busyStock.getBeginStock();
			BigDecimal beginOrder = busyStock.getBeginOrder();
			BigDecimal beginEnableOrder = busyStock.getBeginEnableOrder();
			BigDecimal planPrdNum = busyStock.getPlanPrdNum();
			BigDecimal planDlvNum = busyStock.getPlanDlvNum();
			BigDecimal endStock = busyStock.getEndStock();
			
			BigDecimal addOnePlanPrdNum = busyStock.getAddOnePlanPrdNum();
			BigDecimal addOnePlanDlvNum = busyStock.getAddOnePlanDlvNum();
			BigDecimal addOnePlanEndStock = busyStock.getAddOnePlanEndStock();
			
			BigDecimal addTwoPlanPrdNum = busyStock.getAddTwoPlanPrdNum();
			BigDecimal addTwoPlanDlvNum = busyStock.getAddTwoPlanDlvNum();
			BigDecimal addTwoPlanEndStock = busyStock.getAddTwoPlanEndStock();
			
			BigDecimal addThreePlanPrdNum = busyStock.getAddThreePlanPrdNum();
			BigDecimal addThreePlanDlvNum = busyStock.getAddThreePlanDlvNum();
			BigDecimal addThreePlanEndStock = busyStock.getAddThreePlanEndStock();
			
			BigDecimal addFourPlanPrdNum = busyStock.getAddFourPlanPrdNum();
			BigDecimal addFourPlanDlvNum = busyStock.getAddFourPlanDlvNum();
			BigDecimal addFourPlanEndStock = busyStock.getAddFourPlanEndStock();
			
			BigDecimal addFivePlanPrdNum = busyStock.getAddFivePlanPrdNum();
			BigDecimal addFivePlanDlvNum = busyStock.getAddFivePlanDlvNum();
			BigDecimal addFivePlanEndStock = busyStock.getAddFivePlanEndStock();
			
			BigDecimal addSixPlanPrdNum = busyStock.getAddSixPlanPrdNum();
			BigDecimal addSixPlanDlvNum = busyStock.getAddSixPlanDlvNum();
			BigDecimal addSixPlanEndStock = busyStock.getAddSixPlanEndStock();
			
			BigDecimal addSevenPlanPrdNum = busyStock.getAddSevenPlanPrdNum();
			BigDecimal addSevenPlanDlvNum = busyStock.getAddSevenPlanDlvNum();
			BigDecimal addSevenPlanEndStock = busyStock.getAddSevenPlanEndStock();
			
			BigDecimal addEightPlanPrdNum = busyStock.getAddEightPlanPrdNum();
			BigDecimal addEightPlanDlvNum = busyStock.getAddEightPlanDlvNum();
			BigDecimal addEightPlanEndStock = busyStock.getAddEightPlanEndStock();
			
			BigDecimal addNinePlanPrdNum = busyStock.getAddNinePlanPrdNum();
			BigDecimal addNinePlanDlvNum = busyStock.getAddNinePlanDlvNum();
			BigDecimal addNinePlanEndStock = busyStock.getAddNinePlanEndStock();
			
			BigDecimal addTenPlanPrdNum = busyStock.getAddTenPlanPrdNum();
			BigDecimal addTenPlanDlvNum = busyStock.getAddTenPlanDlvNum();
			BigDecimal addTenPlanEndStock = busyStock.getAddTenPlanEndStock();
			
			BigDecimal addElevenPlanPrdNum = busyStock.getAddElevenPlanPrdNum();
			BigDecimal addElevenPlanDlvNum = busyStock.getAddElevenPlanDlvNum();
			BigDecimal addElevenPlanEndStock = busyStock.getAddElevenPlanEndStock();
			
			BigDecimal addTwelvePlanPrdNum =busyStock.getAddTwelvePlanPrdNum();
			BigDecimal addTwelvePlanDlvNum = busyStock.getAddTwelvePlanDlvNum();
			BigDecimal addTwelvePlanEndStock = busyStock.getAddTwelvePlanEndStock();
			
			if(!itemCode.equals(tempItemCode)){
				BusyStock subBusyStock=new BusyStock();

				subBusyStock.setItemName(tempItemName);
				if(StringUtils.isEmpty(tempItemName)){
					subBusyStock.setMateDesc("未知类别合计"); 
					subBusyStock.setItemName("未知类别"); 
				}else{
					subBusyStock.setItemName(tempItemName);
					subBusyStock.setMateDesc(tempItemName+"合计"); 
				}
				subBusyStock.setBeginStock(subBeginStock);
				subBusyStock.setBeginOrder(subBeginOrder);
				subBusyStock.setBeginEnableOrder(subBeginEnableOrder);
				subBusyStock.setPlanPrdNum(subPlanPrdNum);
				subBusyStock.setPlanDlvNum(subPlanDlvNum);
				subBusyStock.setEndStock(subEndStock);
				
				subBusyStock.setAddOnePlanPrdNum(subAddOnePlanPrdNum);
				subBusyStock.setAddOnePlanDlvNum(subAddOnePlanDlvNum);
				subBusyStock.setAddOnePlanEndStock(subAddOnePlanEndStock);
				
				subBusyStock.setAddTwoPlanPrdNum(subAddTwoPlanPrdNum);
				subBusyStock.setAddTwoPlanDlvNum(subAddTwoPlanDlvNum);
				subBusyStock.setAddTwoPlanEndStock(subAddTwoPlanEndStock);
				
				subBusyStock.setAddThreePlanPrdNum(subAddThreePlanPrdNum);
				subBusyStock.setAddThreePlanDlvNum(subAddThreePlanDlvNum);
				subBusyStock.setAddThreePlanEndStock(subAddThreePlanEndStock);
				
				subBusyStock.setAddFourPlanPrdNum(subAddFourPlanPrdNum);
				subBusyStock.setAddFourPlanDlvNum(subAddFourPlanDlvNum);
				subBusyStock.setAddFourPlanEndStock(subAddFourPlanEndStock);
				
				subBusyStock.setAddFivePlanPrdNum(subAddFivePlanPrdNum);
				subBusyStock.setAddFivePlanDlvNum(subAddFivePlanDlvNum);
				subBusyStock.setAddFivePlanEndStock(subAddFivePlanEndStock);
				
				subBusyStock.setAddSixPlanPrdNum(subAddSixPlanPrdNum);
				subBusyStock.setAddSixPlanDlvNum(subAddSixPlanDlvNum);
				subBusyStock.setAddSixPlanEndStock(subAddSixPlanEndStock);
				
				subBusyStock.setAddSevenPlanPrdNum(subAddSevenPlanPrdNum);
				subBusyStock.setAddSevenPlanDlvNum(subAddSevenPlanDlvNum);
				subBusyStock.setAddSevenPlanEndStock(subAddSevenPlanEndStock);
				
				subBusyStock.setAddEightPlanPrdNum(subAddEightPlanPrdNum);
				subBusyStock.setAddEightPlanDlvNum(subAddEightPlanDlvNum);
				subBusyStock.setAddEightPlanEndStock(subAddEightPlanEndStock);
				
				subBusyStock.setAddNinePlanPrdNum(subAddNinePlanPrdNum);
				subBusyStock.setAddNinePlanDlvNum(subAddNinePlanDlvNum);
				subBusyStock.setAddNinePlanEndStock(subAddNinePlanEndStock);
				
				subBusyStock.setAddTenPlanPrdNum(subAddTenPlanPrdNum);
				subBusyStock.setAddTenPlanDlvNum(subAddTenPlanDlvNum);
				subBusyStock.setAddTenPlanEndStock(subAddTenPlanEndStock);
				
				subBusyStock.setAddElevenPlanPrdNum(subAddElevenPlanPrdNum);
				subBusyStock.setAddElevenPlanDlvNum(subAddElevenPlanDlvNum);
				subBusyStock.setAddElevenPlanEndStock(subAddElevenPlanEndStock);
				
				subBusyStock.setAddTwelvePlanPrdNum(subAddTwelvePlanPrdNum);
				subBusyStock.setAddTwelvePlanDlvNum(subAddTwelvePlanDlvNum);
				subBusyStock.setAddTwelvePlanEndStock(subAddTwelvePlanEndStock);						
				list.add(subBusyStock);
				//从新设置值
				 tempItemCode=itemCode;
				 tempItemName=itemName;
				 subPlanPrdNum = bg0;
				 subPlanDlvNum = bg0;
				 subBeginOrder = bg0;
				 subBeginStock = bg0;
				 subBeginEnableOrder = bg0;
				 subEndStock = bg0;
				 subAddOnePlanPrdNum = bg0;
				 subAddOnePlanDlvNum = bg0;
				 subAddOnePlanEndStock = bg0;
				 subAddTwoPlanPrdNum = bg0;
				 subAddTwoPlanDlvNum = bg0;
				 subAddTwoPlanEndStock = bg0;
				 subAddThreePlanPrdNum = bg0;
				 subAddThreePlanDlvNum = bg0;
				 subAddThreePlanEndStock = bg0;
				 subAddFourPlanPrdNum = bg0;
				 subAddFourPlanDlvNum = bg0;
				 subAddFourPlanEndStock = bg0;
				 subAddFivePlanPrdNum = bg0;
				 subAddFivePlanDlvNum = bg0;
				 subAddFivePlanEndStock = bg0;
				 subAddSixPlanPrdNum = bg0;
				 subAddSixPlanDlvNum = bg0;
				 subAddSixPlanEndStock = bg0;
				 subAddSevenPlanPrdNum = bg0;
				 subAddSevenPlanDlvNum = bg0;
				 subAddSevenPlanEndStock = bg0;
				 subAddEightPlanPrdNum = bg0;
				 subAddEightPlanDlvNum = bg0;
				 subAddEightPlanEndStock = bg0;
				 subAddNinePlanPrdNum = bg0;
				 subAddNinePlanDlvNum = bg0;
				 subAddNinePlanEndStock = bg0;
				 subAddTenPlanPrdNum = bg0;
				 subAddTenPlanDlvNum = bg0;
				 subAddTenPlanEndStock = bg0;
				 subAddElevenPlanPrdNum = bg0;
				 subAddElevenPlanDlvNum = bg0;
				 subAddElevenPlanEndStock = bg0;
				 subAddTwelvePlanPrdNum = bg0;
				 subAddTwelvePlanDlvNum = bg0;
				 subAddTwelvePlanEndStock = bg0;
			}
			list.add(busyStock);
			subBeginStock=BigDecimalUtil.add(subBeginStock, beginStock);
			subBeginOrder=BigDecimalUtil.add(subBeginOrder, beginOrder);
			subBeginEnableOrder=BigDecimalUtil.add(subBeginEnableOrder, beginEnableOrder);
			subPlanPrdNum=BigDecimalUtil.add(subPlanPrdNum, planPrdNum);
			subPlanDlvNum=BigDecimalUtil.add(subPlanDlvNum, planDlvNum);
			subEndStock=BigDecimalUtil.add(subEndStock, endStock);
			subBeginStock=BigDecimalUtil.add(subBeginStock, beginStock);
			
			subAddOnePlanPrdNum=BigDecimalUtil.add(subAddOnePlanPrdNum, addOnePlanPrdNum);
			subAddOnePlanDlvNum=BigDecimalUtil.add(subAddOnePlanDlvNum, addOnePlanDlvNum);
			subAddOnePlanEndStock=BigDecimalUtil.add(subAddOnePlanEndStock,addOnePlanEndStock);
			
			subAddTwoPlanPrdNum=BigDecimalUtil.add(subAddTwoPlanPrdNum, addTwoPlanPrdNum);
			subAddTwoPlanDlvNum=BigDecimalUtil.add(subAddTwoPlanDlvNum, addTwoPlanDlvNum);
			subAddTwoPlanEndStock=BigDecimalUtil.add(subAddTwoPlanEndStock,addTwoPlanEndStock);
			
			subAddThreePlanPrdNum=BigDecimalUtil.add(subAddThreePlanPrdNum, addThreePlanPrdNum);
			subAddThreePlanDlvNum=BigDecimalUtil.add(subAddThreePlanDlvNum, addThreePlanDlvNum);
			subAddThreePlanEndStock=BigDecimalUtil.add(subAddThreePlanEndStock,addThreePlanEndStock);
			
			subAddFourPlanPrdNum=BigDecimalUtil.add(subAddFourPlanPrdNum, addFourPlanPrdNum);
			subAddFourPlanDlvNum=BigDecimalUtil.add(subAddFourPlanDlvNum, addFourPlanDlvNum);
			subAddFourPlanEndStock=BigDecimalUtil.add(subAddFourPlanEndStock,addFourPlanEndStock);
			
			subAddFivePlanPrdNum=BigDecimalUtil.add(subAddFivePlanPrdNum, addFivePlanPrdNum);
			subAddFivePlanDlvNum=BigDecimalUtil.add(subAddFivePlanDlvNum, addFivePlanDlvNum);
			subAddFivePlanEndStock=BigDecimalUtil.add(subAddFivePlanEndStock,addFivePlanEndStock);
			
			subAddSixPlanPrdNum=BigDecimalUtil.add(subAddSixPlanPrdNum, addSixPlanPrdNum);
			subAddSixPlanDlvNum=BigDecimalUtil.add(subAddSixPlanDlvNum, addSixPlanDlvNum);
			subAddSixPlanEndStock=BigDecimalUtil.add(subAddSixPlanEndStock,addSixPlanEndStock);
			
			subAddSevenPlanPrdNum=BigDecimalUtil.add(subAddSevenPlanPrdNum, addSevenPlanPrdNum);
			subAddSevenPlanDlvNum=BigDecimalUtil.add(subAddSevenPlanDlvNum, addSevenPlanDlvNum);
			subAddSevenPlanEndStock=BigDecimalUtil.add(subAddSevenPlanEndStock,addSevenPlanEndStock);
			
			subAddEightPlanPrdNum=BigDecimalUtil.add(subAddEightPlanPrdNum, addEightPlanPrdNum);
			subAddEightPlanDlvNum=BigDecimalUtil.add(subAddEightPlanDlvNum, addEightPlanDlvNum);
			subAddEightPlanEndStock=BigDecimalUtil.add(subAddEightPlanEndStock,addEightPlanEndStock);
			
			subAddNinePlanPrdNum=BigDecimalUtil.add(subAddNinePlanPrdNum, addNinePlanPrdNum);
			subAddNinePlanDlvNum=BigDecimalUtil.add(subAddNinePlanDlvNum, addNinePlanDlvNum);
			subAddNinePlanEndStock=BigDecimalUtil.add(subAddNinePlanEndStock,addNinePlanEndStock);
			
			subAddTenPlanPrdNum=BigDecimalUtil.add(subAddTenPlanPrdNum, addTenPlanPrdNum);
			subAddTenPlanDlvNum=BigDecimalUtil.add(subAddTenPlanDlvNum, addTenPlanDlvNum);
			subAddTenPlanEndStock=BigDecimalUtil.add(subAddTenPlanEndStock,addTenPlanEndStock);
			
			subAddElevenPlanPrdNum=BigDecimalUtil.add(subAddElevenPlanPrdNum, addElevenPlanPrdNum);
			subAddElevenPlanDlvNum=BigDecimalUtil.add(subAddElevenPlanDlvNum, addElevenPlanDlvNum);
			subAddElevenPlanEndStock=BigDecimalUtil.add(subAddElevenPlanEndStock,addElevenPlanEndStock);
			
			subAddTwelvePlanPrdNum=BigDecimalUtil.add(subAddTwelvePlanPrdNum, addTwelvePlanPrdNum);
			subAddTwelvePlanDlvNum=BigDecimalUtil.add(subAddTwelvePlanDlvNum, addTwelvePlanDlvNum);
			subAddTwelvePlanEndStock=BigDecimalUtil.add(subAddTwelvePlanEndStock,addTwelvePlanEndStock);
			if(i==size-1){
				BusyStock subBusyStock=new BusyStock();
				subBusyStock.setItemCode(tempItemCode);
				if(StringUtils.isEmpty(tempItemName)){
					subBusyStock.setMateDesc("未知类别合计"); 
					subBusyStock.setItemName("未知类别"); 
				}else{
					subBusyStock.setItemName(tempItemName);
					subBusyStock.setMateDesc(tempItemName+"合计"); 
				}
				subBusyStock.setBeginStock(subBeginStock);
				subBusyStock.setBeginOrder(subBeginOrder);
				subBusyStock.setBeginEnableOrder(subBeginEnableOrder);
				subBusyStock.setPlanPrdNum(subPlanPrdNum);
				subBusyStock.setPlanDlvNum(subPlanDlvNum);
				subBusyStock.setEndStock(subEndStock);
				
				subBusyStock.setAddOnePlanPrdNum(subAddOnePlanPrdNum);
				subBusyStock.setAddOnePlanDlvNum(subAddOnePlanDlvNum);
				subBusyStock.setAddOnePlanEndStock(subAddOnePlanEndStock);
				
				subBusyStock.setAddTwoPlanPrdNum(subAddTwoPlanPrdNum);
				subBusyStock.setAddTwoPlanDlvNum(subAddTwoPlanDlvNum);
				subBusyStock.setAddTwoPlanEndStock(subAddTwoPlanEndStock);
				
				subBusyStock.setAddThreePlanPrdNum(subAddThreePlanPrdNum);
				subBusyStock.setAddThreePlanDlvNum(subAddThreePlanDlvNum);
				subBusyStock.setAddThreePlanEndStock(subAddThreePlanEndStock);
				
				subBusyStock.setAddFourPlanPrdNum(subAddFourPlanPrdNum);
				subBusyStock.setAddFourPlanDlvNum(subAddFourPlanDlvNum);
				subBusyStock.setAddFourPlanEndStock(subAddFourPlanEndStock);
				
				subBusyStock.setAddFivePlanPrdNum(subAddFivePlanPrdNum);
				subBusyStock.setAddFivePlanDlvNum(subAddFivePlanDlvNum);
				subBusyStock.setAddFivePlanEndStock(subAddFivePlanEndStock);
				
				subBusyStock.setAddSixPlanPrdNum(subAddSixPlanPrdNum);
				subBusyStock.setAddSixPlanDlvNum(subAddSixPlanDlvNum);
				subBusyStock.setAddSixPlanEndStock(subAddSixPlanEndStock);
				
				subBusyStock.setAddSevenPlanPrdNum(subAddSevenPlanPrdNum);
				subBusyStock.setAddSevenPlanDlvNum(subAddSevenPlanDlvNum);
				subBusyStock.setAddSevenPlanEndStock(subAddSevenPlanEndStock);
				
				subBusyStock.setAddEightPlanPrdNum(subAddEightPlanPrdNum);
				subBusyStock.setAddEightPlanDlvNum(subAddEightPlanDlvNum);
				subBusyStock.setAddEightPlanEndStock(subAddEightPlanEndStock);
				
				subBusyStock.setAddNinePlanPrdNum(subAddNinePlanPrdNum);
				subBusyStock.setAddNinePlanDlvNum(subAddNinePlanDlvNum);
				subBusyStock.setAddNinePlanEndStock(subAddNinePlanEndStock);
				
				subBusyStock.setAddTenPlanPrdNum(subAddTenPlanPrdNum);
				subBusyStock.setAddTenPlanDlvNum(subAddTenPlanDlvNum);
				subBusyStock.setAddTenPlanEndStock(subAddTenPlanEndStock);
				
				subBusyStock.setAddElevenPlanPrdNum(subAddElevenPlanPrdNum);
				subBusyStock.setAddElevenPlanDlvNum(subAddElevenPlanDlvNum);
				subBusyStock.setAddElevenPlanEndStock(subAddElevenPlanEndStock);
				
				subBusyStock.setAddTwelvePlanPrdNum(subAddTwelvePlanPrdNum);
				subBusyStock.setAddTwelvePlanDlvNum(subAddTwelvePlanDlvNum);
				subBusyStock.setAddTwelvePlanEndStock(subAddTwelvePlanEndStock);
				list.add(subBusyStock);
			}
		}
		return list;
	}
	@Override
	public List<QualSupp> getSuppByMateMonth(Map<String, Object> map) {
		return actReachMapper.getSuppByMateMonth(map);
	}

	@Override
	public List<PdrDetailVo> getPrdDetailByMap(Map<String, Object> map) {
		return actReachMapper.getPrdDetailByMap(map);
	}

	@Override
	public List<InnerControl> getSuppSumInnerControl(List<InnerControl> oldList) {
		List<InnerControl> list=new ArrayList<>();
		for (InnerControl innerControl : oldList) {
			list.add(innerControl);
		}
		//按照供应商进行排序
		list.sort(new Comparator<InnerControl>() {
			@Override
			public int compare(InnerControl o1, InnerControl o2) {
				String suppNo1 = o1.getSuppNo();
				String suppNo2 = o2.getSuppNo();
				if(suppNo1.compareTo(suppNo2)>0){
					return 1;
				}else if(suppNo1.compareTo(suppNo2)<0){
					return -1;
				}
				return 0;
			}
		});
		//获取供应商合计数据
		List<InnerControl> suppSubControl=new ArrayList<>();
		if(list==null || list.size()==0 ){
			return suppSubControl;
		}
		String tempSuppNo = list.get(0).getSuppNo();
		String tempSuppName = list.get(0).getSuppName();

		BigDecimal bg0=new BigDecimal(0);
		
		BigDecimal subBeginOrder = bg0;
		BigDecimal subBeginStock = bg0;
		BigDecimal subBeginEnableOrder = bg0;
		
		BigDecimal subPlanPrdNum = bg0;
		BigDecimal subActPrdNum = bg0;
		BigDecimal subPlanDlvNum = bg0;
		BigDecimal subActDlvNum = bg0;
		BigDecimal subSuppActNum=bg0;
		
		BigDecimal subEndStock=bg0;
		
		BigDecimal subAddOnePlanPrdNum=bg0;
		BigDecimal subAddOnePlanDlvNum=bg0;
		BigDecimal subAddOnePlanEndStock=bg0;
				
		BigDecimal subAddTwoPlanPrdNum=bg0;
		BigDecimal subAddTwoPlanDlvNum=bg0;
		BigDecimal subAddTwoPlanEndStock=bg0;
	
		
		BigDecimal subAddThreePlanPrdNum=bg0;
		BigDecimal subAddThreePlanDlvNum=bg0;
		BigDecimal subAddThreePlanEndStock=bg0;
			
		BigDecimal subAddFourPlanPrdNum=bg0;
		BigDecimal subAddFourPlanDlvNum=bg0;
		BigDecimal subAddFourPlanEndStock=bg0;
	
		
		BigDecimal subAddFivePlanPrdNum=bg0;
		BigDecimal subAddFivePlanDlvNum=bg0;
		BigDecimal subAddFivePlanEndStock=bg0;
		
		BigDecimal subAddSixPlanPrdNum=bg0;
		BigDecimal subAddSixPlanDlvNum=bg0;
	    BigDecimal subAddSixPlanEndStock=bg0;
		
		BigDecimal subAddSevenPlanPrdNum=bg0;
		BigDecimal subAddSevenPlanDlvNum=bg0;
		BigDecimal subAddSevenPlanEndStock=bg0;
		
		BigDecimal subAddEightPlanPrdNum=bg0;
		BigDecimal subAddEightPlanDlvNum=bg0;
		BigDecimal subAddEightPlanEndStock=bg0;
		
		
		BigDecimal subAddNinePlanPrdNum=bg0;
		BigDecimal subAddNinePlanDlvNum=bg0;
		BigDecimal subAddNinePlanEndStock=bg0;
		
		BigDecimal subAddTenPlanPrdNum=bg0;
		BigDecimal subAddTenPlanDlvNum=bg0;
		BigDecimal subAddTenPlanEndStock=bg0;
		
		BigDecimal subAddElevenPlanPrdNum=bg0;
		BigDecimal subAddElevenPlanDlvNum=bg0;
		BigDecimal subAddElevenPlanEndStock=bg0;
		
		BigDecimal subAddTwelvePlanPrdNum=bg0;
		BigDecimal subAddTwelvePlanDlvNum=bg0;
		BigDecimal subAddTwelvePlanEndStock=bg0;
		
		BigDecimal subAddThirteenPlanDlvNum=bg0;
		
		for (int i=0;i<list.size();i++) {
			InnerControl innerControl = list.get(i);
			
			String suppNo = innerControl.getSuppNo();

			BigDecimal beginOrder = innerControl.getBeginOrder();
			BigDecimal beginStock = innerControl.getBeginStock();
			BigDecimal beginEnableOrder = innerControl.getBeginEnableOrder();
			
			BigDecimal planPrdNum = innerControl.getPlanPrdNum();
			BigDecimal actPrdNum = innerControl.getActPrdNum();
			BigDecimal planDlvNum = innerControl.getPlanDlvNum();
			BigDecimal actDlvNum = innerControl.getActDlvNum();
			BigDecimal suppActNum = innerControl.getSuppActNum();
			BigDecimal endStock = innerControl.getEndStock();
			
			BigDecimal addOnePlanPrdNum = innerControl.getAddOnePlanPrdNum();
			BigDecimal addOnePlanDlvNum = innerControl.getAddOnePlanDlvNum();
			BigDecimal addOnePlanEndStock = innerControl.getAddOnePlanEndStock();
			
			BigDecimal addTwoPlanPrdNum = innerControl.getAddTwoPlanPrdNum();
			BigDecimal addTwoPlanDlvNum = innerControl.getAddTwoPlanDlvNum();
			BigDecimal addTwoPlanEndStock = innerControl.getAddTwoPlanEndStock();
			
			BigDecimal addThreePlanPrdNum = innerControl.getAddThreePlanPrdNum();
			BigDecimal addThreePlanDlvNum = innerControl.getAddThreePlanDlvNum();
			BigDecimal addThreePlanEndStock = innerControl.getAddThreePlanEndStock();
			
			BigDecimal addFourPlanPrdNum = innerControl.getAddFourPlanPrdNum();
			BigDecimal addFourPlanDlvNum = innerControl.getAddFourPlanDlvNum();
			BigDecimal addFourPlanEndStock = innerControl.getAddFourPlanEndStock();
			
			BigDecimal addFivePlanPrdNum = innerControl.getAddFivePlanPrdNum();
			BigDecimal addFivePlanDlvNum = innerControl.getAddFivePlanDlvNum();
			BigDecimal addFivePlanEndStock = innerControl.getAddFivePlanEndStock();
			
			BigDecimal addSixPlanPrdNum = innerControl.getAddSixPlanPrdNum();
			BigDecimal addSixPlanDlvNum = innerControl.getAddSixPlanDlvNum();
			BigDecimal addSixPlanEndStock = innerControl.getAddSixPlanEndStock();
			
			BigDecimal addSevenPlanPrdNum = innerControl.getAddSevenPlanPrdNum();
			BigDecimal addSevenPlanDlvNum = innerControl.getAddSevenPlanDlvNum();
			BigDecimal addSevenPlanEndStock = innerControl.getAddSevenPlanEndStock();
			
			BigDecimal addEightPlanPrdNum = innerControl.getAddEightPlanPrdNum();
			BigDecimal addEightPlanDlvNum = innerControl.getAddEightPlanDlvNum();
			BigDecimal addEightPlanEndStock = innerControl.getAddEightPlanEndStock();
			
			BigDecimal addNinePlanPrdNum = innerControl.getAddNinePlanPrdNum();
			BigDecimal addNinePlanDlvNum = innerControl.getAddNinePlanDlvNum();
			BigDecimal addNinePlanEndStock = innerControl.getAddNinePlanEndStock();
			
			BigDecimal addTenPlanPrdNum = innerControl.getAddTenPlanPrdNum();
			BigDecimal addTenPlanDlvNum = innerControl.getAddTenPlanDlvNum();
			BigDecimal addTenPlanEndStock = innerControl.getAddTenPlanEndStock();
			
			BigDecimal addElevenPlanPrdNum = innerControl.getAddElevenPlanPrdNum();
			BigDecimal addElevenPlanDlvNum = innerControl.getAddElevenPlanDlvNum();
			BigDecimal addElevenPlanEndStock = innerControl.getAddElevenPlanEndStock();
			
			BigDecimal addTwelvePlanPrdNum = innerControl.getAddTwelvePlanPrdNum();
			BigDecimal addTwelvePlanDlvNum = innerControl.getAddTwelvePlanDlvNum();
			BigDecimal addTwelvePlanEndStock = innerControl.getAddTwelvePlanEndStock();
			
			BigDecimal addThirteenPlanDlvNum = innerControl.getAddThirteenPlanDlvNum();

			
			if(!suppNo.equals(tempSuppNo)){
				InnerControl subInnerControl=new InnerControl();
				subInnerControl.setSuppNo(tempSuppNo);
				subInnerControl.setSuppName(tempSuppName);
				subInnerControl.setBeginOrder(subBeginOrder);
				subInnerControl.setBeginStock(subBeginStock);
				subInnerControl.setBeginEnableOrder(subBeginEnableOrder);
				subInnerControl.setPlanPrdNum(subPlanPrdNum);
				subInnerControl.setActPrdNum(subActPrdNum);
				
				subInnerControl.setPlanDlvNum(subPlanDlvNum);
				subInnerControl.setActDlvNum(subActDlvNum);
				
				subInnerControl.setSuppActNum(subSuppActNum);
				subInnerControl.setEndStock(subEndStock);
				
				subInnerControl.setAddOnePlanPrdNum(subAddOnePlanPrdNum);
				subInnerControl.setAddOnePlanDlvNum(subAddOnePlanDlvNum);
				subInnerControl.setAddOnePlanEndStock(subAddOnePlanEndStock);
				
				subInnerControl.setAddTwoPlanPrdNum(subAddTwoPlanPrdNum);
				subInnerControl.setAddTwoPlanDlvNum(subAddTwoPlanDlvNum);
				subInnerControl.setAddTwoPlanEndStock(subAddTwoPlanEndStock);
				
				subInnerControl.setAddThreePlanPrdNum(subAddThreePlanPrdNum);
				subInnerControl.setAddThreePlanDlvNum(subAddThreePlanDlvNum);
				subInnerControl.setAddThreePlanEndStock(subAddThreePlanEndStock);
				
				subInnerControl.setAddFourPlanPrdNum(subAddFourPlanPrdNum);
				subInnerControl.setAddFourPlanDlvNum(subAddFourPlanDlvNum);
				subInnerControl.setAddFourPlanEndStock(subAddFourPlanEndStock);
				
				subInnerControl.setAddFivePlanPrdNum(subAddFivePlanPrdNum);
				subInnerControl.setAddFivePlanDlvNum(subAddFivePlanDlvNum);
				subInnerControl.setAddFivePlanEndStock(subAddFivePlanEndStock);
				
				subInnerControl.setAddSixPlanPrdNum(subAddSixPlanPrdNum);
				subInnerControl.setAddSixPlanDlvNum(subAddSixPlanDlvNum);
				subInnerControl.setAddSixPlanEndStock(subAddSixPlanEndStock);
				
				subInnerControl.setAddSevenPlanPrdNum(subAddSevenPlanPrdNum);
				subInnerControl.setAddSevenPlanDlvNum(subAddSevenPlanDlvNum);
				subInnerControl.setAddSevenPlanEndStock(subAddSevenPlanEndStock);
				
				subInnerControl.setAddEightPlanPrdNum(subAddEightPlanPrdNum);
				subInnerControl.setAddEightPlanDlvNum(subAddEightPlanDlvNum);
				subInnerControl.setAddEightPlanEndStock(subAddEightPlanEndStock);
				
				subInnerControl.setAddNinePlanPrdNum(subAddNinePlanPrdNum);
				subInnerControl.setAddNinePlanDlvNum(subAddNinePlanDlvNum);
				subInnerControl.setAddNinePlanEndStock(subAddNinePlanEndStock);
				
				subInnerControl.setAddTenPlanPrdNum(subAddTenPlanPrdNum);
				subInnerControl.setAddTenPlanDlvNum(subAddTenPlanDlvNum);
				subInnerControl.setAddTenPlanEndStock(subAddTenPlanEndStock);
				
				subInnerControl.setAddElevenPlanPrdNum(subAddElevenPlanPrdNum);
				subInnerControl.setAddElevenPlanDlvNum(subAddElevenPlanDlvNum);
				subInnerControl.setAddElevenPlanEndStock(subAddElevenPlanEndStock);
				
				subInnerControl.setAddTwelvePlanPrdNum(subAddTwelvePlanPrdNum);
				subInnerControl.setAddTwelvePlanDlvNum(subAddTwelvePlanDlvNum);
				subInnerControl.setAddTwelvePlanEndStock(subAddTwelvePlanEndStock);
				
				subInnerControl.setAddThirteenPlanDlvNum(subAddThirteenPlanDlvNum);

				suppSubControl.add(subInnerControl);
				//从新设置值
				 tempSuppNo=suppNo;
				 tempSuppName=innerControl.getSuppName();
				 subBeginOrder=bg0;
				 subBeginStock=bg0;
				 subBeginEnableOrder=bg0;
				 subPlanPrdNum = bg0;
				 subActPrdNum = bg0;
				 subPlanDlvNum = bg0;
				 subActDlvNum = bg0;
				 subSuppActNum=bg0;
				 subEndStock=bg0;
					
				 subAddOnePlanPrdNum=bg0;
				 subAddOnePlanDlvNum=bg0;
				 subAddOnePlanEndStock=bg0;
				
				
				 subAddTwoPlanPrdNum=bg0;
				 subAddTwoPlanDlvNum=bg0;
				 subAddTwoPlanEndStock=bg0;
			
				
				 subAddThreePlanPrdNum=bg0;
				 subAddThreePlanDlvNum=bg0;
				 subAddThreePlanEndStock=bg0;
					
				 subAddFourPlanPrdNum=bg0;
				 subAddFourPlanDlvNum=bg0;
				 subAddFourPlanEndStock=bg0;
			
				
				 subAddFivePlanPrdNum=bg0;
				 subAddFivePlanDlvNum=bg0;
				 subAddFivePlanEndStock=bg0;
				
				 subAddSixPlanPrdNum=bg0;
				 subAddSixPlanDlvNum=bg0;
			     subAddSixPlanEndStock=bg0;
				
				 subAddSevenPlanPrdNum=bg0;
				 subAddSevenPlanDlvNum=bg0;
				 subAddSevenPlanEndStock=bg0;
				
				 subAddEightPlanPrdNum=bg0;
				 subAddEightPlanDlvNum=bg0;
				 subAddEightPlanEndStock=bg0;
				
				
				 subAddNinePlanPrdNum=bg0;
				 subAddNinePlanDlvNum=bg0;
				 subAddNinePlanEndStock=bg0;
				
				 subAddTenPlanPrdNum=bg0;
				 subAddTenPlanDlvNum=bg0;
				 subAddTenPlanEndStock=bg0;
				
				 subAddElevenPlanPrdNum=bg0;
				 subAddElevenPlanDlvNum=bg0;
				 subAddElevenPlanEndStock=bg0;
				
				 subAddTwelvePlanPrdNum=bg0;
				 subAddTwelvePlanDlvNum=bg0;
				 subAddTwelvePlanEndStock=bg0;
				
				 subAddThirteenPlanDlvNum=bg0;

			}			
			subBeginOrder=BigDecimalUtil.add(subBeginOrder, beginOrder);
			subBeginStock=BigDecimalUtil.add(subBeginStock, beginStock);
			subBeginEnableOrder=BigDecimalUtil.add(subBeginEnableOrder, beginEnableOrder);
			subPlanPrdNum=BigDecimalUtil.add(subPlanPrdNum, planPrdNum);
			subActPrdNum=BigDecimalUtil.add(subActPrdNum, actPrdNum);
			subPlanDlvNum=BigDecimalUtil.add(subPlanDlvNum, planDlvNum);
			subActDlvNum=BigDecimalUtil.add(subActDlvNum, actDlvNum);
			
			subSuppActNum=BigDecimalUtil.add(subSuppActNum, suppActNum);
			subEndStock=BigDecimalUtil.add(subEndStock, endStock);

			subAddOnePlanPrdNum=BigDecimalUtil.add(subAddOnePlanPrdNum, addOnePlanPrdNum);
			subAddOnePlanDlvNum=BigDecimalUtil.add(subAddOnePlanDlvNum, addOnePlanDlvNum);
			subAddOnePlanEndStock=BigDecimalUtil.add(subAddOnePlanEndStock, addOnePlanEndStock);

			subAddTwoPlanPrdNum=BigDecimalUtil.add(subAddTwoPlanPrdNum, addTwoPlanPrdNum);
			subAddTwoPlanDlvNum=BigDecimalUtil.add(subAddTwoPlanDlvNum, addTwoPlanDlvNum);
			subAddTwoPlanEndStock=BigDecimalUtil.add(subAddTwoPlanEndStock, addTwoPlanEndStock);

			subAddThreePlanPrdNum=BigDecimalUtil.add(subAddThreePlanPrdNum, addThreePlanPrdNum);
			subAddThreePlanDlvNum=BigDecimalUtil.add(subAddThreePlanDlvNum, addThreePlanDlvNum);
			subAddThreePlanEndStock=BigDecimalUtil.add(subAddThreePlanEndStock, addThreePlanEndStock);

			subAddFourPlanPrdNum=BigDecimalUtil.add(subAddFourPlanPrdNum, addFourPlanPrdNum);
			subAddFourPlanDlvNum=BigDecimalUtil.add(subAddFourPlanDlvNum, addFourPlanDlvNum);
			subAddFourPlanEndStock=BigDecimalUtil.add(subAddFourPlanEndStock, addFourPlanEndStock);

			subAddFivePlanPrdNum=BigDecimalUtil.add(subAddFivePlanPrdNum, addFivePlanPrdNum);
			subAddFivePlanDlvNum=BigDecimalUtil.add(subAddFivePlanDlvNum, addFivePlanDlvNum);
			subAddFivePlanEndStock=BigDecimalUtil.add(subAddFivePlanEndStock, addFivePlanEndStock);

			subAddSixPlanPrdNum=BigDecimalUtil.add(subAddSixPlanPrdNum, addSixPlanPrdNum);
			subAddSixPlanDlvNum=BigDecimalUtil.add(subAddSixPlanDlvNum, addSixPlanDlvNum);
			subAddSixPlanEndStock=BigDecimalUtil.add(subAddSixPlanEndStock, addSixPlanEndStock);

			subAddSevenPlanPrdNum=BigDecimalUtil.add(subAddSevenPlanPrdNum, addSevenPlanPrdNum);
			subAddSevenPlanDlvNum=BigDecimalUtil.add(subAddSevenPlanDlvNum, addSevenPlanDlvNum);
			subAddSevenPlanEndStock=BigDecimalUtil.add(subAddSevenPlanEndStock, addSevenPlanEndStock);

			subAddEightPlanPrdNum=BigDecimalUtil.add(subAddEightPlanPrdNum, addEightPlanPrdNum);
			subAddEightPlanDlvNum=BigDecimalUtil.add(subAddEightPlanDlvNum, addEightPlanDlvNum);
			subAddEightPlanEndStock=BigDecimalUtil.add(subAddEightPlanEndStock, addEightPlanEndStock);

			subAddNinePlanPrdNum=BigDecimalUtil.add(subAddNinePlanPrdNum, addNinePlanPrdNum);
			subAddNinePlanDlvNum=BigDecimalUtil.add(subAddNinePlanDlvNum, addNinePlanDlvNum);
			subAddNinePlanEndStock=BigDecimalUtil.add(subAddNinePlanEndStock, addNinePlanEndStock);

			subAddTenPlanPrdNum=BigDecimalUtil.add(subAddTenPlanPrdNum, addTenPlanPrdNum);
			subAddTenPlanDlvNum=BigDecimalUtil.add(subAddTenPlanDlvNum, addTenPlanDlvNum);
			subAddTenPlanEndStock=BigDecimalUtil.add(subAddTenPlanEndStock, addTenPlanEndStock);

			subAddElevenPlanPrdNum=BigDecimalUtil.add(subAddElevenPlanPrdNum, addElevenPlanPrdNum);
			subAddElevenPlanDlvNum=BigDecimalUtil.add(subAddElevenPlanDlvNum, addElevenPlanDlvNum);
			subAddElevenPlanEndStock=BigDecimalUtil.add(subAddElevenPlanEndStock, addElevenPlanEndStock);

			subAddTwelvePlanPrdNum=BigDecimalUtil.add(subAddTwelvePlanPrdNum, addTwelvePlanPrdNum);
			subAddTwelvePlanDlvNum=BigDecimalUtil.add(subAddTwelvePlanDlvNum, addTwelvePlanDlvNum);
			subAddTwelvePlanEndStock=BigDecimalUtil.add(subAddTwelvePlanEndStock, addTwelvePlanEndStock);

			subAddThirteenPlanDlvNum=BigDecimalUtil.add(subAddThirteenPlanDlvNum, addThirteenPlanDlvNum);
			
			if(i==list.size()-1){
				InnerControl subInnerControl=new InnerControl();
				subInnerControl.setSuppNo(tempSuppNo);
				subInnerControl.setSuppName(tempSuppName);
				subInnerControl.setBeginOrder(subBeginOrder);
				subInnerControl.setBeginStock(subBeginStock);
				subInnerControl.setBeginEnableOrder(subBeginEnableOrder);
				subInnerControl.setPlanPrdNum(subPlanPrdNum);
				subInnerControl.setActPrdNum(subActPrdNum);
				
				subInnerControl.setPlanDlvNum(subPlanDlvNum);
				subInnerControl.setActDlvNum(subActDlvNum);
				
				subInnerControl.setSuppActNum(subSuppActNum);
				subInnerControl.setEndStock(subEndStock);
				
				subInnerControl.setAddOnePlanPrdNum(subAddOnePlanPrdNum);
				subInnerControl.setAddOnePlanDlvNum(subAddOnePlanDlvNum);
				subInnerControl.setAddOnePlanEndStock(subAddOnePlanEndStock);
				
				subInnerControl.setAddTwoPlanPrdNum(subAddTwoPlanPrdNum);
				subInnerControl.setAddTwoPlanDlvNum(subAddTwoPlanDlvNum);
				subInnerControl.setAddTwoPlanEndStock(subAddTwoPlanEndStock);
				
				subInnerControl.setAddThreePlanPrdNum(subAddThreePlanPrdNum);
				subInnerControl.setAddThreePlanDlvNum(subAddThreePlanDlvNum);
				subInnerControl.setAddThreePlanEndStock(subAddThreePlanEndStock);
				
				subInnerControl.setAddFourPlanPrdNum(subAddFourPlanPrdNum);
				subInnerControl.setAddFourPlanDlvNum(subAddFourPlanDlvNum);
				subInnerControl.setAddFourPlanEndStock(subAddFourPlanEndStock);
				
				subInnerControl.setAddFivePlanPrdNum(subAddFivePlanPrdNum);
				subInnerControl.setAddFivePlanDlvNum(subAddFivePlanDlvNum);
				subInnerControl.setAddFivePlanEndStock(subAddFivePlanEndStock);
				
				subInnerControl.setAddSixPlanPrdNum(subAddSixPlanPrdNum);
				subInnerControl.setAddSixPlanDlvNum(subAddSixPlanDlvNum);
				subInnerControl.setAddSixPlanEndStock(subAddSixPlanEndStock);
				
				subInnerControl.setAddSevenPlanPrdNum(subAddSevenPlanPrdNum);
				subInnerControl.setAddSevenPlanDlvNum(subAddSevenPlanDlvNum);
				subInnerControl.setAddSevenPlanEndStock(subAddSevenPlanEndStock);
				
				subInnerControl.setAddEightPlanPrdNum(subAddEightPlanPrdNum);
				subInnerControl.setAddEightPlanDlvNum(subAddEightPlanDlvNum);
				subInnerControl.setAddEightPlanEndStock(subAddEightPlanEndStock);
				
				subInnerControl.setAddNinePlanPrdNum(subAddNinePlanPrdNum);
				subInnerControl.setAddNinePlanDlvNum(subAddNinePlanDlvNum);
				subInnerControl.setAddNinePlanEndStock(subAddNinePlanEndStock);
				
				subInnerControl.setAddTenPlanPrdNum(subAddTenPlanPrdNum);
				subInnerControl.setAddTenPlanDlvNum(subAddTenPlanDlvNum);
				subInnerControl.setAddTenPlanEndStock(subAddTenPlanEndStock);
				
				subInnerControl.setAddElevenPlanPrdNum(subAddElevenPlanPrdNum);
				subInnerControl.setAddElevenPlanDlvNum(subAddElevenPlanDlvNum);
				subInnerControl.setAddElevenPlanEndStock(subAddElevenPlanEndStock);
				
				subInnerControl.setAddTwelvePlanPrdNum(subAddTwelvePlanPrdNum);
				subInnerControl.setAddTwelvePlanDlvNum(subAddTwelvePlanDlvNum);
				subInnerControl.setAddTwelvePlanEndStock(subAddTwelvePlanEndStock);
				
				subInnerControl.setAddThirteenPlanDlvNum(subAddThirteenPlanDlvNum);

				suppSubControl.add(subInnerControl);
			}
		}
		calInnerControlSafeScale(suppSubControl);
		return suppSubControl;
	}
}
