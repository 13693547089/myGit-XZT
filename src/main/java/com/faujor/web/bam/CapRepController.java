package com.faujor.web.bam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.bam.psm.CapRep;
import com.faujor.entity.bam.psm.CapRepMate;
import com.faujor.entity.bam.psm.CapRepOrder;
import com.faujor.entity.bam.psm.CapRepStock;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.CapRepService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.PrivilegesCommon;
import com.faujor.utils.RestCode;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UUIDUtil;
import com.faujor.utils.UserCommon;
/**
 * 产能上报月报
 * @author 
 * 2018年10月26日 下午5:04:32
 * CapRepController.java
 */
@Controller
@RequestMapping("/capRep")
public class CapRepController {
	@Autowired
	private CapRepService capRepService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private PrivilegesCommon privilegesCommon;
	@Autowired
	private QualSuppService qualSuppService;
	/**
	 * 日期转换
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
	/**
	 * 产能月报列表页
	 * @return
	 */
	@RequestMapping("/page/list")
	public String getListPage(Model model){	
		List<Dic> statusList = basicService.findDicListByCategoryCode("CAP_STATUS");
		model.addAttribute("statusList", statusList);
		return "bam/capRep/list";
	}
	/**
	 * 获取产能月报编辑页面
	 * @param type 1新增 2编辑 3查看
	 * @param id
	 * @return
	 */
	@RequestMapping("/page/edit")
	public String getEditPage(Model model, String type,String id){
		CapRep capRep=null;
		SysUserDO user = UserCommon.getUser();
		if(StringUtil.isNullOrEmpty(type) || type.equals("1")){
			capRep=new CapRep();
			capRep.setId(UUIDUtil.getUUID());
			capRep.setCreateTime(new Date());
			capRep.setSuppNo(user.getSuppNo());
			capRep.setSuppName(user.getName());
		}else{
			capRep=capRepService.getById(id);
		}
		//获取自己管理的供应商的列表
		List<String> suppNos = privilegesCommon.getAllSupplierCode();
		List<QualSupp> suppList = qualSuppService.queryQualSuppBySapCodes(suppNos);
		model.addAttribute("suppList", suppList);
		String userType = user.getUserType();
		model.addAttribute("userType", userType);
		model.addAttribute("type", type);
		model.addAttribute("capRep", capRep);
		return "bam/capRep/edit";
	}
	/**
	 * 获取订单的添加页面
	 * @param model
	 * @param mainId
	 * @return
	 */
	@RequestMapping("/page/order/add")
	public String getAddOrderPage(Model model,String mainId){
		model.addAttribute("mainId", mainId);
		return "bam/capRep/addOrder";
	}
	/**
	 * 获取库存的添加页面
	 * @param model
	 * @param mainId
	 * @return
	 */
	@RequestMapping("/page/stock/add")
	public String getAddStockPage(Model model,String mainId){
		model.addAttribute("mainId", mainId);
		return "bam/capRep/addStock";
	}
	/**
	 * 分页获取月报数据
	 * @param page
	 * @param capCode
	 * @param status
	 * @param createTime
	 * @param repMonth
	 * @param suppName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public LayuiPage<CapRep> getCapRepByPage(LayuiPage<CapRep> page,String capCode,String status,
			Date startDate,Date endDate,String repMonth,String suppName){
		Map<String, Object> map=new HashMap<>();
		page.calculatePage();
		map.put("page", page);
		map.put("status", status);
		map.put("repMonth", repMonth);
		if(StringUtil.isNotNullOrEmpty(capCode)){
			map.put("capCode", "%"+capCode+"%");
		}
		if(StringUtil.isNotNullOrEmpty(suppName)){
			map.put("suppName", "%"+suppName+"%");
		}
		if(startDate!=null){
			map.put("startDate", startDate);
			map.put("endDate", DateUtils.addDate(endDate, 1));
		}

		//获取自己管理的供应商编码
		List<String> suppCodes = privilegesCommon.getAllSupplierCode();
		map.put("suppCodes", suppCodes);
		return capRepService.getCapRepByPage(map);
	}
	/**
	 * 根据主键获取物料信息
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/mate/list")
	public List<CapRepMate> getMateByMainId(String mainId){	
		return capRepService.getMateByMainId(mainId);
	}
	/**
	 * 获取供应商的物料
	 * @param buyerId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/mate/supp/list")
	public List<CapRepMate> getSuppMate(String mainId,String suppNo){
		return capRepService.getMateBySuppNo(suppNo,mainId);
	}
	/**
	 * 根据主键获取订单信息
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/order/list")
	public List<CapRepOrder> getOrderByMainId(String mainId){
		return capRepService.getOrderByMainId(mainId);
	}
	/**
	 * 根据主键 获取库存信息
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/stock/list")
	public List<CapRepStock> getStockByMainId(String mainId){
		return capRepService.getStockByMainId(mainId);
	}
	/**
	 * 保存日报数据
	 * @param capRep
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RestCode save(CapRep capRep,String type,String mateStr,String orderStr ,String stockStr){		
		String id = capRep.getId();
		List<CapRepMate> mates=JsonUtils.jsonToList(mateStr, CapRepMate.class);
		List<CapRepOrder> orders=JsonUtils.jsonToList(orderStr, CapRepOrder.class);
		List<CapRepStock> stocks=JsonUtils.jsonToList(stockStr, CapRepStock.class);
		if(StringUtil.isNullOrEmpty(type) || "1".equals(type)){
			String capCode = codeService.getCodeByCodeType("capRepNo");
			capRep.setCapCode(capCode);
			capRepService.save(capRep, mates, orders, stocks);
		}else{
			capRepService.update(capRep, mates, orders, stocks);
		}
		return new RestCode().put("data", capRepService.getById(id));
	}
	/**
	 * 删除月报数据
	 * @param idStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del")
	public RestCode delete(String idStr){
		List<String> ids = JsonUtils.jsonToList(idStr, String.class);
		capRepService.delete(ids);
		return new RestCode();
	}
	/**
	 * 更新月报数据状态
	 * @param idStr
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update/status")
	public RestCode updateStatus(String idStr,String status){
		List<String> ids = JsonUtils.jsonToList(idStr, String.class);
		capRepService.updateStstus(ids, status);
		return new RestCode();
	}
	/**
	 * 校验必填项
	 * @param id
	 * @param repMonth
	 * @param suppNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/check/repeat")
	public boolean checkRepeat(String id,String repMonth,String suppNo){
		Map<String, Object> map=new HashMap<>();
		map.put("id", id);
		map.put("repMonth", repMonth);
		map.put("suppNo", suppNo);
		return capRepService.checkRepeat(map);
	}		
}
