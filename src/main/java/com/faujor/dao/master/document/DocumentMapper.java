package com.faujor.dao.master.document;

import java.util.List;
import java.util.Map;

import com.faujor.entity.document.Document;

public interface DocumentMapper {
	/**
	 * 分页获取文档数据
	 * @param map
	 * @return
	 */
	List<Document> getDocByPage(Map<String, Object> map);
	/**
	 * 获取分页总数据条数
	 * @param map
	 * @return
	 */
	Integer getDocNum(Map<String, Object> map);
	/**
	 * 根据Id或者realName获取该文档信息
	 * @param map
	 * @return
	 */
	Document getDoc(Map<String, Object> map);
	/**
	 * 删除文档信息
	 * @param map
	 */
	void deleteDoc(String id);
	/**
	 * 更新文档信息
	 * @param doc
	 */
	void updateDocLink(Document doc);
	/**
	 * 保存文档信息
	 * @param doc
	 */
	void saveDoc(Document doc);
	/**
	 * 根据关联编号获取附件列表
	 * @param linkNo
	 * @return
	 */
	List<Document> getDocByLinkNo(Map<String, Object> map);
	/**
	 * 根据关联编号信息删除附件列表
	 * @param map
	 */
	void deleteDocByLinkNo(Map<String, Object> map);
	/**
	 * 获取页面的关联文档
	 * @param menuId
	 * @return
	 */
	List<Document> getDocByMenuId(Long menuId);
}
