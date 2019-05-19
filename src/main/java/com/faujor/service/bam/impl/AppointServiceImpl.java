package com.faujor.service.bam.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.bam.AppointMapper;
import com.faujor.dao.master.bam.DeliveryMapper;
import com.faujor.entity.bam.AppoCar;
import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.AppoQueryDO;
import com.faujor.entity.bam.Appoint;
import com.faujor.entity.bam.AppointMail;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.AppointService;
import com.faujor.service.bam.DeliveryService;
import com.faujor.service.bam.OrderMateCheckService;
import com.faujor.service.mail.MailCommonService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.UserCommon;

@Service(value = "AppointService")
public class AppointServiceImpl implements AppointService {

	@Autowired
	private AppointMapper appointMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private OrderMateCheckService orderMateCheckService;
	@Autowired
	private DeliveryMapper deliMapper;
//	@Autowired
//	private MailCommonService mailCommonService;
	@Autowired
	private QualSuppService qualSuppService;

	@Override
	@Transactional
	public boolean addAppoint(Appoint appo, List<AppoMate> list, List<AppoCar> carList) {
		int i = appointMapper.addAppoint(appo);
		int count = 0;
		for (AppoMate am : list) {
			int j = 0;
			am.setAppoId(appo.getAppoId());
			j = appointMapper.addAppoMate(am);
			count += j;
		}
		int count2 = 0;
		for (AppoCar ac : carList) {
			int k = 0;
			ac.setAppoId(appo.getAppoId());
			k = appointMapper.addAppoCar(ac);
			count2 += k;
		}
		if (i == 1 && count == list.size() && count2 == carList.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> queryAppointOfSuppByPage(Map<String, Object> map) {
		Map<String, Object> page = new HashMap<String, Object>();
		List<Appoint> list = appointMapper.queryAppointOfSuppByPage(map);
		int count = appointMapper.queryAppointOfSuppByPageCount(map);
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	@Transactional
	public boolean deleteAppointByAppoId(String[] appoIds) {
		int i = appointMapper.deleteAppoMateByAppoId(appoIds);
		int k = appointMapper.deleteAppoCarByManyAppoId(appoIds);
		int j = appointMapper.deleteAppointByAppoId(appoIds);
		if (j == appoIds.length) {
			TaskParamsDO params = new TaskParamsDO();
			for (int h = 0; h < appoIds.length; h++) {
				params.setSdata1(appoIds[h]);
				taskService.removeTaskBySdata1(params);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean upateAppoint(Appoint appo, List<AppoMate> appoMate, List<AppoCar> carList) {
		int i = appointMapper.upateAppoint(appo);
		int j = appointMapper.deleteAppoMateByOneAppoId(appo.getAppoId());
		int k = appointMapper.deleteAppoCarByAppoId(appo.getAppoId());
		for (AppoMate am : appoMate) {
			am.setAppoId(appo.getAppoId());
			appointMapper.addAppoMate(am);
		}
		for (AppoCar ac : carList) {
			ac.setAppoId(appo.getAppoId());
			appointMapper.addAppoCar(ac);
		}
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Appoint queryAppointByAppoId(String appoId) {
		Appoint appo = appointMapper.queryAppointByAppoId(appoId);
		return appo;
	}

	@Override
	public Map<String, Object> queryAppointForManagerByPage(Map<String, Object> map) {
		Map<String, Object> page = new HashMap<String, Object>();
		List<Appoint> list = appointMapper.queryAppointForManagerByPage(map);
		/*
		 * for (Appoint a : list) { String prodVeriId = a.getProdVeriId();
		 * if(prodVeriId != "" && prodVeriId != null){
		 * 
		 * } }
		 */
		int count = appointMapper.queryAppointForManagerByPageCount(map);
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	@Transactional
	public boolean updateAppointPriorityByAppoId(Appoint appo) {
		int i = appointMapper.updateAppointPriorityByAppoId(appo);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateAppoStatusByAppoId(Map<String, Object> map) {
		String status = (String) map.get("status");
		if ("2".equals(status)) {// 产销拒绝
			int k = appointMapper.updateAppoRefuseReasonByAppoIds(map);
		}
		int i = appointMapper.updateAppoStatusByAppoId(map);
		int j = (int) map.get("size");
		if (i == j) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> queryAppointForIssueByPage(Map<String, Object> map) {
		List<Appoint> list = appointMapper.queryAppointForIssueByPage(map);
		int count = appointMapper.queryAppointForIssueByPageCount(map);
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("data", list);
		page.put("msg", "");
		page.put("code", 0);
		page.put("count", count);
		return page;
	}

	@Override
	public List<Appoint> queryAppointForIssueByAppoDate(Date appoDate) {
		List<Appoint> list = appointMapper.queryAppointForIssueByAppoDate(appoDate);
		return list;
	}

	@Override
	@Transactional
	public Map<String, Object> updateAffirmDate(Appoint appo, String funtype) {
		Map<String, Object> result = new HashMap<String, Object>();
		int i = appointMapper.updateAffirmDate(appo);
		if (i == 1) {
			if("2".equals(funtype)) {//发布
				//预约单发布成功,发送邮件
				Appoint app = appointMapper.queryAppointByAppoId(appo.getAppoId());
				List<AppoMate> list = app.getAppoMates();
				AppointMail appoint = new AppointMail();
				appoint.setAppointCode(app.getAppoCode());
				Integer truckNum = app.getTruckNum();//车辆数量
				if(truckNum==null) {
					truckNum = 0;
				}
				appoint.setCarNum(truckNum.toString());
				String allCarType = appointMapper.getAllCarType(appo.getAppoId());
				appoint.setCarType(allCarType);//车辆类型
				appoint.setContact(app.getContact());//联系人
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date appoDate = app.getAppoDate();
				String format2 = format.format(appoDate);
				appoint.setDeliveryDate(format2);//送货时间
				appoint.setDestination(app.getReceAddr());//收货详细地址
				QualSupp qualSupp = qualSuppService.queryOneQualSuppbySuppId(app.getSuppId());
				if(qualSupp == null) {
					result.put("judge", false);
					result.put("judge", "预约申请发布失败,供应商未找到！");
					return result;
				}
				appoint.setSupplierEmail(qualSupp.getEmail());//供应商邮箱（来自供应商主数据）
//				appoint.setSupplierEmail("1002436465@qq.com");//测试使用
				appoint.setSupplierName(app.getSuppName()+"(FAX:"+qualSupp.getFaxNumber()+")");//供应商名称（来自供应商主数据，由两个字段拼接而成，供应商名称和FAX）
				appoint.setTelphone(app.getPhone());//电话
				appoint.setWarehouse(app.getReceUnit());//仓库
				appoint.setStatus("已发布");
				boolean b = true;
//				boolean b = mailCommonService.sendOutlookMail(appoint , list);
				//判断邮件是否发送成功，成功则修改修改邮箱发送状态和时间
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("appoId", appo.getAppoId());
				map.put("emailDate", new Date());
				if(b) {
					result.put("judge", true);
					result.put("judge", "预约申请发布成功,邮件发送成功");
					map.put("emailStatus", "成功");
				}else {
					result.put("judge", false);
					result.put("msg", "预约申请发布成功,邮件发送失败！");
					map.put("emailStatus", "失败");
				}
				appointMapper.updateEmailStatus(map);
			}else {
				result.put("judge", true);
				result.put("judge", "预约申请保存成功");
			}
		} else {
			result.put("judge", false);
			if("2".equals(funtype)) {
				result.put("msg", "预约申请发布失败,未发送邮件！");
			}else {
				result.put("msg", "预约申请保存失败,未发送邮件！");
			}
		}
		return result;
	}

	@Override
	@Transactional
	public boolean updateAffirmDateByAppoId(List<Appoint> list, String type) {
		SysUserDO user = UserCommon.getUser();
		int count = 0;
		for (Appoint a : list) {
			int i = 0;
			if ("2".equals(type)) {
				a.setAppoStatus("已发布");
				a.setCdcPublId(user.getUserId().toString());
				a.setCdcPublDate(new Date());
				a.setCdcPublStatus("同意");
			}
			i = appointMapper.updateAffirmDate(a);
			if ("2".equals(type)) {
				Appoint app = appointMapper.queryAppointByAppoId(a.getAppoId());
				List<AppoMate> appoMatelist = app.getAppoMates();
				AppointMail appoint = new AppointMail();
				appoint.setAppointCode(app.getAppoCode());
				Integer truckNum = app.getTruckNum();//车辆数量
				if(truckNum==null) {
					truckNum = 0;
				}
				appoint.setCarNum(truckNum.toString());
				String allCarType = appointMapper.getAllCarType(a.getAppoId());
				appoint.setCarType(allCarType);//车辆类型
				appoint.setContact(app.getContact());//联系人
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date appoDate = app.getAppoDate();
				String format2 = format.format(appoDate);
				appoint.setDeliveryDate(format2);//送货时间
				appoint.setDestination(app.getReceAddr());//收货详细地址
				QualSupp qualSupp = qualSuppService.queryOneQualSuppbySuppId(app.getSuppId());
				if(qualSupp == null) {
					return false;
				}
				appoint.setSupplierEmail(qualSupp.getEmail());//供应商邮箱（来自供应商主数据）---正式使用
//				appoint.setSupplierEmail("1002436465@qq.com");//测试使用
				appoint.setSupplierName(app.getSuppName()+"(FAX:"+qualSupp.getFaxNumber()+")");//供应商名称（来自供应商主数据，由两个字段拼接而成，供应商名称和FAX）
				appoint.setTelphone(app.getPhone());//电话
				appoint.setWarehouse(app.getReceUnit());//仓库
				appoint.setStatus("已发布");
				boolean b = true;
//				boolean b = mailCommonService.sendOutlookMail(appoint , appoMatelist);
				//判断邮件是否发送成功，成功则修改修改邮箱发送状态和时间
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("appoId", a.getAppoId());
				map.put("emailDate", new Date());
				if(b) {
					map.put("emailStatus", "成功");
				}else {
					map.put("emailStatus", "失败");
				}
				appointMapper.updateEmailStatus(map);
			}
			count += i;
		}
		if (count == list.size()) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<Appoint> queryAppoStatByAppoDate(String appoDate) {
		List<Appoint> list = appointMapper.queryAppoStatByAppoDate(appoDate);
		return list;
	}

	@Override
	public Map<String, Object> queryAppointByAppoCode(String appoCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Appoint appo = appointMapper.queryAppointByAppoCode(appoCode);
		if (appo != null) {
			List<AppoMate> list = appo.getAppoMates();
			map.put("list", list);
			map.put("appo", appo);
			map.put("judge", true);
		} else {
			map.put("judge", false);
		}
		return map;
	}

	@Override
	@Transactional
	public boolean updateAppoStatusByAppoCode(Map<String, Object> map) {
		int i = appointMapper.updateAppoStatusByAppoCode(map);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<AppoCar> queryAppoCarOfAppoint(String appoId) {
		return appointMapper.queryAppoCarOfAppoint(appoId);
	}

	@Override
	@Transactional
	public Map<String, Object> cancellAppointForManByAppoId(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		int i = appointMapper.cancellAppointForManByAppoId(map);
		int size = (int) map.get("size");
		if (i == size) {
			List<Appoint> appoints =  (List<Appoint>) map.get("appoints");
			List<AppoMate> appoMatelist = new ArrayList<>();
			String appoCodes = "";
			String msg = "";
			for (Appoint app : appoints) {
				String appoCode = app.getAppoCode();
				String suppId = app.getSuppId();
				AppointMail appoint = new AppointMail();
				appoint.setAppointCode(appoCode);
				appoint.setStatus("已作废");
				QualSupp qualSupp = qualSuppService.queryOneQualSuppbySuppId(suppId);
				if(qualSupp != null) {
					appoint.setSupplierEmail(qualSupp.getEmail());//供应商邮箱（来自供应商主数据）---正式使用
//					appoint.setSupplierEmail("1002436465@qq.com");//测试使用
					boolean b = true;
//					boolean b = mailCommonService.sendOutlookMail(appoint , appoMatelist);
					//判断邮件是否发送成功，成功则修改修改邮箱发送状态和时间
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("appoCode", appoCode);
					param.put("emailDate", new Date());
					if(b) {
						param.put("emailStatus", "成功");
					}else {
						msg+=appoCode+"作废成功,邮件发送失败;";
						param.put("emailStatus", "失败");
					}
					appointMapper.updateEmailStatus(param);
				}else {
					appoCodes+=appoCode+"未找到供应商信息，未发送邮件！";
				}
			}
			if(!StringUtils.isEmpty(appoCodes) || !StringUtils.isEmpty(msg)) {
				result.put("judge", false);
				result.put("msg", appoCodes+msg);
			}else {
				result.put("judge", true);
				result.put("msg", "预约申请作废成功");
			}
		} else {
			result.put("judge", false);
			result.put("msg", "预约申请作废失败，未发送邮件！");
		}
		return result;
	}

	@Override
	public List<Appoint> queryAllPublishedAppoint(Map<String, Object> map) {
		return appointMapper.queryAllPublishedAppoint(map);
	}

	@Override
	public Appoint queryOneAppointbyAppoCode(String appoCode) {
		return appointMapper.queryOneAppointbyAppoCode(appoCode);
	}

	@Override
	public List<Appoint> queryManyAppointByAppoIds(List<String> appoIds) {
		return appointMapper.queryManyAppointByAppoIds(appoIds);
	}

	@Override
	public Map<String, Object> queryAppoByAppoCode(String mapgCode,String deliCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Appoint appo = appointMapper.queryAppointByAppoCode(mapgCode);
		if (appo != null) {
			List<AppoMate> list = appo.getAppoMates();
			List<String> mateCodes = new ArrayList<String>();
			for (AppoMate mate : list) {
				mateCodes.add(mate.getMateCode());
			}
			Map<String, Object> result = orderMateCheckService.checkOutAppoMate(appo.getSuppId(), mateCodes,deliCode);
			boolean res = (boolean) result.get("judge");
			if (res) {
				map.put("list", list);
				map.put("appo", appo);
				map.put("judge", true);
			} else {
				map.put("judge", false);
				map.put("msg", result.get("result"));
			}
		} else {
			map.put("judge", false);
			map.put("msg", "预约单号：" + mapgCode + "不存在");
		}
		return map;
	}

	@Override
	@Transactional
	public boolean updateAppoDate(Appoint appo, String type) {
		if ("1".equals(type)) {// 需要修改送货单的预约日期和确认送货时间
			boolean c = deliMapper.updateDeliDate(appo);
			boolean b = appointMapper.updateAppoDate(appo);
			return b && c;
		} else {
			// 修改预约单的预约日期和确认送货时间
			boolean b = appointMapper.updateAppoDate(appo);
			return b;
		}
	}

	@Override
	public List<AppoQueryDO> queryAppoReportByParams(Map<String, Object> map) {
		List<AppoQueryDO> list = appointMapper.queryAppoReportByParams(map);
		List<AppoQueryDO> result = new ArrayList<AppoQueryDO>();
		AppoQueryDO totalDO = new AppoQueryDO();// 预约总数
		AppoQueryDO confirmDO = new AppoQueryDO();// 已确认，未确认
		AppoQueryDO publishDO = new AppoQueryDO();// 已发布,未发布
		AppoQueryDO straDO = new AppoQueryDO();// 直发通知
		result.add(totalDO);
		result.add(confirmDO);
		result.add(publishDO);
		int confTN = 0;// 已确认的车辆
		double confAN = 0;// 已确认的方量
		int confBN = 0;// 已确认的单据量
		int pubTN = 0;// 已发布的车辆
		double pubAN = 0;// 已发布的方量
		int pubBN = 0;// 已发布的单据量

		for (AppoQueryDO appo : list) {
			String title = appo.getReportTitle();
			double amountNum = appo.getAmountNum();
			int truckNum = appo.getTruckNum();
			int billNum = appo.getBillNum();
			if ("未确认".equals(title)) {
				// 未确认的量
				confirmDO.setAmountBackNum(amountNum);
				confirmDO.setTruckBackNum(truckNum);
				confirmDO.setBillBackNum(billNum);
			} else if ("已确认".equals(title)) {
				// 已确认 未发布
				confTN += truckNum;
				confAN += amountNum;
				confBN += billNum;
				// 未发布
				publishDO.setAmountBackNum(amountNum);
				publishDO.setTruckBackNum(truckNum);
				publishDO.setBillBackNum(billNum);
			} else if ("已发布".equals(title) || "待发货".equals(title) || "已发货".equals(title)) {
				// 已确认，已发布
				confTN += truckNum;
				confAN += amountNum;
				confBN += billNum;
				pubTN += truckNum;
				pubAN += amountNum;
				pubBN += billNum;
			} else if ("直发数量".equals(title)) {
				straDO.setAmountNum(amountNum);
				straDO.setAmountStr(amountNum + "");
				straDO.setBillNum(billNum);
				straDO.setBillStr(billNum + "");
				straDO.setTruckNum(truckNum);
				straDO.setTruckStr(truckNum + "");
			} else {
				appo.setAmountStr(amountNum + "");
				appo.setBillStr(billNum + "");
				appo.setTruckStr(truckNum + "");
				result.add(appo);
			}
		}
		// 总数
		totalDO.setReportTitle("总预约数");
		int d1 = confTN + confirmDO.getTruckBackNum();
		totalDO.setTruckStr(d1 + "");
		int d2 = confBN + confirmDO.getBillBackNum();
		totalDO.setBillStr(d2 + "");
		double d3 = confAN + confirmDO.getAmountBackNum();
		totalDO.setAmountStr(d3 + "");
		// 确认、未确认
		confirmDO.setReportTitle("已确认/未确认");
		confirmDO.setAmountStr(confAN + "/" + confirmDO.getAmountBackNum());
		confirmDO.setBillStr(confBN + "/" + confirmDO.getBillBackNum());
		confirmDO.setTruckStr(confTN + "/" + confirmDO.getTruckBackNum());
		// 发布未发布
		publishDO.setReportTitle("已发布/未发布");
		publishDO.setAmountStr(pubAN + "/" + publishDO.getAmountBackNum());
		publishDO.setBillStr(pubBN + "/" + publishDO.getBillBackNum());
		publishDO.setTruckStr(pubTN + "/" + publishDO.getTruckBackNum());

		List<AppoQueryDO> list2 = appointMapper.queryAppoReportForDate(map);
		AppoQueryDO do1 = new AppoQueryDO();
		do1.setReportTitle("8:00-10:00 时段");
		AppoQueryDO do2 = new AppoQueryDO();
		do2.setReportTitle("10:00-13:00 时段");
		AppoQueryDO do3 = new AppoQueryDO();
		do3.setReportTitle("13:00-15:00 时段");
		AppoQueryDO do4 = new AppoQueryDO();
		do4.setReportTitle("15:00-16:00 时段");
		AppoQueryDO do5 = new AppoQueryDO();
		do5.setReportTitle("16:00- 时段");
		for (AppoQueryDO appoQueryDO : list2) {
			String title = appoQueryDO.getReportTitle();
			double amountNum = appoQueryDO.getAmountNum();
			int billNum = appoQueryDO.getBillNum();
			double truckNum = appoQueryDO.getTruckNum();
			if ("8:00-10:00".equals(title)) {
				do1.setAmountStr(amountNum + "");
				do1.setBillStr(billNum + "");
				do1.setTruckStr(truckNum + "");
			} else if ("10:00-13:00".equals(title)) {
				do2.setAmountStr(amountNum + "");
				do2.setBillStr(billNum + "");
				do2.setTruckStr(truckNum + "");
			} else if ("13:00-15:00".equals(title)) {
				do3.setAmountStr(amountNum + "");
				do3.setBillStr(billNum + "");
				do3.setTruckStr(truckNum + "");
			} else if ("15:00-16:00".equals(title)) {
				do4.setAmountStr(amountNum + "");
				do4.setBillStr(billNum + "");
				do4.setTruckStr(truckNum + "");
			} else {
				do5.setAmountStr(amountNum + "");
				do5.setBillStr(billNum + "");
				do5.setTruckStr(truckNum + "");
			}
		}
		result.add(do1);
		result.add(do2);
		result.add(do3);
		result.add(do4);
		result.add(do5);
		// 直发
		straDO.setReportTitle("直发通知单");
		result.add(straDO);
		// 总数
		AppoQueryDO allDO = new AppoQueryDO();// 汇总
		allDO.setReportTitle("总计");
		int d4 = d1 + straDO.getTruckNum();
		int d5 = d2 + straDO.getBillNum();
		double d6 = d3 + straDO.getAmountNum();
		allDO.setTruckStr(d4 + "");
		allDO.setBillStr(d5 + "");
		allDO.setAmountStr(d6 + "");
		result.add(allDO);
		return result;
	}

	@Override
	public Map<String, Object> queryRefuseAppoByAppoCode(String appoCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		Appoint appoint = appointMapper.queryRefuseAppoByAppoCode(appoCode);
		if (appoint != null) {
			List<AppoCar> listCar = appointMapper.queryAppoCarOfAppoint(appoint.getAppoId());
			List<AppoMate> appoMates = appoint.getAppoMates();
			map.put("judge", true);
			map.put("appo", appoint);
			map.put("listCar", listCar);
			map.put("appoMates", appoMates);
		} else {
			map.put("msg", "预约单不存在,选择正确的预约单号");
			map.put("judge", false);
		}
		return map;
	}

	@Override
	public Map<String, Object> sendEmailOfAppoint(List<Appoint> appoints) {
		Map<String, Object> result = new HashMap<String, Object>();
		String appoCodes = "";
		String msg = "";
		for (Appoint appo : appoints) {
			String appoCode = appo.getAppoCode();
			String appoId = appo.getAppoId();
			String suppId = appo.getSuppId();
			String appoStatus = appo.getAppoStatus();
			AppointMail appoint = new AppointMail();
			appoint.setAppointCode(appoCode);
			if("已作废".equals(appoStatus) || "已发布".equals(appoStatus)) {
				appoint.setStatus(appoStatus);//已发布，已作废
			}else {
				result.put("judge", false);
				result.put("msg", "预约单状态不是已发布或者已作废");
				return result;
			}
			QualSupp qualSupp = qualSuppService.queryOneQualSuppbySuppId(suppId);
			if(qualSupp != null) {
				Appoint app = appointMapper.queryAppointByAppoId(appoId);
				List<AppoMate> appoMatelist = app.getAppoMates();
				Integer truckNum = app.getTruckNum();//车辆数量
				if(truckNum==null) {
					truckNum = 0;
				}
				appoint.setCarNum(truckNum.toString());
				String allCarType = appointMapper.getAllCarType(appoId);
				appoint.setCarType(allCarType);//车辆类型
				appoint.setContact(app.getContact());//联系人
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date appoDate = app.getAppoDate();
				String format2 = format.format(appoDate);
				appoint.setDeliveryDate(format2);//送货时间
				appoint.setDestination(app.getReceAddr());//收货详细地址
				appoint.setSupplierEmail(qualSupp.getEmail());//供应商邮箱（来自供应商主数据）---正式使用
//				appoint.setSupplierEmail("1002436465@qq.com");//测试使用
				appoint.setSupplierName(app.getSuppName()+"(FAX:"+qualSupp.getFaxNumber()+")");//供应商名称（来自供应商主数据，由两个字段拼接而成，供应商名称和FAX）
				appoint.setTelphone(app.getPhone());//电话
				appoint.setWarehouse(app.getReceUnit());//仓库
				boolean b = true;
//				boolean b = mailCommonService.sendOutlookMail(appoint , appoMatelist);
				//判断邮件是否发送成功，成功则修改修改邮箱发送状态和时间
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("appoCode", appoCode);
				param.put("emailDate", new Date());
				if(b) {
					param.put("emailStatus", "成功");
				}else {
					msg+=appoCode+"邮件发送失败;";
					param.put("emailStatus", "失败");
				}
				appointMapper.updateEmailStatus(param);
			}else {
				appoCodes+=appoCode+"未找到供应商信息！";
			}
		}
		if(!StringUtils.isEmpty(appoCodes) || !StringUtils.isEmpty(msg)) {
			result.put("judge", false);
			result.put("msg", appoCodes+msg);
		}else {
			result.put("judge", true);
			result.put("msg", "邮件重发成功");
		}
		return result;
	}

}
