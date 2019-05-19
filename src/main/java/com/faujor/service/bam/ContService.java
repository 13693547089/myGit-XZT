package com.faujor.service.bam;

import java.util.List;
import java.util.Map;

import com.faujor.entity.bam.Contract;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.utils.RestCode;

public interface ContService {
	/**
	 * 分页获取合同表格
	 * @param map
	 * @return
	 */
	LayuiPage<Contract> getContByPage(Map<String, Object> map);
	/**
	 * 获取文档 根据合同编码或ID
	 * @param map
	 * @return
	 */
	Contract getCont(Map<String, Object> map);
	/**
	 * 保存合同信息
	 * @param cont
	 * @param docs
	 */
	void saveCont(Contract cont,List<Document> docs);
	/**
	 * 更新合同
	 * @param cont
	 * @param docs
	 * @param tempMenus
	 */
	void updateCont(Contract cont, List<Document>docs);
	/**
	 * 根据合同编码获取合同信息
	 * @param contNo
	 * @return
	 */
	List<TempLog> getTempLogByContNo(String contId);
	/**
	 * 删除附件信息
	 * @param docIds
	 */
	RestCode deleteFile(List<String> docIds);
	/**
	 * 删除合同
	 * @param tempNo
	 */
	void deleteCont(List<String> contIds);
	/**
	 * 更新合同状态
	 * @param tempNos
	 * @param status
	 */
	void changeContStatus(List<String> contIds,String status);
	
	/**
	 * 判断合同编码是否存在
	 * @param id
	 * @param contCode
	 * @return
	 */
	boolean checkIsExist(String id,String contCode);
}
