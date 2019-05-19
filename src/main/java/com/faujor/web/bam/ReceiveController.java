package com.faujor.web.bam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.bam.Receive;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.ReceiveService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.common.GetQRimg;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UserCommon;

@Controller
public class ReceiveController {

	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private GetQRimg getQRimg;
	@Autowired
	private QualSuppService qualSuppService;

	/**
	 * 跳转到收货单列表页面 RECEDJZT
	 * 
	 * @return
	 */
	@RequestMapping("/getReceiveListHtml")
	public String getReceiveListHtml(Model model) {
		List<Dic> statusList = basicService.findDicListByCategoryCode("RECEDJZT");
		model.addAttribute("statusList", statusList);
		return "bam/receive/receiveList";
	}

	/**
	 * 跳转到收货单查看界面
	 * 
	 * @return
	 */
	@RequestMapping("/getAllReceiveListHtml")
	public String getAllReceiveListHtml(Model model) {
		List<Dic> statusList =basicService.findDicListByCategoryCode("RECEDJZT");
		model.addAttribute("statusList", statusList);
		return "bam/receive/allreceiveList";
	}

	/**
	 * 收货列表的数据
	 * 
	 * @param limit
	 * @param page
	 * @param rece
	 * @return
	 */
	@Log(value = "获取收货单列表")
	@ResponseBody
	@RequestMapping("/queryReceiveByPage")
	public Map<String, Object> queryReceiveByPage(Integer limit, Integer page, Receive rece) {
		if (limit == null) {
			limit = 10;
		}
		if (page == null) {
			page = 1;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		SysUserDO user = UserCommon.getUser();
		String format = DateUtils.format(rece.getCreateDate(), "yyyy/MM/dd");
		rece.setQrurl(format);
		Map<String, Object> map = new HashMap<String, Object>();
		// 数据权限（根据postid），根据创建人应该没问题
		map.put("createId", user.getUserId().toString());
		map.put("start", start);
		map.put("end", end);
		map.put("rece", rece);
		Map<String, Object> page2 = receiveService.queryReceiveByPage(map);
		return page2;
	}

	/**
	 * 收货列表查看界面数据
	 * 
	 * @param limit
	 * @param page
	 * @param rece
	 * @return
	 */
	@Log(value = "获取收货单查看列表")
	@ResponseBody
	@RequestMapping("/queryAllReceiveByPage")
	public Map<String, Object> queryAllReceiveByPage(Integer limit, Integer page, Receive rece) {
		if (limit == null) {
			limit = 10;
		}
		if (page == null) {
			page = 1;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		Map<String, Object> map = new HashMap<String, Object>();
		String format = DateUtils.format(rece.getCreateDate(), "yyyy/MM/dd");
		rece.setQrurl(format);
		SysUserDO user = UserCommon.getUser();
		if ("supplier".equals(user.getUserType())) {
			QualSupp supp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("suppId", supp.getSuppId());
		}
		map.put("start", start);
		map.put("end", end);
		map.put("rece", rece);
		Map<String, Object> page2 = receiveService.queryAllReceiveByPage(map);
		return page2;
	}

	/**
	 * 收货单列表的删除收货单
	 * 
	 * @param receIds
	 * @return
	 */
	@Log(value = "删除收货单")
	@ResponseBody
	@RequestMapping("/deleteReceiveByReceId")
	public boolean deleteReceiveByReceId(String[] receIds, String deliCodes2) {
		return receiveService.deleteReceiveByReceId2(receIds,deliCodes2);
	}

	/**
	 * 跳转到新建收货单页面
	 * 
	 * @return
	 */
	@RequestMapping("/getReceiveAddHtml")
	public String getReceiveAddHtml(Model model) {
		Receive rece = new Receive();
		List<Dic> dicList = basicService.findDicListByCategoryCode("PLANT");
		model.addAttribute("dicList", dicList);
		rece.setReceDate(new Date());
		model.addAttribute("rece", rece);
		return "bam/receive/receiveAdd";
	}

	/**
	 * 选择工厂的库位信息
	 * 
	 * @param model
	 * @param factoryAddr
	 * @return
	 */
	@RequestMapping("/storageLocationInfo")
	public String storageLocationInfo(Model model, String factoryAddr) {
		Dic dic = basicService.findDicByDicCodeANDCategoryCode(factoryAddr, "PLANT");
		if (dic != null) {
			List<Dic> dicList = basicService.findDicListByParentId(dic.getId());
			String dicJson = JsonUtils.beanToJson(dicList);
			model.addAttribute("dicJson", dicJson);
		} else {
			model.addAttribute("dicJson", "");
		}
		return "bam/receive/storageLocation";
	}
	/**
	 * 选择工厂的库位下拉框信息
	 * 
	 * @param model
	 * @param storLocationJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryStorLocationList")
	public List<Dic> queryStorLocationList(Model model, String factoryAddrJson) {
		List<String> list = JsonUtils.jsonToList(factoryAddrJson, String.class);
		Set<String> set = new HashSet<>();
		for (String str : list) {
			set.add(str);
		}
		List<String> dicId = new ArrayList<>();
		for (String s : set) {
			Dic dic = basicService.findDicByDicCodeANDCategoryCode(s, "PLANT");
			if(dic != null) {
				dicId.add(dic.getId());
			}
		}
		List<Dic> totalList = new ArrayList<>();
		if (dicId.size() >0 ) {
			for (String id : dicId) {
				List<Dic> dicList = basicService.findDicListByParentId(id);
				if(dicList.size()>0) {
					totalList.addAll(dicList);
				}
			}
			return totalList;
		} 
		return totalList;
	}

	/**
	 * 根据送货单号查询送货信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryDeliveryByDeliCode")
	public Map<String, Object> queryDeliveryByDeliCode(String deliCode) {
		Map<String, Object> map = receiveService.queryDeliveryByDeliCode(deliCode);
		return map;
	}

	/**
	 * 
	 * @param rece
	 * @param receMateData
	 * @param type
	 * @return
	 */
	/*@Log(value = "新建收货单")
	@ResponseBody
	@RequestMapping("/addReceive2")
	public RestCode addReceive2(Receive rece, String receMateData, String type) {
		SysUserDO user = UserCommon.getUser();
		List<ReceMate> list = JsonUtils.jsonToList(receMateData, ReceMate.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deliCode", rece.getDeliCode());
		String receCode = codeService.getCodeByCodeType("receiveNo");
		rece.setReceCode(receCode);
		rece.setCreateId(user.getUserId().toString());
		rece.setCreator(user.getName());
		if ("add".equals(type) || "regress".equals(type)) {
			// 验退 或者待收货
			if ("regress".equals(type)) {
				rece.setStatus("已验退");
				map.put("status", "已验退");
			} else {
				rece.setStatus("已保存");
				map.put("status", "待收货");
			}
			boolean b = receiveService.addReceive(rece, list);
			boolean c = deliveryService.updateDeliStatusByDeliCode(map);
			if (b && c)
				return RestCode.ok();
			return RestCode.error();
		} else {
			rece.setStatus("已收货");
			map.put("status", "已收货");
			boolean b = receiveService.addReceive(rece, list);
			boolean c = deliveryService.updateDeliStatusByDeliCode(map);
			// boolean e = orderService.updateLineProject(list);
			// 只有预约申请才能（预约送货，特殊送货）调接口
			if (!RestCode.error().equals(rece.getReceType())) {
				String str = receiveService.asyncToSAP(rece, receMateData);
				if (b && c) {
					if (StringUtils.isEmpty(str))
						return RestCode.ok();
					return RestCode.ok(2, str);
				}
				return RestCode.ok();
			}
			if (b && c)
				return RestCode.ok();
			return RestCode.error();
		}
	}*/
	/**
	 * 新建页面的收货和保存和验退
	 * @param rece
	 * @param receMateData
	 * @param type
	 * @return
	 */
	@Log(value = "新建收货单")
	@ResponseBody
	@RequestMapping("/addReceive")
	public Map<String, Object> addReceive(Receive rece, String receMateData, String type) {
		return receiveService.createReceive(rece,receMateData,type);
	}

	/**
	 * funType=1 : 修改 funType=2 : 查看 跳转到收货单的编辑/查看页面
	 * 
	 * @param model
	 * @param receId
	 * @param funType
	 * @return
	 */
	@RequestMapping("/getReceiveEditHtml")
	public String getReceiveEditHtml(Model model, String receId, String funType) {
		Receive rece = receiveService.queryReceiveByReceId(receId);
		model.addAttribute("funType", funType);
		model.addAttribute("rece", rece);
		model.addAttribute("check", "no");
		return "bam/receive/receiveEdit";
	}

	/**
	 * 收货单查看界面，点击查看进入详情
	 * 
	 * @param model
	 * @param receId
	 * @param funType
	 * @return
	 */
	@RequestMapping("/getReceiveEditHtml2")
	public String getReceiveEditHtml2(Model model, String receId, String funType) {
		Receive rece = receiveService.queryReceiveByReceId(receId);
		model.addAttribute("funType", funType);
		model.addAttribute("rece", rece);
		model.addAttribute("check", "yes");
		return "bam/receive/receiveEdit";
	}

	/**
	 * 根据收货单的主键查询收货单的下的物资信息
	 * 
	 * @param receId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryReceMatesByReceId")
	public List<ReceMate> queryReceMatesByReceId(String receId) {
		return receiveService.queryReceMatesByReceId(receId);
	}

	/**
	 * type=add:保存 type=rece:收货
	 * 
	 * 修改页面的保存和收货
	 * 
	 * @param rece
	 * @param receMateData
	 * @param type
	 * @param deliCode2
	 * @return
	 */
	/*@Log(value = "编辑收货单")
	@ResponseBody
	@RequestMapping("/updateReceive2")
	public RestCode updateReceive2(Receive rece, String receMateData, String type, String deliCode2) {
		List<ReceMate> list = JsonUtils.jsonToList(receMateData, ReceMate.class);
		if ("add".equals(type) || "regress".equals(type)) {
			if (!deliCode2.equals(rece.getDeliCode())) {// 送货单号被修改
				// 原来的送货单状态变为 “已签到”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("status", "已签到");
				map1.put("deliCode", deliCode2);
				boolean c = deliveryService.updateDeliStatusByDeliCode(map1);
				// 新关联的送货单的状态变为 “待收货”，或者是已验退
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("status", "待收货");
				if ("regress".equals(type)) {
					rece.setStatus("已验退");
					map2.put("status", "已验退");
				}
				map2.put("deliCode", rece.getDeliCode());
				boolean d = deliveryService.updateDeliStatusByDeliCode(map2);
				boolean b = receiveService.updateReceiveByReceId(rece, list);
				if (b && d && c)
					return RestCode.ok();
				return RestCode.error();
			} else {
				boolean b = receiveService.updateReceiveByReceId(rece, list);
				if (b)
					return RestCode.ok();
				return RestCode.error();
			}
		} else {
			boolean c = true;
			if (!deliCode2.equals(rece.getDeliCode())) {// 送货单号被修改
				// 原来的送货单状态变为 “已签到”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("status", "已签到");
				map1.put("deliCode", deliCode2);
				c = deliveryService.updateDeliStatusByDeliCode(map1);
			}
			rece.setStatus("已收货");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("status", "已收货");
			map2.put("deliCode", rece.getDeliCode());
			boolean d = deliveryService.updateDeliStatusByDeliCode(map2);
			boolean b = receiveService.updateReceiveByReceId(rece, list);
			// boolean e = orderService.updateLineProject(list);
			// 只有预约申请才能（预约送货，特殊送货）调接口
			if (!"0".equals(rece.getReceType())) {
				String str = receiveService.asyncToSAP(rece, receMateData);
				if (b && d && c) {
					if (StringUtils.isEmpty(str))
						return RestCode.ok();
					return RestCode.ok(2, str);
				}
				return RestCode.error();
			}
			if (b && d && c)
				return RestCode.ok();
			return RestCode.error();
		}
	}*/
	/**
	 * type=add:保存 type=rece:收货
	 * 
	 * 修改页面的保存和收货
	 * 
	 * @param rece
	 * @param receMateData
	 * @param type
	 * @param deliCode2
	 * @return
	 */
	@Log(value = "编辑收货单")
	@ResponseBody
	@RequestMapping("/updateReceive")
	public RestCode updateReceive(Receive rece, String receMateData, String type, String deliCode2) {
		return receiveService.updateReceive(rece,receMateData,type,deliCode2);
	}

	/**
	 * 跳转收货单打印页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getRecePrintHtml")
	public String getRecePrintHtml(String receId, Model model) throws Exception {
		Receive rece = receiveService.queryReceiveByReceId(receId);
		String qrurl = getQRimg.getQRUrl(rece.getReceCode());
		/*
		 * String str = MatrixToImageWriter.getQRCode(rece.getReceCode());
		 * model.addAttribute("realPath", str);
		 */
		model.addAttribute("rece", rece);
		model.addAttribute("qrurl", qrurl);
		return "bam/receive/receivePrint";
	}

	/**
	 * 同步收货单到sap,完成内向交货单
	 * 
	 * @param rece
	 * @param receMateData
	 * @return
	 */
	@Log(value = "同步收货单到sap,完成内向交货单")
	@ResponseBody
	@RequestMapping("/synchReceiveSap")
	public RestCode synchReceiveSap(Receive rece, String receMateData) {
		String str = receiveService.asyncToSAP(rece, receMateData);
		if (StringUtils.isEmpty(str))
			return RestCode.ok();
		return RestCode.ok(2, str);
	}

	/**
	 * 收货单冲销
	 * 
	 * @param receCode
	 * @param inboDeliCodes
	 * @return
	 */
	@Log(value = "冲销收货单")
	@ResponseBody
	@RequestMapping("/writeoffReceive")
	public Map<String, Object> writeoffReceive(String receCode, String inboDeliCodes, String deliCode) {
		Map<String, Object> map = receiveService.writeoffReceive(receCode, inboDeliCodes, deliCode);
		return map;
	}
}
