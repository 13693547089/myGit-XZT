package com.faujor.service.document;

import java.util.List;
import java.util.Map;

import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.document.Document;
import com.faujor.utils.RestCode;

public interface DocumentService {
	/**
	 * 分页获取文档表格
	 * @param map
	 * @return
	 */
	LayuiPage<Document> getDocByPage(Map<String, Object> map);
	/**
	 * 获取文档 根据文档名或ID
	 * @param map
	 * @return
	 */
	Document getDoc(Map<String, Object> map);
	/**
	 * 保存多个文件数据
	 * @param docs
	 */
	void saveDocs(List<Document> docs);
	/**
	 * 根据关联单号获取该单据
	 * @param map
	 * @return
	 */
	List<Document> getDocByLinkNo(Map<String, Object> map);
	/**
	 * 删除附件
	 * @param docIds
	 */
	void deleteDocs(List<String> docIds);
	/**
	 * 删除附件
	 * @param docIds
	 * @return
	 */
	 RestCode deleteFile(List<String> docIds);

}
