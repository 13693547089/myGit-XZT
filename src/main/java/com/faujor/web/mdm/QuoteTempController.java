package com.faujor.web.mdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.bam.ReceiveMessage;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.QuoteTemp;
import com.faujor.entity.mdm.QuoteTempDetails;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QuoteTempService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UUIDUtil;

@Controller
@RequestMapping("/quote")
public class QuoteTempController {
	@Autowired
	private QuoteTempService tempService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private ReceiveMessageService receiveMessageService;

	@RequestMapping("/tempList")
	public String templateIndex() {
		return "mdm/quote/template";
	}

	/**
	 * 模板列表信息
	 * 
	 * @return
	 */
	@GetMapping("/tempData")
	@ResponseBody
	public Map<String, Object> tempData(QuoteTemp temp, String page, String limit) {
		int p = page != null ? Integer.parseInt(page) : 1;
		int l = Integer.parseInt(limit);
		int offset = (p - 1) * l;
		RowBounds rb = new RowBounds(offset, l);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tempCode", temp.getTempCode());
		params.put("tempName", temp.getTempName());
		params.put("status", temp.getStatus());
		return tempService.tempList(rb, params);
	}

	/**
	 * 编辑模板
	 * 
	 * @param model
	 * @param tempId
	 * @return
	 */
	@GetMapping("/tempEdit")
	public String tempEdit(Model model, String tempId) {
		QuoteTemp temp = new QuoteTemp();
		if (tempId != null) {
			temp = tempService.findTempById(tempId);
		}
		model.addAttribute("temp", temp);
		return "mdm/quote/tempEdit";
	}

	/**
	 * 查看模板
	 * 
	 * @param model
	 * @param tempId
	 * @return
	 */
	@GetMapping("/tempView")
	public String tempView(Model model, String tempId) {
		QuoteTemp temp = new QuoteTemp();
		if (tempId != null) {
			temp = tempService.findTempById(tempId);
		}
		model.addAttribute("temp", temp);
		return "mdm/quote/tempView";
	}

	/**
	 * 复制报价模板
	 * 
	 * @param model
	 * @param tempId
	 * @return
	 */
	@RequestMapping("/tempCopy")
	public String tempCopy(Model model, String tempId) {
		QuoteTemp temp = tempService.findTempById(tempId);
		String target = UUIDUtil.getUUID();
		temp.setId(target);
		temp.setTempCode("");
		temp.setdVersion(null);
		List<QuoteTempDetails> copyTemp = tempService.copyTemp(tempId, target);
		model.addAttribute("temp", temp);
		model.addAttribute("tempId", tempId);
		String json = JsonUtils.beanToJson(copyTemp);
		model.addAttribute("detailsList", json);
		return "mdm/quote/tempCopy";
	}

	/**
	 * 获取段信息
	 * 
	 * @param tempId
	 * @return
	 */
	@GetMapping("/tempDetails")
	@ResponseBody
	public List<QuoteTempDetails> tempDetailsList(String tempId) {
		List<QuoteTempDetails> list = tempService.tempDetailsListByTempId(tempId);
		return list;
	}

	@GetMapping("/test")
	@ResponseBody
	public List<QuoteTempDetails> test(String tempId) {
		List<QuoteTempDetails> list = tempService.tempDetailsListByTempId(tempId);
		return list;
	}

	/**
	 * 添加段信息
	 * 
	 * @param model
	 * @param pid
	 * @param tempId
	 * @return
	 */
	@GetMapping("/addSeg/{pid}")
	public String addSeg(Model model, @PathVariable("pid") String pid, String segmCode, String segmName,
			String asseCode) {
		model.addAttribute("parentId", pid);
		QuoteTempDetails td = new QuoteTempDetails();
		if (!pid.equals("0")) {
			td.setSegmCode(segmCode);
			td.setSegmName(segmName);
			td.setAsseCode(asseCode);
		}
		// 段字典信息
		List<Dic> segmList = basicService.findDicListByCategoryCode("QUOTE_SEGM");
		model.addAttribute("segmList", segmList);
		String segmJson = JsonUtils.beanToJson(segmList);
		model.addAttribute("segmJson", segmJson);
		if(StringUtil.isNotNullOrEmpty(segmCode) && "60".equals(segmCode)){
			List<ReceiveMessage> list = receiveMessageService.findReceiveAddr();
			List<MateDO> mateList=new ArrayList<>();
			for (ReceiveMessage recMsg : list) {
				MateDO mate=new MateDO();
				mate.setMateCode(recMsg.getFreightRange());
				mate.setSkuEnglish(recMsg.getReceUnit());
				mate.setMateName(recMsg.getReceUnit());
				mateList.add(mate);
				String mateJson = JsonUtils.beanToJson(mateList);		
				model.addAttribute("mateList", mateList);
				model.addAttribute("mateJson", mateJson);
			}
		}else{
			// 物料信息
			MateDO mate = new MateDO();
			// 查询所有类型为001的物料
			mate.setMateType("0001");
			List<MateDO> mateList = materialService.findMateDOList(mate);
			String mateJson = JsonUtils.beanToJson(mateList);		
			model.addAttribute("mateList", mateList);
			model.addAttribute("mateJson", mateJson);
		}
		model.addAttribute("td", td);
		return "mdm/quote/tempAdd";
	}

	/**
	 * 编辑段信息
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("/editSeg")
	public String editSeg(Model model,String segmCode, QuoteTempDetails td) {
		// 段字典信息
		List<Dic> segmList = basicService.findDicListByCategoryCode("QUOTE_SEGM");
		model.addAttribute("segmList", segmList);
		String segmJson = JsonUtils.beanToJson(segmList);
		model.addAttribute("segmJson", segmJson);
		if(StringUtil.isNotNullOrEmpty(segmCode) && "60".equals(segmCode)){
			List<ReceiveMessage> list = receiveMessageService.findReceiveAddr();
			List<MateDO> mateList=new ArrayList<>();
			for (ReceiveMessage recMsg : list) {
				MateDO mate=new MateDO();
				mate.setMateCode(recMsg.getFreightRange());
				mate.setSkuEnglish(recMsg.getReceUnit());
				mate.setMateName(recMsg.getReceUnit());
				mateList.add(mate);
				String mateJson = JsonUtils.beanToJson(mateList);		
				model.addAttribute("mateList", mateList);
				model.addAttribute("mateJson", mateJson);
			}
		}else{
			// 物料信息
			MateDO mate = new MateDO();
			// 查询所有类型为001的物料
			mate.setMateType("0001");
			List<MateDO> mateList = materialService.findMateDOList(mate);
			String mateJson = JsonUtils.beanToJson(mateList);		
			model.addAttribute("mateList", mateList);
			model.addAttribute("mateJson", mateJson);
		}
		model.addAttribute("td", td);
		return "mdm/quote/tempAdd";
	}
	@GetMapping("/segmCode/mateList")
	@ResponseBody
	public List<MateDO> getMateBySegmCode(String segmCode){
		if(StringUtil.isNotNullOrEmpty(segmCode) && "60".equals(segmCode)){
			List<ReceiveMessage> list = receiveMessageService.findReceiveAddr();
			List<MateDO> mateList=new ArrayList<>();
			for (ReceiveMessage recMsg : list) {
				MateDO mate=new MateDO();
				mate.setMateCode(recMsg.getFreightRange());
				mate.setSkuEnglish(recMsg.getReceUnit());
				mate.setMateName(recMsg.getReceUnit());
				mateList.add(mate);
			}
			return mateList;
		}else{
			// 物料信息
			MateDO mate = new MateDO();
			// 查询所有类型为001的物料
			mate.setMateType("0001");
			List<MateDO> mateList = materialService.findMateDOList(mate);
			return mateList;
		}
	}
	
	/**
	 * 删除段信息
	 * 
	 * @param id
	 * @param parentId
	 * @return
	 */
	@PostMapping("/removeSeg")
	@ResponseBody
	public RestCode removeSeg(String id, String parentId) {
		int i = tempService.removeSeg(id, parentId);
		if (i > 0) {
			return RestCode.ok();
		} else {
			return RestCode.error();
		}
	}

	/**
	 * 保存模板
	 * 
	 * @param temp
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	public RestCode save(QuoteTemp temp, String detailsList) {
		int i = tempService.save(temp, detailsList);
		if (i > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	@PostMapping("/saveTemp")
	@ResponseBody
	public RestCode saveTemp(QuoteTemp temp) {
		int i = tempService.save(temp, null);
		if (i > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	/**
	 * 保存段信息
	 * 
	 * @param details
	 * @return
	 */
	@PostMapping("/saveSeg")
	@ResponseBody
	public RestCode saveSeg(QuoteTempDetails details) {
		int i = tempService.saveSeg(details);
		if (i > 0) {
			return RestCode.ok();
		}
		return RestCode.error();
	}

	/**
	 * 逐条删除模板
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/removeTemp")
	@ResponseBody
	public RestCode removeTemp(String id) {
		int i = tempService.removeTemp(id);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	/**
	 * 批量删除模板信息
	 * 
	 */
	@PostMapping("/batchRemoveTemp")
	@ResponseBody
	public RestCode batchRemoveTemp(String ids) {
		List<String> list = JsonUtils.jsonToList(ids, String.class);
		int i = tempService.batchRemoveTemp(list);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@GetMapping("checkCode")
	@ResponseBody
	public RestCode checkCode(String code, String id) {
		int i = tempService.checkCode(code, id);
		if (i == 0)
			return RestCode.ok();
		return RestCode.error();
	}
}
