package com.faujor.service.rm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.rm.UserSuppMate;

public interface UserSuppMateService {
	/**
	 * 获取货源一览列表数据
	 * @param map
	 * @return
	 */
	Map<String, Object> getUserSuppMateListByPage(Map<String, Object> map);
	/**
	 * //查询采购货源数据集合
	 * @param userSuppMate
	 * @return
	 */
	List<UserSuppMate> getUserSuppMateList(UserSuppMate userSuppMate);

}
