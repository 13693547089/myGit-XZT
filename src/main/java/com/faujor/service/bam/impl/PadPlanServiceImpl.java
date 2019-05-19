package com.faujor.service.bam.impl;

import java.math.BigDecimal;
import java.text.ParseException;
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
import com.faujor.dao.master.bam.PadPlanMapper;
import com.faujor.dao.master.bam.SaleForecastMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.entity.bam.psm.PadMateMess;
import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.PadPlanDetail;
import com.faujor.entity.bam.psm.PadPlanDetailForm;
import com.faujor.entity.bam.psm.PadPlanRecord;
import com.faujor.entity.bam.psm.Psi;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.common.BaseEntity;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.bam.PadPlanService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.CommonUtils;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

/**
 * 生产/交货计划 实现类
 * @author Vincent
 *
 */
@Service
public class PadPlanServiceImpl implements PadPlanService {

	@Autowired
	private PadPlanMapper padPlanMapper;
	@Autowired
	private SaleForecastMapper saleFcstMapper;
	@Autowired
	private OrgService orgService;
	@Autowired
	private MaterialMapper materialMapper;
	private String format;
	
	/**
	 * 分页获取 生产/交货计划 主表信息
	 * @param map
	 * @return
	 */
	@Override
	public LayuiPage<PadPlan> getPadPlanByPage(Map<String, Object> map) {
		LayuiPage<PadPlan> page = new LayuiPage<PadPlan>();
		int count = 0;
		List<PadPlan> list = padPlanMapper.getPadPlanByPage(map);
		count = padPlanMapper.getPadPlanCount(map);
		page.setCount(count);
		page.setData(list);
		
		return page;
	}
	
	/**
	 * 获取分页获取 生产/交货计划 主表数量
	 * @param map
	 * @return
	 */
	@Override
	public int getPadPlanCount(Map<String, Object> map) {
		return padPlanMapper.getPadPlanCount(map);
	}
	
	/**
	 * 根据条件获取单条 生产/交货计划 主表信息
	 * @param map
	 * @return
	 */
	@Override
	public PadPlan getPadPlanByMap(Map<String, Object> map) {
		return padPlanMapper.getPadPlanByMap(map);
	}

	/**
	 * 根据ID删除 生产/交货计划 信息
	 * 主表加明细数据
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int delPadPlanInfoById(String id) {
		padPlanMapper.delPadPlanDetailByMainId(id);
		
		return padPlanMapper.delPadPlanById(id);
	}

	/**
	 * 根据IDs批量删除 生产/交货计划 信息
	 * 主表加明细数据
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional
	public int delBatchPadPlanInfoByIds(List<String> ids) {
		int rs = 0;
		for(int i=0;i<ids.size();i++){
			padPlanMapper.delPadPlanDetailByMainId(ids.get(i));
			padPlanMapper.delPadPlanById(ids.get(i));
			
			rs++;
		}
		return rs;
	}
	
	/**
	 * 保存 生产/交货计划 信息
	 * @param padPlan
	 * @param detailList
	 * @param userCode
	 * @return
	 */
	@Override
	@Transactional
	public int savePadPlanInfo(PadPlan padPlan,List<PadPlanDetail> detailList,String userCode) {
		
		// 删除明细【】
		//padPlanMapper.delPadPlanDetailByMainId(padPlan.getId());

		// 根据用户对应的系列删除
		Map<String, Object> delMap=new HashMap<String,Object>();
		delMap.put("userCode", userCode);
		delMap.put("mainId", padPlan.getId());
		padPlanMapper.delPadPlanDetailByUserSeries(delMap);
		
		if(detailList.size()>0){
			
			List<List<?>> splitList = CommonUtils.splitList(detailList, 500);
			
			for(int j=0;j<splitList.size();j++){
				List<PadPlanDetail> itemList = (List<PadPlanDetail>) splitList.get(j);
				// 根据用户系列保存明细
				Map<String, Object> saveMap=new HashMap<String,Object>();
				saveMap.put("list", itemList);
				saveMap.put("mainId", padPlan.getId());
				saveMap.put("userCode", userCode);
				padPlanMapper.savePadPlanDetailListByUserSeries(saveMap);
			}
		}
		
		// 更新或插入主表
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("id", padPlan.getId());
		PadPlan padPlanE = padPlanMapper.getPadPlanByMap(map);
		int rs = 0;
		if(padPlanE != null){
			// 存在
			rs = padPlanMapper.updatePadPlan(padPlan);
		}else{
			// 不存在
			rs = padPlanMapper.savePadPlan(padPlan);
		}
		//备份当前月份的生产交货计划数据
		String status = padPlan.getStatus();
		if("已提交".equals(status)) {
			String uuid = UUIDUtil.getUUID();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id", padPlan.getId());
			param.put("versionCode", padPlan.getPlanMonth());
			param.put("versionDesc", padPlan.getPlanMonth()+"版本");
			param.put("uuid", uuid);
			//备份生产交货计划主表数据
			padPlanMapper.copyPadPlanData(param);
			//备份生产交货计划从表数据
			padPlanMapper.copyPadPlanDetailListData(param);
			
		}
		
		
		// 计划月份
		String planMonth = padPlan.getPlanMonth();
		// 当前年月
		String currYm = DateUtils.format(new Date(),DateUtils.DATE_YM_PATTERN);
		// 获取最大的后续月份
		String maxMonth = padPlanMapper.getMaxPadPlanMonth();
		// 当期月份计划ID
		String currId = padPlan.getId();
		// 获取激活的销售预测
		Map<String, Object> saleMap=new HashMap<String,Object>();
		saleMap.put("status", "激活");
		List<SaleForecast> saleFcstList =  saleFcstMapper.getSaleFcstByCondition(saleMap);
		String fcstId = "";
		String fcstYear = "";
		if(saleFcstList.size()>0){
			fcstId = saleFcstList.get(0).getId();
			fcstYear = saleFcstList.get(0).getFsctYear();
		}
		
		// 上个月计划id
		String preId = currId;
		int decMonths = DateUtils.calcYmDec(planMonth, maxMonth);
		for(int i=1;i<=decMonths;i++){
			// 下个月
			String nextMonth = DateUtils.dateMonthCalc(planMonth, i);
			// 获取下个月的计划
			Map<String, Object> nextMap=new HashMap<String,Object>();
			nextMap.put("planMonth", nextMonth);
			PadPlan nextPadPlan = padPlanMapper.getPadPlanByMap(nextMap);
			
			if(nextPadPlan == null){
				continue;
			}
			// 下个月份 ID
			String nextId = nextPadPlan.getId();
			// 下个月份 状态
			String nextStatus = nextPadPlan.getStatus();
			
			// 销售预测的计算获取
			String saleColumn = "";
			String saleFore = "";
			String nextSaleFore = "";
			
			if(!fcstYear.equals("")){
				//*** 存在激活状态的销售预测
				// 预测期间
				String[] yearArr = fcstYear.split("~");
				String sYm = yearArr[0].trim();
				
				// 计算与初始的年月相差月份
				int qty = DateUtils.calcYmDec(sYm, nextMonth);
				if(qty>=0 && qty<24){
					if(qty>=12){
						saleFore = "sale_Fore"+(qty-11);
						if(qty-10>12){
							// 当前月份为最后一月
							nextSaleFore = "sale_Fore1";
						}else{
							nextSaleFore = "sale_Fore"+(qty-10);
						}
					}else{
						saleFore = "sale_Fore_Qty"+(qty+1);
						if(qty+2>12){
							nextSaleFore = "sale_Fore1"; // +((qty+2)%12)
						}else{
							nextSaleFore = "sale_Fore_Qty"+(qty+2);
						}
					}
				}else{
					saleFore = "'0'";
					nextSaleFore = "'0'";
				}
			}else{
				//*** 未获取到激活状态下的销售预测
				saleFore = "'0'";
				nextSaleFore = "'0'";
			}
			
			saleColumn = saleFore + " as saleFore,"+nextSaleFore+" as nextSaleFore";
			
			int mNum = DateUtils.calcYmDec(currYm, nextMonth);
			Map<String, Object> dealMap=new HashMap<String,Object>();
			if(mNum>0){
				// 未来月份
				if(nextStatus.equals("已保存")){
					dealMap.put("nextId", nextId);
					dealMap.put("saleColumn", saleColumn);
					dealMap.put("saleId", fcstId);
					dealMap.put("currId", currId);

					// 为后续月份计划中添加缺少的物料
					padPlanMapper.saveFutureMonthExtraMat(dealMap);
					
					// 为后续的月份删除多余的物料
					padPlanMapper.delFutureMonthExtraMat(dealMap);
				}
				
				// 未来月起，下个月中的上个月全国库存获取
				dealMap.put("nextId", nextId);
				dealMap.put("preId", preId);
				padPlanMapper.updateNextPlanPreStock(dealMap);
				
				// 修改后续月份的计算列
				padPlanMapper.updateFutureMonthCalc(dealMap);
				
				//只有单当前月份的为已提交，接下来的月份为已保存状态的生产交货计划数据才需要备份
				if("已提交".equals(status) && "已保存".equals(nextStatus)) {
					String uuid = UUIDUtil.getUUID();
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("id", nextId);
					param.put("versionCode", padPlan.getPlanMonth());
					param.put("versionDesc", padPlan.getPlanMonth()+"版本");
					param.put("uuid", uuid);
					//备份生产交货计划主表数据
					padPlanMapper.copyPadPlanData(param);
					//备份生产交货计划从表数据
					padPlanMapper.copyPadPlanDetailListData(param);
				}
			}else{
				// 当前及以前月份
				
				// 为后续月份计划中添加缺少的物料
				if(nextStatus.equals("已保存")){
					dealMap.put("nextId", nextId);
					dealMap.put("saleColumn", saleColumn);
					dealMap.put("saleId", fcstId);
					dealMap.put("currId", currId);
					
					String[] ymArr = nextMonth.split("-");
					dealMap.put("year", ymArr[0]);
					dealMap.put("month", ymArr[1]);
					
					padPlanMapper.saveCurrPreMonthExtraMat(dealMap);
				}
				
				// 修改当前及以前月份的计算列
				dealMap.put("nextId", nextId);
				padPlanMapper.updateCurrPreMonthCalc(dealMap);
			}
			
			// 保存上个月计划id
			preId = nextId;
		}
		
		return rs;
	}

	/**
	 * 更新 生产/交货计划 信息
	 * 暂时未用到 。。。。。。。。
	 * @param padPlan
	 * @param detailList
	 * @return
	 */
	@Override
	@Transactional
	public int updatePadPlanInfo(PadPlan padPlan,List<PadPlanDetail> detailList) {
		String id = padPlan.getId();
		// 删除原有明细
		padPlanMapper.delPadPlanDetailByMainId(id);
		
		padPlanMapper.savePadPlanDetailList(detailList);
		
		return padPlanMapper.savePadPlan(padPlan);
	}
	
	/**
	 * 获取 生产/交货计划 明细列表信息
	 * @param mainId
	 * @return
	 */
	@Override
	public LayuiPage<PadPlanDetail> getPadPlanDetailPage(Map<String, Object> map) {
		LayuiPage<PadPlanDetail> page = new LayuiPage<PadPlanDetail>();
		int count = 0;
		List<PadPlanDetail> list = padPlanMapper.getPadPlanDetailPage(map);
		count = padPlanMapper.getPadPlanDetailCount(map.get("mainId").toString());
		page.setCount(count);
		page.setData(list);
		
		return page;
	}
	
	/**
	 * 获取 生产/交货计划 明细列表信息
	 * @param mainId
	 * @return
	 */
	@Override
	public List<PadPlanDetail> getPadPlanDetailListByMainId(String mainId) {
		List<PadPlanDetail> list = padPlanMapper.getPadPlanDetailListByMainId(mainId);
		
		return list;
	}

	/**
	 * 获取 生产/交货计划 明细数量
	 * @param mainId
	 * @return
	 */
	@Override
	public int getPadPlanDetailCount(String mainId) {
		return padPlanMapper.getPadPlanDetailCount(mainId);
	}

	/**
	 * 修改 生产/交货计划 主表 的状态
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int updatePadPlanStatus(String status, String id) {
		return padPlanMapper.updatePadPlanStatus(status, id);
	}
	
	/**
	 * 修改 生产/交货计划 主表 的同步标记
	 * @param syncFlag
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int updatePadPlanSyncFlag(String syncFlag,String id){
		return padPlanMapper.updatePadPlanSyncFlag(syncFlag, id);
	}

	/**
	 * 根据年月获取 生产/交货计划 所有物料的产品系列
	 * @param ym
	 * @return
	 */
	@Override
	public List<BaseEntity> getMatProdSeriesByYearMonth(Map<String, Object> map) {
		return padPlanMapper.getMatProdSeriesByYearMonth(map);
	}

	/**
	 * 根据年月、产品系列获取 生产/交货计划 明细数据
	 * @param ym
	 * @param prodSeries
	 * @return
	 */
	@Override
	public List<PadPlanDetail> getPadPlanDetailByMap(Map<String, Object> map) {
		return padPlanMapper.getPadPlanDetailByMap(map);
	}
	
	/**
	 * 根据条件获取 物料进销存报表大品项汇总信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Psi> getPsiSumByMap(Map<String, Object> map){
		return padPlanMapper.getPsiSumByMap(map);
	}
	
	/**
	 * 根据条件获取 物料进销存报表 信息
	 * @param map
	 * @return
	 */
	@Override
	public List<Psi> getPsiInfoByMap(Map<String, Object> map){
		return padPlanMapper.getPsiInfoByMap(map);
	}
	

	/**
	 * 获取未来月份的计算方式的数据
	 * @param map
	 * @return
	 */
	@Override
	public List<PadPlanDetail> getPadPlanTempDetailByMap(Map<String, Object> map){
		return padPlanMapper.getPadPlanTempDetailByMap(map);
	}
	
	/**
	 * 根据本月计划更新下个月计划的明细
	 * @param map
	 */
	@Override
	@Transactional
	public void updateNextPadPlanDetail(Map<String, Object> map){
		padPlanMapper.updateNextPadPlanDetail(map);
	}

	/**
	 * 通过 t_ora_cxjh表更新某月 生产交货计划明细数据
	 * @param map
	 */
	@Override
	public void updateMonthPadDetail(Map<String, Object> map) {
		padPlanMapper.updateMonthPadDetail(map);
	}

	//-----------------------物料交货计划整年调整开始-----------------------------
	/**
	 * 获取物料未来一年的生产交货计划详情数据
	 * @param map
	 * @return
	 */
	@Override
	public List<PadPlanDetail> getYearPadDetailByMap(String mateCode, String planMonth) {
		Map<String, Object> map=new HashMap<>();
		List<PadPlanDetail> list=new ArrayList<>();
		map.put("mateCode", mateCode);
		//添加月份信息
		List<String> months=new ArrayList<>();
		Date date = DateUtils.parse(planMonth, "yyyy-MM");
		if(date!=null){
			months.add(planMonth);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			for (int i = 0; i < 12; i++) {
				cal.add(Calendar.MONTH, 1);
				months.add(DateUtils.format(cal.getTime(),"yyyy-MM"));
			}
			map.put("list",months);
			list=padPlanMapper.getYearPadDetailByMap(map);
		}
		return list;
	}
	//-----------------------物料交货计划整年调整结束-----------------------------

	
	//-----------------------批量修改物料--------------------------
	@Override
	public boolean checkMateIsSave(String planCode, String matCode) {
		String id = padPlanMapper.getMateByPlanCodeAndMatCode(planCode,matCode);
		if(StringUtils.isEmpty(id))
			return false;
		return true;
	}

	@Override
	public List<PadMateMess> getPlanMessageListOfMateByMatCode(String matCode) {
		return padPlanMapper.getPlanMessageListOfMateByMatCode(matCode);
	}

	@Override
	@Transactional
	public Map<String, Object> confirmUpdatePadPlanMess(String dataJson, String matCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		//修改交货计划物料编码为matCode的本月生产/交货计划值,
		List<PadMateMess> jsonToList = JsonUtils.jsonToList(dataJson, PadMateMess.class);
		String minPlanMonth = jsonToList.get(0).getPlanMonth();
		Date minPlanMonthParse = DateUtils.parse(minPlanMonth, "yyyy-MM");
		for (PadMateMess padMateMess : jsonToList) {
			String planMonth = padMateMess.getPlanMonth();
			Date planMonthParse = DateUtils.parse(planMonth, "yyyy-MM");
			//求最小月份
			minPlanMonthParse = planMonthParse.before(minPlanMonthParse) ? planMonthParse:minPlanMonthParse;
			String id = padMateMess.getId();
			Float padPlanQty = padMateMess.getPadPlanQty();
			map.put("id", id);
			map.put("padPlanQty", padPlanQty);
			//修改物料的生产/交货计划值
			padPlanMapper.updatePadPlanDetailByid(map);
		}
		String nowDateFormat = DateUtils.format(new Date(),"yyyy-MM");
		Date nowDateParse = DateUtils.parse(nowDateFormat, "yyyy-MM");
		String minPlanMonthFormat = DateUtils.format(minPlanMonthParse, "yyyy-MM");
		//查询需要修改的物料数据
		List<PadPlanDetail> list = padPlanMapper.getMateByPlanMonthAndMatCode(minPlanMonthFormat,matCode);
		Date lastMongthparse = null;
		Float lastNationStock1=0F;
		for (int i =0; i<list.size();i++) {
			PadPlanDetail padPlanDetail = list.get(i);
			String planMonth = padPlanDetail.getPlanMonth();//计划月份
			Date parse = DateUtils.parse(planMonth, "yyyy-MM");
			if(lastMongthparse !=null) {
				try {
					//求相差月份
					int monthSpace = getMonthSpace(lastMongthparse,parse);
					if(monthSpace == 1) {
						//相差一个月，当前月的上个月全国库存 
						padPlanDetail.setNationStock2(lastNationStock1);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			lastMongthparse = parse;
			//上个月全国库存
			Float nationStock2 = padPlanDetail.getNationStock2()==null?0:padPlanDetail.getNationStock2();
			BigDecimal nationStock2Big = new BigDecimal(nationStock2.toString());
			if(parse.before(nowDateParse) || planMonth.equals(nowDateFormat)) {
				//计算当前及以前月份
				// 预计交货
				Float estDeliQty = padPlanDetail.getEstDeliQty()==null?0:padPlanDetail.getEstDeliQty();
				BigDecimal estDeliQtyBig = new BigDecimal(estDeliQty.toString());
				// 预计销售
				Float estSaleQty = padPlanDetail.getEstSaleQty()==null?0:padPlanDetail.getEstSaleQty();
				BigDecimal estSaleQtyBig = new BigDecimal(estSaleQty.toString());
				// xx月全国预测库存
				BigDecimal nationStock1Big = nationStock2Big.add(estDeliQtyBig).subtract(estSaleQtyBig);
				padPlanDetail.setNationStock1(nationStock1Big.floatValue());
				// 周转天数
				if(estSaleQty==0) {
					padPlanDetail.setTurnOverDays(0F);
				}else {
					BigDecimal tunOverDaysBig = (nationStock1Big.divide(estSaleQtyBig,2,BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(30));
					padPlanDetail.setTurnOverDays(tunOverDaysBig.floatValue());
				}
			}else {
				//计算未来月份的数据
				// 生产交货计划
				Float padPlanQty = padPlanDetail.getPadPlanQty() == null ?0:padPlanDetail.getPadPlanQty();
				BigDecimal padPlanQtyBig = new BigDecimal(padPlanQty.toString());
				// 销售预测
				Float saleForeQty = padPlanDetail.getSaleForeQty() == null?0:padPlanDetail.getSaleForeQty();
				BigDecimal saleForeQtyBig = new BigDecimal(saleForeQty.toString());
				// xx月全国预测库存
				BigDecimal nationStock1Big = nationStock2Big.add(padPlanQtyBig).subtract(saleForeQtyBig);
				padPlanDetail.setNationStock1(nationStock1Big.floatValue());
				// 周转天数
				if(saleForeQty==0) {
					padPlanDetail.setTurnOverDays(0F);
				}else {
					BigDecimal tunOverDaysBig = (nationStock1Big.divide(saleForeQtyBig,2,BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(30));
					padPlanDetail.setTurnOverDays(tunOverDaysBig.floatValue());
				}
			}
			lastNationStock1 = padPlanDetail.getNationStock1();
		}
		//修改数据库数据
		int count = 0;
		for (PadPlanDetail pad : list) {
			int i=0;
			i = padPlanMapper.updatePadPlandDetailsById(pad);
			count +=i;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if(count == list.size()) {
			result.put("judge", true);
		}else {
			result.put("judge", false);
		}
		return result;
	}
	
	 public int getMonthSpace(Date date1, Date date2) throws ParseException
	             {
	        Calendar bef = Calendar.getInstance();
	        Calendar aft = Calendar.getInstance();
	        bef.setTime(date1);
	        aft.setTime(date2);
	        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
	        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;;
	        return result+month;

	    }

	
	//-----------------------批量修改物料--------------------------
	 
	 
	 
	 @Override
	 public Map<String, Object> getPadPlanRecordListByPage(Map<String, Object> map) {
		//处理版本范围问题
		 map = dealWithVersionScope(map);
		 SysUserDO user = UserCommon.getUser();
		 if(!"admin".equals(user.getUsername())) {
			 Map<String, Object> paramMap = new HashMap<String, Object>();
			 paramMap.put("ownId", user.getUserId());
			 paramMap.put("orgCode", "PURCHAROR");
			 paramMap.put("isContainOwn", true);
			 // 获取这个管理员下的采购员
			 List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
			 if(userList.size()>0) {
				 //查询这些采购员在采购货源中对应的物料编码集合
//				 List<String> mateCodeList = materialMapper.getMateCodeListByUsers(userList);
//				 if(mateCodeList.size()>0) {
//					 map.put("mateCodeList", mateCodeList);
//				 }else {
//					 map.put("judge", "wwwwww");
//				 }
				 map.put("userList", userList);
			 }else {
				 map.put("judge", "wwwwww");
			 }
		 }
		 List<PadPlanRecord> list = padPlanMapper.getPadPlanRecordListByPage(map);
		 int count = padPlanMapper.getPadPlanRecordListByPageCount(map);
		 Map<String, Object> page = new HashMap<String, Object>();
		 page.put("data", list);
		 page.put("msg", "");
		 page.put("code", 0);
		 page.put("count", count);
		 return page;
	 }
	 //处理版本范围问题
	@Override
	public Map<String, Object> dealWithVersionScope(Map<String, Object> map) {
		PadPlanDetailForm paForm = (PadPlanDetailForm) map.get("paForm");
		//起始版本
		String startVersion = paForm.getStartVersion();
		map.put("one", startVersion);
		//截止版本
		String endVersion = paForm.getEndVersion();
		int decMonths = DateUtils.calcYmDec(startVersion, endVersion);
		for (int i = 1; i <= decMonths; i++) {
			String nextMonth = DateUtils.dateMonthCalc(startVersion, i);
			int count = i+1;
			if(count==2) {
				map.put("two", nextMonth);
			}else if(count ==3) {
				map.put("three", nextMonth);
			}else if(count ==4) {
				map.put("four", nextMonth);
			}else if(count ==5) {
				map.put("five", nextMonth);
			}else if(count ==6) {
				map.put("six", nextMonth);
			}else if(count ==7) {
				map.put("seven", nextMonth);
			}else if(count ==8) {
				map.put("eight", nextMonth);
			}else if(count ==9) {
				map.put("nine", nextMonth);
			}else if(count ==10) {
				map.put("ten", nextMonth);
			}else if(count ==11) {
				map.put("eleven", nextMonth);
			}else if(count ==12) {
				map.put("twelve", nextMonth);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getPadPlanRecordFields(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>(); 
		result = dealWithVersionScope(map);
		return result;
	}

	@Override
	public List<PadPlanRecord> getPadPlanRecordList(Map<String, Object> map) {
		 SysUserDO user = UserCommon.getUser();
		 if(!"admin".equals(user.getUsername())) {
			 Map<String, Object> paramMap = new HashMap<String, Object>();
			 paramMap.put("ownId", user.getUserId());
			 paramMap.put("orgCode", "PURCHAROR");
			 paramMap.put("isContainOwn", true);
			 // 获取这个管理员下的采购员
			 List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
			 if(userList.size()>0) {
//				 //查询这些采购员在采购货源中对应的物料编码集合
//				 List<String> mateCodeList = materialMapper.getMateCodeListByUsers(userList);
//				 if(mateCodeList.size()>0) {
//					 map.put("mateCodeList", mateCodeList);
//				 }else {
//					 map.put("judge", "wwwwww");
//				 }
				 map.put("userList", userList);
			 }else {
				 map.put("judge", "wwwwww");
			 }
		 }
		return padPlanMapper.getPadPlanRecordList(map);
	}

}
