package com.faujor.web.bam;

import java.util.ArrayList;
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
import com.faujor.entity.bam.AppoCar;
import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.AppointService;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class AppointManController {

	@Autowired
	private AppointService appointService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private ReceiveMessageService receiveMessageService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private DeliveryService deliService;

	/**
	 * 跳转到预约管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/getappointManListHtml")
	public String getappointManListHtml(Model model) {
		List<Dic> priorityList = basicService.findDicListByCategoryCode("YXJ");
		List<Dic> list = basicService.findDicListByCategoryCode("YYZT");
		List<Dic> appoStatusList = new ArrayList<Dic>();
		for (Dic d : list) {
			String status = d.getDicName();
			if (!"已保存".equals(status)) {
				appoStatusList.add(d);
			}
		}
		List<Dic> expectlist = basicService.findDicListByCategoryCode("QWSHSJ");
		model.addAttribute("expectlist", expectlist);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("appoStatusList", appoStatusList);
		return "bam/appoMan/appointManList";
	}

	/**
	 * 预约管理分页展示数据
	 * 
	 * @return
	 */
	@Log(value = "获取预约管理列表")
	@ResponseBody
	@RequestMapping("/queryAppointForManagerByPage")
	public Map<String, Object> queryAppointForManagerByPage(String statusJson, Appoint appo, Integer limit,
			Integer page) {
		if (limit == null) {
			limit = 10;
		}
		if (page == null) {
			page = 1;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (statusJson != null) {
			List<String> statusList = JsonUtils.jsonToList(statusJson, String.class);
			if (statusList.size() > 0) {
				map.put("statusList", statusList);
			}
		}
		int start = (page - 1) * limit + 1;
		int end = page * limit;
		map.put("appo", appo);
		map.put("start", start);
		map.put("end", end);
		Map<String, Object> page2 = appointService.queryAppointForManagerByPage(map);
		return page2;
	}

	/**
	 * 跳转到紧急预约/预约确认页面 1:紧急预约 ，2：预约确认
	 * 
	 * @param model
	 * @param type
	 * @return
	 */
	@RequestMapping("/getAppointUrgeAddHtml")
	public String getAppointUrgeAddHtml(Model model, String type, String appoId) {
		// 任务信息
		TaskDO task = null;
		if (appoId != null) {
			task = taskService.getTask(appoId);
		} else {
			task = new TaskDO();
			task.setIsOwn(false);
		}
		model.addAttribute("task", task);
		List<Dic> expectlist = basicService.findDicListByCategoryCode("QWSHSJ");
		List<Dic> priorityList = basicService.findDicListByCategoryCode("YXJ");
		Map<String, Object> map = qualSuppService.queryAllQualSupp();
		List<QualSupp> suppList = (List<QualSupp>) map.get("data");
		List<String> receUnitList = receiveMessageService.queryAllReceUnitOfReceiveMess();
		model.addAttribute("receUnitList", receUnitList);
		model.addAttribute("suppList", suppList);
		model.addAttribute("expectlist", expectlist);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("type", type);
		if ("1".equals(type)) {
			Appoint appo = new Appoint();
			Date date = new Date();
			appo.setPriority("紧急");
			appo.setCreateDate(date);
			appo.setAppoStatus("未确认");
			model.addAttribute("appo", appo);
		} else {
			Appoint appo = appointService.queryAppointByAppoId(appoId);
			model.addAttribute("appo", appo);
		}
		return "bam/appoMan/appointUrgeAdd";
	}

	/**
	 * 弹出框页面
	 * 
	 * @return
	 */
	@RequestMapping("/getSuppMateForManListHtml")
	public String getSuppMateForManListHtml() {
		return "bam/appoMan/suppMateList";
	}

	/**
	 * 根据供应商的名称查询供应商
	 * 
	 * @param suppName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryQualSuppBySuppName")
	public Map<String, Object> queryQualSuppBySuppName(String suppName) {
		Map<String, Object> map = new HashMap<String, Object>();
		QualSupp qualSupp = qualSuppService.queryQualSuppBySuppName(suppName);
		if (qualSupp != null) {
			map.put("judge", true);
			map.put("qualSupp", qualSupp);
		} else {
			map.put("jude", false);
		}
		return map;
	}

	/**
	 * 获取合格供应商所对应的所有物料
	 * 
	 * @param suppId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllMaterialOfSuppForMan")
	public List<MateDO> queryAllMaterialOfSuppForMan(String suppId, Material mate) {
		// List<MateDO> list = materialService.queryAllMaterialOfSupp(suppId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("suppId", suppId);
		map.put("mate", mate);
		List<MateDO> list2 = materialService.queryAllMaterialOfSuppByParams(map);
		return list2;
	}

	/**
	 * type=1:紧急预约保存/type=2:预约确认保存（编辑优先级）
	 * 
	 * @return
	 */
	@Log(value = "新建保存/编辑保存预约申请")
	@ResponseBody
	@RequestMapping("/addUrgeAppoint")
	public boolean addUrgeAppoint(String type, Appoint appo, String appoMateData, String appoCarData) {
		SysUserDO user = UserCommon.getUser();
		List<AppoMate> list = JsonUtils.jsonToList(appoMateData, AppoMate.class);
		List<AppoCar> carList = JsonUtils.jsonToList(appoCarData, AppoCar.class);
		boolean b = false;
		if ("1".equals(type)) {
			String appoCode = codeService.getCodeByCodeType("appointNo");
			appo.setAppoCode(appoCode);
			appo.setAppoStatus("未确认");
			appo.setCreateId(user.getUserId().toString());
			appo.setCreator(user.getName());
			b = appointService.addAppoint(appo, list, carList);
		} else if ("2".equals(type)) {
			appo.setModifieId(user.getUserId().toString());
			appo.setModifier(user.getName());
			b = appointService.updateAppointPriorityByAppoId(appo);
		}
		return b;
	}

	/**
	 * status=1:展示列表的确认/status=2:拒绝
	 * 
	 * @param status
	 * @param appoIds
	 * @return
	 */
	@Log(value = "确认/拒绝预约申请")
	@ResponseBody
	@RequestMapping("/updateAppoStatusByAppoId")
	public boolean updateAppoStatusByAppoId(String status, String appoIds, String text) {
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> appoIdList = JsonUtils.jsonToList(appoIds, String.class);
		Appoint appo = new Appoint();
		appo.setProdVeriId(user.getUserId().toString());
		appo.setProdVeriDate(new Date());
		if ("1".equals(status)) {
			appo.setAppoStatus("已确认");
			appo.setProdVeriStatus("同意");
		} else if ("2".equals(status)) {
			appo.setAppoStatus("已拒绝");
			appo.setProdVeriStatus("不同意");
			map.put("text", text);
		}
		int size = appoIdList.size();
		map.put("appoIds", appoIdList);
		map.put("size", size);
		map.put("appo", appo);
		map.put("status", status);
		boolean b = appointService.updateAppoStatusByAppoId(map);
		return b;
	}

	/**
	 * 预约确认页面的确认功能
	 * 
	 * @param appo
	 * @return
	 */
	@Log(value = "确认预约申请")
	@ResponseBody
	@RequestMapping("/affirmAppoint")
	public boolean affirmAppoint(Appoint appo) {
		SysUserDO user = UserCommon.getUser();
		appo.setModifieId(user.getUserId().toString());
		appo.setModifier(user.getName());
		// appo.setProdVeriId(user.getUserId().toString());
		// appo.setProdVeriStatus("同意");
		// appo.setProdVeriDate(new Date());
		// appo.setAppoStatus("已确认");
		appo.setAppoStatus("未确认");
		boolean b = appointService.updateAppointPriorityByAppoId(appo);
		return b;
	}

	/**
	 * 紧急预约页面的提交功能
	 * 
	 * @param appo
	 * @return
	 */
	@Log(value = "新建提交预约申请")
	@ResponseBody
	@RequestMapping("/submitUrgeAppoint")
	public boolean submitUrgeAppoint(Appoint appo, String appoMateData, String appoCarData) {
		SysUserDO user = UserCommon.getUser();
		List<AppoMate> list = JsonUtils.jsonToList(appoMateData, AppoMate.class);
		List<AppoCar> carList = JsonUtils.jsonToList(appoCarData, AppoCar.class);
		String appoCode = codeService.getCodeByCodeType("appointNo");
		appo.setAppoCode(appoCode);
		// appo.setProdVeriId(user.getUserId().toString());
		// appo.setProdVeriStatus("同意");
		// appo.setProdVeriDate(new Date());
		// appo.setAppoStatus("已确认");
		appo.setAppoStatus("未确认");
		appo.setCreateId(user.getUserId().toString());
		appo.setCreator(user.getName());
		boolean b = appointService.addAppoint(appo, list, carList);
		return true;
	}

	/**
	 * 预约申请作废
	 * 
	 * @param appoIds
	 * @return
	 */
	@Log(value = "作废预约申请")
	@ResponseBody
	@RequestMapping("/cancellAppointForManByAppoId")
	public Map<String, Object> cancellAppointForManByAppoId(String appoIds,String appos) {
		List<String> appoIdList = JsonUtils.jsonToList(appoIds, String.class);
		List<Appoint> appoints = JsonUtils.jsonToList(appos, Appoint.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appoIds", appoIdList);
		map.put("size", appoIdList.size());
		map.put("status", "已作废");
		map.put("appoints", appoints);
		return appointService.cancellAppointForManByAppoId(map);
	}

	/**
	 * 跳转到预约送货任务审核流程页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppointAuditHtml")
	public String getAppointAuditHtml(Model model, TaskParamsDO taskPDO) {
		List<Dic> expectlist = basicService.findDicListByCategoryCode("QWSHSJ");
		List<Dic> affirmtlist = basicService.findDicListByCategoryCode("QWSHSJ");
		List<Dic> priorityList = basicService.findDicListByCategoryCode("YXJ");
		List<String> receUnitList = receiveMessageService.queryAllReceUnitOfReceiveMess();
		Appoint appo = appointService.queryAppointByAppoId(taskPDO.getSdata1());
		// 查询预约日期当日已发布的预约在每个时间段的状况
		List<Appoint> list = appointService.queryAppointForIssueByAppoDate(appo.getAppoDate());
		// 8:00-10:00
		int eightCount = 0;
		// 10:00-13:00
		int tenCount = 0;
		// 13:00-15:00
		int thirCount = 0;
		// 15:00-16:00
		int fiftCount = 0;
		// 16:00-
		int sixCount = 0;
		for (Appoint a : list) {
			if ("8:00-10:00".equals(a.getAffirmDate())) {
				eightCount++;
			} else if ("10:00-13:00".equals(a.getAffirmDate())) {
				tenCount++;
			} else if ("13:00-15:00".equals(a.getAffirmDate())) {
				thirCount++;
			} else if ("15:00-16:00".equals(a.getAffirmDate())) {
				fiftCount++;
			} else if ("16:00-".equals(a.getAffirmDate())) {
				sixCount++;
			}
		}
		/*
		 * for(Dic d:affirmtlist){ if("8:00-10:00".equals(appo.getAffirmDate())
		 * && d.getDicName().equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+eightCount); }else
		 * if("10:00-13:00".equals(appo.getAffirmDate()) &&
		 * d.getDicName().equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+tenCount); }else
		 * if("13:00-15:00".equals(appo.getAffirmDate()) &&
		 * d.getDicName().equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+thirCount); }else
		 * if("15:00-16:00".equals(appo.getAffirmDate()) &&
		 * d.getDicName().equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+fiftCount); }else
		 * if("16:00-".equals(appo.getAffirmDate()) &&
		 * d.getDicName().equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+sixCount); }
		 * 
		 * } for(Dic d:affirmtlist){
		 * if("8:00-10:00".equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+eightCount); }else
		 * if("10:00-13:00".equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+tenCount); }else
		 * if("13:00-15:00".equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+thirCount); }else
		 * if("15:00-16:00".equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+fiftCount); }else
		 * if("16:00-".equals(appo.getAffirmDate())){
		 * appo.setAffirmDate(d.getDicName()+" 已约"+sixCount); }
		 * 
		 * } for(Dic d:affirmtlist){ if("8:00-10:00".equals(d.getDicName())){
		 * d.setDicName(d.getDicName()+" 已约"+eightCount); }else
		 * if("10:00-13:00".equals(d.getDicName())){
		 * d.setDicName(d.getDicName()+" 已约"+tenCount); }else
		 * if("13:00-15:00".equals(d.getDicName())){
		 * d.setDicName(d.getDicName()+" 已约"+thirCount); }else
		 * if("15:00-16:00".equals(d.getDicName())){
		 * d.setDicName(d.getDicName()+" 已约"+fiftCount); }else
		 * if("16:00-".equals(d.getDicName())){
		 * d.setDicName(d.getDicName()+" 已约"+sixCount); }
		 * 
		 * }
		 */
		for (Dic d : affirmtlist) {
			if ("8:00-10:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + eightCount);
			} else if ("10:00-13:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + tenCount);
			} else if ("13:00-15:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + thirCount);
			} else if ("15:00-16:00".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + fiftCount);
			} else if ("16:00-".equals(appo.getAffirmDate()) && d.getDicName().equals(appo.getAffirmDate())) {
				appo.setAffirmDate(d.getDicName() + " 已约" + sixCount);
			}

		}
		for (Dic d : affirmtlist) {
			if ("8:00-10:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + eightCount);
			} else if ("10:00-13:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + tenCount);
			} else if ("13:00-15:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + thirCount);
			} else if ("15:00-16:00".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + fiftCount);
			} else if ("16:00-".equals(d.getDicName())) {
				d.setDicName(d.getDicName() + " 已约" + sixCount);
			}

		}
		model.addAttribute("expectlist", expectlist);
		model.addAttribute("affirmtlist", affirmtlist);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("receUnitList", receUnitList);
		model.addAttribute("appo", appo);
		model.addAttribute("taskPDO", taskPDO);
		return "bam/appoMan/appointAudit";
	}

	/**
	 * 确认预约申请
	 * 
	 * @param appoStatus
	 * @param appoIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/confirmAppointByAppoIds")
	public boolean confirmAppointByAppoIds(String appoStatus, String appoIds) {
		List<String> appoIdList = JsonUtils.jsonToList(appoIds, String.class);
		SysUserDO user = UserCommon.getUser();
		Appoint appo = new Appoint();
		appo.setAppoStatus(appoStatus);
		appo.setProdVeriId(user.getUserId().toString());
		appo.setProdVeriStatus("同意");
		appo.setProdVeriDate(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appoIds", appoIdList);
		map.put("size", appoIdList.size());
		map.put("appo", appo);
		boolean b = appointService.updateAppoStatusByAppoId(map);
		return b;
	}

	@RequestMapping("/getUpdateAppoHtml")
	public String getUpdateAppoHtml(String appoCode, Model model) {
		List<Dic> affirmtlist = basicService.findDicListByCategoryCode("QWSHSJ");
		Appoint appoint = appointService.queryOneAppointbyAppoCode(appoCode);
		model.addAttribute("affirmtlist", affirmtlist);
		model.addAttribute("appo", appoint);
		return "/bam/appoMan/updateAppoDate";
	}

	@ResponseBody
	@RequestMapping("/updateAppoDate")
	public boolean updateAppoDate(String appoCode, String appoDate, String affirmDate, String type) {
		Date date = DateUtils.parse(appoDate, "yyyy-MM-dd");
		Appoint appo = new Appoint();
		appo.setAppoCode(appoCode);
		appo.setAppoDate(date);
		appo.setAffirmDate(affirmDate);
		return appointService.updateAppoDate(appo, type);
	}
	/**
	 * 邮件重发
	 * @param appos
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendEmailOfAppoint")
	public Map<String, Object> sendEmailOfAppoint(String appos) {
		List<Appoint> appoints = JsonUtils.jsonToList(appos, Appoint.class);
		return appointService.sendEmailOfAppoint(appoints);
	}
	
	

}
