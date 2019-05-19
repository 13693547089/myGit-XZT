package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.psm.UserSeries;
import com.faujor.entity.bam.psm.UserSeriesOrder;
import com.faujor.entity.common.LayuiPage;

/**
 * 配置服务类
 * ：用户系列配置
 * 
 * @ClassName:   ConfigService
 * @Instructions:
 * @author: Vincent
 * @date:   2018年9月3日 下午5:56:57
 *
 */
public interface ConfigService {
	/**
	 * 根据条件获取用户系列数据
	 * @param map
	 * @return
	 */
	public List<UserSeries> getUserSeriesByMap(Map<String,Object> map);
	
	/**
	 * 保存用户系列数据
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int saveUserSeriesInfo(List<UserSeries> list) throws Exception;
	
	/**
	 * 根据条件删除用户系列数据
	 * @param map
	 * @return
	 */
	public int delUserSeriesInfo(Map<String,Object> map) throws Exception;
	
	/**
	 * 批量删除系列
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int delBatchUserSeriesData(String[] ids) throws Exception;
	
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
	public LayuiPage<UserSeriesOrder> getSeriesOrderUserByPage(Map<String,Object> map);
	
	/**
	 * 获取用户系列排序的用户数量
	 * @param map
	 * @return
	 */
	public int getSeriesOrderUserByCount(Map<String,Object> map);
	
	/**
	 * 保存用户系列排序的用户数据
	 * @param seriesOrderUserList
	 * @return
	 */
	public int saveSeriesOrderUserData(List<UserSeriesOrder> seriesOrderUserList) throws Exception;
	
	/**
	 * 删除用户系列排序的数据
	 * :用户数据、对应系列数据
	 * @param idsList
	 * @return
	 */
	public int delSeriesOrderUserInfo(List<String> idsList) throws Exception;
	
	/**
	 * 获取用户对应的系列排序数据
	 * @param parentId
	 * @return
	 */
	public List<UserSeriesOrder> getUserSeriesOrderDetail(String parentId);
	
	/**
	 * 保存用户系列排序的数据
	 * @param userSeriesOrderList
	 * @return
	 */
	public int saveUserSeriesOrderDetail(List<UserSeriesOrder> userSeriesOrderList) throws Exception;
	
	/**
	 * 删除用户系类排序的数据
	 * @param parentId
	 * @return
	 */
	public int delUserSeriesOrderDetail(String parentId) throws Exception;
	
	/**
	 * 删除用户系列排序的数据(多条数据)
	 * @param idsArr
	 * @return
	 */
	public int delUserSeriesOrderInfo(String[] idsArr) throws Exception;
}
