package com.faujor.web.mdm;

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
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.AsyncLog;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualPapers;
import com.faujor.entity.mdm.QualProc;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.AsyncLogService;
import com.faujor.service.mdm.QualPapersService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Controller
public class QualSuppController {

	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private QualPapersService qualPapersService;
	@Autowired
	private BasicService basicService;

	@Autowired
	private AsyncLogService asyncLogService;

	/**
	 * 跳转到合格供应商展示列表
	 * 
	 * @return
	 */
	@RequestMapping("/getQualSuppListHtml")
	public String getQualSuppListHtml() {
		return "mdm/supp/qualSuppList";
	}

	/**
	 * 合格供应商分页展示
	 * 
	 * @param qualSupp
	 * @param page
	 * @param limit
	 * @return
	 */
	@Log(value ="获取合格供应商列表")
	@ResponseBody
	@RequestMapping("/queryQualSuppByPage")
	public Map<String, Object> queryQualSuppByPage(QualSupp qualSupp, Integer page, Integer limit) {
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
		Map<String, Object> suppByPage = qualSuppService.queryQualSuppByPage(map);
		return suppByPage;
	}

	/**
	 * 跳转到合格供应商详情查看页面
	 * 
	 * @param model
	 * @param suppId
	 * @return
	 */
	@Log(value ="查看合格供应商的详情")
	@RequestMapping("/getCheckQualSuppHtml")
	public String getCheckQualSuppHtml(Model model, String suppId, String type) {
		// 合格供应商的信息
		QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(suppId);
		// 证件信息
		List<QualPapers> papersList = qualPapersService.queryQualPapersBySuppId(suppId);
		// 采购数据信息
		List<QualProc> proList = qualPapersService.queryQualProcBySuppId(suppId);
		// 供应商类型
		List<Dic> suppCateList = basicService.findDicListByCategoryCode("SUPPCATE");
		// 付款条件
		List<Dic> payList = basicService.findDicListByCategoryCode("FUTJ");
		// 惯用币种
		List<Dic> currList = basicService.findDicListByCategoryCode("GYBZ");
		// 惯用税种
		List<Dic> taxeList = basicService.findDicListByCategoryCode("GYSZ");
		// 附件类型
		List<Dic> acceList = basicService.findDicListByCategoryCode("ACCETYPE");
		// 证件名称
		List<Dic> taxenameList = basicService.findDicListByCategoryCode("ACCENAME");
		model.addAttribute("suppCateList", suppCateList);
		model.addAttribute("payList", payList);
		model.addAttribute("currList", currList);
		model.addAttribute("taxeList", taxeList);
		model.addAttribute("supp", supp);
		model.addAttribute("papersList", papersList);
		model.addAttribute("proList", proList);
		model.addAttribute("acceList", acceList);
		model.addAttribute("taxenameList", taxenameList);
		model.addAttribute("type", type);
		return "mdm/supp/qualSuppLook";
	}

	/**
	 * 查询所有合格供应商
	 * 
	 * @param qualSupp
	 * @param page
	 * @param limit
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllQualSupp")
	public Map<String, Object> queryAllQualSupp() {
		Map<String, Object> page = qualSuppService.queryAllQualSupp();
		return page;
	}

	@RequestMapping("/asyncSuppInfo")
	@ResponseBody
	public RestCode asyncSuppInfo() {
		// 插入同步记录
		AsyncLog al = new AsyncLog();
		String alId = UUIDUtil.getUUID();
		al.setId(alId);
		al.setAsyncName("合格供应商主数据同步");
		asyncLogService.saveAsyncLog(al);
		int k = qualSuppService.asyncSuppInfo(al);
		if (k > 0) {
			return RestCode.ok("成功同步" + k + "条！");
		} else {
			return RestCode.error("无数据更新！");
		}
	}

	/**
	 * 根据sapId查询合格供应商
	 * 
	 * @param sapId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryQualSuppBySuppId")
	public Map<String, Object> queryQualSuppBySapId(String suppId) {
		Map<String, Object> map = new HashMap<String, Object>();
		QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(suppId);
		if (supp != null) {
			List<QualProc> proList = qualPapersService.queryQualProcBySapId2(supp.getSapId());
			map.put("judge", true);
			map.put("supp", supp);
			map.put("proList", proList);
		} else {
			map.put("judge", false);
		}
		return map;
	}
	/**
	 * 根据sapId查询合格供应商的所有供应商子范围信息
	 * @param sapId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/querySuppRangeOfQualSuppBySapId")
	public Map<String, Object> querySuppRangeOfQualSuppBySapId(String sapId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<QualProc> proList = qualPapersService.queryQualProcBySapId2(sapId);
		map.put("proList", proList);
		return map;
	}

	/**
	 * 保存合格供应商的附件
	 * 
	 * @param suppId
	 * @param paperTableData
	 * @return
	 */
	@Log(value ="编辑合格供应商的附件信息")
	@ResponseBody
	@RequestMapping("/updatePaperOfQualSuppBySuppId")
	public boolean updatePaperOfQualSuppBySuppId(String suppId, String paperData) {
		List<QualPapers> list = JsonUtils.jsonToList(paperData, QualPapers.class);
		return qualSuppService.updatePaperOfQualSuppBySuppId(suppId, list);
	}

	/**
	 * 查询合格供应商的附件
	 * 
	 * @param suppId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryPapersOfQualSuppBysuppId")
	public List<QualPapers> queryPapersOfQualSuppBysuppId(String suppId) {
		return qualSuppService.queryPapersOfQualSuppBysuppId(suppId);
	}

}
