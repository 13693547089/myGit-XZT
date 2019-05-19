package com.faujor.service.rm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.dao.master.bam.CutLiaisonForBaoCaiMapper;
import com.faujor.dao.master.bam.CutLiaisonMapper;
import com.faujor.dao.master.mdm.MaterialMapper;
import com.faujor.dao.master.privilege.AssignMapper;
import com.faujor.dao.master.privilege.OrgMapper;
import com.faujor.dao.master.rm.PackInfoMapper;
import com.faujor.entity.bam.CutBaoCai;
import com.faujor.entity.bam.CutLiaiField;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.privileges.OrgDo;
import com.faujor.entity.privileges.RoleDO;
import com.faujor.entity.privileges.UserDO;
import com.faujor.entity.rm.CutMatePack;
import com.faujor.entity.rm.PackOrderVO;
import com.faujor.entity.rm.SemiMatePack;
import com.faujor.service.bam.CutLiaisonForBaoCaiService;
import com.faujor.service.bam.CutLiaisonService;
import com.faujor.service.privileges.OrgService;
import com.faujor.service.rm.PackInfoService;
import com.faujor.utils.UserCommon;
import com.lowagie.text.Row;
@Service(value="packInfoService")
public class PackInfoServiceImpl implements PackInfoService {

	@Autowired
	private PackInfoMapper packInfoMapper;

	@Autowired
	private CutLiaisonService cutLiaisonService;
	@Autowired
	private CutLiaisonMapper cutLiaisonMapper;
	@Autowired
	private CutLiaisonForBaoCaiMapper cutLiaisonBaoCaiMapper;
	@Autowired
	private CutLiaisonForBaoCaiService cutLiaisonBaoCaiService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private MaterialMapper materialMapper;
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private AssignMapper assignMapper;
	@Override
	public Map<String, Object> getSemiMatePackList(Map<String, Object> map) {
		List<SemiMatePack> list = packInfoMapper.getSemiMatePackList(map);
		int count = packInfoMapper.getSemiMatePackListCount(map);
		Map<String, Object> result = new HashMap<>();
		result.put("data", list);
		result.put("count", count);
		result.put("msg", "");
		result.put("code", 0);
		return result;
	}

	@Override
	public List<SemiMatePack> getSemimatePacks(Map<String, Object> map) {
		return packInfoMapper.getSemimatePacks(map);
	}

	@Override
	public Map<String, Object> getCutMatePackListByPage(Map<String, Object> map) {
		 SysUserDO user = UserCommon.getUser();
		 OrgDo org = new OrgDo();
		 org.setScode("PURCHAROR");
		 org.setSfid(user.getUserId().toString());
		 //判断当前登录人员是不是采购员
		 UserDO userDO = orgMapper.findOrgByParams2(org);
		 if(userDO != null) {
			 //是采购员
			 Map<String, Object> paramMap = new HashMap<String, Object>();
			 paramMap.put("ownId", user.getUserId());
			 paramMap.put("orgCode", "PURCHAROR");
			 paramMap.put("isContainOwn", true);
			 // 获取这个管理员下的采购员
			 List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
			 if(userList.size()>0) {
				 //查询这些采购员在采购货源中对应的物料编码集合
//				 List<String> mateCodeList = materialMapper.getMateCodeListByUsers(userList);
//				 if(mateCodeList.size()>0) {
//					 map.put("mateCodeList", mateCodeList);
//				 }else {
//					 map.put("judge", "wwwwww");
//				 }
				 map.put("userList", userList);
			 }else {
				 map.put("judge", "wwwwww");
			 }
		 }
		List<CutMatePack> list = packInfoMapper.getCutMatePackListByPage(map);
//		int count = packInfoMapper.getCutMatePackListByPageCount(map);
		CutMatePack cutMatePack = null;
		if(list.size()>0) {
			cutMatePack = list.get(0);
		}
		//打切包材列表抬头信息
		JSONArray jsonArray = getCutMatePackFields(cutMatePack);
		//处理数据信息
		JSONArray JAData = dealWithData(list,jsonArray);
		Map<String, Object> result = new HashMap<>();
		result.put("data", JAData);
		result.put("jsonArray", jsonArray);
		result.put("count", JAData.size());
		result.put("msg", "");
		result.put("code", 0);
		return result;
	}
	private JSONArray dealWithData(List<CutMatePack> list, JSONArray jsonArray) {
		JSONArray ja = new JSONArray();
		if(list.size()>0) {
			for (CutMatePack cPack : list) {
				JSONObject jo = new JSONObject();
				jo.put("mateCode", cPack.getMateCode());
				jo.put("mateName", cPack.getMateName());
				jo.put("version", "WWW".equals(cPack.getVersion())?"":cPack.getVersion());
				jo.put("boxNumber", cPack.getBoxNumber());
				//获取OEM打切联络单数据
				jo.put("oemSuppName", cPack.getOemSuppName());
				jo.put("sumOutNum", cPack.getSumOutNum());
				jo.put("outNum", cPack.getOutNum());
				jo.put("sumInveNum", cPack.getSumInveNum());
				jo.put("inveNum", cPack.getInveNum());
				String fields = cPack.getFields();//OEM数据
				//获取包材打切联络单数据
				JSONArray jj = JSONArray.parseArray(fields);
				for (int i = 0; i < jj.size(); i++) {
					JSONObject object = jj.getJSONObject(i);
					Set<String> keySet = object.keySet();
					for (String str : keySet) {
						jo.put(str, object.get(str));
					}
				}
				jo.put("baoSuppName", cPack.getBaoSuppName());
				String baoFields = cPack.getBaoFields();//包材数据
				JSONArray baocaijj = JSONArray.parseArray(baoFields);
				if(baocaijj != null) {
					for (int i = 0; i < baocaijj.size(); i++) {
						JSONObject object = baocaijj.getJSONObject(i);
						Set<String> keySet = object.keySet();
						for (String str : keySet) {
							jo.put(str, object.get(str));
						}
					}
				}
				jo.put("oemSapId", cPack.getOemSapId());
				jo.put("baoSapId", cPack.getBaoSapId());
				ja.add(jo);
			}
		}
		return ja;
	}

	//获取列表抬头信息
	private JSONArray getCutMatePackFields(CutMatePack cutMatePack) {
		String liaiId = "";
		String baoLiaiId = "";
		String cutMonth = "";
		if(cutMatePack != null ) {
			liaiId = cutMatePack.getLiaiId();
			baoLiaiId = cutMatePack.getBaoLiaiId();
			cutMonth = cutMatePack.getCutMonth();
		}else {
			//获取最新打切月份的一条数据
			CutLiaison cutLiaison = cutLiaisonMapper.queryNewCutMonthLiaison();
			liaiId = cutLiaison.getLiaiId();
			cutMonth = cutLiaison.getCutMonth();
		}
		//解析OEM打切联络单物料列表抬头信息
		JSONArray OEMJA = cutLiaisonService.queryLiaiMateFields(liaiId);
		if(StringUtils.isEmpty(baoLiaiId)) {
			CutBaoCai cutBaoCai = cutLiaisonBaoCaiMapper.queryBaoCaiListCutLiaisonByCutMonth2(cutMonth);
			if(cutBaoCai != null) {
				baoLiaiId = cutBaoCai.getLiaiId();
			}
		}
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("title", "序号");
		jo.put("type", "numbers");
		jo.put("fixed", "left");
		ja.add(jo);
		JSONObject jo1 = new JSONObject();
		jo1.put("title", "物料编号");
		jo1.put("field", "mateCode");
		jo1.put("fixed", "left");
		jo1.put("align", "center");
		jo1.put("width", "140");
		ja.add(jo1);
		JSONObject jo2 = new JSONObject();
		jo2.put("title", "物料描述");
		jo2.put("field", "mateName");
		jo2.put("fixed", "left");
		jo2.put("align", "center");
		jo2.put("width", "140");
		ja.add(jo2);
		JSONObject jo3 = new JSONObject();
		jo3.put("title", "版本");
		jo3.put("field", "version");
		jo3.put("fixed", "left");
		jo3.put("align", "center");
		jo3.put("width", "70");
		ja.add(jo3);
		JSONObject jo4 = new JSONObject();
		jo4.put("title", "箱入数");
		jo4.put("field", "boxNumber");
		jo4.put("fixed", "left");
		jo4.put("align", "center");
		jo4.put("width", "70");
		ja.add(jo4);
		//插入OEM打切联络单列头
		JSONObject jo7 = new JSONObject();
		jo7.put("title", "OEM供应商");
		jo7.put("field", "oemSuppName");
		jo7.put("align", "center");
		jo7.put("width", "140");
		ja.add(jo7);
		JSONObject jo8 = new JSONObject();
		jo8.put("title", "订单在外量");
		jo8.put("field", "sumOutNum");
		jo8.put("align", "center");
		jo8.put("width", "90");
		ja.add(jo8);
		JSONObject jo9 = new JSONObject();
		jo9.put("title", "订单打切在外量");
		jo9.put("field", "outNum");
		jo9.put("align", "center");
		jo9.put("width", "110");
		ja.add(jo9);
		JSONObject jo10 = new JSONObject();
		jo10.put("title", "成品库存");
		jo10.put("field", "sumInveNum");
		jo10.put("align", "center");
		jo10.put("width", "90");
		ja.add(jo10);
		JSONObject jo11 = new JSONObject();
		jo11.put("title", "打切成品库存");
		jo11.put("field", "inveNum");
		jo11.put("align", "center");
		jo11.put("width", "110");
		ja.add(jo11);
		//动态列的插入
		for (int i = 0; i < OEMJA.size(); i++) {
			CutLiaiField f = OEMJA.getObject(i, CutLiaiField.class);
			String fieldName = f.getField();
			if(fieldName != null ) {
				if( fieldName.startsWith("A") || fieldName.startsWith("B")) {
					String title = f.getTitle();
					if(title.contains("OEM")) {
						JSONObject jo13 = new JSONObject();
						jo13.put("title", title);
						jo13.put("field", fieldName);
						jo13.put("align", "center");
						jo13.put("width", "107");
						ja.add(jo13);
					}
				}
			}
		}
		if(!StringUtils.isEmpty(baoLiaiId)) {
			//插入包材打切联络单列头
			JSONObject jo12 = new JSONObject();
			jo12.put("title", "包材供应商");
			jo12.put("field", "baoSuppName");
			jo12.put("align", "center");
			jo12.put("width", "140");
			ja.add(jo12);
			//解析包材打切联络单物料列表抬头信息
			JSONArray baocaiJA = cutLiaisonBaoCaiService.queryBaoCaiLiaiMateFields(baoLiaiId);
			//动态列的插入
			for (int i = 0; i < baocaiJA.size(); i++) {
				CutLiaiField f = baocaiJA.getObject(i, CutLiaiField.class);
				String field = f.getField();
				if(field != null) {
					if(field.contains("Out") || field.contains("OutCut") || field.contains("StockPack")) {
						String title = f.getTitle();
						JSONObject jo14 = new JSONObject();
						jo14.put("title", title);
						jo14.put("field", field);
						jo14.put("align", "center");
						jo14.put("width", "140");
						ja.add(jo14);
					}
				}
			}
		}
		JSONObject jo5 = new JSONObject();
		jo5.put("title", "OEM供应商编号");
		jo5.put("field", "oemSapId");
		jo5.put("align", "center");
		jo5.put("width", "110");
		ja.add(jo5);
		if(!StringUtils.isEmpty(baoLiaiId)) {
			JSONObject jo6 = new JSONObject();
			jo6.put("title", "包材供应商编号");
			jo6.put("field", "baoSapId");
			jo6.put("align", "center");
			jo6.put("width", "100");
			ja.add(jo6);
		}
		return ja;
	}

	//获取列表抬头信息
	@Override
	public String getCutMatePackListFields(String cutMonth) {
		
		return null;
	}

	@Override
	public Map<String, Object>  getCutMatePacks(Map<String, Object> map) {
		 SysUserDO user = UserCommon.getUser();
		 OrgDo org = new OrgDo();
		 org.setScode("PURCHAROR");
		 org.setSfid(user.getUserId().toString());
		 //判断当前登录人员是不是采购员
		 UserDO userDO = orgMapper.findOrgByParams2(org);
		 if(userDO != null) {
			 //是采购员
			 Map<String, Object> paramMap = new HashMap<String, Object>();
			 paramMap.put("ownId", user.getUserId());
			 paramMap.put("orgCode", "PURCHAROR");
			 paramMap.put("isContainOwn", true);
			 // 获取这个管理员下的采购员
			 List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
			 if(userList.size()>0) {
				 //查询这些采购员在采购货源中对应的物料编码集合
				 /*List<String> mateCodeList = materialMapper.getMateCodeListByUsers(userList);
				 if(mateCodeList.size()>0) {
					 map.put("mateCodeList", mateCodeList);
				 }else {
					 map.put("judge", "wwwwww");
				 }*/
				 map.put("userList", userList);
			 }else {
				 map.put("judge", "wwwwww");
			 }
		 }
		List<CutMatePack> list = packInfoMapper.getCutMatePackList(map);
		CutMatePack cutMatePack = null;
		if(list.size()>0) { 
			cutMatePack = list.get(0);
		}
		//打切包材列表抬头信息
		JSONArray jsonArray = getCutMatePackFields(cutMatePack);
		//处理数据信息
		JSONArray JAData = dealWithData(list,jsonArray);
		Map<String, Object> result = new HashMap<>();
		result.put("data", JAData);
		result.put("jsonArray", jsonArray);
		result.put("count", JAData.size());
		result.put("msg", "");
		result.put("code", 0);
		return result;
	}

	@Override
	public Map<String, Object> getPackOrderListByPage(Map<String, Object> map) {
		SysUserDO user = UserCommon.getUser();
		RowBounds rb = new RowBounds(0, 10);
		RoleDO role = new RoleDO();
		role.setRoleName(user.getUsername());
//		role.setRoleId(48L);//测试系统中管理员角色编号
		role.setRoleId(1L);//测试系统中管理员角色编号
		//查询当前用户是否拥有管理员角色
		List<OrgDo> orgDoList = assignMapper.assignListByRoleId(rb, role);
		if(orgDoList.size()==0) {
			//没有
			//判断是否是采购员还是oem供应商
			String userType = user.getUserType();
			if("supplier".equals(userType)) {
				//供应商
				String suppNo = user.getSuppNo();//oem供应商的sap编码
				map.put("oemSapId", suppNo);
			}else {
				//采购员
				//是采购员
				 Map<String, Object> paramMap = new HashMap<String, Object>();
				 paramMap.put("ownId", user.getUserId());
				 paramMap.put("orgCode", "PURCHAROR");
				 paramMap.put("isContainOwn", true);
				 // 获取这个管理员下的采购员
				 List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
				 if(userList.size()>0) {
					 //查询这些采购员在采购货源中对应的物料编码集合
//					 List<String> mateCodeList = materialMapper.getMateCodeListByUsers(userList);
//					 if(mateCodeList.size()>0) {
//						 map.put("mateCodeList", mateCodeList);
//					 }else {
//						 map.put("judge", "wwwwww");
//					 }
					 map.put("userList", userList);
				 }else {
					 map.put("judge", "wwwwww");
				 }
			}
		}
		List<PackOrderVO> list = packInfoMapper.getPackOrderListByPage(map);
		int count = packInfoMapper.getPackOrderListByPageCount(map);
		Map<String, Object> result = new HashMap<>();
		result.put("data", list);
		result.put("count", count);
		result.put("msg", "");
		result.put("code", 0);
		return result;
	}

	@Override
	public List<PackOrderVO> getPackOrderList(Map<String, Object> map) {
		SysUserDO user = UserCommon.getUser();
		RowBounds rb = new RowBounds(0, 10);
		RoleDO role = new RoleDO();
		role.setRoleName(user.getUsername());
//		role.setRoleId(48L);//测试系统中管理员角色编号
		role.setRoleId(1L);//正式系统中管理员角色编号
		//查询当前用户是否拥有管理员角色
		List<OrgDo> orgDoList = assignMapper.assignListByRoleId(rb, role);
		if(orgDoList.size()==0) {
			//没有
			//判断是否是采购员还是oem供应商
			String userType = user.getUserType();
			if("supplier".equals(userType)) {
				//供应商
				String suppNo = user.getSuppNo();//oem供应商的sap编码
				map.put("oemSapId", suppNo);
			}else {
				//采购员
				//是采购员
				 Map<String, Object> paramMap = new HashMap<String, Object>();
				 paramMap.put("ownId", user.getUserId());
				 paramMap.put("orgCode", "PURCHAROR");
				 paramMap.put("isContainOwn", true);
				 // 获取这个管理员下的采购员
				 List<UserDO> userList = orgService.manageSubordinateUsers(paramMap);
				 if(userList.size()>0) {
					 //查询这些采购员在采购货源中对应的物料编码集合
//					 List<String> mateCodeList = materialMapper.getMateCodeListByUsers(userList);
//					 if(mateCodeList.size()>0) {
//						 map.put("mateCodeList", mateCodeList);
//					 }else {
//						 map.put("judge", "wwwwww");
//					 }
					 map.put("userList", userList);
				 }else {
					 map.put("judge", "wwwwww");
				 }
			}
		}
		return packInfoMapper.getPackOrderList(map);
	}
}
