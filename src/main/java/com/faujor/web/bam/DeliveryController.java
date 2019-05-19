package com.faujor.web.bam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.DeliMate;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.OrderDO;
import com.faujor.entity.bam.OrderMate;
import com.faujor.entity.bam.OrderRele;
import com.faujor.entity.bam.StraMessage;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.AppointService;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.OrderMateCheckService;
import com.faujor.service.bam.OrderService;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.bam.StraMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.common.GetQRimg;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class DeliveryController {

	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private AppointService appointService;
	@Autowired
	private StraMessageService straMessageService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private GetQRimg getQRimg;
	@Autowired
	private OrderMateCheckService orderMateCheckService;
	@Autowired
	private ReceiveMessageService receiveMessageService;

	/**
	 * 跳转到送货单列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/getDeliveryListHtml")
	public String getDeliveryListHtml(Model model) {
		List<Dic> statusList = basicService.findDicListByCategoryCode("DJZT");
		List<Dic> deliTypeList = basicService.findDicListByCategoryCode("DELITYE");
		List<String> receUnitList= receiveMessageService.queryAllReceUnitOfReceiveMess();
		model.addAttribute("statusList", statusList);
		model.addAttribute("deliTypeList", deliTypeList);
		model.addAttribute("receUnitList", receUnitList);
		return "bam/delivery/deliveryList";
	}

	/**
	 * 送货单列表数据
	 * 
	 * @param limit
	 * @param page
	 * @param deli
	 * @return
	 */
	@Log(value = "获取送货单列表")
	@ResponseBody
	@RequestMapping("/queryDeliveryByPage")
	public Map<String, Object> queryDeliveryByPage(Integer limit, Integer page, Delivery deli) {
		SysUserDO user = UserCommon.getUser();
		if (limit == null) {
			limit = 10;
		}
		if (page == null) {
			page = 1;
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		String format = DateUtils.format(deli.getCreateDate(), "yyyy/MM/dd");
		deli.setQrurl(format);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("deli", deli);
		// 供应商只能查看自己创建的送货单，其他人可以查看所有
		if ("supplier".equals(user.getUserType())) {
			map.put("createId", user.getUserId().toString());
		}
		Map<String, Object> page2 = deliveryService.queryDeliveryByPage(map);
		return page2;
	}

	/**
	 * deliType=1:预约送货 deliType=0:直发送货 工具栏删除送货单
	 * 
	 * @param deliIds
	 * @return
	 */
	@Log(value = "删除送货单")
	@ResponseBody
	@RequestMapping("/deleteDeliveryBydeliId")
	public boolean deleteDeliveryBydeliId(String[] deliIds, String mapgCode, String deliType) {
		boolean b = deliveryService.deleteDeliveryBydeliId(deliIds);
		Map<String, Object> map = new HashMap<String, Object>();
		if ("0".equals(deliType)) {//修改直发通知单的状态
			map.put("messStatus", "已通知");
			map.put("messCode", mapgCode);
			boolean d = straMessageService.updateMessStatusByMessCode(map);
			return b && d;
		}else{//修改预约申请的状态
			map.put("appoStatus", "已发布");
			map.put("appoCode", mapgCode);
			boolean c = appointService.updateAppoStatusByAppoCode(map);
			return b && c;
		}
	}

	/**
	 * deliType=1:表示预约送货 deliType=0:表示直发送货 跳转到新建预约送货页面
	 * 
	 * @return
	 */
	@RequestMapping("/getDeliveryAddHtml")
	public String getDeliveryAddHtml(Model model, String deliType) {
		SysUserDO user = UserCommon.getUser();
		Delivery deli = new Delivery();
		deli.setDeliType(deliType);
		Map<String, Object> map = new HashMap<String, Object>();
		if ("supplier".equals(user.getUserType())) {
			QualSupp qualSupp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("suppId", qualSupp.getSuppId());
		}
		map.put("status", "已发布");
		List<Appoint> appoList = appointService.queryAllPublishedAppoint(map);
		model.addAttribute("appoList", appoList);
		model.addAttribute("deli", deli);
		return "bam/delivery/deliveryAdd";
	}

	/**
	 * 根据预约单号查询预约详情和预约单下的物资 特殊送货，选择预约单后校验查询预约单的信息
	 * 
	 * @param mapgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAppointByAppoCode")
	public Map<String, Object> queryAppointByAppoCode(String mapgCode,String deliCode) {
		Map<String, Object> map = appointService.queryAppoByAppoCode(mapgCode,deliCode);
		return map;
	}

	/**
	 * type=add :保存 type=sub :提交 新建预约送货页面 保存/提交预约送货
	 * 
	 * @return
	 */
	@Log(value = "保存/提交预约送货/特殊送货")
	@ResponseBody
	@RequestMapping("/addDelivery")
	public boolean addDelivery(Delivery deli, String deliMateData, String type) {
		SysUserDO user = UserCommon.getUser();
		List<DeliMate> list = JsonUtils.jsonToList(deliMateData, DeliMate.class);
		String deliCode = codeService.getCodeByCodeType("deliveryNo");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appoCode", deli.getMapgCode());
		/*
		 * String str=""; try { str = MatrixToImageWriter.getQRCode(deliCode); }
		 * catch (Exception e) { e.printStackTrace(); } deli.setQrurl(str);
		 */
		deli.setDeliCode(deliCode);
		deli.setCreateId(user.getUserId().toString());
		deli.setCreator(user.getName());
		if ("add".equals(type)) {
			deli.setStatus("已保存");
			map.put("appoStatus", "待发货");
		} else if ("sub".equals(type)) {
			deli.setStatus("已发货");
			map.put("appoStatus", "已发货");
		}
		boolean b = deliveryService.addDelivery(deli, list);
		boolean c = appointService.updateAppoStatusByAppoCode(map);
		return b && c;
	}

	/**
	 * 推荐采购订单
	 * 
	 * @param mapgCode
	 * @return
	 */
	@RequestMapping("/recommendPurchaseOrder")
	@ResponseBody
	public Map<String, Object> recommendPurchaseOrder(String mapgCode,String deliCode) {
		// 校验是否可以创建送货单
		Map<String, Object> map2 = appointService.queryAppoByAppoCode(mapgCode,deliCode);
		Map<String, Object> map = null;
		boolean result = (boolean) map2.get("judge");
		if (result) {// 校验通过
			map = orderMateCheckService.recommendPurchaseOrder(mapgCode);
		} else {// 校验不通过
			return map2;
		}
		return map;
	}

	/**
	 * 跳转到预约送货单编辑/查看页面
	 * 
	 * @param deliId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping("/getDeliEditHtml")
	public String getDeliEditHtml(String deliId, String type, Model model) {
		Delivery deli = deliveryService.queryDeliveryByDeliId(deliId);
		Appoint appoint = appointService.queryOneAppointbyAppoCode(deli.getMapgCode());
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if ("supplier".equals(user.getUserType())) {
			QualSupp qualSupp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("suppId", qualSupp.getSuppId());
		}
		map.put("status", "已发布");
		List<Appoint> appoList = appointService.queryAllPublishedAppoint(map);
		appoList.add(appoint);
		model.addAttribute("appoList", appoList);
		model.addAttribute("type", type);
		model.addAttribute("deli", deli);
		model.addAttribute("appoint", "no");
		return "bam/delivery/deliveryEdit";
	}

	/**
	 * 查询送货单下的物资信息
	 * 
	 * @param deliId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryDeliMateByDeliId")
	public List<DeliMate> queryDeliMateByDeliId(String deliId) {
		List<DeliMate> list = deliveryService.queryDeliMateByDeliId(deliId);
		return list;
	}

	/**
	 * subtype=add:保存 subtype=sub:提交 编辑预约送货页面 的提交和保存预约送货
	 * 
	 * @param deli
	 * @param deliMateData
	 * @param type
	 * @return
	 */
	@Log(value = "保存/提交预约送货/特殊送货")
	@ResponseBody
	@RequestMapping("/updateDelivery")
	public boolean updateDelivery(Delivery deli, String deliMateData, String subtype, String mapgCode2) {
		List<DeliMate> list = JsonUtils.jsonToList(deliMateData, DeliMate.class);
		if ("add".equals(subtype)) {
			if (!mapgCode2.equals(deli.getMapgCode())) {// 预约单号被修改
				// 原来的预约单状态变为 “已发布”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("appoStatus", "已发布");
				map1.put("appoCode", mapgCode2);
				boolean c = appointService.updateAppoStatusByAppoCode(map1);
				// 新关联的预约单的状态变为 “待发货”
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("appoStatus", "待发货");
				map2.put("appoCode", deli.getMapgCode());
				boolean d = appointService.updateAppoStatusByAppoCode(map2);
				boolean b = deliveryService.updateDeliveryByDeliId(deli, list);
				return b && c && d;
			} else {
				boolean b = deliveryService.updateDeliveryByDeliId(deli, list);
				return b;
			}

		} else {
			int count = 0;
			boolean c = false;
			if (!mapgCode2.equals(deli.getMapgCode())) {// 预约单号被修改
				// 原来的预约单状态变为 “已发布”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("appoStatus", "已发布");
				map1.put("appoCode", mapgCode2);
				c = appointService.updateAppoStatusByAppoCode(map1);
				count++;
			}
			deli.setStatus("已发货");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("appoStatus", "已发货");
			map2.put("appoCode", deli.getMapgCode());
			boolean d = appointService.updateAppoStatusByAppoCode(map2);
			boolean b = deliveryService.updateDeliveryByDeliId(deli, list);
			if (count == 0) {
				return b && d;
			} else {
				return b && d && c;
			}
		}
	}

	/**
	 * 跳转到直发送货页面
	 * 
	 * @param deliType
	 * @param model
	 * @return
	 */
	@RequestMapping("/getStraDeliAddHtml")
	public String getStraDeliAddHtml(String deliType, Model model) {
		Delivery deli = new Delivery();
		deli.setDeliType(deliType);
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if ("supplier".equals(user.getUserType())) {
			// QualSupp qualSupp =
			// qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("zzoem", user.getSuppNo());
		}
		map.put("status", "已通知");
		List<StraMessage> straMesslist = straMessageService.queryAllNotifiedStraMessage(map);
		model.addAttribute("straMesslist", straMesslist);
		model.addAttribute("deli", deli);
		return "bam/delivery/deliStraAdd";
	}

	/**
	 * 根据提货单号查询直发通知详情和直发通知单下的物资
	 * 
	 * @param mapgCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryStraMessageBymessCode")
	public Map<String, Object> queryStraMessageBymessCode(String mapgCode) {
		Map<String, Object> map = straMessageService.queryStraMessageBymessCode(mapgCode);
		return map;
	}

	/**
	 * type=add :保存 type=sub :提交 新建直发送货页面 保存/提交直发送货
	 * 
	 * @return
	 */
	@Log(value = "保存/提交直发送货")
	@ResponseBody
	@RequestMapping("/addDeliveryTwo")
	public boolean addDeliveryTwo(Delivery deli, String deliMateData, String type) {
		return deliveryService.addDeliveryTwo(deli, deliMateData, type);
	}

	/**
	 * 跳转到直发送货单编辑/查看页面
	 * 
	 * @param deliId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping("/getDeliStraEditHtml")
	public String getDeliStraEditHtml(String deliId, String type, Model model) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if ("supplier".equals(user.getUserType())) {
			// QualSupp qualSupp =
			// qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("zzoem", user.getSuppNo());
		}
		map.put("status", "已通知");
		List<StraMessage> straMesslist = straMessageService.queryAllNotifiedStraMessage(map);
		Delivery deli = deliveryService.queryDeliveryByDeliId(deliId);
		StraMessage straMess = straMessageService.queryOneStraMessageByMessCode(deli.getMapgCode());
		straMesslist.add(straMess);
		model.addAttribute("straMesslist", straMesslist);
		model.addAttribute("type", type);
		model.addAttribute("deli", deli);
		model.addAttribute("straMess", "no");
		return "bam/delivery/deliStraEdit";
	}

	/**
	 * subtype=add:保存 subtype=sub:提交 编辑直发送货页面 的提交和保存直发送货
	 * 
	 * @param deli
	 * @param deliMateData
	 * @param type
	 * @return
	 */
	@Log(value = "保存/提交直发送货")
	@ResponseBody
	@RequestMapping("/updateDeliveryTwo")
	public boolean updateDeliveryTwo(Delivery deli, String deliMateData, String subtype, String mapgCode2) {
		List<DeliMate> list = JsonUtils.jsonToList(deliMateData, DeliMate.class);
		if ("add".equals(subtype)) {
			if (!mapgCode2.equals(deli.getMapgCode())) {// 直发通知单号被修改
				// 原来的直发通知单状态变为 “已通知”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("messStatus", "已通知");
				map1.put("messCode", mapgCode2);
				boolean c = straMessageService.updateMessStatusByMessCode(map1);
				// 新关联的直发通知单的状态变为 “待发货”
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("messStatus", "待发货");
				map2.put("messCode", deli.getMapgCode());
				boolean d = straMessageService.updateMessStatusByMessCode(map2);
				boolean b = deliveryService.updateDeliveryByDeliId(deli, list);
				return b && d && c;
			} else {
				boolean b = deliveryService.updateDeliveryByDeliId(deli, list);
				return b;
			}
		} else {
			int count = 0;
			boolean c = false;
			if (!mapgCode2.equals(deli.getMapgCode())) {// 直发通知单号被修改
				// 原来的直发通知单状态变为 “已通知”
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("messStatus", "已通知");
				map1.put("messCode", mapgCode2);
				c = straMessageService.updateMessStatusByMessCode(map1);
				count++;
			}
			deli.setStatus("已发货");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("messStatus", "已发货");
			map2.put("messCode", deli.getMapgCode());
			boolean d = straMessageService.updateMessStatusByMessCode(map2);
			boolean b = deliveryService.updateDeliveryByDeliId(deli, list);
			if (count == 0) {
				return b && d;
			} else {
				return b && d && c;
			}
		}
	}

	/**
	 * 获取采购订单号
	 * 
	 * @param suppId
	 * @param mateCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/querycontOrdeNumbOfOrderReleBySuppIdAndMateCode")
	public Map<String, Object> querycontOrdeNumbOfOrderReleBySuppIdAndMateCode(String suppId, String mateCode,
			Integer num) {
		QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(suppId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (supp != null) {
			OrderRele order = orderService.querycontOrdeNumbOfOrderReleBySapIdAndMateCode(supp.getSapId(), mateCode,
					num);
			if (order != null) {
				List<OrderMate> mates = orderService.findOrderMateByOrderNo(order.getContOrdeNumb());
				if (mates != null) {
					OrderMate orderMate = mates.get(0);
					map.put("judge", true);
					map.put("order", order);
					map.put("orderMate", orderMate);
				} else {
					map.put("msg", supp.getSuppName() + "的采购订单 " + order.getContOrdeNumb() + ",没有物料 " + mateCode);
					map.put("judge", false);
				}
			} else {
				map.put("msg", supp.getSuppName() + "的物料：" + mateCode + "未查询到采购订单");
				map.put("judge", false);
			}
		} else {
			map.put("msg", "未查询到合格供应商");
			map.put("judge", false);
		}

		return map;

	}

	/**
	 * 弹出框
	 * 
	 * @return
	 */
	@RequestMapping("getOrderListHtml")
	public String getOrderListHtml() {
		return "bam/delivery/orderList";
	}

	/**
	 * 获取这个物料对应的所有采购订单
	 * 
	 * @param suppId
	 * @param mateCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllOrderOfMate")
	public List<OrderDO> queryAllOrderOfMate(Model model, String suppId, String mateCode,String suppRange) {
		QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(suppId);
		List<OrderDO> list = deliveryService.queryAllOrderOfMate(supp, mateCode,suppRange);
		return list;
	}

	/**
	 * 跳转到送货单打印预览页面
	 * 
	 * @param deliId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getDeliPrintHtml")
	public String getDeliPrintHtml(String deliId, Model model, HttpServletRequest request) throws Exception {
		Delivery deli = deliveryService.queryDeliveryByDeliId(deliId);
		// String qrurl = MatrixToImageWriter.getQRCode2(deli.getDeliCode());
		String qrurl = getQRimg.getQRUrl(deli.getDeliCode());
		model.addAttribute("deli", deli);
		model.addAttribute("qrurl", qrurl);
		return "bam/delivery/deliveryPrint";
	}

	/**
	 * 跳转到特殊送货页面
	 * 
	 * @param deliType
	 * @param model
	 * @return
	 */
	@RequestMapping("getSpecialDeliAddHtml")
	public String getSpecialDeliAddHtml(String deliType, Model model) {
		SysUserDO user = UserCommon.getUser();
		Delivery deli = new Delivery();
		deli.setDeliType(deliType);
		Map<String, Object> map = new HashMap<String, Object>();
		if ("supplier".equals(user.getUserType())) {
			QualSupp qualSupp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("suppId", qualSupp.getSuppId());
		}
		map.put("status", "已发布");
		List<Appoint> appoList = appointService.queryAllPublishedAppoint(map);
		model.addAttribute("appoList", appoList);
		model.addAttribute("deli", deli);
		return "bam/delivery/specialDeliAdd";
	}

	/**
	 * 跳转到编辑特殊送货页面
	 * 
	 * @param deliId
	 * @param type
	 * @return
	 */
	@RequestMapping("/getSpecialDeliEditHtml")
	public String getSpecialDeliEditHtml(String deliId, String type, Model model) {
		Delivery deli = deliveryService.queryDeliveryByDeliId(deliId);
		Appoint appoint = appointService.queryOneAppointbyAppoCode(deli.getMapgCode());
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		if ("supplier".equals(user.getUserType())) {
			QualSupp qualSupp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("suppId", qualSupp.getSuppId());
		}
		map.put("status", "已发布");
		List<Appoint> appoList = appointService.queryAllPublishedAppoint(map);
		appoList.add(appoint);
		model.addAttribute("appoList", appoList);
		model.addAttribute("type", type);
		model.addAttribute("deli", deli);
		model.addAttribute("special", "no");
		return "bam/delivery/specialDeliEdit";
	}

	@ResponseBody
	@RequestMapping("/queryOrderBySuppIdAndMateCode")
	public Map<String, Object> queryOrderBySuppIdAndMateCode(String suppId, String mateCode, Integer num,
			boolean oldCodeJudge,String orderId,String suppRange) {
		
		return deliveryService.queryOrderBySuppIdAndMateCode(suppId, mateCode, num, oldCodeJudge,orderId,suppRange);
	}
	
	@ResponseBody
	@RequestMapping("/cancellDeliveryByDeliIds")
	public boolean cancellDeliveryByDeliIds(String deliIds){
		List<String> list = JsonUtils.jsonToList(deliIds, String.class);
		return deliveryService.cancellDeliveryByDeliIds(list);
	}
}
