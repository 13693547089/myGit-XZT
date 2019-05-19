package com.faujor.dao.master.bam;

import com.faujor.entity.bam.psm.SaleFcstDetail;
import com.faujor.entity.bam.psm.SaleForecast;
import java.util.List;
import java.util.Map;

/**
 * 销售预测操作类
 * @author Vincent
 *
 */
public interface SaleForecastMapper {
	
	public List<SaleForecast> getSaleFcstByPage(Map<String,Object> map);
	
	public int getSaleFcstCount(Map<String,Object> map);
	
	public SaleForecast getSaleFcstById(String id);
	
	public List<SaleForecast> getSaleFcstByCondition(Map<String,Object> map);
	
	public int delSaleFcstById(String id);
	
	public void saveSaleFcst(SaleForecast saleForecast);
	
	public int updateSaleFcstStatus(Map<String,Object> map);
	
	public List<SaleFcstDetail> getSaleFcstDetailListByMainId(String mainId);
	
	public int getSaleFcstDetailCount(String mainId);
	
	public void saveSaleFcstDetailList(List<SaleFcstDetail> list);
	
	public int delSaleFcstDetailByMainId(String mainId);
	
	/**
	 * 根据销售预测修改对应年月的生产交货中的销售当月与下月的预测数据
	 * @param map
	 * @return
	 */
	public int updatePadPlanDetailBySaleFcst(Map<String,Object> map);
	
	/**
	 * 保存导入的明细数据
	 * @param list
	 */
	public void saveImpSaleFcstDetailList(List<SaleFcstDetail> list);
	
	/**
	 * 获取物料的月份销售预测
	 * @param map
	 * @return
	 */
	public List<SaleFcstDetail> getSaleFcstDataByCxjh(Map<String,Object> map);
	
	/**
	 * 更新销售预测的汇总
	 * @param map
	 * @return
	 */
	public int updateSaleFcstDetailSum(Map<String,Object> map);
	
	/**
	 * 更新未来月份的生产交货计划
	 * @param map
	 * @return
	 */
	public int updateFuturePadPlanDetailCalc(Map<String,Object> map);
	
	/**
	 * 月底最后一日更新对应月份的销售数量为实际销量
	 * @param map
	 * @return
	 */
	public int updateSaleForeByCxjh(Map<String,Object> map);
	
	/**
	 * 获取根据系列、品项汇总的导出数据
	 * @param mainId
	 * @return
	 */
	public List<SaleFcstDetail> getSaleFcstSumByMainId(String mainId);
	
	/**
	 * 更新导入的数据中空白的数据为去年同期的实际销量
	 * @param map
	 * @return
	 */
	public int updatePadPlanDetailByLastYm(Map<String,Object> map);
}