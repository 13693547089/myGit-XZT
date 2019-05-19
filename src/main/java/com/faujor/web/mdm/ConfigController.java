package com.faujor.web.mdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.faujor.common.annotation.Log;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.OemPackSupp;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.mdm.QualSuppDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.UserDO;
import com.faujor.service.basic.BasicService;
import com.faujor.service.mdm.ConfigService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.privileges.OrgService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@Controller
public class ConfigController {

	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private ConfigService configService;

	/**
	 * 跳转到采购供应源配置页面
	 * 
	 * @return
	 */
	@RequestMapping("/getUserAndQualSuppConfigHtml")
	public String getUserAndQualSuppConfigHtml() {
		return "mdm/config/userAndSupp";
	}

	/**
	 * 采购供应源配置/采购货源配置中 采购员列表
	 * 
	 * @return
	 */
	@Log(value = "采购供应源配置/采购货源配置中 获取采购员信息列表")
	@ResponseBody
	@RequestMapping("/queryAllUser")
	public Map<String, Object> queryAllUser(String params) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ownId", user.getUserId());
		paramMap.put("orgCode", "PURCHAROR");
		paramMap.put("isContainOwn", true);
		// 获取这个管理员下的采购员
		List<UserDO> list = orgService.manageSubordinateUsers(paramMap);
		List<UserDO> list2 = null;
		if (!StringUtils.isEmpty(params)) {
			list2 = new ArrayList<UserDO>();
			for (UserDO u : list) {
				String username = u.getUserName();
				String name = u.getName();
				if (username.indexOf(params) != -1 || name.indexOf(params) != -1) {
					list2.add(u);
				}
			}
		} else {
			list2 = list;
		}
		map.put("code", "0");
		map.put("data", list2);
		map.put("count", list2.size());
		map.put("msg", "");
		return map;
	}

	/**
	 * 采购供应源配置中 合格供应商列表
	 * 
	 * @param qualSupp
	 * @param userId
	 * @return
	 */
	@Log(value = "采购供应源配置中 获取合格供应商信息列表")
	@ResponseBody
	@RequestMapping("/queryQualSuppOfUser")
	public Map<String, Object> queryQualSuppOfUser(QualSupp qualSupp, Integer userId, Integer page, Integer limit) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("qualSupp", qualSupp);
		map.put("userId", userId);
		Map<String, Object> page2 = qualSuppService.queryQualSuppByUserId(map);
		return page2;
	}

	/**
	 * 采购供应源配置中 删除采购员下的供应商
	 * 
	 * @param userId
	 * @param suppIds
	 * @return
	 */
	@Log(value = "采购供应源配置中 删除采购员下的合格供应商")
	@ResponseBody
	@RequestMapping("/deleteQualSuppOfUserByUserId")
	public boolean deleteQualSuppOfUserByUserId(Integer userId, String[] suppIds) {
		boolean b = qualSuppService.deleteQualSuppOfUser(userId, suppIds);
		return b;
	}

	/**
	 * 采购供应源配置中 添加的弹出框内容
	 * 
	 * @return
	 */
	@RequestMapping("/getAllQualSuppListHtml")
	public String getAllQualSuppListHtml(Model model, Integer userId) {
		model.addAttribute("userId", userId);
		return "mdm/config/allQualSuppList";
	}

	/**
	 * 采购供应源配置中 获取所有的供应商信息
	 * 
	 * @return
	 */
	@Log(value = "采购供应源配置中 获取弹出中的供应商信息列表")
	@ResponseBody
	@RequestMapping("/getAllQualSuppList")
	public Map<String, Object> getAllQualSuppList(QualSupp qualSupp, Integer page, Integer limit) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("qualSupp", qualSupp);
		Map<String, Object> suppByPage = qualSuppService.queryAllQualSuppByPage(map);
		return suppByPage;
	}

	/**
	 * 采购供应源配置中 给采购员添加供应商
	 * 
	 * @param userId
	 * @param suppIds
	 * @return
	 */
	@Log(value = "采购供应源配置中 给采购员添加供应商")
	@ResponseBody
	@RequestMapping("/addQualSuppForUser")
	public boolean addQualSuppForUser(Integer userId, String[] suppIds) {
		boolean b = qualSuppService.addQualSuppForUser(userId, suppIds);
		return b;
	}

	/**
	 * 跳转到采购货源配置页面
	 * 
	 * @return
	 */
	@RequestMapping("/getUserAndMateHtml")
	public String getUserAndMateHtml() {
		return "mdm/config/userAndMate";
	}

	/**
	 * 采购货源配置中 物料数据列表
	 * 
	 * @param mate
	 * @param userId
	 * @return
	 */
	@Log(value = "采购货源配置中 获取物料数据信息列表")
	@ResponseBody
	@RequestMapping("/queryMaterialOfUser")
	public Map<String, Object> queryMaterialOfUser(Material mate, Integer userId, Integer page, Integer limit) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("mate", mate);
		map.put("start", start);
		map.put("end", end);
		return materialService.queryMaterialOfUser(map);
	}

	/**
	 * 采购货源配置中 删除采购员下的物料
	 * 
	 * @param mateIds
	 * @param userId
	 * @return
	 */
	@Log(value = "采购货源配置中 删除采购员下的物料")
	@ResponseBody
	@RequestMapping("/deleteMaterialOfUser")
	public boolean deleteMaterialOfUser(String mateIds, Integer userId) {
		List<String> mateIdList = JsonUtils.jsonToList(mateIds, String.class);
		boolean b = materialService.deleteMaterialOfUser(mateIdList, userId);
		return b;
	}

	/**
	 * 采购货源配置中 弹出框
	 * 
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getAllMateListHtml")
	public String getAllMateListHtml(Model model, Integer userId) {
		model.addAttribute("userId", userId);
		return "mdm/config/allMateList";
	}

	/**
	 * 采购货源配置中 弹出框中的内容 查询所有物料
	 * 
	 * @return
	 */
	@Log(value = "采购货源配置中 获取弹出框中的物料信息列表")
	@ResponseBody
	@RequestMapping("/queryAllMaterial")
	public Map<String, Object> queryAllMaterial(Material mate, Integer page, Integer limit) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("mate", mate);
		Map<String, Object> page2 = materialService.queryMaterialByPage(map);
		return page2;
	}

	/**
	 * 采购货源配置中 为采购员添加物料
	 * 
	 * @param mateIds
	 * @param userId
	 * @return
	 */
	@Log(value = "采购货源配置中 为采购员添加物料")
	@ResponseBody
	@RequestMapping("/addMaterialForUser")
	public Map<String, Object> addMaterialForUser(String[] mateIds, Integer userId) {
		Map<String, Object> map = materialService.addMaterialOfUser(mateIds, userId);
		return map;
	}

	/**
	 * 跳转到供应商供货配置页面
	 * 
	 * @return
	 */
	@RequestMapping("/getQualSuppAndMateHtml")
	public String getQualSuppAndMateHtml() {
		return "mdm/config/suppAndMate";
	}

	/**
	 * 供应商供货配置中 采购员的所有供应商列表
	 * 
	 * @param qualSupp
	 * @return
	 */
	@Log(value = "供应商供货配置中 获取供应商信息列表")
	@ResponseBody
	@RequestMapping("/queryAllQualSuppOfUser")
	public Map<String, Object> queryAllQualSuppOfUser(QualSuppDO qualSuppDO, Integer page, Integer limit) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取这个管理员下的采购员(包含采购员自己)
		/*
		 * List<UserDO> list = orgService.manageOrgByCode(user.getUserId().intValue(),
		 * "PURCHAROR", ""); if (list.size() != 0) { map.put("userDO", list); } else {
		 * UserDO userDO = new UserDO(); userDO.setId(user.getUserId());
		 * list.add(userDO); map.put("userDO", list); }
		 */
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ownId", user.getUserId());
		paramMap.put("orgCode", "PURCHAROR");
		paramMap.put("isContainOwn", true);
		// 获取这个管理员下的采购员
		List<UserDO> list = orgService.manageSubordinateUsers(paramMap);
		// List<UserDO> list = new ArrayList<UserDO>();
		// UserDO userDO = new UserDO();
		// userDO.setId(user.getUserId());
		// list.add(userDO);
		map.put("userDO", list);

		map.put("start", start);
		map.put("end", end);
		map.put("qualSuppDO", qualSuppDO);
		Map<String, Object> page2 = qualSuppService.queryQualSuppByUserIds(map);
		return page2;
	}

	/**
	 * 供应商供货配置中 物料数据列表
	 * 
	 * @param mate
	 * @param suppId
	 * @return
	 */
	@Log(value = "供应商供货配置中 获取物料数据列表")
	@ResponseBody
	@RequestMapping("/queryMaterialOfUserAndSupp")
	public Map<String, Object> queryMaterialOfUserAndSupp(Material mate, String suppId, Integer page, Integer limit) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownId", user.getUserId());
		params.put("orgCode", "PURCHAROR");
		params.put("isContainOwn", true);
		List<UserDO> list = orgService.manageSubordinateUsers(params);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userDO", list);
		map.put("suppId", suppId);
		map.put("mate", mate);
		Map<String, Object> page2 = materialService.queryMaterialOfUserAndSupp(map);
		return page2;
	}

	/**
	 * 供应商供货配置中 删除采购员下供应商的物料
	 * 
	 * @param mateIds
	 * @param suppId
	 * @return
	 */
	@Log(value = "供应商供货配置中 删除采购员下供应商的物料")
	@ResponseBody
	@RequestMapping("/deleteMaterialOfUserAndSupp")
	public boolean deleteMaterialOfUserAndSupp(String mateIds, String suppId) {
		List<String> mateList = JsonUtils.jsonToList(mateIds, String.class);
		boolean b = materialService.deleteMaterialOfUserAndSupp(mateList, suppId);
		return b;
	}

	/**
	 * 供应商供货配置中 弹出框页面
	 * 
	 * @param model
	 * @param suppId
	 * @return
	 */
	@RequestMapping("/getAllMateListOfUserHtml")
	public String getAllMateListOfUserHtml(Model model, String suppId) {
		model.addAttribute("suppId", suppId);
		return "mdm/config/allMateListOfUser";
	}

	/**
	 * 供应商供货配置中 弹出框展示 获取采购员下的所有物料
	 * 
	 * @return
	 */
	@Log(value = "供应商供货配置中 获取弹出框中的物料信息列表")
	@ResponseBody
	@RequestMapping("/getAllMaterialListOfUser")
	public Map<String, Object> getAllMaterialListOfUser(Material mate, String suppId, Integer page, Integer limit) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getUserId());
		map.put("suppId", suppId);
		map.put("start", start);
		map.put("end", end);
		map.put("mate", mate);
		Map<String, Object> page2 = materialService.queryMaterialOfUser(map);
		return page2;
	}

	/**
	 * 供应商供货配置中 为采购员下的供应商分配物料
	 * 
	 * @param mateIds
	 * @param suppId
	 * @return
	 */
	@Log(value = "供应商供货配置中 给采购员下的供应商分配物料")
	@ResponseBody
	@RequestMapping("/addMaterialForUserAndSupp")
	public boolean addMaterialForUserAndSupp(String[] mateIds, String suppId) {
		return materialService.addMaterialForUserAndSupp(mateIds, suppId);
	}

	/**
	 * 查询一个采购员的所有合格供应商，返回一个List集合
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllQualSuppListOfUser")
	public List<QualSupp> queryAllQualSuppListOfUser(String userId) {
		return qualSuppService.queryAllQualSuppListOfUser(userId);
	}

	/**
	 * 查询一个采购员的所有物料，返回一个List集合
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllMaterialListOfUser")
	public List<MateDO> queryAllMaterialListOfUser(String userId) {
		return materialService.queryAllMaterialListOfUser(userId);
	}

	/**
	 * 项次信息维护
	 * 
	 * @return
	 */
	@RequestMapping("/itemInfo")
	public String itemInfo(Model model) {
		/*
		 * List<Dic> itemTypeList = basicService.findDicListByCategoryCode("ItemInfo");
		 * model.addAttribute("itemTypeList", itemTypeList); List<Dic> boardList =
		 * basicService.findDicListByCategoryCode("BOARD");
		 * model.addAttribute("boardList", boardList);
		 * 
		 * return "mdm/config/mateItem";
		 */
		List<Dic> mateType = basicService.findDicListByCategoryCode("MATETYPE");
		model.addAttribute("mateTypeList", mateType);
		return "mdm/config/capacityConfig";
	}

	@RequestMapping("/itemInfo/itemPage")
	@ResponseBody
	public Map<String, Object> itemPage(String pageNumber, String pageSize, String mateCode, String mateType) {
		int pageNo = pageNumber == null ? 1 : Integer.parseInt(pageNumber);
		int limit = pageSize == null ? 10 : Integer.parseInt(pageSize);
		int offset = (pageNo - 1) * limit;
		Map<String, Object> params = new HashMap<String, Object>();
		SysUserDO user = UserCommon.getUser();
		params.put("userId", user.getUserId());
		RowBounds rb = new RowBounds(offset, limit);
		if (!StringUtils.isEmpty(mateCode))
			mateCode = "%" + mateCode + "%";
		params.put("mateCode", mateCode);
		params.put("mateType", mateType);
		Map<String, Object> result = materialService.getItemPage(rb, params);
		return result;
	}

	/**
	 * 获取产能配置的物料数据
	 */
	@RequestMapping("/itemInfo/capacityPage")
	@ResponseBody
	public LayuiPage<MateDO> capacityPage(LayuiPage<MateDO> page, String matInfo, String matType) {
		Map<String, Object> params = new HashMap<>();
		page.calculatePage();
		params.put("page", page);
		SysUserDO user = UserCommon.getUser();
		Long userId = user.getUserId();
		params.put("userId", userId);
		params.put("matInfo", matInfo);
		params.put("matType", matType);

		return materialService.getCapacityPage(params);
	}

	/**
	 * 物料批量更新弹框
	 * 
	 * @return
	 */
	@RequestMapping("/itemInfo/updateDialog")
	public String updateDialog(Model model) {

		List<Dic> itemTypeList = basicService.findDicListByCategoryCode("ItemInfo");
		model.addAttribute("itemTypeList", itemTypeList);
		List<Dic> boardList = basicService.findDicListByCategoryCode("BOARD");
		model.addAttribute("boardList", boardList);

		return "mdm/config/matUpdateDialog";
	}

	@RequestMapping("/item/getItemData")
	@ResponseBody
	public List<Dic> getItemData() {
		return basicService.findDicListByCategoryCode("ItemInfo");
	}

	/**
	 * 获取字典表中的机台
	 * 
	 * @return
	 */
	@RequestMapping("/item/getBoardData")
	@ResponseBody
	public List<Dic> getBoardData() {
		return basicService.findDicListByCategoryCode("BOARD");
	}

	@RequestMapping("/item/saveItemInfo")
	@ResponseBody
	public RestCode saveItemInfo(MateDO mate) {
		int i = materialService.saveItemInfo(mate);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 更新物料信息（类型、机台）
	 * 
	 * @param matData
	 * @return
	 */
	@RequestMapping("/item/updateMatPartByBatch")
	@ResponseBody
	public RestCode updateMatPartByBatch(String matData) {

		List<MateDO> list = JSON.parseArray(matData, MateDO.class);

		materialService.updateMatPartByBatch(list);

		return RestCode.ok();
	}

	/**
	 * 跳转到采购货源配置页面
	 * 
	 * @return
	 */
	@RequestMapping("/item/purConfigAdjustPage")
	public String purConfigAdjustPage() {
		return "mdm/config/purConfigAdjust";
	}

	/**
	 * 获取采购员数据
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/item/getPurchaserInfo")
	public Map<String, Object> getPurchaserInfo() {

		Map<String, Object> map = new HashMap<String, Object>();

		OrgDo org = new OrgDo();
		org.setScode("%PURCHAROR%");

		// 查询所有的采购员
		List<UserDO> list = orgService.getOrgUserByCondition(org);

		map.put("code", "0");
		map.put("data", list);
		map.put("count", list.size());
		map.put("msg", "");
		return map;
	}

	/**
	 * 处理调整的用户数据
	 * 
	 * @param adjustData
	 * @return
	 */
	@RequestMapping("/item/dealAdjustData")
	@ResponseBody
	public RestCode dealAdjustData(String adjustData) {
		try {
			materialService.dealAdjustData(adjustData);
		} catch (Exception e) {
			return RestCode.error(700, "");
		}
		return RestCode.ok();
	}

	/**
	 * OEM供应商和包材供应商关联关系
	 * 
	 * @return
	 */
	@RequestMapping("/supp_pack_mate/list")
	public String suppPackMate() {

		return "/mdm/config/supp_baocai_mate";
	}

	@RequestMapping("/supp_pack_mate/multiple_choice_mate")
	public String multipleChoiceMate(Model model, String oemPackId) {
		// Z001
		QualSupp qualSupp = new QualSupp();
		qualSupp.setCategory("Z001");
		List<QualSuppDO> list = qualSuppService.queryQualSuppByQualSupp(qualSupp);
		model.addAttribute("suppList", list);
		model.addAttribute("suppListStr", JsonUtils.beanToJson(list));
		OemPackSupp oemPackSupp = new OemPackSupp();
		if (!StringUtils.isEmpty(oemPackId))
			oemPackSupp = new OemPackSupp();
		model.addAttribute("oemPackSupp", oemPackSupp);
		return "/mdm/config/multipleChoiceMate";
	}

	/**
	 * 获取
	 * 
	 * @param oemPackSupp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/supp_pack_mate/mate_data")
	public List<MateDO> mateData(OemPackSupp oemPackSupp) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ownId", user.getUserId());
		paramMap.put("orgCode", "PURCHAROR");
		paramMap.put("isContainOwn", true);
		// 获取这个管理员下的采购员
		List<UserDO> list = orgService.manageSubordinateUsers(paramMap);
		paramMap.put("purcharor", list);
		MateDO mateDO = new MateDO();
		mateDO.setMateType("0002");
		mateDO.setMateCode(oemPackSupp.getMateCode());
		paramMap.put("mate", mateDO);
		List<MateDO> mateList = materialService.findMateListOfPurcharor(paramMap);
		String oemSuppCode = oemPackSupp.getOemSuppCode();
		oemSuppCode = StringUtils.isEmpty(oemSuppCode) ? "999999" : oemSuppCode;
		oemPackSupp.setOemSuppCode(oemSuppCode);
		String packSuppCode = oemPackSupp.getPackSuppCode();
		packSuppCode = StringUtils.isEmpty(packSuppCode) ? "999999" : packSuppCode;
		oemPackSupp.setPackSuppCode(packSuppCode);
		List<String> selectedList = configService.findSelectedMateCodesByOemPackSupp(oemPackSupp);
		if (selectedList.size() > 0)
			for (MateDO mate : mateList) {
				String mateCode = mate.getMateCode();
				if (selectedList.contains(mateCode))
					mate.setLAY_CHECKED("yes");
			}
		return mateList;
	}

	/**
	 * 保存数据
	 * 
	 * @param jsonData
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/supp_pack_mate/save_oem_pack_supp")
	public RestCode saveOemPackSuppInfo(String jsonData) {
		int i = configService.saveOemPackSuppInfo(jsonData);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 供应商管理配置列表
	 * 
	 * @param ops
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/supp_pack_mate/list_data")
	public Map<String, Object> oemPackSuppList(OemPackSupp ops, Integer page, Integer limit) {
		page = page == null ? 1 : page;
		limit = limit == null ? 10 : limit;
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return configService.findOemPackSuppList(ops, rb);
	}

	/**
	 * 删除
	 * 
	 * @param jsonData
	 * @return
	 */
	@RequestMapping("/supp_pack_mate/remove_oem_pack_supp")
	@ResponseBody
	public RestCode removeOemPackSupp(String jsonData) {
		if (!StringUtils.isEmpty(jsonData)) {
			List<OemPackSupp> list = JsonUtils.jsonToList(jsonData, OemPackSupp.class);
			int k = configService.removeOemPackSupp(list);
			if (k > 0)
				return RestCode.ok();
		}
		return RestCode.error();
	}

	@RequestMapping("/supp_pack_mate/upload_excel")
	@ResponseBody
	public Map<String, Object> uploadImg(@RequestParam MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 判断文件名是否为空
		if (file == null)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取文件名
		String name = file.getOriginalFilename();

		// 判断文件大小、即名称
		long size = file.getSize();
		if (name == null || ("").equals(name) && size == 0)
			return null;

		try {
			// 把文件转换成字节流形式
			String errTxt = configService.importExcel(name, file);
			if ("".equals(errTxt)) {
				String Msg = "批量导入EXCEL成功！";
				map.put("msg", Msg);
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("message", errTxt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
