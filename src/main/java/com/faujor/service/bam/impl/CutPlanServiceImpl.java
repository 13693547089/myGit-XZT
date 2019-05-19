package com.faujor.service.bam.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.bam.CutLiaisonMapper;
import com.faujor.dao.master.bam.CutPlanMapper;
import com.faujor.dao.sapcenter.bam.SapCutPlanMapper;
import com.faujor.entity.bam.CutLiaiField;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.bam.CutLiaisonVO;
import com.faujor.entity.bam.CutPlan;
import com.faujor.entity.bam.CutPlanMate;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.bam.CutLiaisonService;
import com.faujor.service.bam.CutPlanService;
import com.faujor.service.bam.SaleFcstService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

import oracle.sql.BLOB;

@Service
public class CutPlanServiceImpl  implements CutPlanService{
	@Autowired
	private CutPlanMapper cutPlanMapper;
	@Autowired
	private SapCutPlanMapper sapCutPlanMapper;
	@Autowired
	private CutLiaisonMapper cutLiaisonMapper;
	@Autowired
	private SaleFcstService saleFcstService;
	@Autowired
	private CutLiaisonService cutLiaisonService;
	@Override
	public LayuiPage<CutPlan> getCutPlanByPage(Map<String, Object> map) {
		LayuiPage<CutPlan> page=new LayuiPage<CutPlan>();
		List<CutPlan> data = cutPlanMapper.getCutPlanByPage(map);
		int count = cutPlanMapper.getCutPlanNum(map);
		page.setData(data);
		page.setCount(count);
		return page;
	}

	@Override
	@Transactional
	public void saveCutPlan(CutPlan cutPlan, List<CutPlanMate> mates) {
		SysUserDO user = UserCommon.getUser();
		cutPlan.setCreateId(user.getUserId()+"");
		cutPlan.setCreater(user.getName());
		cutPlan.setCreateDate(new Date());
		String planId = UUIDUtil.getUUID();
		cutPlan.setPlanId(planId);
		for (CutPlanMate mate : mates) {
			mate.setPlanMateId(UUIDUtil.getUUID());
			mate.setPlanId(planId);
		}
		cutPlanMapper.saveCutPlan(cutPlan);
		cutPlanMapper.saveCutPlanMates(mates);
	}

	@Override
	@Transactional
	public void udateCutPlan(CutPlan cutPlan, List<CutPlanMate> mates) {
			String planId = cutPlan.getPlanId();
			cutPlanMapper.updateCutPlan(cutPlan);
			cutPlanMapper.delCutPlanMateByPlanId(planId);
			for (CutPlanMate mate : mates) {
				mate.setPlanMateId(UUIDUtil.getUUID());
				mate.setPlanId(planId);
			}
			cutPlanMapper.saveCutPlanMates(mates);
	}

	@Override
	@Transactional
	public void delCutPlan(List<String> ids) {
		for (String planId : ids) {
			cutPlanMapper.delCutPlanById(planId);
			cutPlanMapper.delCutPlanMateByPlanId(planId);
		}
	}

	@Override
	public CutPlan getCutPlanByPlanId(String planId) {
		CutPlan cutPlan = cutPlanMapper.getCutPlanById(planId);
		return cutPlan;
	}

	@Override
	public List<CutPlanMate> getCutPlanMateByPlanId(String planId) {
		List<CutPlanMate> mates = cutPlanMapper.getCutPlanMateByPlanId(planId);
		return mates;
	}

	@Override
	public void changeCutPlanStatus(List<String> planIds, String status) {
		cutPlanMapper.changeCutPlanStatus(planIds, status);
	}

	@Override
	public List<CutPlanMate> getCutPlanMateFromLiaison(String planMonth,String columnName,String saleFcstId) {
		//从数据库中获取数据
		List<CutPlanMate> mates = cutPlanMapper.getCutPlanMateFromLiaison(planMonth,columnName,saleFcstId);

		if(mates==null || mates.size()==0){
			return mates;
		}
		//判断是否需要从新计算主包材数的数据
		mates = isNeedCountMainStruNum(mates,planMonth);
		//计算剩余量和预计替换时间
		List<CutPlanMate> returnMates = calCutPlanMate(mates, planMonth);
		return returnMates;
	}
	
	//判断是否需要从新计算主包材数的数据
	private List<CutPlanMate> isNeedCountMainStruNum(List<CutPlanMate> mates, String planMonth) {
		
		for (CutPlanMate cutPlanMate : mates) {
			String isChange = cutPlanMate.getIsChange();
			if("different".equals(isChange)) {
				//说明打切品中的主包材和打切联络单中的主包材不相同，主包材数量需要重新计算
				String mateId = cutPlanMate.getMateId();//成品物料编码
				String mateVersion = cutPlanMate.getMateVersion();
				if(StringUtils.isEmpty(mateVersion)) {
					mateVersion="QQQ";
				}
				//获取字段和对应数据(一个月份，这个物料可能被多个供应商做打切联络单)
				List<CutLiaisonVO> list = cutLiaisonMapper.getHeaderFieldsAndFieldsByMateCodeAndVersionAndCutMonth(mateId,mateVersion,planMonth);
				//循环把每个打切联络单关于这个物料的主包材数量计算出来，然后累加为total
				BigDecimal zero = new BigDecimal(0);
				BigDecimal total=zero;
				for (CutLiaisonVO vo : list) {
					/*String headerFields = vo.getHeaderFields();
					if(StringUtils.isEmpty(headerFields)) {
						//获取打切联络单物料头部字段信息
						headerFields = getHeaderFieldsFromFieldsBlob(vo.getLiaiId());
					}*/
					JSONArray headerJA = cutLiaisonService.queryLiaiMateFields(vo.getLiaiId());
					Set<CutLiaiField> set  = new HashSet<CutLiaiField>();
					for(int i= 0;i<headerJA.size();i++){
						CutLiaiField field = headerJA.getObject(i, CutLiaiField.class);
						set.add(field);
					}
					String fields = vo.getFields();//获取物料的字段和对应的数据
					String mainStru = cutPlanMate.getMainStru();//获取打切品中物料对应的主包材
					int indexOf = mainStru.indexOf(" ");
					String className = mainStru.substring(indexOf+1);
					String f = className+"合计";
					String field2="";
					for (CutLiaiField c : set) {
						if(f.equals(c.getTitle())){
							field2 = c.getField();
							break;
						}
					}
					JSONArray jj = JSONArray.parseArray(fields);
					if(jj.size()!=0){
						outer:
							for (int i = 0; i < jj.size(); i++) {
								JSONObject object = jj.getJSONObject(i);
								Set<String> keySet = object.keySet();
								for (String str : keySet) {
									if(field2.equals(str)){
										String object2 = (String) object.get(str);
										if(object2==null || "".equals(object2) || "NaN".equals(object2)){
											object2 ="0";
										}
										Double b = Double.parseDouble(object2);//主包材数量
										BigDecimal valueOf = BigDecimal.valueOf(b);
										total = total.add(valueOf);
										break outer;
									}
								}
							}
					}
				}
				//更改主包材的数量
				cutPlanMate.setMainStruNum(total);
			}
		}
		return mates;
	}
	//获取打切联络单物料头部字段信息
	private String getHeaderFieldsFromFieldsBlob(String liaiId) {
		CutLiaison cutLiai = cutLiaisonMapper.queryCutLiaiFieldsBlobByLiaiId(liaiId);
		BLOB fieldsBlob = (BLOB) cutLiai.getFieldsBlob();
		InputStream in = null; 
		try {  
		    in=fieldsBlob.getBinaryStream();  
		} catch (SQLException e) {  
		    e.printStackTrace();  
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
	    byte[] buffer = new byte[8192];
	    int n = 0;
	    try {
			while (-1 != (n = in.read(buffer))) {
			    output.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    byte[] byteArray = output.toByteArray();
	    String headerFields = new String(byteArray);
		return headerFields;
	}

	public List<Map<String, Object>> getForecastMonth(Date date){
		List<Map<String, Object>> monthList=new ArrayList<Map<String,Object>>();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -1);
		for(int i=0;i<6;i++){
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("year", calendar.get(Calendar.YEAR));
			map.put("month", calendar.get(Calendar.MONTH));
			calendar.add(Calendar.MONTH, 1);
			monthList.add(map);
		}
		return monthList;
	}
	
	/**
	 * 计算物料的剩余量和预计替换时间
	 * @param cutPlanMates
	 * @return
	 */
	public List<CutPlanMate> calCutPlanMate(List<CutPlanMate> cutPlanMates,String planMonth){
		  BigDecimal bg0=new BigDecimal(0);
		  Date date = DateUtils.parse(planMonth, "yyyy-MM");
		for (CutPlanMate mate : cutPlanMates) {
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			BigDecimal nowInve = mate.getNowInve();
			BigDecimal outInve = mate.getOutInve();
			if(outInve==null){
				outInve=bg0;
			}
			BigDecimal inveNum = mate.getInveNum();
			if(inveNum==null){
				inveNum=bg0;
			}
			BigDecimal mainStruNum = mate.getMainStruNum();
			if(mainStruNum==null){
				mainStruNum=bg0;
			}
			BigDecimal addOne = mate.getAddOne();
			BigDecimal addTwo = mate.getAddTwo();
			BigDecimal addThree = mate.getAddThree();
			BigDecimal addFour = mate.getAddFour();
			BigDecimal addFive = mate.getAddFive();
			BigDecimal addSix = mate.getAddSix();
			BigDecimal remainNum=nowInve.add(inveNum).add(mainStruNum);
 			/*if(remainNum.compareTo(new BigDecimal(0))<=0){
				mate.setReplaceDate(DateUtils.format(date, "M")+"月");
			}else if(remainNum.compareTo(addOne)<=0){
				mate.setReplaceDate(DateUtils.format(date, "M")+"月");
				remainNum=remainNum.subtract(addOne);
			}else if(remainNum.compareTo(addTwo)<=0){
				calendar.add(Calendar.MONTH, 1);
				mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
				remainNum=remainNum.subtract(addTwo);
			}else if(remainNum.compareTo(addThree)<=0){
				calendar.add(Calendar.MONTH, 2);
				mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
				remainNum=remainNum.subtract(addThree);
			}else if(remainNum.compareTo(addFour)<=0){
				calendar.add(Calendar.MONTH, 3);
				mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
				remainNum=remainNum.subtract(addFour);
			}else if(remainNum.compareTo(addFive)<=0){
				calendar.add(Calendar.MONTH, 4);
				mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
				remainNum=remainNum.subtract(addFive);
			}else if(remainNum.compareTo(addSix)<=0){
				calendar.add(Calendar.MONTH, 5);
				mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
				remainNum=remainNum.subtract(addSix);
			}else{
				mate.setReplaceDate("待定");
			}*/
			if(remainNum.compareTo(new BigDecimal(0))<=0){
				mate.setReplaceDate(DateUtils.format(date, "M")+"月");
			}
			//6月
			remainNum=remainNum.subtract(addOne);
			if(remainNum.compareTo(bg0)<=0){
				mate.setReplaceDate(DateUtils.format(date, "M")+"月");
			}else{
				remainNum=remainNum.subtract(addTwo);
				if(remainNum.compareTo(bg0)<=0){
					calendar.add(Calendar.MONTH, 1);
					mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
				}else{
					remainNum=remainNum.subtract(addThree);
					if(remainNum.compareTo(bg0)<=0){
						calendar.add(Calendar.MONTH, 2);
						mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
					}else{
						remainNum=remainNum.subtract(addFour);
						if(remainNum.compareTo(bg0)<=0){
							calendar.add(Calendar.MONTH, 3);
							mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
						}else{
							remainNum=remainNum.subtract(addFive);
							if(remainNum.compareTo(bg0)<=0){
								calendar.add(Calendar.MONTH, 4);
								mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
							}else{
								remainNum=remainNum.subtract(addSix);
								if(remainNum.compareTo(bg0)<=0){
									calendar.add(Calendar.MONTH, 5);
									mate.setReplaceDate(DateUtils.format(new Date(calendar.getTimeInMillis()), "M")+"月");
								}else{
									mate.setReplaceDate("待定");
								}
							}
						}
					}
				}
			}
			BigDecimal residue = nowInve.add(inveNum).add(mainStruNum).subtract(addOne).subtract(addTwo).subtract(addThree).subtract(addFour).subtract(addFive).subtract(addSix);
 			mate.setResidue(residue);
		}
		return cutPlanMates;
	}

	@Override
	public List<String> queryCutPlanCodeListByStatus() {
		return cutPlanMapper.queryCutPlanCodeListByStatus();
	}

	@Override
	public Map<String, Object> getCutScheOfCutPlanMate(String cutMonth, String citeCode) {
		// 获取激活状态下的销售预测的字段
		Map<String, Object> sMap = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		sMap.put("status", "激活");
		List<SaleForecast> saleList = saleFcstService.getSaleFcstByCondition(sMap);
		
		String[] currYm = cutMonth.split("-");
		int mYear = Integer.parseInt(currYm[0].trim());
		int mMonth = Integer.parseInt(currYm[1].trim());
		
		String saleId = "";
		String columnName = "'0' as q1,'0' as q2,'0' as q3,'0' as q4,'0' as q5";
		if(saleList.size()>0){
			SaleForecast saleFore = saleList.get(0);
			// 预测id
			saleId = saleFore.getId();
			// 预测期间
			String saleYm = saleFore.getFsctYear();
			String[] yearArr = saleYm.split("~");
			String sYm = yearArr[0].trim();
			
			String[] sYmArr = sYm.split("-");
			String sYear = sYmArr[0];
			String sMonth = sYmArr[1];
			int sY = Integer.parseInt(sYear);
			int sM = Integer.parseInt(sMonth);
			
			columnName = "";
			// 计算与初始的年月相差月份
			int qty = (mYear-sY)*12+mMonth-sM;
			if(qty>=0 && qty<24){
				for(int i=1;i<=5;i++){
					if(qty+i+1>12){
						if(qty-12+i+1<=12){
							columnName += "sale_Fore"+(qty-12+i+1)+" as q"+i+",";
						}else{
							// 超过12
							columnName += "sale_Fore"+(qty-12+i+1-12)+" as q"+i+",";
						}
					}else{
						columnName += "sale_Fore_Qty"+(qty+i+1)+" as q"+i+",";
					}
				}
			}else if(qty>=24){
				columnName = "'0' as q1,'0' as q2,'0' as q3,'0' as q4,'0' as q5,";
			}else{
				for(int i=1;i<=5;i++){
					if(qty+i+1<1){
						columnName+="'0' as q"+i+",";
					}else{
						columnName+="sale_Fore_Qty"+(qty+i+1)+" as q"+i+",";
					}
				}
			}
			// 字段
			columnName = columnName.substring(0,columnName.length()-1);
		}
		List<CutPlanMate> mates =getCutPlanMateFromLiaison2(cutMonth,columnName,saleId,citeCode);
		result.put("data", mates);
		result.put("judge", true);
		return result;
	}
	
	//获取打切进度
	public List<CutPlanMate> getCutPlanMateFromLiaison2(String planMonth,String columnName,String saleFcstId,String citeCode) {
		List<CutPlanMate> mates = cutPlanMapper.getCutPlanMateFromLiaison2(planMonth,columnName,saleFcstId,citeCode);

		if(mates==null || mates.size()==0){
			return mates;
		}
		//判断是否需要重新计算主包材数量
		mates = isNeedCountMainStruNum(mates,planMonth);
		//计算剩余量和预计替换时间
		List<CutPlanMate> returnMates = calCutPlanMate(mates, planMonth);
		return returnMates;
	}
	
	

	@Override
	public List<String> queryCutPlansByCutMonth(String cutMonth) {
		return cutPlanMapper.queryCutPlansByCutMonth(cutMonth);
	}
}
