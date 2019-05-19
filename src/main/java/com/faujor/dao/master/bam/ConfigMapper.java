package com.faujor.dao.master.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.UserSeries;
import com.faujor.entity.bam.psm.UserSeriesOrder;

/**
 * 配置管理Mapper
 * 
 * @ClassName:   ConfigMapper
 * @Instructions:
 * @author: Vincent
 * @date:   2018年8月31日 下午5:57:28
 *
 */
public interface ConfigMapper {
	
	/**
	 * 根据条件获取用户系列数据
	 * @param map
	 * @return
	 */
	public List<UserSeries> getUserSeriesByMap(Map<String,Object> map);
	
	/**
	 * 保存用户系列数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int insertUserSeriesInfo(Map<String,Object> map);
	
	/**
	 * 根据条件删除用户系列数据
	 * @param map
	 * @return
	 */
	public int delUserSeriesInfo(Map<String,Object> map);
	
	/**
	 * 获取物料的系列数据
	 * @param map
	 * @return
	 */
	public List<UserSeries> getMaterialSeriesData(Map<String,Object> map);
	
	/**
	 * 获取用户对应的系列数据
	 * @param map
	 * @return
	 */
	public List<UserSeries> getUserSeriesData(Map<String,Object> map);
	
	/***********************用户系列排序**************************/
	/**
	 * 获取用户系列排序的用户数据
	 * @param map
	 * @return
	 */
	public List<UserSeriesOrder> getSeriesOrderUserByPage(Map<String,Object> map);

	/**
	 * 获取用户系列排序的用户数量
	 * @param map
	 * @return
	 */
	public int getSeriesOrderUserByCount(Map<String,Object> map);
	
	/**
	 * 插入用户系列排序的用户信息
	 * @param seriesOrderList
	 * @return
	 */
	public int insertSeriesOrderUser(List<UserSeriesOrder> seriesOrderList);
	
	/**
	 * 删除用户系列排序的用户或单个排序明细信息
	 * @param parentId
	 * @return
	 */
	public int delSeriesOrderUserInfo(String parentId);
	
	/**
	 * 获取用户对应的系列排序数据
	 * @param map
	 * @return
	 */
	public List<UserSeriesOrder> getUserSeriesOrderDetail(String parentId);
	
	/**
	 * 插入用户对应系列排序的数据
	 * @param list
	 * @return
	 */
	public int insertUserSeriesOrderDetail(List<UserSeriesOrder> list);
	
	/**
	 * 删除用户对应系列排序的数据
	 * @param parentId
	 * @return
	 */
	public int delUserSeriesOrderDetail(String parentId);
}
