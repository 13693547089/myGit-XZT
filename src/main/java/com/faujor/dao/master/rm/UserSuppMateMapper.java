package com.faujor.dao.master.rm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.rm.UserSuppMate;

public interface UserSuppMateMapper {
	/**
	 * 获取货源一览列表数据
	 * @param map
	 * @return
	 */
	List<UserSuppMate> getUserSuppMateListByPage(Map<String, Object> map);
	
	/**
	 * 获取货源一览列表数据的总数量
	 * @param map
	 * @return
	 */
	int getUserSuppMateListByPageCount(Map<String, Object> map);
	/**
	 * 查询采购货源数据集合
	 * @param userSuppMate
	 * @return
	 */
	List<UserSuppMate> getUserSuppMateList(UserSuppMate userSuppMate);

}
