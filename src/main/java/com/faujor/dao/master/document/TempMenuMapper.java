package com.faujor.dao.master.document;

import java.util.List;
import java.util.Map;

import com.faujor.entity.common.MenuDO;
import com.faujor.entity.document.TempMenu;

public interface TempMenuMapper {
	/**
	 * 保存模板菜单关联信息
	 * 
	 * @param tempMenu
	 */
	void saveTempMenu(TempMenu tempMenu);

	/**
	 * 删除菜单权限关系
	 * 
	 * @param
	 */
	void deleteTempMenuByTempNo(Map<String, Object> map);

	/**
	 * 获取模板的关联页面列表
	 * 
	 * @param tempNo
	 * @return
	 */
	List<MenuDO> listMenuByTempNo(String tempNo);
}
