package com.faujor.dao.master.mdm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.faujor.entity.cluster.mdm.SupplierDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.mdm.QualSuppDO;
import com.faujor.entity.mdm.UserSupp;
import com.faujor.entity.privileges.UserDO;

public interface QualSuppMapper {

	/**
	 * 合格供应商的添加
	 * 
	 * @param qualSupp
	 * @return
	 */
	public int insertQualSupp(QualSupp qualSupp);

	/**
	 * 分页展示合格供应商列表
	 * 
	 * @param map
	 * @return
	 */
	public List<QualSupp> queryQualSuppByPage(Map<String, Object> map);

	/**
	 * 查询总条数
	 * 
	 * @param map
	 * @return
	 */
	public int queryQualSuppCountbyPage(Map<String, Object> map);

	/**
	 * 根据供应商编码查询合格供应商的信息
	 * 
	 * @param suppId
	 * @return
	 */
	public QualSupp queryOneQualSuppBySuppId(String suppId);

	/**
	 * 根据用户（采购员）的编号查询这个采购员下的供应商
	 * 
	 * @param userId
	 * @return
	 */
	public List<QualSupp> queryQualSuppByUserId(Map<String, Object> map);

	/**
	 * 根据用户（采购员）的编号查询这个采购员下供应商的总数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryQualSuppByUserIdCount(Map<String, Object> map);

	/**
	 * 删除采购员下的供应商
	 * 
	 * @param map
	 * @return
	 */
	public int deleteQualSuppOfUser(Integer userId, String suppId);

	/**
	 * 查询所有合格供应商
	 * 
	 * @param map
	 * @return
	 */
	public List<QualSupp> queryAllQualSupp();

	/**
	 * 查询所有合格供应商的数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryAllQualSuppCount();

	/**
	 * 为采购员添加合格供应商
	 * 
	 * @return
	 */
	public int addQualSuppForUser(UserSupp userSupp);

	/**
	 * 根据合格供应商的名称查询供应商信息
	 * 
	 * @param suppName
	 * @return
	 */
	public QualSupp queryQualSuppBySuppName(String suppName);

	/**
	 * 根据供应商的sapId查询供应商信息
	 * 
	 * @param sapId
	 * @return
	 */
	public QualSupp queryQualSuppBySapId(String sapId);

	/**
	 * 根据物料编码查询提供这项物料的所有合格供应商
	 * 
	 * @param mateCode
	 * @return
	 */
	public List<QualSupp> queryQualSuppOfMateByMateCode(Map<String, Object> map);

	/**
	 * 根据供应商的SAP编码获取供应商列表
	 * 
	 * @param sapCodes
	 * @return
	 */
	List<QualSupp> queryQualSuppBySapCodes(@Param("sapCodes") List<String> sapCodes);

	/**
	 * 查询所有的合格供应商编码
	 * 
	 * @return
	 */
	@Select("select sap_id as sapId from mdm_qual_supp ")
	public List<String> findQualSuppCode();

	public int updateQualSuppFromEDI(SupplierDO sd);

	public int batchSaveSuppFromEDI(List<SupplierDO> addList);

	/**
	 * 根据合格供应商的sap编码和物料的编码查询采购员编号
	 * 
	 * @param sapId
	 * @param mateCode
	 * @return
	 */
	public String queryByuerIdBySapIdAndMateCode(String sapId, String mateCode);

	/**
	 * 查询合格供应商的所有主键
	 * 
	 * @return
	 */
	public List<String> queryAllSuppIdOfQualSupp();

	/**
	 * 查询采购员对应合格供应商的sapId
	 * 
	 * @param users
	 * @return
	 */
	public List<String> findSuppCodesByUsers(List<UserDO> users);

	/**
	 * 查询所有供应商分页数据
	 * 
	 * @return
	 */
	public List<QualSupp> queryAllQualSuppByPage(Map<String, Object> map);

	/**
	 * 查询所有供应商分页，供应商数量
	 * 
	 * @return
	 */
	public int queryAllQualSuppByPageCount(Map<String, Object> map);

	/**
	 * 查询多个采购员下的供应商
	 * 
	 * @param map
	 * @return
	 */
	public List<QualSuppDO> queryQualSuppByUserIds(Map<String, Object> map);

	/**
	 * 查询多个采购员下供应商的总数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryQualSuppByUserIdsCount(Map<String, Object> map);

	/**
	 * 根据suppId和mateId查询采购员
	 * 
	 * @param suppId
	 * @param mateId
	 * @return
	 */
	public String queryBuyerIdBySuppIdAndMateId(String suppId, String mateId);

	/**
	 * 查询一个采购员的所有合格供应商，返回一个List集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<QualSupp> queryAllQualSuppListByUserId(String userId);

	/**
	 * 查询多个采购员的所有合格供应商，返回一个List集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<QualSupp> queryAllQualSuppListByUserIds(Map<String, Object> map);

	/**
	 * 查询供应商信息
	 * 
	 * @param map
	 * @return
	 */
	public List<QualSupp> findQualSuppInfoByParams(Map<String, Object> map);

	/**
	 * 查询负责这个供应商的所有采购员编号
	 * 
	 * @param suppId
	 * @return
	 */
	public List<String> getUserIdsOfSuppBySuppId(String suppId);

	/**
	 * 根据供应商的sapId和物料的mateId查询货源清单
	 * 
	 * @param suppId
	 * @param mateId
	 * @return
	 */
	public List<String> getUserIdsBySapIdAndMateId(String sapId, String mateId);

	/**
	 * 查询多个采购员所对应的供应商
	 * 
	 * @param userList
	 * @return
	 */
	public List<QualSupp> queryQualSuppListByUserDOList(Map<String, Object> param);

	/**
	 * 根据用户和供应商数据删除货源配置信息
	 * 
	 * @param map
	 */
	public void deleteSuppMateOfUserByUserIdAndSuppIdList(Map<String, Object> map);

	/**
	 * 根据条件查询出合格供应商信息
	 * 
	 * @param qualSupp
	 * @return
	 */
	public List<QualSuppDO> queryQualSuppByQualSupp(QualSupp qualSupp);

	/**
	 * 根据条件查询出合格供应商信息
	 * 
	 * @param qualSupp
	 * @return
	 */
	public List<String> queryQualSuppConcatColByQualSupp(QualSupp qualSupp);
	/**
	 * 通过供应商子范围编码和供应商的SAP编码，查询供应商主数据中的供应商子范围描述
	 * @param suppNo
	 * @param suppRange
	 * @return
	 */
	public String getSuppRangeDescBySapIdAndSuppRange(@Param("suppNo")String suppNo, @Param("suppRange")String suppRange);

}
