package com.faujor.service.sapcenter.bam.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.sapcenter.bam.MatInfoMapper;
import com.faujor.entity.sapcenter.bam.OraCxjhEntity;
import com.faujor.entity.sapcenter.bam.PadPlanMatDetail;
import com.faujor.entity.sapcenter.bam.TranPlanMatDetail;
import com.faujor.service.sapcenter.bam.MatInfoService;

/**
 * 物料信息实现类
 * @author Vincent
 *
 */
@Service
public class MatInfoServiceImpl implements MatInfoService {

	@Autowired
	private MatInfoMapper matInfoMapper;
	
	/**
	 * 根据年月获取 物料信息 
	 * @param map
	 * @return
	 */
	@Override
	public List<OraCxjhEntity> getMatInfoByYm(String year, String month) {
		return matInfoMapper.getMatInfoByYm(year, month);
	}

	/**
	 * 根据条件获取 生产/交货计划 物料信息
	 * @param map
	 * @return
	 */
	@Override
	public List<PadPlanMatDetail> getPadPlanMatInfo(Map<String, Object> map) {
		return matInfoMapper.getPadPlanMatInfo(map);
	}

	/**
	 * 根据条件获取 调拨任务 物料信息
	 * @param map
	 * @return
	 */
	@Override
	public List<TranPlanMatDetail> getTranPlanMatInfo(Map<String,Object> map){
		return matInfoMapper.getTranPlanMatInfo(map);
	}
}
