package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.ContTemp;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.utils.RestCode;

public interface ContTempService {
	/**
	 * 分页获取文档表格
	 * @param map
	 * @return
	 */
	LayuiPage<ContTemp> getTempByPage(Map<String, Object> map);
	/**
	 * 获取文档 根据文档名或ID
	 * @param map
	 * @return
	 */
	ContTemp getTemp(Map<String, Object> map);
	/**
	 * 保存模板信息
	 * @param temp
	 * @param docs
	 */
	void saveTemp(ContTemp temp,List<Document> docs);
	/**
	 * 更新模板
	 * @param temp
	 * @param docs
	 * @param tempMenus
	 */
	void updateTemp(ContTemp temp, List<Document>docs);
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
	 * 删除合同模板
	 * @param tempNo
	 */
	void deleteContTemp(List<String> tempNos);
	/**
	 * 更新合同模板状态
	 * @param tempNos
	 * @param status
	 */
	void changeTempStatus(List<String> tempNos,String status);
}
