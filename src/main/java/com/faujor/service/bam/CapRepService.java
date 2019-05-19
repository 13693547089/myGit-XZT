package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.CapRep;
import com.faujor.entity.bam.psm.CapRepMate;
import com.faujor.entity.bam.psm.CapRepOrder;
import com.faujor.entity.bam.psm.CapRepStock;
import com.faujor.entity.common.LayuiPage;

public interface CapRepService {
	/**
	 * 根据ID进行查询
	 * @param id
	 * @return
	 */
	CapRep getById(String id);
	/**
	 * 月报保存
	 * @param capRep
	 * @param mates
	 * @param orders
	 * @param stocks
	 * @return
	 */
	int save(CapRep capRep,List<CapRepMate> mates,List<CapRepOrder> orders,List<CapRepStock> stocks);
	/**
	 * 月报更新
	 * @param capRep
	 * @param mates
	 * @param orders
	 * @param stocks
	 * @return
	 */
	int update(CapRep capRep,List<CapRepMate> mates,List<CapRepOrder> orders,List<CapRepStock> stocks);
	/**
	 * 分页获取月报
	 * @param map
	 * @return
	 */
	LayuiPage<CapRep> getCapRepByPage(Map<String, Object> map);
	/**
	 * 获取月报物料信息
	 * @param mainId
	 * @return
	 */
	List<CapRepMate> getMateByMainId(String mainId);
	/**
	 * 获取月报订单信息
	 * @param mainId
	 * @return
	 */
	List<CapRepOrder> getOrderByMainId(String mainId);
	/**
	 * 获取月报库存信息
	 * @param mainId
	 * @return
	 */
	List<CapRepStock> getStockByMainId(String mainId);	
	/**
	 * 获取供应商物料
	 * @param mainId
	 * @return
	 */
	List<CapRepMate> getMateBySuppNo(String suppNo,String mainId);
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	int delete(List<String> ids);
	/**
	 * 更新月报状态
	 * @param ids
	 * @param status
	 * @return
	 */
	int updateStstus(List<String> ids,String status);
	/**
	 * 校验产能月报是否重复
	 * @param map
	 * @return
	 */
	boolean checkRepeat(Map<String,Object> map);
}
