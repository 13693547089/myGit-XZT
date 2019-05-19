package com.faujor.service.bam.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.master.bam.PadPlanMapper;
import com.faujor.dao.master.bam.SaleForecastMapper;
import com.faujor.entity.bam.psm.PadPlan;
import com.faujor.entity.bam.psm.SaleFcstDetail;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.common.LayuiPage;
import com.faujor.service.bam.SaleFcstService;
import com.faujor.utils.DateUtils;

/**
 * 销售预测 实现类
 * @author Vincent
 *
 */
@Service
public class SaleFcstServiceImpl implements SaleFcstService {

	@Autowired
	private SaleForecastMapper saleFcstMapper;
	@Autowired
	private PadPlanMapper padPlanMapper;
	
	/**
	 * 分页获取  主表信息
	 * @param map
	 * @return
	 */
	@Override
	public LayuiPage<SaleForecast> getSaleFcstByPage(Map<String, Object> map) {
		LayuiPage<SaleForecast> page = new LayuiPage<SaleForecast>();
		int count = 0;
		List<SaleForecast> list = saleFcstMapper.getSaleFcstByPage(map);
		count = saleFcstMapper.getSaleFcstCount(map);
		page.setCount(count);
		page.setData(list);
		
		return page;
	}

	/**
	 * 获取分页获取 销售预测 主表数量
	 * @param map
	 * @return
	 */
	@Override
	public int getSaleFcstCount(Map<String, Object> map) {
		return saleFcstMapper.getSaleFcstCount(map);
	}

	/**
	 * 根据条件获取单条 销售预测  主表信息
	 * @param map
	 * @return
	 */
	@Override
	public SaleForecast getSaleFcstById(String id) {
		return saleFcstMapper.getSaleFcstById(id);
	}

	/**
	 * 根据条件获取 销售预测  主表信息
	 * @param map
	 * @return
	 */
	@Override
	public List<SaleForecast> getSaleFcstByCondition(Map<String,Object> map){
		return saleFcstMapper.getSaleFcstByCondition(map);
	}
	
	/**
	 * 根据ID删除 销售预测  信息
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int delSaleFcstById(String id) throws Exception {
		saleFcstMapper.delSaleFcstDetailByMainId(id);
		return saleFcstMapper.delSaleFcstById(id);
	}

	/**
	 * 根据IDs批量删除 销售预测 信息
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional
	public int delBatchSaleFcstInfoByIds(List<String> ids) throws Exception {
		int rs = 0;
		for(int i=0;i<ids.size();i++){
			saleFcstMapper.delSaleFcstDetailByMainId(ids.get(i));
			saleFcstMapper.delSaleFcstById(ids.get(i));
			
			rs++;
		}
		return rs;
	}

	/**
	 * 保存 销售预测  信息
	 * @param saleForecast
	 * @param detailList
	 * @param isImport
	 */
	@Override
	@Transactional
	public void saveSaleFcstInfo(SaleForecast saleForecast, List<SaleFcstDetail> detailList,int isImport) throws Exception {
		
		String id = saleForecast.getId();
		
		saleFcstMapper.delSaleFcstDetailByMainId(id);
		saleFcstMapper.delSaleFcstById(id);
		
		saleFcstMapper.saveSaleFcstDetailList(detailList);
		saleFcstMapper.saveSaleFcst(saleForecast);
		
		// 更新汇总
		Map<String,Object> pMap = new HashMap<String,Object>();
		
		// 年月处理
		String years = saleForecast.getFsctYear();
		String[] yearArr = years.split("~");
		String sYm = yearArr[0].trim();
		String eYm = yearArr[1].trim();
		
		String[] sYmArr = sYm.split("-");
		String sYear = sYmArr[0];
		String sMonth = sYmArr[1];
		int sY = Integer.parseInt(sYear);
		int sM = Integer.parseInt(sMonth);
		
		String[] eYmArr = eYm.split("-");
		String eYear = eYmArr[0];
		String eMonth = eYmArr[1];
		int eY = Integer.parseInt(eYear);
		int eM = Integer.parseInt(eMonth);
		
		//*********************导入的数据为空时获取去年同月的实际销量*************************
		if(isImport == 1){
			Map<String,Object> conditionMap = new HashMap<String,Object>();
			// 导入的情况
			int index = 0;
			if(sMonth.equals("01")){
				for(int j=1;j<=12;j++){
					index++;
					conditionMap.put("year"+index, sY-1+"");
					conditionMap.put("month"+index, j<10?("0"+j):(j+""));
				}
				for(int j=1;j<=12;j++){
					index++;
					conditionMap.put("year"+index, eY-1+"");
					conditionMap.put("month"+index, j<10?("0"+j):(j+""));
				}
			}else{
				// 初始年份
				for(int j=sM;j<=12;j++){
					index++;
					conditionMap.put("year"+index, sY-1+"");
					conditionMap.put("month"+index, j<10?("0"+j):(j+""));
				}
				// 中间年份
				for(int j=1;j<=12;j++){
					index++;
					conditionMap.put("year"+index, sY+"");
					conditionMap.put("month"+index, j<10?("0"+j):(j+""));
				}
				// 结束年份
				for(int j=1;j<=eM;j++){
					index++;
					conditionMap.put("year"+index, eY-1+"");
					conditionMap.put("month"+index, j<10?("0"+j):(j+""));
				}
			}
			conditionMap.put("mainId", id);
			saleFcstMapper.updatePadPlanDetailByLastYm(conditionMap);
		}
		
		//*********************计算年度汇总*************************
		// 获取汇总的字段
		String sumC1 = "";
		String sumC2 = "";
		String sumC3 = "";
		
		if(sMonth.equals("01")){
			sumC1 = "(a.SALE_FORE_QTY1+a.SALE_FORE_QTY2+a.SALE_FORE_QTY3+a.SALE_FORE_QTY4+"+
	"a.SALE_FORE_QTY5+a.SALE_FORE_QTY6+a.SALE_FORE_QTY7+a.SALE_FORE_QTY8+"+
	"a.SALE_FORE_QTY9+a.SALE_FORE_QTY10+a.SALE_FORE_QTY11+a.SALE_FORE_QTY12) ";
			sumC2 = " (a.SALE_FORE1+a.SALE_FORE2+a.SALE_FORE3+a.SALE_FORE4+"+
	"a.SALE_FORE5+a.SALE_FORE6+a.SALE_FORE7+a.SALE_FORE8+"+
	"a.SALE_FORE9+a.SALE_FORE10+a.SALE_FORE11+a.SALE_FORE12) ";
			sumC3 = "0";
		}else{
			for(int i=0;i<=12-sM;i++){
				sumC1 += "a.SALE_FORE_QTY"+(i+1)+"+";
			}
			sumC1 = "("+sumC1.substring(0, sumC1.length()-1)+") ";
			
			for(int i=12;i>12-eM;i--){
				sumC2 += "a.SALE_FORE_QTY"+i+"+";
			}
			for(int i=1;i<=12-eM;i++){
				sumC2 += "a.SALE_FORE"+i+"+";
			}
			sumC2 = "("+sumC2.substring(0, sumC2.length()-1)+") ";
			
			for(int i=12;i>12-eM;i--){
				sumC3 += "a.SALE_FORE"+i+"+";
			}
			sumC3 = "("+sumC3.substring(0, sumC3.length()-1)+") ";
		}
		
		pMap.put("sum1", sumC1);
		pMap.put("sum2", sumC2);
		pMap.put("sum3", sumC3);
		pMap.put("mainId", id);
		saleFcstMapper.updateSaleFcstDetailSum(pMap);
		
		
		if(saleForecast.getStatus().equals("激活")){
			// 修改生产交货计划中的销售预测
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if(sMonth.equals("01")){
				for(int j=1;j<=12;j++){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("ym", sYear+"-"+(j<10?("0"+j):j));
					item.put("num", j);
					list.add(item);
				}
				for(int j=1;j<=12;j++){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("ym", eYear+"-"+(j<10?("0"+j):j));
					item.put("num", j+12);
					list.add(item);
				}
			}else{
				int ssi = 12-sM;
				// 初始年份
				for(int j=0;j<=ssi;j++){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("ym", sYear+"-"+((sM+j)<10?("0"+(sM+j)):(sM+j)));
					item.put("num", j+1);
					list.add(item);
				}
				// 中间年份
				int mYear = sY+1;
				for(int j=1;j<=12;j++){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("ym", ""+mYear+"-"+(j<10?("0"+j):j));
					item.put("num", ssi+j+1);
					list.add(item);
				}
				// 结束年份
				for(int i=1;i<=eM;i++){
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("ym", eYear+"-"+(i<10?("0"+i):i));
					item.put("num", ssi+12+i+1);
					list.add(item);
				}
			}
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("sYm", sYm);
			map.put("eYm", eYm);
			map.put("fcstId", id);
			map.put("list", list);
			// 根据销售预测修改生产交货中的本月与下月销售数据
			saleFcstMapper.updatePadPlanDetailBySaleFcst(map);
			
			/*//【已弃用】 修改生产交货计划中的本月全国库存、周转天数等
			Map<String,Object> calcMap = new HashMap<String,Object>();
			String currYm = DateUtils.format(new Date(),DateUtils.DATE_YM_PATTERN);
			calcMap.put("sYm", currYm);
			calcMap.put("eYm", eYm);
			saleFcstMapper.updateFuturePadPlanDetailCalc(calcMap);*/
			
			//*************** 修改未来月份生产交货计划中的本月全国库存、周转天数等
			// 当前年月
			String currYm = DateUtils.format(new Date(),DateUtils.DATE_YM_PATTERN);
			// 获取最大的后续月份
			String maxMonth = padPlanMapper.getMaxPadPlanMonth();
			
			if(maxMonth != null && !maxMonth.equals("")){
				//***** 存在最大月份
				// 上个月计划id
				String preId = "";
				// 未来月份重新计算库存等数据
				int decMonths = DateUtils.calcYmDec(currYm, maxMonth);
				for(int i=1;i<=decMonths;i++){
					// 下个月
					String nextMonth = DateUtils.dateMonthCalc(currYm, i);
					// 获取下个月的计划
					Map<String, Object> nextMap=new HashMap<String,Object>();
					nextMap.put("planMonth", nextMonth);
					PadPlan nextPadPlan = padPlanMapper.getPadPlanByMap(nextMap);
					
					// 当月
					String cMonth = DateUtils.dateMonthCalc(currYm, i-1);
					// 获取当月的计划
					Map<String, Object> cMap=new HashMap<String,Object>();
					cMap.put("planMonth", cMonth);
					PadPlan cPadPlan = padPlanMapper.getPadPlanByMap(cMap);
					if(cPadPlan != null){
						// 保存上个月计划id
						preId = cPadPlan.getId();
					}else{
						preId = "";
					}
					
					if(nextPadPlan != null && !preId.equals("")){
						// 下个月份 ID
						String nextId = nextPadPlan.getId();
						// 下个月份 状态
						//String nextStatus = nextPadPlan.getStatus();
						
						Map<String, Object> dealMap=new HashMap<String,Object>();
						
						// 未来月起，下个月中的上个月全国库存获取
						dealMap.put("nextId", nextId);
						dealMap.put("preId", preId);
						if(i!=1){
							// 	i=1 情况下不需要获取上个月的库存
							padPlanMapper.updateNextPlanPreStock(dealMap);
						}
						
						// 修改后续月份的计算列
						padPlanMapper.updateFutureMonthCalc(dealMap);
					}
				}
			}
			
			//*************** 激活状态下，其他销售预测修改成未激活状态
			Map<String,Object> paraMap = new HashMap<String,Object>();
			paraMap.put("status", "未激活");
			paraMap.put("nonId", id);
			saleFcstMapper.updateSaleFcstStatus(paraMap);
		}
	}

	/**
	 * 修改 销售预测 主表 的状态
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public int updateSaleFcstStatus(Map<String,Object> map) throws Exception {
		return saleFcstMapper.updateSaleFcstStatus(map);
	}

	/**
	 * 获取 销售预测  明细列表信息
	 * @param mainId
	 * @return
	 */
	@Override
	public List<SaleFcstDetail> getSaleFcstDetailListByMainId(String mainId) {
		return saleFcstMapper.getSaleFcstDetailListByMainId(mainId);
	}

	/**
	 * 获取 销售预测 明细数量
	 * @param mainId
	 * @return
	 */
	@Override
	public int getSaleFcstDetailCount(String mainId) {
		return saleFcstMapper.getSaleFcstDetailCount(mainId);
	}
	
	/**
	 * 保存 导入的销售预测  信息
	 * @param saleForecast
	 * @param detailList
	 */
	@Override
	@Transactional
	public void saveImpSaleFcstInfo(SaleForecast saleForecast, List<SaleFcstDetail> detailList) throws Exception {
		saleFcstMapper.saveImpSaleFcstDetailList(detailList);
		saleFcstMapper.saveSaleFcst(saleForecast);
	}
	
	/**
	 * 获取物料的月份销售预测
	 * @param map
	 * @return
	 */
	public List<SaleFcstDetail> getSaleFcstDataByCxjh(Map<String,Object> map){
		return saleFcstMapper.getSaleFcstDataByCxjh(map);
	}
	
	/**
	 * 获取根据系列、品项汇总的导出数据
	 * @param mainId
	 * @return
	 */
	@Override
	public List<SaleFcstDetail> getSaleFcstSumByMainId(String mainId){
		return saleFcstMapper.getSaleFcstSumByMainId(mainId);
	}
}
