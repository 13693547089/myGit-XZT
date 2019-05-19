package com.faujor.service.document;

import java.util.List;
import java.util.Map;

import javax.print.Doc;

import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.MenuDO;
import com.faujor.entity.common.Tree;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.entity.document.TempMenu;
import com.faujor.entity.document.Template;
import com.faujor.utils.RestCode;

public interface TemplateService {
	/**
	 * 分页获取文档表格
	 * @param map
	 * @return
	 */
	LayuiPage<Template> getTempByPage(Map<String, Object> map);
	/**
	 * 获取文档 根据文档名或ID
	 * @param map
	 * @return
	 */
	Template getTemp(Map<String, Object> map);
	/**
	 * 保存模板信息
	 * @param temp
	 * @param docs
	 */
	void saveTemp(Template temp,List<Document> docs,List<TempMenu> tempMenus);
	/**
	 * 获取模板的关联页面的树
	 * @param tempNo
	 * @return
	 */
	Tree<MenuDO> getTree(String tempNo);
	/**
	 * 获取模板的菜单列表
	 * @param tempNo
	 * @return
	 */
	List<MenuDO> getMenuList(String tempNo);
	/**
	 * 获取选中的菜单
	 * @param menuIds
	 * @return
	 */
	List<MenuDO> getCkeckedMenus(List<Long> menuIds);
	/**
	 * 更新模板
	 * @param temp
	 * @param docs
	 * @param tempMenus
	 */
	void updateTemp(Template temp, List<Document>docs,List<TempMenu> tempMenus);
	/**
	 * 根据模板编号获取模板操作信息
	 * @param tempNo
	 * @return
	 */
	List<TempLog> getTempLogByTempNo(String tempNo);
	/**
	 * 删除附件信息
	 * @param docIds
	 */
	RestCode deleteFile(List<String> docIds);
	
	/**
	 * 删除文件模板
	 * @param temp
	 */
	void deleteTemp(List<String> tempNos);
	/**
	 * 根据菜单Id获取菜单的关联文档
	 * @param menuId
	 * @return
	 */
	List<Document> getDocByMenuId(Long menuId);
}
