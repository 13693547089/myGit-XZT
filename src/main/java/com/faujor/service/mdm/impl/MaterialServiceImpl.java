package com.faujor.service.mdm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.cluster.mdm.ClusterMaterialMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.entity.cluster.mdm.MaterialDO;
import com.faujor.entity.cluster.mdm.MaterialUnitDO;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateBasicInfo;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.MateUnit;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.UserSuppMate;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Service(value = "materialService")
public class MaterialServiceImpl implements MaterialService {

	@Autowired
	private MaterialMapper materialMapper;
	@Autowired
	private ClusterMaterialMapper clusterMaterialMapper;
	@Autowired
	private AsyncLogService asyncLogService;

	@Override
	public Map<String, Object> queryMaterialByPage(Map<String, Object> map) {
		// List<Material> list = materialMapper.queryMaterialByPage(map);
		List<MateDO> list = materialMapper.findMateDO(map);
		int count = materialMapper.queryMaterialCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		return page;
	}

	@Override
	public Material queryOneMaterialByMateId(String mateId) {
		Material mate = materialMapper.queryOneMaterialByMateId(mateId);
		return mate;
	}

	@Override
	public Map<String, Object> queryMaterialOfUser(Map<String, Object> map) {
		/*
		 * String userId = map.get("userId").toString(); String suppId = (String)
		 * map.get("suppId"); Material mate2 = new Material(); Map<String, Object> param
		 * = new HashMap<String,Object>(); param.put("userId", userId);
		 * param.put("suppId", suppId); param.put("mate", mate2); List<MateDO> list2 =
		 * materialMapper.queryMaterialOfUserAndSupp(param); if(list!= null && list2!=
		 * null){ for (MateDO m : list) { for (MateDO m2 : list2) {
		 * if(m2.getMateCode().equals(m.getMateCode())){ m.setLAY_CHECKED("true"); } } }
		 * }
		 */
		List<MateDO> list = materialMapper.queryMaterialOfUser(map);
		int count = materialMapper.queryMaterialOfUserCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	@Transactional
	public boolean deleteMaterialOfUser(List<String> mateIds, Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("mateIds", mateIds);
		int i = materialMapper.deleteMaterialOfUser(map);
		materialMapper.deleteSuppMateOfUserByUserIdAndMateIdList(map);
		if (i == mateIds.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> queryMaterialOfUserAndSupp(Map<String, Object> map) {
		List<MateDO> list = materialMapper.queryMaterialOfUserAndSupp(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("count", "");
		page.put("code", 0);
		return page;
	}

	@Override
	@Transactional
	public boolean deleteMaterialOfUserAndSupp(List<String> mateIds, String suppId) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getUserId());
		map.put("suppId", suppId);
		map.put("mateIds", mateIds);
		int i = materialMapper.deleteMaterialOfUserAndSupp(map);
		if (i == mateIds.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean addMaterialForUserAndSupp(String[] mateIds, String suppId) {
		SysUserDO user = UserCommon.getUser();
		UserSuppMate usm = new UserSuppMate();
		usm.setBuyerId(user.getUserId().intValue());
		usm.setCreator(user.getName());
		usm.setSuppId(suppId);
		int a = 0;
		for (int i = 0; i < mateIds.length; i++) {
			int j = 0;
			usm.setMateId(mateIds[i]);
			j = materialMapper.addMaterialForUserAndSupp(usm);
			a += j;
		}
		if (a == mateIds.length) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<MateDO> queryAllMaterial() {
		List<MateDO> list = materialMapper.queryAllMaterial();
		return list;
	}

	@Override
	@Transactional
	public Map<String, Object> addMaterialOfUser(String[] mateIds, Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "";
		int count = 0;
		for (int i = 0; i < mateIds.length; i++) {
			// 校验物料是否已经配置给其他采购员
			String mateId = mateIds[i];
			List<String> buyerIds = materialMapper.queryBuyerFromBuyerAndMateIdByMateId(mateId);
			if (buyerIds.size() == 0) {
				// 没有配置给其他采购员
				count++;
			} else {
				// 已被其他采购员使用，则不能添加
				result += mateId + ",";
			}

		}
		int a = 0;
		if (count == mateIds.length) {// 选中的物料都没有配置给其他采购员，才可以添加到配置中
			SysUserDO user = UserCommon.getUser();
			UserSuppMate usm = new UserSuppMate();
			usm.setBuyerId(userId);
			usm.setCreator(user.getName());
			for (int i = 0; i < mateIds.length; i++) {
				int j = 0;
				usm.setMateId(mateIds[i]);
				j = materialMapper.addMaterialOfUser(usm);
				a += j;
			}
		} else {
			map.put("judge", false);
			map.put("msg", result + "已配置给其他采购员,不能添加");
			return map;
		}
		if (a == mateIds.length) {
			map.put("judge", true);
		} else {
			map.put("judge", false);
			map.put("msg", "添加失败");
		}
		return map;
	}

	@Override
	public List<MateDO> queryAllMaterialOfSupp(String suppId) {
		List<MateDO> list = materialMapper.queryAllMaterialOfSupp(suppId);
		return list;
	}

	@Override
	public int asyncMateInfo(String mateType, AsyncLog al) {
		int k = 0;
		try {
			List<String> typeList = new ArrayList<String>();
			typeList.add("0001");
			typeList.add("0002");
			typeList.add("0003");
			typeList.add("0005");
			typeList.add("0006");
			typeList.add("0007");
			typeList.add("0008");
			typeList.add("0011");
			typeList.add("0012");
			Material mate = new Material();
			List<String> codes = materialMapper.findMaterialCodes(mate);
			for (int i = 0; i < typeList.size(); i++) {
				String elem = typeList.get(i);
				MaterialDO md = new MaterialDO();
				md.setMateType(elem);
				List<MaterialDO> list = clusterMaterialMapper.findClusterMaterialList(md);
				if (list.size() > 0) {
					List<MaterialDO> addList = new ArrayList<MaterialDO>();
					int count = 0;
					int temp = 200;
					for (MaterialDO l : list) {
						List<MaterialUnitDO> units = l.getMaterialUnits();
						// 判断是否存在
						if (codes.contains(l.getMateCode())) {
							k += materialMapper.updateMaterialByEDI(l);
						} else {
							// 生成主表主键id
							count++;
							addList.add(l);
						}
						// 处理单位转换
						List<String> unitIds = materialMapper.queryMateUnitIDsOfMaterialByMateId(l.getMateId());
						List<MaterialUnitDO> addUnitList = new ArrayList<MaterialUnitDO>();
						for (MaterialUnitDO mud : units) {
							if (unitIds.contains(mud.getId())) {
								materialMapper.updateMaterialUnti(mud);
							} else {
								mud.setMateId(l.getMateId());
								String id = mud.getId();
								if (!StringUtils.isEmpty(id))
									addUnitList.add(mud);
							}
						}
						if (addUnitList.size() > 0)
							materialMapper.batchSaveMaterialUnitFromEDI(addUnitList);
						if (count == temp) {
							k += materialMapper.batchSaveMaterialFromEDI(addList);
							addList = new ArrayList<MaterialDO>();
							temp += 200;
						}
					}
					if (addList.size() > 0)
						k += materialMapper.batchSaveMaterialFromEDI(addList);
				}
			}
			if (k > 0)
				materialMapper.updateSemiMate();
			al.setAsyncNum(k);
			al.setAsyncStatus("同步成功");
			asyncLogService.updateAsyncLog(al);
		} catch (Exception e) {
			// 抛出异常
			al.setAsyncStatus("同步异常");
			String message = e.getMessage();
			if (!StringUtils.isEmpty(message)) {
				int length = message.length();
				if (length < 2999) {
					al.setErrorMsg(message);
				} else {
					String errorMsg = message.substring(0, 2999);
					al.setErrorMsg(errorMsg);
				}
			} else {
				al.setErrorMsg("未知原因！");
			}
			asyncLogService.updateAsyncLog(al);
		}
		return k;
	}

	@Override
	public List<MateDO> queryAllMaterialOfSuppBySapId(String sapId) {
		List<MateDO> list = materialMapper.queryAllMaterialOfSuppBySapId(sapId);
		return list;
	}

	@Override
	public List<MateUnit> queryMateUnitOfMaterialByMateId(String mateId) {
		List<MateUnit> list = materialMapper.queryMateUnitOfMaterialByMateId(mateId);
		return list;
	}

	@Override
	public Material queryMaterialByMateCode(String mateCode) {
		return materialMapper.queryMaterialByMateCode(mateCode);
	}

	@Override
	public List<MateDO> findMaterialIsNotQuote(Long userId) {

		return materialMapper.findMaterialIsNotQuote(userId);
	}

	@Override
	public List<Material> queryManyMaterialByMateCode(String mateCode) {
		return materialMapper.queryManyMaterialByMateCode(mateCode);
	}

	@Override
	public List<MateDO> findMateDOList(MateDO mate) {
		return materialMapper.findMateDOList(mate);
	}

	@Override
	public List<MateDO> findMateListOfSupp(Map<String, Object> params) {
		return materialMapper.findMateListOfSupp(params);
	}

	@Override
	public List<MateBasicInfo> findMateBasicListByMateId(String mateId) {
		return materialMapper.findMateBasicListByMateId(mateId);
	}

	@Override
	public int saveBasicInfo(String mateId, List<MateBasicInfo> list) {
		List<String> ids = materialMapper.findMateBasicIDsByMateId(mateId);
		List<MateBasicInfo> addList = new ArrayList<MateBasicInfo>();
		int i = 0;
		for (MateBasicInfo basic : list) {
			int k = ids.indexOf(basic.getId());
			if (k > -1) {
				i += materialMapper.updateBasicInfo(basic);
				ids.remove(k);
			} else {
				basic.setId(UUIDUtil.getUUID());
				basic.setMateId(mateId);
				addList.add(basic);
			}
		}
		if (ids.size() > 0)
			materialMapper.batchRemoveBasicInfoByIDs(ids);
		if (addList.size() > 0)
			i += materialMapper.batchSaveBasicInfo(addList);
		if (i > 0) {
			Material mate = materialMapper.findMateById(mateId);
			mate.setIsMaintenance("已维护");
			materialMapper.updateMaterial(mate);
		}
		return i;
	}

	@Override
	public Map<String, Object> queryAllMaterialOfSuppBySapIdByPage(Map<String, Object> map) {
		List<MateDO> list = materialMapper.queryAllMaterialOfSuppBySapIdByPage(map);
		int count = materialMapper.queryAllMaterialOfSuppBySapIdByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("count", count);
		page.put("code", 0);
		return page;
	}

	@Override
	public Map<String, Object> queryMateOfSuppMateConfig(Map<String, Object> map) {
		String cutProdType = (String) map.get("cutProdType");
		List<MateDO> list = null;
		int count = 0;
		if ("AHAN".equals(cutProdType)) {// 含半成品（货源中半成品0002对应的成品物料）
			list = materialMapper.queryMateOfSuppMateConfig(map);
			for (MateDO m : list) {
				m.setIsHaveSeim("YES");// 有半成品
				m.setIsSpecial("NO");// 不特殊
			}
			count = materialMapper.queryMateOfSuppMateConfigCount(map);
		} else if ("BNOHAN".equals(cutProdType)) {// 不含半成品（货源中成品0005没有对应的半成品）
			list = materialMapper.queryFinMateOfSuppMateConfig(map);
			for (MateDO m : list) {
				m.setIsHaveSeim("NO");// 没有半成品
				m.setIsSpecial("NO");// 不特殊
			}
			count = materialMapper.queryFinMateOfSuppMateConfigCount(map);
		} else {// 特殊自制品（主数据当中所有0005类型的成品物料）
			list = materialMapper.queryFinMaterialByPage(map);
			for (MateDO m : list) {
				m.setIsSpecial("YES");// 特殊
			}
			count = materialMapper.queryFinMaterialByPageCount(map);
		}
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("count", count);
		page.put("code", 0);
		return page;
	}

	@Override
	public List<String> getMateByUserIds(List<Long> userIds) {
		List<String> mateCodes = materialMapper.getMatesByUserIds(userIds);
		return mateCodes;
	}

	@Override
	public List<MateDO> queryAllMaterialListOfUser(String userId) {
		return materialMapper.queryAllMaterialListOfUser(userId);
	}

	@Override
	public Map<String, Object> getItemPage(RowBounds rb, Map<String, Object> params) {
		List<MateDO> list = materialMapper.getItemPage(rb, params);
		int count = materialMapper.countItemPage(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", count);
		return result;
	}

	@Override
	public int saveItemInfo(MateDO mate) {
		int i = materialMapper.updateMaterialByMateDO(mate);
		return i;
	}

	@Override
	public List<MateDO> queryAllMaterialListOfUsers(List<Long> userIds) {
		return materialMapper.queryAllMaterialListOfUsers(userIds);
	}

	@Override
	public List<MateDO> queryPrdPlanMateListOfUsers(Map<String, Object> map) {
		return materialMapper.queryPrdPlanMateListOfUsers(map);
	}

	/**
	 * 批量更新物料的部分信息 （类型、机台）
	 * 
	 * @param list
	 * @return
	 */
	@Transactional
	public void updateMatPartByBatch(List<MateDO> list) {
		materialMapper.updateMatPartByBatch(list);
	}

	/**
	 * 分页获取 产能配置的物料信息
	 * 
	 * @param map
	 * @return
	 */
	public LayuiPage<MateDO> getCapacityPage(Map<String, Object> map) {
		LayuiPage<MateDO> page = new LayuiPage<>();
		int count = 0;
		List<MateDO> list = materialMapper.getCapacityPage(map);
		count = materialMapper.getCapacityCount(map);
		page.setCount(count);
		page.setData(list);
		return page;
	}

	/**
	 * 获取 产能配置的物料信息的数量
	 * 
	 * @param map
	 * @return
	 */
	public int getCapacityCount(Map<String, Object> map) {
		return materialMapper.getCapacityCount(map);
	}

	/**
	 * 采购配置调整处理
	 * 
	 * @param adjustData
	 */
	@Override
	@Transactional
	public void dealAdjustData(String adjustData) throws Exception {

		JSONArray adjustJson = JSON.parseArray(adjustData);
		// 更新表中不等于 buyer_id 的 s_buyer_id的数据
		materialMapper.updateUserSuppSID();
		materialMapper.updateUserMatSID();
		materialMapper.updateUserSuppMatSID();

		for (int i = 0; i < adjustJson.size(); i++) {
			JSONObject obj = adjustJson.getJSONObject(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sId", obj.get("sId"));
			map.put("tId", obj.get("tId"));
			// 修改用户与物料关系
			materialMapper.updateUserMatRelation(map);
			// 修改用户与供应商关系
			materialMapper.updateUserSuppRelation(map);
			// 修改用户的供应商与物料关系
			materialMapper.updateUserSuppMatRelation(map);
		}
	}

	@Override
	public List<MateDO> queryAllMaterialOfSuppByParams(Map<String, Object> map) {
		return materialMapper.queryAllMaterialOfSuppByParams(map);
	}

	@Override
	public Map<String, Object> chooseSingleData(Integer page, Integer limit, MateDO mate) {
		Map<String, Object> result = new HashMap<>();
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		List<MateDO> list = materialMapper.findMaterialListByRB(mate, rb);
		int count = materialMapper.countMaterialListByRB(mate);
		result.put("data", list);
		result.put("count", count);
		result.put("code", 0);
		result.put("msg", "");
		return result;
	}

	@Override
	public List<MateDO> findMateListOfPurcharor(Map<String, Object> paramMap) {
		return materialMapper.findMateListOfPurcharor(paramMap);
	}

	@Override
	public List<String> findConcatColOfPurcharor(Map<String, Object> paramMap) {
		return materialMapper.findConcatColOfPurcharor(paramMap);
	}
}
