package com.faujor.service.mdm;

import java.util.List;
import java.util.Map;

import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.mdm.QualPapers;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.mdm.QualSuppDO;
import com.faujor.entity.privileges.UserDO;

public interface QualSuppService {

	/**
	 * 合格供应商分页展示
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryQualSuppByPage(Map<String, Object> map);

	/**
	 * 查询合格供应商的详细信息
	 * 
	 * @param suppId
	 * @return
	 */
	QualSupp queryOneQualSuppbySuppId(String suppId);

	/**
	 * 查询一个采购员下的供应商
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, Object> queryQualSuppByUserId(Map<String, Object> map);

	/**
	 * 查询多个采购员下的供应商
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, Object> queryQualSuppByUserIds(Map<String, Object> map);

	/**
	 * 移除采购员下的供应商
	 * 
	 * @param userId
	 * @param suppIds
	 * @return
	 */
	boolean deleteQualSuppOfUser(Integer userId, String[] suppIds);

	/**
	 * 查询所有供应商
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryAllQualSupp();

	/**
	 * 给采购员添加供应商
	 * 
	 * @param userSupp
	 * @return
	 */
	boolean addQualSuppForUser(Integer userId, String[] suppIds);

	/**
	 * 根据供应商名称查询供应商
	 * 
	 * @param suppName
	 * @return
	 */
	QualSupp queryQualSuppBySuppName(String suppName);

	/**
	 * 根据供应商的sapId查询供应商信息
	 * 
	 * @param username
	 * @return
	 */
	QualSupp queryQualSuppBySapId(String sapId);

	/**
	 * 根据物料编码查询提供这项物料的所有合格供应商
	 * 
	 * @param mateCode
	 * @return
	 */
	List<QualSupp> queryQualSuppOfMateByMateCode(Map<String, Object> map);

	/**
	 * 根据供应商的SAP编码获取供应商列表
	 * 
	 * @param sapCodes
	 * @return
	 */
	List<QualSupp> queryQualSuppBySapCodes(List<String> sapCodes);

	/**
	 * 同步合格供应商的数据
	 * 
	 * @param al
	 * 
	 * @return
	 */
	int asyncSuppInfo(AsyncLog al);

	/**
	 * 根据合格供应商的sap编码和物料的编码查询采购员编号
	 * 
	 * @param suppId
	 * @param MateId
	 * @return
	 */
	String queryByuerIdBySapIdAndMateCode(String sapId, String mateCode);

	/**
	 * 查询合格供应商的所有编码（主键）
	 * 
	 * @return
	 */
	List<String> queryAllSuppIdOfQualSupp();

	/**
	 * 查询采购员对应合格供应商的sapId
	 * 
	 * @param users
	 * @return
	 */
	List<String> findSuppCodesByUsers(List<UserDO> users);

	/**
	 * 查询所有供应商分页
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryAllQualSuppByPage(Map<String, Object> map);

	/**
	 * 编辑合格供应商的附近
	 * 
	 * @param suppId
	 * @param list
	 * @return
	 */
	boolean updatePaperOfQualSuppBySuppId(String suppId, List<QualPapers> list);

	/**
	 * 查询合格供应商的附件
	 * 
	 * @param suppId
	 * @return
	 */
	List<QualPapers> queryPapersOfQualSuppBysuppId(String suppId);

	/**
	 * 查询一个采购员的所有合格供应商，返回一个List集合
	 * 
	 * @param userId
	 * @return
	 */
	List<QualSupp> queryAllQualSuppListOfUser(String userId);

	/**
	 * 查询一个采购员的所有合格供应商，返回一个List集合
	 * 
	 * @param userId
	 * @return
	 */
	List<QualSupp> queryAllQualSuppListOfUsers(Map<String, Object> map);

	/**
	 * 根据 条件查询合格供应商信息
	 * 
	 * @param QualSuppDO
	 * @return
	 */
	List<QualSuppDO> queryQualSuppByQualSupp(QualSupp qualSupp);

	/**
	 * 根据 条件查询合格供应商信息
	 * 
	 * @param QualSuppDO
	 * @return
	 */
	List<String> queryQualSuppConcatColByQualSupp(QualSupp qualSupp);
	/**
	 * 通过供应商子范围编码和供应商的SAP编码，查询供应商主数据中的供应商子范围描述
	 * @param suppNo
	 * @param suppRange
	 * @return
	 */
	String getSuppRangeDescBySapIdAndSuppRange(String suppNo, String suppRange);

}
