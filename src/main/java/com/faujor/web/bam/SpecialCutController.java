package com.faujor.web.bam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.CutLiaiMate;
import com.faujor.entity.bam.CutLiaison;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.service.bam.CutLiaisonService;
import com.faujor.service.bam.CutProductService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class SpecialCutController {
	@Autowired
	private CutLiaisonService cutLiaisonService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private BasicService basicService;
	/**
	 *
	 * 跳转到特殊打切联络单列表
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSpecialCutListHtml")
	public String getSpecialCutListHtml(Model model) {
		List<Dic> cutLiaiStatusList = basicService.findDicListByCategoryCode("SPECUTLIAISTATUS");
		model.addAttribute("cutLiaiStatusList", cutLiaiStatusList);
		return "bam/placut/SpecailCut/specialCutList";
	}

	/**
	 * 特殊打切联络单列表数据
	 * 
	 * @param limit
	 * @param page
	 * @param cutLiai
	 * @return
	 */
	@Log(value = "获取特殊打切联络单列表")
	@ResponseBody
	@RequestMapping("/querySpeCutLiaisonByPage")
	public Map<String, Object> querySpeCutLiaisonByPage(Integer limit, Integer page, CutLiaison cutLiai) {
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
		//map.put("userId", user.getUserId().toString());
		Map<String, Object> page2 = cutLiaisonService.querySpeCutLiaisonByPage(map);
		return page2;
	}
	
	/**
	 * 特殊录入打切
	 * 新建 type =1
	 * 编辑 type =2 
	 * 查看 type =3
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSpecialCutAddHtml")
	public String getSpecialCutAddHtml(Model model,String type,String liaiId){
		model.addAttribute("type", type);
		if("1".equals(type)){
			Date date = new Date();
			CutLiaison cutLiai = new CutLiaison();
			cutLiai.setCreateDate(date);
			model.addAttribute("cutLiai", cutLiai);
		}else{
			CutLiaison cutLiai = cutLiaisonService.queryCutLiaisonByLiaiId(liaiId);
			model.addAttribute("cutLiai", cutLiai);
		}
		return "bam/placut/SpecailCut/specialCutAdd";
	}
	
	/**
	 * 创建特殊打切时，获取特殊打切品物料数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/querySpecialCutLiaiMate")
	public Map<String,Object> querySpecialCutLiaiMate(String cutMonth){
		return cutLiaisonService.querySpecialCutLiaiMate(cutMonth);
	}
	/**
	 * 新建保存和编辑保存
	 * type : 1 和  type : 2
	 * @param cutLiai
	 * @param cutLiaiMateData
	 * @param type
	 * @return
	 */
	@Log(value = "保存/修改特殊打切联络单")
	@ResponseBody
	@RequestMapping("/addSpeCutLiaison")
	public boolean addSpeCutLiaison(CutLiaison cutLiai,String cutLiaiMateData,String type){
		List<CutLiaiMate> list = JsonUtils.jsonToList(cutLiaiMateData, CutLiaiMate.class);
		if("1".equals(type)){
			cutLiai.setStatus("已保存");
			SysUserDO user = UserCommon.getUser();
			String liaiCode = codeService.getCodeByCodeType("cutLiaiNo");
			cutLiai.setLiaiCode(liaiCode);
			cutLiai.setCreateId(user.getUserId().toString());
			cutLiai.setCreator(user.getName());
			cutLiai.setIsSpecial("YES");
			return cutLiaisonService.addCutLiaison(cutLiai,list);
		}else{
			return cutLiaisonService.updateSpeCutLiaison(cutLiai,list);
		}
	}
	/**
	 * 新建提交和编辑提交
	 * 
	 * type =1 和type = 2
	 * @param cutLiai
	 * @param cutLiaiMateData
	 * @param type
	 * @return
	 */
	@Log(value = "提交/修改特殊打切联络单")
	@ResponseBody
	@RequestMapping("/commitSpeCutLiaison")
	public boolean commitSpeCutLiaison(CutLiaison cutLiai,String cutLiaiMateData,String type){
		List<CutLiaiMate> list = JsonUtils.jsonToList(cutLiaiMateData, CutLiaiMate.class);
		cutLiai.setStatus("已确认");
		if("1".equals(type)){
			SysUserDO user = UserCommon.getUser();
			String liaiCode = codeService.getCodeByCodeType("cutLiaiNo");
			cutLiai.setLiaiCode(liaiCode);
			cutLiai.setCreateId(user.getUserId().toString());
			cutLiai.setCreator(user.getName());
			cutLiai.setIsSpecial("YES");
			return cutLiaisonService.addCutLiaison(cutLiai,list);
		}else{
			return cutLiaisonService.updateSpeCutLiaison2(cutLiai,list);
		}
	}
	/**
	 * 删除特殊打切联络单
	 * @param liaiIds
	 * @return
	 */
	@Log(value = "删除特殊打切联络单")
	@ResponseBody
	@RequestMapping("/deleteSpeCutLiaisonByliaiIds")
	public boolean deleteSpeCutLiaisonByliaiIds(String[] liaiIds){
		return cutLiaisonService.deleteSpeCutLiaisonByliaiIds(liaiIds);
	}
	/**
	 * 特殊打切列表上提交打切联络单
	 * @param liaiIds
	 * @return
	 */
	@Log(value = "提交特殊打切联络单")
	@ResponseBody
	@RequestMapping("/updateSpeCutLiaiStatusByliaiIds")
	public boolean updateSpeCutLiaiStatusByliaiIds(String liaiIds){
		List<String> list = JsonUtils.jsonToList(liaiIds, String.class);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("liaiIds", list);
		map.put("size", list.size());
		map.put("status", "已确认");
		return cutLiaisonService.updateCutLiaiStatusByliaiIds(map);
	}
	/**
	 * 已保存的特殊打切联络单更新物料数据
	 * @param liaiId
	 * @param cutMonth
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSpeCutLiaiMateByMonth")
	public Map<String, Object> updateSpeCutLiaiMateByMonth(String liaiId,String cutMonth){
		return cutLiaisonService.updateSpeCutLiaiMateByMonth(liaiId,cutMonth);
	}
	
	/**
	 * 取消已确认的特殊打切联络单
	 * @param liaiIds
	 * @return
	 */
	@Log(value = "取消已确认的特殊打切联络单")
	@ResponseBody
	@RequestMapping("/cancleSpeCutLiaiByliaiIds")
	public boolean cancleSpeCutLiaiByliaiIds(String liaiIds){
		List<String> list = JsonUtils.jsonToList(liaiIds, String.class);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("liaiIds", list);
		map.put("size", list.size());
		map.put("status", "已保存");
		return cutLiaisonService.updateCutLiaiStatusByliaiIds(map);
	}

}
