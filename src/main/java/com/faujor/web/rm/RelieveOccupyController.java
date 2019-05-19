package com.faujor.web.rm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.Delivery;
import com.faujor.entity.bam.ReceMate;
import com.faujor.entity.bam.Receive;
import com.faujor.entity.bam.StraMessage;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.AppointService;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.ReceiveService;
import com.faujor.service.bam.StraMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.rm.DelieveOccupyService;
import com.faujor.utils.UserCommon;

@Controller
public class RelieveOccupyController {

	@Autowired
	private DelieveOccupyService delieveOccupyService;
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private AppointService appointService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private StraMessageService straMessageService;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private BasicService basicService;
	/**
	 * 跳转到解除占用页面
	 * @return
	 */
	@RequestMapping("/getRelieveOccupyHtml")
	public String getRelieveOccupyHtml() {
		
		return "rm/appoDeli/delieveOccupy";
	}
	/**
	 * 获取解除占用页面数据
	 * @param code
	 * @param codeDesc
	 * @return
	 */
	@Log(value ="获取解除占用页面数据")
	@ResponseBody
	@RequestMapping("/getAppoDeliData")
	public Map<String, Object> getAppoDeliData(String code,String codeDesc) {
		
		return delieveOccupyService.getAppoDeliData(code,codeDesc);
	}
	
	/**
	 * 跳转到预约送货单查看页面
	 * 
	 * @param deliId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppoDeliveryCheckHtml")
	public String getAppoDeliveryCheckHtml(String deliId, String type, Model model) {
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
		model.addAttribute("appoint","yes");
		model.addAttribute("deli", deli);
		return "bam/delivery/deliveryEdit";
	}
	
	
	/**
	 * 跳转到直发送货单查看页面
	 * 
	 * @param deliId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping("/getstraMessDeliveryCheckHtml")
	public String getstraMessDeliveryCheckHtml(String deliId, String type, Model model) {
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
		model.addAttribute("straMess", "yes");
		return "bam/delivery/deliStraEdit";
	}
	
	
	/**
	 * 跳转到特殊送货查看页面
	 * 
	 * @param deliId
	 * @param type
	 * @return
	 */
	@RequestMapping("/getSpecialDeliveryCheckHtml")
	public String getSpecialDeliveryCheckHtml(String deliId, String type, Model model) {
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
		model.addAttribute("special", "yes");
		model.addAttribute("type", type);
		model.addAttribute("deli", deli);
		return "bam/delivery/specialDeliEdit";
	}
	
	
	/**
	 * funType=1 : 修改 funType=2 : 查看 跳转到收货单的编辑/查看页面
	 * 
	 * @param model
	 * @param receId
	 * @param funType
	 * @return
	 */
	@RequestMapping("/getReceiveCheckHtml")
	public String getReceiveCheckHtml(Model model, String receId, String funType) {
		Receive rece = receiveService.queryReceiveByReceId(receId);
		model.addAttribute("funType", funType);
		model.addAttribute("rece", rece);
		model.addAttribute("check", "no");
		return "rm/appoDeli/receiveEdit";
	}
	
	/**
	 * 编辑状态的弹窗
	 * @param model
	 * @return
	 */
	@RequestMapping("/getEditStatusHtml")
	public String getEditStatusHtml(String code,String status,String type,Model model) {
		model.addAttribute("code", code);
		model.addAttribute("status", status);
		if("appoint".equals(type)) {
			List<Dic> appoStatusList = basicService.findDicListByCategoryCode("YYZT");
			model.addAttribute("statusList", appoStatusList);
		}else if("straMess".equals(type)){
			List<Dic> messStatusList = basicService.findDicListByCategoryCode("TZZT");
			model.addAttribute("statusList", messStatusList);
		}else if("delivery".equals(type)){
			List<Dic> statusList = basicService.findDicListByCategoryCode("DJZT");
			model.addAttribute("statusList", statusList);
		}else if("receive".equals(type)) {
			List<Dic> statusList = basicService.findDicListByCategoryCode("RECEDJZT");
			model.addAttribute("statusList", statusList);
		}else {
			List<Dic> statusList = new ArrayList<>();
			model.addAttribute("statusList", statusList);
		}
		return "rm/appoDeli/editStatus";
	}
	
	/**
	 * 修改状态
	 * @param code
	 * @param status
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateStatus")
	public boolean updateStatus(String code,String status,String type) {
		
		return delieveOccupyService.updateStatus(code,status,type);
	}
	/**
	 * 修改收货单的内向交货单号和is_occupy占用状态
	 * @param receMate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateReceMateInboDeliCodeAndIsOccupy")
	public boolean updateReceMateInboDeliCodeAndIsOccupy(ReceMate receMate) {
		return receiveService.updateReceMateInboDeliCodeAndIsOccupy(receMate);
	}
	/**
	 * 修改收货单物料的内向交货单号，是否占用的状态的弹窗
	 * @param model
	 * @return
	 */
	@RequestMapping("/getEditReceMateMessHtml")
	public String getEditReceMateMessHtml(String id,Model model) {
		//收货单物料信息
		ReceMate receMate = receiveService.queryReceMateMessById(id);
		if(receMate!= null) {
			String isOccypy = receMate.getIsOccypy();
			if("yes".equals(isOccypy)) {
				receMate.setIsOccypy("是");
			}else {
				receMate.setIsOccypy("否");
			}
		}
		model.addAttribute("receMate", receMate);
		//是否占用
		List<Dic> isOccupyList = basicService.findDicListByCategoryCode("ISOCCUPY");
		model.addAttribute("isOccupyList", isOccupyList);
		return "rm/appoDeli/editReceMateMess";
	}
	
}
