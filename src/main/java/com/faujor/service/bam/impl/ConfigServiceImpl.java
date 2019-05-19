package com.faujor.service.bam.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faujor.dao.master.bam.ConfigMapper;
import com.faujor.entity.bam.psm.UserSeries;
import com.faujor.entity.bam.psm.UserSeriesOrder;
import com.faujor.entity.common.LayuiPage;
import com.faujor.service.bam.ConfigService;

/**
 * 产销人员配置服务实现类
 * 
 * @ClassName:   ConfigServiceImpl
 * @Instructions:
 * @author: Vincent
 * @date:   2018年9月3日 下午6:00:12
 *
 */
@Service
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	private ConfigMapper configMapper;
	
	/**
	 * 根据条件获取用户系列数据
	 * @param map
	 * @return
	 */
	@Override
	public List<UserSeries> getUserSeriesByMap(Map<String, Object> map) {
		return configMapper.getUserSeriesByMap(map);
	}

	/**
	 * 保存用户系列数据
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@Override
	public int saveUserSeriesInfo(List<UserSeries> list) throws Exception {
		if(list.size()>0){
			// 用户编码
			String userCode = list.get(0).getUserCode();
			
			// 删除原有数据
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userCode", userCode);
			map.put("list", list);
			
			// 重新插入
			configMapper.insertUserSeriesInfo(map);
		}
		return 1;
	}

	/**
	 * 根据条件删除用户系列数据
	 * @param map
	 * @return
	 */
	@Transactional
	@Override
	public int delUserSeriesInfo(Map<String, Object> map) throws Exception {
		configMapper.delUserSeriesInfo(map);
		return 1;
	}

	/**
	 * 批量删除用户系列数据
	 * @param map
	 * @return
	 */
	@Transactional
	@Override
	public int delBatchUserSeriesData(String[] ids) throws Exception {
		if(ids.length>0){
			for(int i=0;i<ids.length;i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", ids[i]);
				configMapper.delUserSeriesInfo(map);
			}
		}
		return 1;
	}
	
	/**
	 * 获取物料的系列数据
	 * @param map
	 * @return
	 */
	@Override
	public List<UserSeries> getMaterialSeriesData(Map<String,Object> map){
		return configMapper.getMaterialSeriesData(map);
	}
	
	/**
	 * 获取用户对应的系列数据
	 * @param map
	 * @return
	 */
	@Override
	public List<UserSeries> getUserSeriesData(Map<String,Object> map){
		return configMapper.getUserSeriesData(map);
	}
	
	/***********************用户系列排序**************************/
	
	/**
	 * 获取用户系列排序的用户数据
	 * @param map
	 * @return
	 */
	@Override
	public LayuiPage<UserSeriesOrder> getSeriesOrderUserByPage(Map<String,Object> map){
		
		LayuiPage<UserSeriesOrder> page = new LayuiPage<UserSeriesOrder>();
		
		List<UserSeriesOrder> list = configMapper.getSeriesOrderUserByPage(map);
		int count = configMapper.getSeriesOrderUserByCount(map);
		
		page.setCount(count);
		page.setData(list);
		
		return page;
	}
	
	/**
	 * 获取用户系列排序的用户数量
	 * @param map
	 * @return
	 */
	@Override
	public int getSeriesOrderUserByCount(Map<String,Object> map){
		return configMapper.getSeriesOrderUserByCount(map);
	}
	
	/**
	 * 保存用户系列排序的用户数据
	 * @param seriesOrderUserList
	 * @return
	 */
	@Override
	@Transactional
	public int saveSeriesOrderUserData(List<UserSeriesOrder> seriesOrderUserList) throws Exception{
		return configMapper.insertSeriesOrderUser(seriesOrderUserList);
	}
	
	/**
	 * 删除用户系列排序的数据
	 * :用户数据、对应系列数据
	 * @param idsList
	 * @return
	 */
	@Override
	@Transactional
	public int delSeriesOrderUserInfo(List<String> idsList) throws Exception{
		for(int i=0;i<idsList.size();i++){
			configMapper.delUserSeriesOrderDetail(idsList.get(i));
			configMapper.delSeriesOrderUserInfo(idsList.get(i));
		}
		return 1;
	}
	
	/**
	 * 获取用户对应的系列排序数据
	 * @param parentId
	 * @return
	 */
	@Override
	public List<UserSeriesOrder> getUserSeriesOrderDetail(String parentId){
		return configMapper.getUserSeriesOrderDetail(parentId);
	}
	
	/**
	 * 保存用户系列排序的数据
	 * @param userSeriesOrderList
	 * @return
	 */
	@Override
	@Transactional
	public int saveUserSeriesOrderDetail(List<UserSeriesOrder> userSeriesOrderList) 
			throws Exception {
		String parentId = userSeriesOrderList.get(0).getParentId();
		configMapper.delUserSeriesOrderDetail(parentId);
		configMapper.insertUserSeriesOrderDetail(userSeriesOrderList);
		
		return 1;
	}

	/**
	 * 删除用户对应系列排序的数据
	 * @param parentId
	 * @return
	 */
	@Override
	@Transactional
	public int delUserSeriesOrderDetail(String parentId) throws Exception{
		return configMapper.delUserSeriesOrderDetail(parentId);
	}
	
	/**
	 * 删除用户系列排序的数据(多条数据)
	 * @param idsArr
	 * @return
	 */
	@Override
	@Transactional
	public int delUserSeriesOrderInfo(String[] idsArr) throws Exception{
		for(int i=0;i<idsArr.length;i++){
			configMapper.delSeriesOrderUserInfo(idsArr[i]);
		}
		return 1;
	}
}
