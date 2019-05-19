package com.faujor.web.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.basic.DicCategory;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.LayuiTree;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.utils.BuildLayuiTree;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;

@Controller
public class BasicController {
	@Autowired
	private BasicService basicService;
	@Autowired
	private AsyncLogService asyncLogService;

	/**
	 * 字典列表
	 * 
	 * @return
	 */
	@RequestMapping("/basic/dicList")
	public String dicList() {
		return "basic/dicList";
	}

	@RequestMapping("/basic/dicInfo")
	@ResponseBody
	public Map<String, Object> dicInfo(DicCategory dic, Integer page, Integer limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		final int start = (page - 1) * limit + 1;
		final int end = page * limit;
		params.put("category", dic);
		params.put("start", start);
		params.put("end", end);
		Map<String, Object> map = basicService.findDicCategoryByParams_1(params);
		return map;
	}

	/**
	 * 字典详情
	 * 
	 * @param type
	 * @param cateId
	 * @return
	 */
	@RequestMapping("/basic/dicDetails")
	public String dicDetails(Model model, String type, String cateId) {
		DicCategory dicCate = new DicCategory();
		if ("add".equals(type)) {

		}
		model.addAttribute("dicCate", dicCate);
		return "basic/dicDetails";
	}

	/**
	 * 获取字典详情数据
	 * 
	 * @param cateId
	 * @return
	 */
	@GetMapping("/basic/getDicDetailsData")
	@ResponseBody
	public List<Dic> getDicDetailsData(String cateId) {
		return basicService.findDicListByCateId(cateId);
	}

	@RequestMapping("/dicAddHtml")
	public String dicAdd(Model model, String id, Integer type) {

		// 0创建1编辑2查看
		DicCategory category = new DicCategory();
		if (type == 0) {// 新建

			model.addAttribute("category", category);
		} else if (type == 1) {// 编辑
			category = basicService.findDicCategoryById(id);
			model.addAttribute("category", category);
		}
		return "basic/dicAdd";
	}

	@RequestMapping("/findDicCategoryById")
	@ResponseBody
	public DicCategory findDicCategoryById(Model model, String id) {
		DicCategory category = basicService.findDicCategoryById(id);
		model.addAttribute("category", category);

		return category;
	}

	@RequestMapping("/dicInfo/dicDetails_1")
	@ResponseBody
	public List<Dic> dicDetails_1(String cateId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cateId", cateId);
		List<Dic> list = basicService.findDicByParams_1(params);
		return list;
	}

	@RequestMapping("/dicInfo")
	public String dicIndex() {
		return "basic/dic";
	}

	@RequestMapping("/dicInfo/dic")
	@ResponseBody
	public Map<String, Object> dicInfo(Model model, DicCategory dic, Integer page, Integer limit, String cateName,
			String cateCode, String cateType) {
		Map<String, Object> params = new HashMap<String, Object>();
		dic.setCateCode(cateCode);
		dic.setCateName(cateName);
		dic.setIsUsed(Long.valueOf((cateType == null || cateType.equals("")) ? "0" : cateType));
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		final int start = (page - 1) * limit + 1;
		final int end = page * limit;

		params.put("start", start);
		params.put("end", end);
		params.put("category", dic);
		Map<String, Object> map = basicService.findDicCategoryByParams(params);
		return map;
	}

	@RequestMapping("/dicDetails")
	public String dicDetailsIndex() {
		return "basic/dicInfo";
	}

	@RequestMapping("/dicInfo/dicDetails")
	@ResponseBody
	public Map<String, Object> dicDetails(String cateId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cateId", cateId);
		Map<String, Object> map = basicService.findDicByParams(params);
		return map;
	}

	/**
	 * 验证编码不重复
	 * 
	 * @param code
	 * @param name
	 * @return
	 */
	@RequestMapping("/checkCode")
	@ResponseBody
	public boolean checkCode(String cateCode, String cateId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("code", cateCode);
		param.put("id", cateId);
		int i = basicService.findDicCategoryByIdANDCode(param);
		if (i == 0) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("/findDiByCateID")
	@ResponseBody
	public Map<String, Object> findDiByCateID(String cateId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cateId", cateId);
		return basicService.findDicByParams(params);
	}

	@RequestMapping("/deleteCategoryInfo")
	@ResponseBody
	public int deleteCategoryInfo(String selected) {
		return basicService.deleteCategoryInfo(selected);
	}

	/**
	 * 根据列表编码获取字典信息
	 * 
	 * @param cateCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/dic/findDicListByCategoryCode")
	public List<Dic> findDicListByCategoryCode(String cateCode) {
		return basicService.findDicListByCategoryCode(cateCode);
	}

	/**
	 * 同步记录列表
	 * 
	 * @return
	 */
	@RequestMapping("/basic/asyncLog")
	public String asyncLog() {
		return "basic/asyncLog";
	}

	@ResponseBody
	@RequestMapping("/basic/findAsyncLogList")
	public Map<String, Object> findAsyncLogList(AsyncLog al, Integer limit, Integer page) {
		int offset = (page - 1) * limit;
		RowBounds rb = new RowBounds(offset, limit);
		return asyncLogService.findAsyncLogList(al, rb);
	}

	@GetMapping("/basic/dic/cate_list1")
	@ResponseBody
	public Map<String, Object> categoryList(DicCategory cate, Integer limit, Integer page) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		final int start = (page - 1) * limit + 1;
		final int end = page * limit;
		params.put("category", cate);
		params.put("start", start);
		params.put("end", end);
		Map<String, Object> map = basicService.findDicCategoryByParams(params);
		return map;
	}

	@GetMapping("/basic/dic/find_dic_by_cate_id")
	@ResponseBody
	public Map<String, Object> findDicByCategoryID(String cateId, String cateType) {
		List<Dic> dicList = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();
		if (!StringUtils.isEmpty(cateId)) {
			dicList = basicService.findDicListByCateId(cateId);
		}
		// 如果是普通字典，则直接返回列表
		if ("normal".equals(cateType)) {
			result.put("cateData", dicList);
			return result;
		}

		List<LayuiTree<Dic>> list = new ArrayList<>();
		for (Dic dic : dicList) {
			LayuiTree<Dic> tree = new LayuiTree<>();
			tree.setId(dic.getId());
			tree.setParentId(dic.getParentId());
			tree.setName(dic.getDicName());
			tree.setSpread(false);
			list.add(tree);
		}
		LayuiTree<Dic> layuiTree = BuildLayuiTree.build(list, "字典信息");

		result.put("treeData", layuiTree);
		return result;
	}

	@RequestMapping("/basic/dic/dic_tree_data1")
	public String dicData(Model model, String id, String parentId) {
		Dic dic = basicService.findDicInfoById(id);
		if (dic == null) {
			dic = new Dic();
			dic.setId(id);
			dic.setParentId(parentId);
		}
		model.addAttribute("dic", dic);
		return "/basic/dic/dic_tree_details::dicDiv";
	}

	/**
	 * 保存
	 * 
	 * @param cate
	 * @param dic
	 * @return
	 */
	@PostMapping("/basic/dic/save_dic_info")
	@ResponseBody
	public String saveDicInfo(DicCategory cate, Dic dic) {
		cate.setId(dic.getCateId());
		String result = basicService.saveDicAndCategoryInfo(cate, dic);
		return result;
	}

	/**
	 * 保存字典信息
	 * 
	 * @param category
	 * @param jsonStr
	 * @return
	 */
	@RequestMapping("/saveDicInfo")
	@ResponseBody
	public boolean saveDicInfo(DicCategory category, String jsonData) {
		List<Dic> jsonToList = JsonUtils.jsonToList(jsonData, Dic.class);
		category.setDicList(jsonToList);
		int i = basicService.saveDicInfo(category);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存 普通字典信息
	 * 
	 * @param category
	 * @param jsonData
	 * @return
	 */
	@RequestMapping("/basic/dic/save_normal_dic_info")
	@ResponseBody
	public RestCode saveNormalDicInfo(DicCategory category, String jsonData) {
		List<Dic> jsonToList = JsonUtils.jsonToList(jsonData, Dic.class);
		category.setDicList(jsonToList);
		String r = basicService.saveNormalDicInfo(category);
		return RestCode.ok(r);
	}

	/**
	 * 删除 字典信息
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/basic/dic/delete_dic_info")
	@ResponseBody
	public String deleteDicInfo(String id) {

		String result = basicService.deleteDicInfo(id);
		return result;
	}
}
