package com.faujor.dao.master.mdm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.cluster.mdm.MaterialDO;
import com.faujor.entity.cluster.mdm.MaterialUnitDO;
import com.faujor.entity.common.BaseEntity;
import com.faujor.entity.mdm.MateBasicInfo;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.MateUnit;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.UserSuppMate;
import com.faujor.entity.privileges.UserDO;

public interface MaterialMapper {

	/**
	 * 添加物料信息
	 * 
	 * @param mate
	 * @return
	 */
	public int insertMaterial(Material mate);

	/**
	 * 物料主数据分页展示
	 * 
	 * @param map
	 * @return
	 */
	public List<Material> queryMaterialByPage(Map<String, Object> map);

	/**
	 * 物料的总条数
	 * 
	 * @param map
	 * @return
	 */
	public int queryMaterialCount(Map<String, Object> map);

	/**
	 * 查询物料的详细信息
	 * 
	 * @param mateId
	 * @return
	 */
	public Material queryOneMaterialByMateId(String mateId);

	/**
	 * 查询采购员下的物料数据
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> queryMaterialOfUser(Map<String, Object> map);

	/**
	 * 根据采购员列表，获取所有物料数据
	 * 
	 * @param users
	 * @return
	 */
	public List<MateDO> findMaterialByUsers(List<UserDO> users);

	public int queryMaterialOfUserCount(Map<String, Object> map);

	/**
	 * 删除采购下的物料
	 * 
	 * @param map
	 * @return
	 */
	public int deleteMaterialOfUser(Map<String, Object> map);

	/**
	 * 添加物料到采购下
	 * 
	 * @param usm
	 * @return
	 */
	public int addMaterialOfUser(UserSuppMate usm);

	/**
	 * 查询采购员下某个供应商所对应的物料
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> queryMaterialOfUserAndSupp(Map<String, Object> map);

	/**
	 * 删除采购员下某个供应商所对应的物料
	 * 
	 * @return
	 */
	public int deleteMaterialOfUserAndSupp(Map<String, Object> map);

	/**
	 * 为采购员下的某个供应商分配物料
	 * 
	 * @return
	 */
	public int addMaterialForUserAndSupp(UserSuppMate usm);

	/**
	 * 查询所有物料
	 * 
	 * @return
	 */
	public List<MateDO> queryAllMaterial();

	/**
	 * 查询分配给供应商的所有物料
	 * 
	 * @param suppId
	 * @return
	 */
	public List<MateDO> queryAllMaterialOfSupp(String suppId);

	/**
	 * 从EDI中获取同步数据
	 * 
	 * @param list
	 * @return
	 */
	public int batchSaveMaterialFromEDI(List<MaterialDO> list);

	public List<Material> findMaterialList(Material mate);

	@Select("select mate_id from mdm_mate where mate_type = #{mateType}")
	public List<String> findMaterialIDs(Material mate);

	/**
	 * 获取已存在的物料编码
	 * 
	 * @param mate
	 * @return
	 */
	@Select("select mate_code from mdm_mate ")
	public List<String> findMaterialCodes(Material mate);

	public int updateMaterialByEDI(MaterialDO l);

	/**
	 * 根据sapId查询供应商的所有物料信息
	 * 
	 * @param username
	 * @return
	 */
	public List<MateDO> queryAllMaterialOfSuppBySapId(String sapId);

	/**
	 * 查询物料的单位转换数据
	 * 
	 * @param mateId
	 * @return
	 */
	public List<MateUnit> queryMateUnitOfMaterialByMateId(String mateId);

	/**
	 * 根据物料查询所有的单位转换IDs
	 * 
	 * @param mateId
	 * @return
	 */
	@Select("select t.id from mdm_unit t where t.mate_id = #{mateId}")
	public List<String> queryMateUnitIDsOfMaterialByMateId(String mateId);

	public int updateMaterialUnti(MaterialUnitDO mud);

	/**
	 * 批量插入物料单位转换
	 * 
	 * @param addUnitList
	 */
	public void batchSaveMaterialUnitFromEDI(List<MaterialUnitDO> addUnitList);

	@Select("select t.mate_number as mateNumber from mdm_unit t where t.mate_id = #{mateId}")
	public List<String> queryMateNumbersOfMaterialByMateId(String mateId);

	public List<MateDO> findMateDO(Map<String, Object> map);

	/**
	 * 根据物料编码查询物料信息
	 * 
	 * @param mateCode
	 * @return
	 */
	public Material queryMaterialByMateCode(String mateCode);

	/**
	 * 更新物料的报价结构信息
	 * 
	 * @param mate
	 * @return
	 */
	public int updateMaterial(Material mate);

	/**
	 * 查询所有没有报价结构的物料
	 * 
	 * @param userId
	 * 
	 * @return
	 */
	public List<MateDO> findMaterialIsNotQuote(Long userId);

	/**
	 * 根据物料编码mateCode 查询多个物料对象
	 * 
	 * @param mateCode
	 * @return
	 */
	public List<Material> queryManyMaterialByMateCode(String mateCode);

	/**
	 * 根据参数查询物料数据
	 * 
	 * @param mate
	 * @return
	 */
	public List<MateDO> findMateDOList(MateDO mate);

	/**
	 * 根据参数查询供应商的物料
	 * 
	 * @param params
	 * @return
	 */
	public List<MateDO> findMateListOfSupp(Map<String, Object> params);

	/**
	 * 根据物料id获取产品基本信息
	 * 
	 * @param mateId
	 * @return
	 */
	public List<MateBasicInfo> findMateBasicListByMateId(String mateId);

	/**
	 * 根据物料的id获取产品基本信息的主键IDs
	 * 
	 * @param mateId
	 * @return
	 */

	@Select("select id from mdm_mate_basic where mate_id = #{mateId}")
	public List<String> findMateBasicIDsByMateId(String mateId);

	/**
	 * 更新产品基础信息
	 * 
	 * @param basic
	 * @return
	 */
	public int updateBasicInfo(MateBasicInfo basic);

	/**
	 * 批量删除产品基础信息
	 * 
	 * @param ids
	 * @return
	 */
	public int batchRemoveBasicInfoByIDs(List<String> ids);

	/**
	 * 批量插入产品基础信息
	 * 
	 * @param addList
	 * @return
	 */
	public int batchSaveBasicInfo(List<MateBasicInfo> addList);

	/**
	 * 根据物料id获取物料信息
	 * 
	 * @param mateId
	 * @return
	 */
	public Material findMateById(String mateId);

	/**
	 * 查询供应商下的物料分页展示
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> queryAllMaterialOfSuppBySapIdByPage(Map<String, Object> map);

	/**
	 * 查询供应商下物料的总数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryAllMaterialOfSuppBySapIdByPageCount(Map<String, Object> map);

	/**
	 * 根据供应商
	 * 
	 * @param userId
	 * @return
	 */
	public List<BaseEntity> getMateSeriesByUserIds(@Param("userIds") List<Long> userIds);

	/**
	 * 查询货源清单中所有物料
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> queryMateOfSuppMateConfig(Map<String, Object> map);

	/**
	 * 查询货源清单中所有物料的总数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryMateOfSuppMateConfigCount(Map<String, Object> map);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<MateDO> queryAllMaterialListOfUser(String userId);

	/**
	 * 根据用户Id获取管理的所有物料
	 * 
	 * @param userIds
	 * @return
	 */
	public List<String> getMatesByUserIds(@Param("userIds") List<Long> userIds);

	/**
	 * 查询物料的项次信息
	 * 
	 * @param rb
	 * @param params
	 * @return
	 */
	public List<MateDO> getItemPage(RowBounds rb, Map<String, Object> params);

	/**
	 * 物料的项次计数
	 * 
	 * @param params
	 * @return
	 */
	public int countItemPage(Map<String, Object> params);

	/**
	 * 保存项次信息
	 * 
	 * @param mate
	 * @return
	 */
	public int updateMaterialByMateDO(MateDO mate);

	/**
	 * 根据用户Id获取管理的所有物料
	 * 
	 * @param userIds
	 * @return
	 */
	public List<MateDO> queryAllMaterialListOfUsers(@Param("userIds") List<Long> userIds);

	/**
	 * 根据用户Id获取管理生产交货计划中存在的所有物料
	 * 
	 * @param userIds
	 * @return
	 */
	public List<MateDO> queryPrdPlanMateListOfUsers(Map<String, Object> map);

	/**
	 * 批量更新物料的部分信息 （类型、机台）
	 * 
	 * @param list
	 * @return
	 */
	public void updateMatPartByBatch(List<MateDO> list);

	/**
	 * 分页获取 产能配置的物料信息
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> getCapacityPage(Map<String, Object> map);

	/**
	 * 获取 产能配置的物料信息的数量
	 * 
	 * @param map
	 * @return
	 */
	public int getCapacityCount(Map<String, Object> map);

	/**
	 * 修改用户与供应商关系
	 * 
	 * @param map
	 * @return
	 */
	public void updateUserSuppRelation(Map<String, Object> map);

	/**
	 * 修改用户与物料关系
	 * 
	 * @param map
	 * @return
	 */
	public void updateUserMatRelation(Map<String, Object> map);

	/**
	 * 修改用户的供应商与物料关系
	 * 
	 * @param map
	 * @return
	 */
	public void updateUserSuppMatRelation(Map<String, Object> map);

	/**
	 * 修改用户与供应商关系 更新 s_buyer_id 为buyer_id
	 * 
	 * @return
	 */
	public void updateUserSuppSID();

	/**
	 * 修改用户与物料关系 更新 s_buyer_id 为buyer_id
	 * 
	 * @param map
	 * @return
	 */
	public void updateUserMatSID();

	/**
	 * 修改用户的供应商与物料关系 更新 s_buyer_id 为buyer_id
	 * 
	 * @param map
	 * @return
	 */
	public void updateUserSuppMatSID();

	/**
	 * 更新标识成品是否有半成品
	 */
	public void updateSemiMate();

	/**
	 * 查询某个供应商的所有物料带条件搜索
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> queryAllMaterialOfSuppByParams(Map<String, Object> map);

	/**
	 * 根据成品物料编码获取半成品列表
	 * 
	 * @param mateCode
	 * @return
	 */
	public List<MateDO> findSemiMateListFromMate(String mateCode);

	/**
	 * 查询所有成品物料0005
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> queryFinMaterialByPage(Map<String, Object> map);

	/**
	 * 查询所有成品物料0005的数量
	 * 
	 * @param map
	 * @return
	 */
	public int queryFinMaterialByPageCount(Map<String, Object> map);

	/**
	 * 根据成品物料查询半成品物料信息
	 * 
	 * @param mateCodes
	 * @return
	 */
	public List<MateDO> querySemiFinMateByMateCodes(List<String> mateCodes);

	/**
	 * 查询货源中的成品物料0005，但是没有对应的半成品物料
	 * 
	 * @param map
	 * @return
	 */
	public List<MateDO> queryFinMateOfSuppMateConfig(Map<String, Object> map);

	/**
	 * 查询货源中的成品物料0005的数量，但是没有对应的半成品物料
	 * 
	 * @param map
	 * @return
	 */
	public int queryFinMateOfSuppMateConfigCount(Map<String, Object> map);

	/**
	 * 查询物料的成品物料编码
	 * 
	 * @param mateCode
	 * @return
	 */
	public String queryFinMateId(String mateCode);

	/**
	 * 根据物料编码查询这个物料对应的采购员编码
	 * 
	 * @param mateId
	 * @return
	 */
	public List<String> queryBuyerFromBuyerAndMateIdByMateId(String mateId);

	/**
	 * 物料分页
	 * 
	 * @param mate
	 * @param rb
	 * @return
	 */
	public List<MateDO> findMaterialListByRB(MateDO mate, RowBounds rb);

	public int countMaterialListByRB(MateDO mate);

	/**
	 * 根据用户和物料信息删除货源配置信息
	 * 
	 * @param map
	 */
	public void deleteSuppMateOfUserByUserIdAndMateIdList(Map<String, Object> map);

	/**
	 * 查询采购员所管理的物料
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<MateDO> findMateListOfPurcharor(Map<String, Object> paramMap);

	/**
	 * 查询采购员所管理的物料编码和名称
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<String> findConcatColOfPurcharor(Map<String, Object> paramMap);
	/**
	 * 查询这些采购员在采购货源中对应的物料编码集合
	 * @param userList
	 * @return
	 */
	public List<String> getMateCodeListByUsers(List<UserDO> userList);
}
