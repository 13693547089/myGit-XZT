package com.faujor.web.bam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.*;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.CutLiaisonForBaoCaiService;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContactSheetForBaoCaiController {
	@Autowired
	private CutLiaisonForBaoCaiService cutLiaisonService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private CutProductService cutProductService;
	@Autowired
	private BasicService basicService;

	/**
	 * 跳转包材打切联络单列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/getBaoCaiContactSheetListHtml")
	public String getContactSheetListHtml(final Model model) {
		//CUTLIAISTATUS
		List<Dic> cutLiaiStatusList = basicService.findDicListByCategoryCode("CUTLIAISTATUS");
		model.addAttribute("cutLiaiStatusList", cutLiaiStatusList);
		return "bam/placut/packingSupplier/contactSheetList";
	}

	/**
	 * 包材打切联络单列表数据
	 * @param limit
	 * @param page
	 * @param cutLiai
	 * @return
	 */
	@Log(value = "获取包材打切联络单列表")
	@ResponseBody
	@RequestMapping("/queryBaoCaiCutLiaisonByPage")
	public Map<String, Object> queryBaoCaiCutLiaisonByPage(Integer limit, Integer page, CutBaoCai cutLiai) {
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		SysUserDO user = UserCommon.getUser();
		map.put("start", start);
		map.put("end", end);
		map.put("cutLiai", cutLiai);
		if("supplier".equals(user.getUserType())){
			map.put("createId", user.getUserId().toString());
		}else{
			map.put("userId", user.getUserId().toString());
		}
		Map<String, Object> page2 = cutLiaisonService.queryBaoCaiCurLiaisonByPage(map);
		return page2;
	}

	/**
	 * 跳转新建包材打切联络单
	 * @param model
	 * @return
	 */
	@RequestMapping("/getBaoCaiContactSheetDetailHtml")
	public String getContactSheetDetailHtml(final Model model) {
		SysUserDO user = UserCommon.getUser();
		Date date = new Date();
		CutBaoCai cutLiai = new CutBaoCai();
		cutLiai.setCreateDate(date);
		cutLiai.setSuppName(user.getName());
		cutLiai.setSuppId("s0000900009");
		if("supplier".equals(user.getUserType())){
			QualSupp supp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			cutLiai.setSuppId(supp.getSuppId());
		}
		model.addAttribute("cutLiai", cutLiai);
		return "bam/placut/packingSupplier/ContactSheetForBaoCaiDetail";
	}

	/**
	 * 获取字段
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBaoCaiFields")
	public String queryBaoCaiFields() {
		JSONArray ja = new JSONArray();
		JSONObject jo1 = new JSONObject();
		jo1.put("title", "序号");
		jo1.put("type", "numbers");
		jo1.put("fixed", "left");
		ja.add(jo1);
		JSONObject jo2 = new JSONObject();
		jo2.put("title", "OEM供应商");
		jo2.put("field", "oemSuppName");
		jo2.put("width", "117");
		jo2.put("fixed", "left");
		ja.add(jo2);
		JSONObject jo3 = new JSONObject();
		jo3.put("title", "物料名称");
		jo3.put("field", "mateName");
		jo3.put("width", "117");
		jo3.put("fixed", "left");
		ja.add(jo3);
		JSONObject jo4 = new JSONObject();
		jo4.put("title", "版本");
		jo4.put("field", "version");
		jo4.put("width", "58");
		jo4.put("fixed", "left");
		ja.add(jo4);
		List<CutStructure> list = cutProductService.queryBaoCaiCutStru();
		for (CutStructure c : list) {

			String a = c.getContentCode();

			JSONObject jo = new JSONObject();
			jo.put("title", c.getClassName() + "订单在外量" + c.getUnit());
			jo.put("field", a + "Out");
			jo.put("edit", "text");
			jo.put("width", "140");
			jo.put("event", "setSign");
			ja.add(jo);

			JSONObject jo16 = new JSONObject();
			jo16.put("title", c.getClassName() + "打切订单在外量" + c.getUnit());
			jo16.put("field", a + "OutCut");
			jo16.put("edit", "text");
			jo16.put("width", "170");
			jo16.put("event", "setSign");
			ja.add(jo16);

			JSONObject jo17 = new JSONObject();
			jo17.put("title", c.getClassName() + "库存包材厂" + c.getUnit());
			jo17.put("field", a + "StockPack");
			jo17.put("edit", "text");
			jo17.put("width", "170");
			jo17.put("event", "setSign");
			ja.add(jo17);

		}
		JSONObject jo14 = new JSONObject();
		jo14.put("title", "OEM供应商编号");
		jo14.put("field", "oemSuppCode");
		jo14.put("width", "147");
//		jo14.put("fixed", "right");
		ja.add(jo14);
		JSONObject jo15 = new JSONObject();
		jo15.put("title", "物料编号");
		jo15.put("field", "mateCode");
		jo15.put("width", "147");
//		jo15.put("fixed", "right");
		ja.add(jo15);
		JSONObject jo5 = new JSONObject();
		jo5.put("title", "特殊联络单");
		jo5.put("field", "isSpecial");
		jo5.put("width", "100");
		ja.add(jo5);
		return ja.toJSONString();
	}

	/**
	 * 根据月份查询
	 * 获取属于这个供应商的所有打切品
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBaoCaiAllCutLiaiMate")
	public Map<String,Object>  queryBaoCaiAllCutLiaiMate(String suppId,String cutMonth) {
		Map<String,Object> result = new HashMap<String,Object>();
		//只有新建打切联络单的时候需要校验
		boolean b = cutLiaisonService.checkoutBaoCaiCutLiaiByMonthAndSuppId(cutMonth,suppId);
		if(!b){
			result.put("judge", false);
			result.put("msg", cutMonth+"已存在打切联络单，相同年月无法创建多个打切联络单。");
			return result;
		}
		Map<String,Object>  map = cutProductService.queryAllCutProductWithSuppOfQualSupp(suppId,cutMonth);
		return map;
	}

	/**
	 * 新增联络单
	 * @param cutLiai
	 * @param cutLiaiMateData
	 * @param type
	 * @return
	 */
	@Log(value ="保存/提交包材打切联络单")
	@ResponseBody
	@RequestMapping("/addBaoCaiCutLiaison")
	public boolean addBaoCaiCutLiaison(CutBaoCai cutLiai,String cutLiaiMateData,String type){
		SysUserDO user = UserCommon.getUser();
		List<CutBaoCaiMate> list = JsonUtils.jsonToList(cutLiaiMateData, CutBaoCaiMate.class);
		String liaiCode = codeService.getCodeByCodeType("baocaiNo");
		cutLiai.setLiaiCode(liaiCode);
		cutLiai.setCreateId(user.getUserId().toString());
		cutLiai.setCreator(user.getName());
		cutLiai.setIsSpecial("NO");
		if("1".equals(type)){
			cutLiai.setStatus("已保存");
		}else if ("2".equals(type)){
			cutLiai.setStatus("已提交");
		}
		return cutLiaisonService.addBaoCaiCutLiaison(cutLiai,list);
	}

	/**
	 * 删除包材打切联络单
	 * @param liaiIds
	 * @return
	 */
	@Log(value ="删除包材打切联络单")
	@ResponseBody
	@RequestMapping("/deleteBaoCaiCutLiaisonByliaiIds")
	public boolean deleteBaoCaiCutLiaisonByliaiIds(String[] liaiIds){
		return cutLiaisonService.deleteBaoCaiCutLiaisonByliaiIds(liaiIds);
	}

	/**
	 * 提交/退回包材打切联络单
	 * @param liaiIds
	 * @return
	 */
	@Log(value ="提交包材打切联络单")
	@ResponseBody
	@RequestMapping("/updateBaoCaiCutLiaiStatusByliaiIds")
	public boolean updateBaoCaiCutLiaiStatusByliaiIds(String liaiIds,String types){
		List<String> liaiId = JsonUtils.jsonToList(liaiIds, String.class);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("liaiIds", liaiId);
		map.put("size", liaiId.size());
		if ("2".equals(types)){
			map.put("status", "已提交");
		}else if ("3".equals(types)){
			map.put("status", "已退回");
		}
		return cutLiaisonService.updateBaoCaiCutLiaiStatusByliaiIds(map);
	}
	
	/**
	 * 跳转到包材打切联络单编辑页面
	 * @param liaiId
	 * @return
	 */
	@RequestMapping("/getBaoCaiContactSheetEditHtml")
	public String getContactSheetEditHtml(Model model,String liaiId){
		CutBaoCai cutLiai = cutLiaisonService.queryBaoCaiCutLiaisonByLiaiId(liaiId);
		model.addAttribute("cutLiai", cutLiai);
		model.addAttribute("cite", "no");
		return "bam/placut/packingSupplier/ContactSheetForBaoCaiEdit";
	}

	/**
	 * 跳转到包材打切联络单编辑页面
	 * @param liaiId
	 * @return
	 */
	@RequestMapping("/getBaoCaiContactSheetCheckHtml")
	public String getContactSheetCheckHtml(Model model,String liaiId){
		CutBaoCai cutLiai = cutLiaisonService.queryBaoCaiCutLiaisonByLiaiId(liaiId);
		model.addAttribute("cutLiai", cutLiai);
		return "bam/placut/packingSupplier/ContactSheetForBaoCaiCheck";
	}
	
	/**
	 * 获取包材打切联络单的物料信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/getBaoCaiData")
	@ResponseBody
	public Map<String, Object> getBaoCaiData(String id){
		Map<String, Object> map = cutLiaisonService.getBaoCaiContactSheet(id);
		return map;
	}

	/**
	 * 解析获取字段
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBaoCaiLiaiMateFields")
	public String queryBaoCaiLiaiMateFields(String liaiId){
		JSONArray ja = cutLiaisonService.queryBaoCaiLiaiMateFields(liaiId);
		return ja.toJSONString();
	}

	/**
	 * 编辑保存和提交
	 * type=1:表示保存
	 * type=2:表示提交
	 * @param cutLiai
	 * @param cutLiaiMateData
	 * @param type
	 * @return
	 */
	@Log(value ="编辑/提交打切联络单")
	@ResponseBody
	@RequestMapping("/udpateBaoCaiCutLiaiMate")
	public Map<String,Object> udpateBaoCaiCutLiaiMate(CutBaoCai cutLiai,String cutLiaiMateData,String type){
		if("已退回".equals(cutLiai.getStatus())){
			Map<String,Object> result = new HashMap<String,Object>();
			CutBaoCai cutLiaison = cutLiaisonService.queryBaoCaiCutLiaisonByLiaiId(cutLiai.getLiaiId());
			String cutMonth = cutLiaison.getCutMonth();
			String suppId = cutLiaison.getSuppId();
			//只有新建打切联络单的时候需要校验
			boolean b = cutLiaisonService.checkoutBaoCaiCutLiaiByMonthAndSuppId(cutMonth,suppId);
			if(!b){
				result.put("judge", false);
				result.put("msg", cutMonth+"已存在打切联络单，相同年月无法创建多个打切联络单。");
				return result;
			}
		}
		List<CutBaoCaiMate> list = JsonUtils.jsonToList(cutLiaiMateData, CutBaoCaiMate.class);
		return cutLiaisonService.udpateBaoCaiCutLiaiMate(cutLiai,list,type);
	}

	/**
	 * 校验同一个供应商相同年月份只有一个有效的打切联络单（已保存，已提交，已确认）
	 * @param suppId
	 * @param cutMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkoutBaoCaiCutLiaiByMonthAndSuppId")
	public boolean checkoutCutLiaiByMonthAndSuppId(String suppId,String cutMonth) {
		return cutLiaisonService.checkoutBaoCaiCutLiaiByMonthAndSuppId(cutMonth, suppId);
	}
	
}
