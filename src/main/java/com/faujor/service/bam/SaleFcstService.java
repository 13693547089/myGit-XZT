package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.SaleFcstDetail;
import com.faujor.entity.bam.psm.SaleForecast;
import com.faujor.entity.common.LayuiPage;

/**
 * 销售预测 服务类
 * @author Vincent
 *
 */
public interface SaleFcstService {
	/**
	 * 分页获取  主表信息
	 * @param map
	 * @return
	 */
	public LayuiPage<SaleForecast> getSaleFcstByPage(Map<String, Object> map);
	
	/**
	 * 获取分页获取 销售预测 主表数量
	 * @param map
	 * @return
	 */
	public int getSaleFcstCount(Map<String, Object> map);
	
	/**
	 * 根据条件获取单条 销售预测  主表信息
	 * @param map
	 * @return
	 */
	public SaleForecast getSaleFcstById(String id);
	
	/**
	 * 根据条件获取 销售预测  主表信息
	 * @param map
	 * @return
	 */
	public List<SaleForecast> getSaleFcstByCondition(Map<String,Object> map);
	
	/**
	 * 根据ID删除 销售预测  信息
	 * @param id
	 * @return
	 */
	public int delSaleFcstById(String id) throws Exception;
	
	/**
	 * 根据IDs批量删除 销售预测 信息
	 * @param ids
	 * @return
	 */
	public int delBatchSaleFcstInfoByIds(List<String> ids) throws Exception;
	
	/**
	 * 保存 销售预测  信息
	 * @param saleForecast
	 * @param detailList
	 * @param isImport
	 */
	public void saveSaleFcstInfo(SaleForecast saleForecast,List<SaleFcstDetail> detailList,int isImport)
		throws Exception;
	
	/**
	 * 修改 销售预测 主表 的状态
	 * @param map
	 * @return
	 */
	public int updateSaleFcstStatus(Map<String,Object> map) throws Exception;
		
	/**
	 * 获取 销售预测  明细列表信息
	 * @param mainId
	 * @return
	 */
	public List<SaleFcstDetail> getSaleFcstDetailListByMainId(String mainId);
	
	/**
	 * 获取 销售预测 明细数量
	 * @param mainId
	 * @return
	 */
	public int getSaleFcstDetailCount(String mainId);
	
	/**
	 * 保存 导入的销售预测  信息
	 * @param saleForecast
	 * @param detailList
	 */ 
	public void saveImpSaleFcstInfo(SaleForecast saleForecast,List<SaleFcstDetail> detailList)
		throws Exception;
	
	/**
	 * 获取物料的月份销售预测
	 * @param map
	 * @return
	 */
	public List<SaleFcstDetail> getSaleFcstDataByCxjh(Map<String,Object> map);
	
	/**
	 * 获取根据系列、品项汇总的导出数据
	 * @param mainId
	 * @return
	 */
	public List<SaleFcstDetail> getSaleFcstSumByMainId(String mainId);
	
}
