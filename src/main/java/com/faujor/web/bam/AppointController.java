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
import com.faujor.entity.bam.ReceiveMessage;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualProc;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.AppointService;
import com.faujor.service.bam.ReceiveMessageService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QualPapersService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.UserCommon;

@Controller
public class AppointController {

	@Autowired
	private AppointService appointService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private ReceiveMessageService receiveMessageService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private QualPapersService qualPapersService;
	/**
	 * 跳转到预约申请列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppointListHtml")
	public String getAppointListHtml(Model model){
		List<Dic> appoStatusList = basicService.findDicListByCategoryCode("YYZT");
		model.addAttribute("appoStatusList", appoStatusList);
		return "bam/appoint/appointList";
	}
	/**
	 * 跳转到预约申请列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppointListHtml2")
	public String getAppointListHtml2(Model model,String appoMates){
		List<Material> Matelist = JsonUtils.jsonToList(appoMates, Material.class);
		List<Dic> appoStatusList = basicService.findDicListByCategoryCode("YYZT");
		model.addAttribute("appoStatusList", appoStatusList);
		model.addAttribute("appoMatelist", appoMates);
		return "bam/appoint/appointList";
	}
	
	/**
	 * 预约申请列表分页展示
	 * @param appo
	 * @param limit
	 * @param page
	 * @return
	 */
	@Log(value ="获取预约申请列表")
	@ResponseBody
	@RequestMapping("/queryAppointOfSuppByPage")
	public Map<String,Object> queryAppointOfSuppByPage(String statusJson,Appoint appo,Integer limit,Integer page){
		if(page == null){page=1;}
		if(limit == null){limit =10;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String,Object>();
		if(statusJson != null){
			List<String> statusList = JsonUtils.jsonToList(statusJson, String.class);
			if(statusList.size()>0){
				map.put("statusList", statusList);
			}
		}
		if("supplier".equals(user.getUserType())){
			QualSupp supp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			map.put("suppId", supp.getSuppId());
		}else{
			map.put("status", "已保存");
		}
		map.put("start", start);
		map.put("end", end);
		map.put("appo", appo);
		Map<String, Object> page2 = appointService.queryAppointOfSuppByPage(map );
		return page2;
	}
	/**
	 * 删除已保存状态的预约申请
	 * @param appoIds
	 * @return
	 */
	@Log(value ="删除预约申请")
	@ResponseBody
	@RequestMapping("/deleteAppointByAppoId")
	public boolean deleteAppointByAppoId(String[] appoIds){
		boolean b = appointService.deleteAppointByAppoId(appoIds);
		return b;
	}
	/**
	 * 跳转到新建预约/修改预约页面 //供应商引用预约单页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAppointAddHtml")
	public String getAppointAddHtml(Model model,String type,String appoId){
		SysUserDO user = UserCommon.getUser();
		List<Dic> expectlist = basicService.findDicListByCategoryCode("QWSHSJ");
		List<String> receUnitList= receiveMessageService.queryAllReceUnitOfReceiveMess();
		model.addAttribute("expectlist", expectlist);
		model.addAttribute("receUnitList", receUnitList);
		model.addAttribute("type", type);
		/*Map<String, Object> map = new HashMap<String,Object>();
		map.put("status", "已拒绝");
		if(supp!=null){
			map.put("suppId", supp.getSuppId());
		}
		//查询当前供应商中已拒绝的预约申请
		List<Appoint> list = appointService.queryAllPublishedAppoint(map);
		model.addAttribute("appoList", list);*/
		//type等于1，为新建预约。type等于2，为修改预约
		if("1".equals(type)){
			Appoint appo = new Appoint();
			appo.setSuppId("900001919191");
			if("supplier".equals(user.getUserType())){
				QualSupp supp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
				appo.setSuppId(supp.getSuppId());
				// 采购数据信息（包括了供应商子范围编码和供应商子范围描述）
				List<QualProc> proList = qualPapersService.queryQualProcBySapId2(user.getSuppNo());
				if(proList == null) {
					proList = new ArrayList<>();
				}
				model.addAttribute("proList", proList);
			}
			appo.setSuppName(user.getName());//注意需要与供应商用户关联
			Date date = new Date();
			appo.setCreateDate(date);
			model.addAttribute("appo", appo);
		}else if("4".equals(type)){
			Appoint appo = appointService.queryAppointByAppoId(appoId);
			appo.setCiteAppo(appo.getAppoCode());
			appo.setAppoCode("");
			appo.setAppoStatus("");
			appo.setCreateDate(new Date());
			appo.setAppoDate(null);
			appo.setSuppName(user.getName());
			appo.setSuppId("900001919191");
			appo.setAffirmDate("");
			if("supplier".equals(user.getUserType())){
				QualSupp supp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
				appo.setSuppId(supp.getSuppId());
				// 采购数据信息（包括了供应商子范围编码和供应商子范围描述）
				List<QualProc> proList = qualPapersService.queryQualProcBySapId2(user.getSuppNo());
				if(proList == null) {
					proList = new ArrayList<>();
				}
				model.addAttribute("proList", proList);
			}
			model.addAttribute("appo", appo);
		}else{
			Appoint appo = appointService.queryAppointByAppoId(appoId);
			List<QualProc> proList2 = new ArrayList<>();
			if(!"supplier".equals(user.getUserType())){
				String suppId = appo.getSuppId();
				QualSupp supp = qualSuppService.queryOneQualSuppbySuppId(suppId);
				proList2 = qualPapersService.queryQualProcBySapId2(supp.getSapId());
			}else {
				proList2 = qualPapersService.queryQualProcBySapId2(user.getSuppNo());
			}
			model.addAttribute("proList", proList2);
			model.addAttribute("appo", appo);
		}
		return "bam/appoint/appointAdd";
	}
	/**
	 * 查询车辆类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryCarType")
	public List<Dic> queryCarType(){
		//附件类型
		List<Dic> typeList = basicService.findDicListByCategoryCode("CARTYPE");
		return typeList;
	}
	
	/**
	 * 预约申请的物质
	 * @param appoId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAppoMateOfAppoint")
	public List<AppoMate> queryAppoMateOfAppoint(String appoId){
		Appoint appo = appointService.queryAppointByAppoId(appoId);
		List<AppoMate> list = appo.getAppoMates();
		return list;
	}
	
	/**
	 * 弹出框
	 * @return
	 */
	@RequestMapping("/getSuppMateListHtml")
	public String getSuppMateListHtml(){
		return "bam/appoint/suppMateList";
	}
	
	
	
	
	/**新建预约中
	 * 弹出框的内容
	 * 查询供应商对应的所有物料
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllMaterialOfSupp")
	public Map<String, Object> queryAllMaterialOfSupp(Material mate,Integer limit,Integer page){
		if(page == null){page=1;}
		if(limit == null){limit =10;}
		int start = (page-1)*limit+1;
		int end = page*limit;
		SysUserDO user = UserCommon.getUser();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("end", end);
		map.put("mate", mate);
		if("supplier".equals(user.getUserType())){
			map.put("sapId", user.getSuppNo());
			Map<String, Object> page2 = materialService.queryAllMaterialOfSuppBySapIdByPage(map);
			return page2;
		}else{
			Map<String, Object> page2 = materialService.queryMaterialByPage(map);
			return page2;
		}
	}
	
	
	/**
	 *type=2：保存修改后的预约/type=1:保存新建预约
	 * @param appo
	 * @param appoMate
	 * @param type
	 * @return
	 */
	@Log(value ="新建/编辑预约申请")
	@ResponseBody
	@RequestMapping("/addAppoint")
	public boolean addAppoint(Appoint appo,String appoMateData,String appoCarData,String type){
		SysUserDO user = UserCommon.getUser();//供应商用户    ---------注意后期修改
		List<AppoMate> list = JsonUtils.jsonToList(appoMateData, AppoMate.class);
		List<AppoCar> carList = JsonUtils.jsonToList(appoCarData, AppoCar.class);
		boolean b;
		if("1".equals(type) || "4".equals(type)){
			String appoCode = codeService.getCodeByCodeType("appointNo");
			QualSupp qualSupp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			if(qualSupp != null){
				appo.setSuppId(qualSupp.getSuppId());//为供应商用户对应的供应商编号
			}
			appo.setAppoCode(appoCode);
			appo.setAppoStatus("已保存");
			appo.setCreateId(user.getUserId().toString());
			appo.setCreator(user.getName());
			appo.setPriority("一般");
			b = appointService.addAppoint(appo, list,carList);
		}else{
			appo.setModifieId(user.getUserId().toString());
			appo.setModifier(user.getName());
			b = appointService.upateAppoint(appo,list,carList);
		}
		return b;
	}
	/**
	 *type=2：提交修改后的预约/type=1:提交新建预约
	 * @param appo
	 * @param appoMate
	 * @param type
	 * @return
	 */
	@Log(value ="新建/编辑预约申请")
	@ResponseBody
	@RequestMapping("/submitAppoint")
	public boolean submitAppoint(Appoint appo,String appoMateData,String appoCarData,String type){
		SysUserDO user = UserCommon.getUser();//供应商用户    ---------注意后期修改
		List<AppoMate> list = JsonUtils.jsonToList(appoMateData, AppoMate.class);
		List<AppoCar> carList = JsonUtils.jsonToList(appoCarData, AppoCar.class);
		boolean b;
		if("1".equals(type) || "4".equals(type)){
			String appoCode = codeService.getCodeByCodeType("appointNo");
			QualSupp qualSupp = qualSuppService.queryQualSuppBySapId(user.getSuppNo());
			if(qualSupp != null){
				appo.setSuppId(qualSupp.getSuppId());//为供应商用户对应的供应商编号
			}
			appo.setAppoCode(appoCode);
			//appo.setAppoStatus("未确认");
			appo.setCreateId(user.getUserId().toString());
			appo.setCreator(user.getName());
			appo.setPriority("一般");
			b = appointService.addAppoint(appo, list,carList);
		}else{
			appo.setModifieId(user.getUserId().toString());
			appo.setModifier(user.getName());
			//appo.setAppoStatus("未确认");
			b = appointService.upateAppoint(appo,list,carList);
		}
		return b;
	}
	/**
	 * 获取接收单位信息
	 * @param receUnit
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryReceiveMessByReceUnit")
	public Map<String,Object> queryReceiveMessByReceUnit(String receUnit){
		ReceiveMessage rece = receiveMessageService.queryReceiveMessByReceUnit(receUnit);
		Map<String,Object> map = new HashMap<String,Object>();
		if(rece != null){
			map.put("judge", true);
			map.put("rece", rece);
		}else{
			map.put("judge", false);
		}
		return map;
	} 
	
	
	/**
	 * 获取预约申请的车辆信息
	 * @param appoId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAppoCarOfAppoint")
	public List<AppoCar> queryAppoCarOfAppoint(String appoId){
		return appointService.queryAppoCarOfAppoint(appoId);
	}
	/**
	 * 根据预约单号修改预约申请的状态
	 * @param appoStatus
	 * @param appoCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAppoStatusByappoCode")
	public boolean updateAppoStatusByappoCode(String appoStatus,String appoCode){
		SysUserDO user = UserCommon.getUser();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("appoStatus", appoStatus);
		map.put("appoCode", appoCode);
		if("已确认".equals(appoStatus)){
			Date date = new Date();
			String format = DateUtils.format(date, "yyyy-MM-dd");
			map.put("prodVeriId", user.getUserId().toString());
			map.put("prodVeriDate",format );
			map.put("prodVeriStatus", "同意");
		}
		boolean b = appointService.updateAppoStatusByAppoCode(map);
		return b;
	}
	/**
	 * 根据预约单的主键修改预约申请的状态
	 * @param appoStatus
	 * @param appoCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAppoStatusByappoIds")
	public boolean updateAppoStatusByappoIds(String appoStatus,String appoIds){
		List<String> appoIdList = JsonUtils.jsonToList(appoIds, String.class);
		Appoint appo = new Appoint();
		appo.setAppoStatus(appoStatus);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("appoIds", appoIdList);
		map.put("size", appoIdList.size());
		map.put("appo", appo);
		boolean b = appointService.updateAppoStatusByAppoId(map);
		return b;
	}
	
	/**
	 * 引用已拒绝的预约单
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryRefuseAppoByAppoCode")
	public Map<String,Object> queryRefuseAppoByAppoCode(String appoCode){
		return appointService.queryRefuseAppoByAppoCode(appoCode);
	}
	
	
	
	
}
