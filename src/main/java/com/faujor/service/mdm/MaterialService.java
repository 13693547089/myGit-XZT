package com.faujor.service.mdm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.mdm.MateBasicInfo;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.MateUnit;
import com.faujor.entity.mdm.Material;

public interface MaterialService {

	/**
	 * 物料主数据列表分页展示
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryMaterialByPage(Map<String, Object> map);

	/**
	 * 查询物料详情
	 * 
	 * @param mateId
	 * @return
	 */
	Material queryOneMaterialByMateId(String mateId);

	/**
	 * 查询采购员下的物料数据
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryMaterialOfUser(Map<String, Object> map);

	/**
	 * 删除采购员下的物料
	 * 
	 * @param mateIds
	 * @param userId
	 * @return
	 */
	boolean deleteMaterialOfUser(List<String> mateIds, Integer userId);

	/**
	 * 为采购员添加物料
	 * 
	 * @return
	 */
	Map<String, Object> addMaterialOfUser(String[] mateIds, Integer userId);

	/**
	 * 查询采购员下某个供应商对应的物料
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryMaterialOfUserAndSupp(Map<String, Object> map);

	/**
	 * 删除采购员下某个供应商对应的物料
	 * 
	 * @param mateIds
	 * @param suppId
	 * @return
	 */
	boolean deleteMaterialOfUserAndSupp(List<String> mateIds, String suppId);

	/**
	 * 为采购员下的某个供应商分配物料
	 * 
	 * @param mateIds
	 * @param suppId
	 * @return
	 */
	boolean addMaterialForUserAndSupp(String[] mateIds, String suppId);

	/**
	 * 查询所有物料
	 * 
	 * @return
	 */
	List<MateDO> queryAllMaterial();

	/**
	 * 查询分配给供应商的所有物料信息
	 * 
	 * @param suppId
	 * @return
	 */
	List<MateDO> queryAllMaterialOfSupp(String suppId);

	int asyncMateInfo(String mateType, AsyncLog al);

	/**
	 * 根据sapId查询供应商的所有物料信息
	 * 
	 * @param username
	 * @return
	 */
	List<MateDO> queryAllMaterialOfSuppBySapId(String sapId);

	List<MateUnit> queryMateUnitOfMaterialByMateId(String mateId);

	/**
	 * 根据物料编码查询物料信息
	 * 
	 * @param mateCode
	 * @return
	 */
	Material queryMaterialByMateCode(String mateCode);

	/**
	 * 查询所有没有报价的物料
	 * 
	 * @param long
	 * 
	 * @return
	 */
	List<MateDO> findMaterialIsNotQuote(Long userId);

	/**
	 * 根据物料编码mateCode 查询多个物料对象
	 * 
	 * @param mateCode
	 * @return
	 */
	public List<Material> queryManyMaterialByMateCode(String mateCode);

	/**
	 * 根据不同的参数查询物料数据
	 * 
	 * @param mate
	 * @return
	 */
	List<MateDO> findMateDOList(MateDO mate);

	/**
	 * 根据参数查询供应商所有的物料
	 * 
	 * @param params
	 * @return
	 */
	List<MateDO> findMateListOfSupp(Map<String, Object> params);

	/**
	 * 根据物料id获取产品基本信息
	 * 
	 * @param mateId
	 * @return
	 */
	List<MateBasicInfo> findMateBasicListByMateId(String mateId);

	/**
	 * 保存产品基本信息
	 * 
	 * @param mateId
	 * @param list
	 * @return
	 */
	int saveBasicInfo(String mateId, List<MateBasicInfo> list);

	/**
	 * 查询供应商的物料分页展示
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryAllMaterialOfSuppBySapIdByPage(Map<String, Object> map);

	/**
	 * 查询货源清单中所有物料
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryMateOfSuppMateConfig(Map<String, Object> map);

	/**
	 * 查询一个采购员的所有物料，返回一个List集合
	 * 
	 * @param userIds
	 * @return
	 */

	List<MateDO> queryAllMaterialListOfUser(String userId);

	/**
	 * 根据用户Id 获取管理的物料
	 * 
	 * @param userIds
	 * @return
	 */
	List<String> getMateByUserIds(List<Long> userIds);

	/**
	 * 获取项次信息
	 * 
	 * @param rb
	 * @param params
	 * @return
	 */
	Map<String, Object> getItemPage(RowBounds rb, Map<String, Object> params);

	/**
	 * 保存项次信息
	 * 
	 * @param mate
	 * @return
	 */
	int saveItemInfo(MateDO mate);

	/**
	 * 查询多个采购员的所有物料，返回一个List集合
	 * 
	 * @param userIds
	 * @return
	 */

	List<MateDO> queryAllMaterialListOfUsers(List<Long> userIds);

	/**
	 * 根据用户Id获取管理生产交货计划中存在的所有物料
	 * 
	 * @param userIds
	 * @return
	 */
	List<MateDO> queryPrdPlanMateListOfUsers(Map<String, Object> map);

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
	public LayuiPage<MateDO> getCapacityPage(Map<String, Object> map);

	/**
	 * 获取 产能配置的物料信息的数量
	 * 
	 * @param map
	 * @return
	 */
	public int getCapacityCount(Map<String, Object> map);

	/**
	 * 采购配置调整处理
	 * 
	 * @param adjustData
	 * @throws Exception
	 */
	public void dealAdjustData(String adjustData) throws Exception;

	/**
	 * 查询某个供应商的所有物料带条件搜索
	 * 
	 * @param suppId
	 * @param mate
	 * @return
	 */
	List<MateDO> queryAllMaterialOfSuppByParams(Map<String, Object> map);

	/**
	 * 物料单选列表
	 * 
	 * @param page
	 * @param limit
	 * @param mate
	 * @return
	 */
	Map<String, Object> chooseSingleData(Integer page, Integer limit, MateDO mate);

	/**
	 * 查询 采购员所有的物料
	 * 
	 * @param paramMap
	 * @return
	 */
	List<MateDO> findMateListOfPurcharor(Map<String, Object> paramMap);

	/**
	 * 查询 采购员所管理的物料编码和名称
	 * 
	 * @param paramMap
	 * @return
	 */
	List<String> findConcatColOfPurcharor(Map<String, Object> paramMap);
}
