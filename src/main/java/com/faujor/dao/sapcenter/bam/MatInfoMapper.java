package com.faujor.dao.sapcenter.bam;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.faujor.entity.sapcenter.bam.OraCxjhEntity;
import com.faujor.entity.sapcenter.bam.PadPlanMatDetail;
import com.faujor.entity.sapcenter.bam.TranPlanMatDetail;

/**
 * 中间表 T_ORA_CXJH 的物料信息处理接口
 * @author Vincent
 *
 */
public interface MatInfoMapper {
	/**
	 * 根据年月获取物料的信息
	 * @param year
	 * @param month
	 * @return
	 */
	public List<OraCxjhEntity> getMatInfoByYm(@Param("year")String year,@Param("month")String month);
	
	/**
	 * 根据条件获取 生产/交货计划 物料信息
	 * @param map
	 * @return
	 */
	public List<PadPlanMatDetail> getPadPlanMatInfo(Map<String,Object> map);
	
	/**
	 * 根据条件获取 调拨任务 物料信息
	 * @param map
	 * @return
	 */
	public List<TranPlanMatDetail> getTranPlanMatInfo(Map<String,Object> map);
}
