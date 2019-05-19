package com.faujor.web.fam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.entity.basic.ApproveDO;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.fam.AuditMate;
import com.faujor.entity.fam.AuditOrder;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.basic.ApproveService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.fam.AuditService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/finance")
public class AuditController {
	@Autowired
	private AuditService auditService;
	@Autowired
	private MaterialService mateService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private ApproveService approveService;

	@RequestMapping("/audit")
	public String auditIndex() {
		return "fam/audit/audit";
	}

	@GetMapping("/auditList")
	@ResponseBody
	public Map<String, Object> auditList(AuditOrder ao, String page, String limit) {
		int p = page != null ? Integer.parseInt(page) : 1;
		int l = Integer.parseInt(limit);
		int offset = (p - 1) * l;
		RowBounds rb = new RowBounds(offset, l);
		SysUserDO user = UserCommon.getUser();
		ao.setCreator(user.getUserId().toString());
		String suppName = ao.getSuppName();
		if (!StringUtils.isEmpty(suppName)) {
			suppName = "%" + suppName + "%";
			ao.setSuppName(suppName);
		}
		Map<String, Object> map = auditService.findAuditListByParams(ao, rb);
		return map;
	}

	@GetMapping("/auditEdit")
	public String auditEdit(Model model, String auditId, int status) {
		// 0创建1编辑2查看
		AuditOrder ao = new AuditOrder();
		ao.setCreateTime(new Date());
		if (status != 0) {
			ao = auditService.findAuditById(auditId);
			// 驳回信息
			List<ApproveDO> list = approveService.findApproveListByMainId(ao.getId());
			String approve = JsonUtils.beanToJson(list);
			model.addAttribute("approve", approve);
		} else {
			SysUserDO user = UserCommon.getUser();
			// String suppId = "s" + user.getUsername();
			String suppId = "s" + user.getSuppNo();
			String suppName = user.getName();
			ao.setSuppId(suppId);
			ao.setSuppName(suppName);
		}
		model.addAttribute("ao", ao);
		model.addAttribute("status", status);
		return "fam/audit/auditEdit";
	}

	@GetMapping("/getMateData")
	@ResponseBody
	public Map<String, Object> getMateData(String auditId) {
		return auditService.getMateData(auditId);
	}

	@GetMapping("/getMouldData")
	@ResponseBody
	public Map<String, Object> getMouldData(String auditId) {

		return auditService.getMouldData(auditId);
	}

	@RequestMapping("/saveAuditData")
	@ResponseBody
	public RestCode saveAuditData(AuditOrder ao, String mateList, String mouldList) {
		int i = auditService.saveAuditData(ao, mateList, mouldList);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@RequestMapping("/removeAudit")
	@ResponseBody
	public RestCode removeAudit(String id) {
		int i = auditService.removeAuditOrder(id);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@RequestMapping("/batchRemoveAudit")
	@ResponseBody
	public RestCode batchRemoveAudit(String rows) {
		int i = auditService.batchRemoveAuditOrder(rows);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@RequestMapping("/auditConfirmList")
	public String auditConfirmList() {

		return "fam/audit/confirm";
	}

	@RequestMapping("/auditConfirmData")
	@ResponseBody
	public Map<String, Object> auditConfirmData(AuditOrder ao, String page, String limit) {
		int p = page != null ? Integer.parseInt(page) : 1;
		int l = Integer.parseInt(limit);
		int offset = (p - 1) * l;
		RowBounds rb = new RowBounds(offset, l);
		SysUserDO user = UserCommon.getUser();
		ao.setCreator(user.getUserId().toString());
		String suppName = ao.getSuppName();
		if (!StringUtils.isEmpty(suppName)) {
			suppName = "%" + suppName + "%";
			ao.setSuppName(suppName);
		}
		return auditService.auditConfirmData(ao, rb);
	}

	@RequestMapping("/auditConfirmDetails")
	public String auditConfirmDetaisl(Model model, String auditId, String type) {
		AuditOrder ao = auditService.findAuditById(auditId);
		// 驳回信息
		List<ApproveDO> list = approveService.findApproveListByMainId(ao.getId());
		String approve = JsonUtils.beanToJson(list);
		model.addAttribute("approve", approve);
		model.addAttribute("ao", ao);
		model.addAttribute("type", type);
		return "fam/audit/confirmDetails";
	}

	@RequestMapping("/auditConfirm")
	@ResponseBody
	public RestCode auditConfirm(String auditIds, String type) {
		int i = auditService.auditConfirm(auditIds, type);
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@RequestMapping("/chooseMate")
	public String chooseMate(Model model, String suppId, String selected) {
		model.addAttribute("mateIds", selected);
		model.addAttribute("suppId", suppId);
		// 物料组下拉数据

		// 物料类型下拉数据
		List<Dic> typeList = basicService.findDicListByCategoryCode("MATETYPE");
		model.addAttribute("typeList", typeList);
		MateDO md = new MateDO();
		model.addAttribute("md", md);
		return "fam/audit/chooseMate";
	}

	@RequestMapping("/chooseMateData")
	@ResponseBody
	public Map<String, Object> chooseMateData(String suppId, MateDO mate) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("suppId", suppId);
		params.put("mate", mate);
		List<MateDO> list = mateService.findMateListOfSupp(params);
		// List<MateDO> list = mateService.queryAllMaterialOfSupp(suppId);
		map.put("data", list);
		map.put("count", list.size());
		map.put("code", "0");
		map.put("msg", "");
		return map;
	}

	@RequestMapping("/rejectInfo")
	public String rejectInfo(Model model, String mainId) {
		ApproveDO approve = new ApproveDO();
		model.addAttribute("approve", approve);
		return "fam/audit/rejectInfo";
	}

	/**
	 * 保存驳回信息
	 * 
	 * @param auditId
	 * @param apprIdea
	 * @return
	 */
	@RequestMapping("/auditReject")
	@ResponseBody
	public RestCode auditReject(String auditId, String apprIdea) {
		AuditOrder order = auditService.findAuditById(auditId);
		int i = 0;
		if (order != null) {
			order.setAuditStatus("已退回");
			ApproveDO approve = new ApproveDO();
			approve.setId(UUIDUtil.getUUID());
			approve.setApprIdea(apprIdea);
			SysUserDO user = UserCommon.getUser();
			approve.setApprId(user.getUserId().toString());
			approve.setApprName(user.getName());
			approve.setApprTime(new Date());
			approve.setMainId(auditId);
			approve.setApprStatus("退回");
			approveService.saveApproveInfo(approve);
			i = auditService.updateAuditOrder(order);
		}
		if (i > 0)
			return RestCode.ok();
		return RestCode.error();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 添加物料时，查询上月月末库存
	 * 
	 * @param suppId
	 * @param mateId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryLastMonthBala")
	public Map<String, Object> queryLastMonthBala(String suppId, String mateId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String gtimelast = sdf.format(c.getTime()); // 上月
		int lastMonthMaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);
		// 按格式输出
		String gtime = sdf.format(c.getTime()); // 上月最后一天
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01");
		String gtime2 = sdf2.format(c.getTime()); // 上月第一天
		param.put("startDate", gtime2);
		param.put("endDate", gtime);
		param.put("suppId", suppId);
		param.put("mateId", mateId);
		AuditMate am = auditService.queryLastMonthBala(param);
		if (am == null) {
			map.put("judge", false);
		} else {
			map.put("judge", true);
			map.put("auditMate", am);
		}
		return map;
	}

	/**
	 * 跳转到财务稽核任务审核页面
	 * 
	 * @param model
	 * @param auditId
	 * @param status
	 * @return
	 */
	@GetMapping("/getTaskAuditHtml")
	public String getTaskAuditHtml(Model model, TaskParamsDO taskPDO) {
		AuditOrder ao = auditService.findAuditById(taskPDO.getSdata1());
		// 驳回信息
		List<ApproveDO> list = approveService.findApproveListByMainId(ao.getId());
		String approve = JsonUtils.beanToJson(list);
		model.addAttribute("approve", approve);
		model.addAttribute("ao", ao);
		model.addAttribute("status", "1");
		model.addAttribute("taskPDO", taskPDO);
		return "fam/audit/taskAudit";
	}

	/**
	 * 根据财务稽核的主键修改财务稽核的状态
	 * 
	 * @param auditStatus
	 * @param auditId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAuditSatusByAuditId")
	public boolean updateAuditSatusByAuditId(String auditStatus, String auditId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("auditStatus", auditStatus);
		map.put("auditId", auditId);
		return auditService.updateAuditSatusByAuditId(map);
	}

}
