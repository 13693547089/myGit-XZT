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

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.MessMate;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderReleDO;
import com.faujor.entity.bam.StraMessage;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualProc;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.OrderService;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.bam.StraMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.common.GetQRimg;
import com.faujor.service.mdm.QualPapersService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class StraMessageController {

	@Autowired
	private StraMessageService straMessageService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private ReceiveMessageService receiveMessageService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private GetQRimg getQRimg;
	@Autowired
	private QualPapersService qualPapersService;

	/**
	 * 跳转到直发通知展示列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/getStraMessageListHtml")
	public String getStraMessageListHtml(Model model) {
		SysUserDO user = UserCommon.getUser();
		model.addAttribute("userId", user.getUserId());
		List<Dic> messStatusList = basicService.findDicListByCategoryCode("TZZT");
		List<String> receUnitList= receiveMessageService.queryAllReceUnitOfReceiveMess();
		model.addAttribute("messStatusList", messStatusList);
		model.addAttribute("receUnitList", receUnitList);
		return "bam/straMess/straMessageList";

	}

	/**
	 * 直发通知列表展示数据
	 * 
	 * @param straMess
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value = "获取直发通知列表")
	@ResponseBody
	@RequestMapping("/queryStraMessageByPage")
	public Map<String, Object> queryStraMessageByPage(StraMessage straMess, Integer limit, Integer page) {
		if (limit == null) {
			limit = 10;
		}
		if (page == null) {
			page = 1;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("straMess", straMess);
		if ("supplier".equals(user.getUserType())) {
			map.put("zzoem", user.getSuppNo());
		} else {
			map.put("userId", user.getUserId());
		}
		Map<String, Object> page2 = straMessageService.queryStraMessageByPage(map);
		return page2;
	}

	/**
	 * 删除直发通知
	 * 
	 * @param messIds
	 * @return
	 */
	@Log(value = "删除直发通知")
	@ResponseBody
	@RequestMapping("/deleteStraMessByMessId")
	public boolean deleteStraMessByMessId(String[] messIds) {
		boolean b = straMessageService.deleteStraMessByMessId(messIds);
		return b;
	}

	/**
	 * 跳转到直发通知新建/直发通知编辑页面
	 * 
	 * @return
	 */
	@Log(value = "创建/编辑直发通知")
	@RequestMapping("/getStraMessageAddHtml")
	public String getStraMessageAddHtml(String type, String messId, Model model) {
		model.addAttribute("type", type);

		// 所有供应商
		List<QualSupp> suppList = receiveMessageService.findSuppInfo("all");
		// DS供应商
		List<QualSupp> dsList = receiveMessageService.findSuppInfo("ds");
		List<String> receUnitList = receiveMessageService.queryAllReceUnitOfReceiveMess();
		model.addAttribute("receUnitList", receUnitList);
		model.addAttribute("suppList", suppList);
		model.addAttribute("dsList", dsList);
		if ("1".equals(type)) {
			StraMessage straMess = new StraMessage();
			Date date = new Date();
			straMess.setCreateDate(date);
			model.addAttribute("straMess", straMess);
			model.addAttribute("sapId", "");
		} else {
			StraMessage straMess = straMessageService.queryStraMessageByMessId(messId);
			QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(straMess.getSuppId());
			model.addAttribute("straMess", straMess);
			model.addAttribute("sapId", supp.getSapId());
		}
		return "bam/straMess/straMessageAdd";
	}

	/**
	 * 根据调拨单号获取到调拨单对应的半成品采购订单
	 * 
	 * 并处理，获取可以用的采购订单
	 * 
	 * @param mm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getStraMates")
	public Map<String, Object> getStraMates(MessMate mm) {
		return straMessageService.getStraMates(mm);
	}

	/**
	 * 查询直发通知物质
	 * 
	 * @param messId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryMessMateForStraMess")
	public Map<String, Object> queryMessMateForStraMess(String messId,String sapId) {
		Map<String, Object> result = new HashMap<String, Object>();
		StraMessage straMess = straMessageService.queryStraMessageByMessId(messId);
		List<MessMate> list = straMess.getMessMates();
		List<QualProc> proList = qualPapersService.queryQualProcBySapId2(sapId);
		result.put("proList", proList);
		result.put("list", list);
		return result;
	}

	/**
	 * type=1:新建保存/type=2:编辑保存
	 * 
	 * @param straMess
	 * @param messMateData
	 * @param type
	 * @return
	 */
	@Log(value = "新建/编辑直发通知")
	@ResponseBody
	@RequestMapping("/addStraMess")
	public boolean addStraMess(StraMessage straMess, String messMateData, String type) {
		SysUserDO user = UserCommon.getUser();// 供应商用户 ---------注意后期修改
		List<MessMate> list = JsonUtils.jsonToList(messMateData, MessMate.class);
		boolean b = false;
		if ("1".equals(type)) {
			String messCode = codeService.getCodeByCodeType("straMessNo");
			straMess.setMessCode(messCode);
			straMess.setMessStatus("已保存");
			straMess.setCreateId(user.getUserId().toString());
			straMess.setCreator(user.getName());
			b = straMessageService.addStraMessage(straMess, list);
		} else if ("2".equals(type)) {
			straMess.setModifieId(user.getUserId().toString());
			straMess.setModifier(user.getName());
			b = straMessageService.updateStraMessageByMessId(straMess, list);
		}
		return b;
	}

	/**
	 * type=1:新建提交/type=2：编辑提交
	 * 
	 * @param straMess
	 * @param messMateData
	 * @param type
	 * @return
	 */
	@Log(value = "新建/编辑直发通知")
	@ResponseBody
	@RequestMapping("/submitStraMess")
	public boolean submitStraMess(StraMessage straMess, String messMateData, String type) {
		SysUserDO user = UserCommon.getUser();// 供应商用户 ---------注意后期修改
		List<MessMate> list = JsonUtils.jsonToList(messMateData, MessMate.class);
		boolean b = false;
		if ("1".equals(type)) {
			String messCode = codeService.getCodeByCodeType("straMessNo");
			/*
			 * String str=""; try { str =
			 * MatrixToImageWriter.getQRCode(messCode); } catch (Exception e) {
			 * e.printStackTrace(); } straMess.setQrurl(str);
			 */
			straMess.setMessCode(messCode);
			straMess.setMessStatus("已通知");
			straMess.setCreateId(user.getUserId().toString());
			straMess.setCreator(user.getName());
			b = straMessageService.addStraMessage(straMess, list);
		} else if ("2".equals(type)) {
			straMess.setModifieId(user.getUserId().toString());
			straMess.setModifier(user.getName());
			straMess.setMessStatus("已通知");
			b = straMessageService.updateStraMessageByMessId(straMess, list);
		}
		String alloNo = straMess.getAlloNo();
		if (!StringUtils.isEmpty(alloNo))
			straMessageService.updateAlloOrderStatus(alloNo);
		return b;
	}

	/**
	 * 修改直发通知状态
	 * 
	 * @param messIdJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateStraMessStatusByMessId")
	public boolean updateStraMessStatusByMessId(String messIdJson) {
		List<String> list = JsonUtils.jsonToList(messIdJson, String.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messStatus", "已通知");
		map.put("size", list.size());
		map.put("messIds", list);
		boolean b = straMessageService.updateStraMessStatusByMessId(map);
		return b;
	}

	/**
	 * 根据供应商的名称查询供应商的信息
	 * 
	 * @param suppName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryQualSuppBySuppName2")
	public Map<String, Object> queryQualSuppBySuppName2(String suppName) {
		Map<String, Object> map = new HashMap<String, Object>();
		QualSupp supp = qualSuppService.queryQualSuppBySuppName(suppName);
		if (supp != null) {
			map.put("judge", true);
			map.put("supp", supp);
		} else {
			map.put("judge", false);
		}
		return map;
	}

	/**
	 * 弹出框
	 * 
	 * @return
	 */
	@RequestMapping("/getAllAllotOrderListHtml")
	public String getAllAllotOrderListHtml() {
		return "bam/straMess/suppMateList";
	}

	/**
	 * 获取某个供应商的调拨单
	 * 
	 * @param sapId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllAllotOrder")
	public Map<String, Object> queryAllAllotOrder(OrderReleDO orDO, String sapId, Integer limit, Integer page,String zzoem) {
		if (limit == null) {
			limit = 10;
		}
		if (page == null) {
			page = 1;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("sapId", sapId);
		map.put("zzoem", zzoem);
		map.put("orderRele", orDO);
		Map<String, Object> page2 = orderService.queryOrderReleOfQualSuppByPage(map);
		return page2;
	}

	/**
	 * 获取某个供应商的调拨单
	 * 
	 * @param sapId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryOrderMateByContOrdeNumb")
	public List<OrderMate> queryOrderMateByContOrdeNumb(String contOrdeNumb) {
		List<OrderMate> list = orderService.queryOrderMateByContOrdeNumb(contOrdeNumb);
		return list;
	}

	/**
	 * 作废直发通知
	 * 
	 * @param messIdJson
	 * @return
	 */
	@Log(value = "作废直发通知")
	@ResponseBody
	@RequestMapping("/cancellStraMessByMessId")
	public boolean cancellStraMessByMessId(String messIdJson) {
		List<String> list = JsonUtils.jsonToList(messIdJson, String.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messStatus", "已作废");
		map.put("size", list.size());
		map.put("messIds", list);
		return straMessageService.cancellStraMessByMessId(map);
	}

	/**
	 * 跳转到打印页面
	 * 
	 * @param messId
	 * @param model
	 * @return
	 */
	@RequestMapping("/getStraMessPrintHtml")
	public String getStraMessPrintHtml(String messId, Model model) {
		StraMessage straMess = straMessageService.queryStraMessageByMessId(messId);
		QualSupp supp = qualSuppService.queryQualSuppBySapId(straMess.getZzoem());
		if (supp != null) {
			model.addAttribute("OEMsuppName", supp.getSuppName());
		} else {
			model.addAttribute("OEMsuppName", straMess.getSuppName());
		}
		String qrurl = getQRimg.getQRUrl(straMess.getMessCode());
		model.addAttribute("qrurl", qrurl);
		model.addAttribute("straMess", straMess);
		return "bam/straMess/straMessPrint";
	}

	/**
	 * 根据sapId查询合格供应商信息
	 * 
	 * @param sapId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/querySuppBySapId")
	public QualSupp querySuppBySapId(String sapId) {
		QualSupp supp = qualSuppService.queryQualSuppBySapId(sapId);
		return supp;
	}

	/**
	 * 弹窗修改提货日期
	 * 
	 * @param messId
	 * @param model
	 * @return
	 */
	@RequestMapping("/getUpdateDeliDateHtml")
	public String getUpdateDeliDateHtml(String messId, Model model) {
		StraMessage message = straMessageService.queryStraMessageByMessId(messId);
		model.addAttribute("arriDate", message.getArriDate());
		model.addAttribute("messId", messId);
		return "/bam/straMess/updateDeliDate";
	}

	@ResponseBody
	@RequestMapping("/updateDeliDate")
	public boolean updateDeliDate(String messCode, String arriDate, String type) {
		Date date = DateUtils.parse(arriDate, "yyyy-MM-dd");
		StraMessage straMess = new StraMessage();
		straMess.setArriDate(date);
		straMess.setMessCode(messCode);
		return straMessageService.updateDeliDate(straMess, type);
	}

}
