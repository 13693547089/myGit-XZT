package com.faujor.service.sapcenter.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.sapcenter.bam.OraCxjhEntity;
import com.faujor.entity.sapcenter.bam.PadPlanMatDetail;
import com.faujor.entity.sapcenter.bam.TranPlanMatDetail;

/**
 * 物料信息 服务类
 * @author Vincent
 *
 */
public interface MatInfoService {
	/**
	 * 物料信息 获取
	 * @param map
	 * @return
	 */
	public List<OraCxjhEntity> getMatInfoByYm(String year,String month);
	
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
