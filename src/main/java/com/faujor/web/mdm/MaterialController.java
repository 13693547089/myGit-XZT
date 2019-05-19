package com.faujor.web.mdm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.common.annotation.Log;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.mdm.MateBasicInfo;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.MateUnit;
import com.faujor.entity.mdm.Material;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;

@Controller
public class MaterialController {

	@Autowired
	private MaterialService materialService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private AsyncLogService asyncLogService;

	/**
	 * 跳转到物料主数据列表展示页面
	 * 
	 * @return
	 */
	@RequestMapping("/getMaterialListHtml")
	public String getMaterialListHtml(Model model) {
		List<Dic> mateTypeList = basicService.findDicListByCategoryCode("MATETYPE");
		model.addAttribute("mateTypeList", mateTypeList);
		return "mdm/mate/materialList";
	}

	/**
	 * 物料主数据列表分页
	 * 
	 * @param mate
	 * @param page
	 * @param limit
	 * @return
	 */
	@Log(value = "获取物料主数据列表")
	@ResponseBody
	@RequestMapping("/queryMaterialByPage")
	public Map<String, Object> queryMaterialByPage(Material mate, Integer page, Integer limit) {
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
	 * 跳转到物料详情页面
	 * 
	 * @param mateId
	 * @param model
	 * @return
	 */
	@Log(value = "查看物料详情")
	@RequestMapping("/getMaterialLookHtml")
	public String getMaterialLookHtml(String mateId, Model model) {
		Material mate = materialService.queryOneMaterialByMateId(mateId);
		List<MateUnit> unitList = materialService.queryMateUnitOfMaterialByMateId(mateId);
		model.addAttribute("mate", mate);
		model.addAttribute("unitList", unitList);
		return "mdm/mate/materialLook";
	}

	/**
	 * 同步物料数据
	 * 
	 * @param mateType
	 * @return
	 */
	@RequestMapping("/asyncMateInfo")
	@ResponseBody
	public RestCode asyncMateInfo(String mateType) {
		// 插入同步记录
		AsyncLog al = new AsyncLog();
		String alId = UUIDUtil.getUUID();
		al.setId(alId);
		al.setAsyncName("物料主数据同步");
		asyncLogService.saveAsyncLog(al);
		int k = materialService.asyncMateInfo(mateType, al);
		if (k > 0)
			return RestCode.ok("成功同步" + k + "条！");
		return RestCode.error("无数据更新！");
	}

	/**
	 * 产品基本信息
	 * 
	 * @param model
	 * @param mateCode
	 * @return
	 */
	@RequestMapping("/mate/basicInfo")
	public String basicInfo(Model model, String mateCode) {
		Material mate = materialService.queryMaterialByMateCode(mateCode);
		model.addAttribute("mate", mate);
		return "mdm/mate/basicInfo";
	}

	/**
	 * 根据物料id获取产品基本信息
	 * 
	 * @param mateId
	 * @return
	 */
	@RequestMapping("/mate/basicData")
	@ResponseBody
	public List<MateBasicInfo> basicData(String mateId) {
		List<MateBasicInfo> list = materialService.findMateBasicListByMateId(mateId);
		return list;
	}

	/**
	 * 保存产品基本信息
	 * 
	 * @param mateId
	 * @param basicInfo
	 * @return
	 */
	@RequestMapping("/mate/saveBasicInfo")
	@ResponseBody
	RestCode saveBasicInfo(String mateId, String basicInfo) {
		List<MateBasicInfo> list = JsonUtils.jsonToList(basicInfo, MateBasicInfo.class);
		int i = materialService.saveBasicInfo(mateId, list);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/***** 物料的单选开始 ****/
	/**
	 * 进入单选功能
	 * 
	 * @return
	 */
	@RequestMapping("/mate/choose_single")
	public String chooseSingle() {

		return "mdm/mate/chooseMateSingle";
	}

	/**
	 * 单选物料列表
	 * 
	 * @param page
	 * @param limit
	 * @param mateCode
	 * @return
	 */
	@RequestMapping("/mate/to_choose_single_data")
	@ResponseBody
	public Map<String, Object> choseSingleData(Integer page, Integer limit, MateDO mate) {

		return materialService.chooseSingleData(page, limit, mate);
	}

	/***** 物料的单选结束 ****/

}
