package com.faujor.web.mdm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.common.User;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QuoteStruDO;
import com.faujor.entity.mdm.QuoteStruDetails;
import com.faujor.entity.mdm.QuoteTemp;
import com.faujor.entity.mdm.QuoteTempDetails;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QuoteStruService;
import com.faujor.service.mdm.QuoteTempService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/quote")
public class QuoteStruController {
	@Autowired
	private QuoteStruService quoteStruService;
	@Autowired
	private QuoteTempService quoteTempService;
	@Autowired
	private MaterialService mateService;

	/**
	 * 报价结构列表
	 * 
	 * @return
	 */
	@RequestMapping("/struList")
	public String index() {

		return "mdm/quote/structure";
	}

	/**
	 * 进入单选功能
	 * 
	 * @return
	 */
	@RequestMapping("/chooseReceiveMsg")
	public String chooseReceiveMsg() {

		return "mdm/quote/chooseReceiveMsgSingle";
	}
	
	
	/**
	 * 报价结构列表分页
	 * 
	 * @param model
	 * @param qsd
	 * @return
	 */
	@RequestMapping("/struData")
	@ResponseBody
	public Map<String, Object> stucList(Model model, QuoteStruDO qsd, String page, String limit) {
		int p = page != null ? Integer.parseInt(page) : 1;
		int l = Integer.parseInt(limit);
		int offset = (p - 1) * l;
		RowBounds rb = new RowBounds(offset, l);
		return quoteStruService.quoteStruList(rb, qsd);
	}

	/**
	 * 报价结构编辑
	 * 
	 * @param model
	 * @param status
	 * @param id
	 * @return
	 */
	@RequestMapping("/struEdit")
	public String struEdit(Model model, String status, String id) {
		QuoteStruDO qsd = new QuoteStruDO();
		SysUserDO user = UserCommon.getUser();
		// 查询没有报价结构的物料（根据采购员过滤）
		List<MateDO> mateList = mateService.findMaterialIsNotQuote(user.getUserId());
		if (!StringUtils.isEmpty(id)) {
			qsd = quoteStruService.findStruById(id);
			// 加上已经选择的这个物料数据
			MateDO md = new MateDO();
			md.setMateId(qsd.getMateId());
			md.setMateCode(qsd.getMateCode());
			md.setMateName(qsd.getMateName());
			md.setMateType(qsd.getMateType());
			md.setBasicUnit(qsd.getBasicUnit());
			md.setChinName(qsd.getChName());
			md.setSkuEnglish(qsd.getEnName());
			mateList.add(md);
		}
		List<QuoteTemp> tempList = quoteTempService.findTempListForSelected();
		String tempJson = JsonUtils.beanToJson(tempList);
		model.addAttribute("tempJson", tempJson);
		String mateJson = JsonUtils.beanToJson(mateList);
		model.addAttribute("mateJson", mateJson);
		model.addAttribute("mateList", mateList);
		model.addAttribute("tempList", tempList);
		model.addAttribute("qsd", qsd);
		model.addAttribute("status", status);
		return "mdm/quote/struEdit";
	}

	/**
	 * 通过报价结构id获取段信息
	 * 
	 * @param struId
	 * @return
	 */
	@RequestMapping("/getDataByStruId")
	@ResponseBody
	public List<QuoteStruDetails> getDataByStruId(String struId) {
		List<QuoteStruDetails> list = quoteStruService.findStruDetailsByStruId(struId);
		return list;
	}

	/**
	 * 通过模板id获取段信息
	 * 
	 * @param tempId
	 * @return
	 */
	@RequestMapping("/getDataByTempId")
	@ResponseBody
	public List<QuoteStruDetails> getDataByTempId(String tempId) {
		List<QuoteTempDetails> list = quoteTempService.tempDetailsListByTempId(tempId);
		List<QuoteStruDetails> qsdList = quoteStruService.copyTempDetailsToStruDetails(list, "");
		return qsdList;
	}

	/**
	 * 保存报价结构信息
	 * 
	 * @param quoteStru
	 * @param struDetailsList
	 * @param status
	 * @return
	 */
	@RequestMapping("/saveStruData")
	@ResponseBody
	public RestCode saveStruData(QuoteStruDO quoteStru, String struDetailsList, String status) {
		List<QuoteStruDetails> list = JsonUtils.jsonToList(struDetailsList, QuoteStruDetails.class);
		int i;
		if ("0".equals(status))
			i = quoteStruService.saveStruData(quoteStru, list);
		i = quoteStruService.updateStruData(quoteStru, list);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 批量删除报价结构
	 * 
	 * @param rows
	 * @return
	 */
	@RequestMapping("/batchRemoveStruData")
	@ResponseBody
	public RestCode batchRemoveStruData(String rows) {
		List<QuoteStruDO> list = JsonUtils.jsonToList(rows, QuoteStruDO.class);
		int k = quoteStruService.batchRemoveStruData(list);
		if (k > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 移除报价信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/removeStru")
	@ResponseBody
	public RestCode removeStru(String id) {
		List<QuoteStruDO> list = new ArrayList<QuoteStruDO>();
		QuoteStruDO qsd = new QuoteStruDO();
		qsd.setId(id);
		list.add(qsd);
		int k = quoteStruService.batchRemoveStruData(list);
		if (k > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	// 批量调整报价结构开始
	/**
	 * 批量调整报价结构
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/batchModify")
	public String batchEdit(Model model) {
		List<QuoteTemp> tempList = quoteTempService.tempList();
		String tempJson = JsonUtils.beanToJson(tempList);
		model.addAttribute("tempJson", tempJson);
		model.addAttribute("tempList", tempList);
		QuoteStruDO qsd = new QuoteStruDO();
		model.addAttribute("qsd", qsd);
		return "mdm/quote/struBatchEdit";
	}

	/***** 物料的多选开始 ****/
	/**
	 * 选择物料弹出框
	 * 
	 * @param model
	 * @param selected
	 * @param type
	 * @return
	 */
	@RequestMapping("/choose")
	public String choose(Model model, String selected, String type) {
		model.addAttribute("selectedRows", selected);
		model.addAttribute("type", type);
		return "mdm/quote/chooseMate";
	}

	/**
	 * 选择物料弹出框，获取物料数据
	 * 
	 * @param type
	 * @param selected
	 * @return
	 */
	@RequestMapping("/toChooseData")
	@ResponseBody
	public Map<String, Object> chooseData(String type) {
		Map<String, Object> map = quoteStruService.mateStruList(type);
		return map;
	}

	/***** 物料的多选结束 ****/

	@RequestMapping("/find_mate_name_by_code")
	@ResponseBody
	public String findMateNameByMateCode(String mateCode) {
		Material mate = mateService.queryMaterialByMateCode(mateCode);
		String result = mate != null && !StringUtils.isEmpty(mate.getMateName()) ? mate.getMateName() : "";
		return result;
	}

	/**
	 * 批量保存报价结构
	 * 
	 * @param qsd
	 * @param struList
	 * @return
	 */
	@RequestMapping("/saveBatchModify")
	@ResponseBody
	public RestCode saveBatchModify(QuoteStruDO qsd, String struList) {
		int i = quoteStruService.saveBatchModify(qsd, struList);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 批量创建报价结构
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/batchCreate")
	public String batchCreate(Model model) {
		List<QuoteTemp> tempList = quoteTempService.tempList();
		String tempJson = JsonUtils.beanToJson(tempList);
		model.addAttribute("tempJson", tempJson);
		model.addAttribute("tempList", tempList);
		QuoteStruDO qsd = new QuoteStruDO();
		model.addAttribute("qsd", qsd);
		return "mdm/quote/struBatchCreate";
	}

	/**
	 * 保存批量创建的报价结构
	 * 
	 * @param qsd
	 * @param struList
	 * @return
	 */
	@RequestMapping("/saveBatchCreate")
	@ResponseBody
	public RestCode saveBatchCreate(QuoteStruDO qsd, String struList) {
		int i = quoteStruService.saveBatchCreate(qsd, struList);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}
}
