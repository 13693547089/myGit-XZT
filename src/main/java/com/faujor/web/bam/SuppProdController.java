package com.faujor.web.bam;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.psm.SuppProd;
import com.faujor.entity.bam.psm.SuppProdPlan;
import com.faujor.entity.common.LayuiPage;
import com.faujor.service.bam.SuppProdService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.PrivilegesCommon;
import com.faujor.utils.RestCode;
@Controller
@RequestMapping("/suppProd")
public class SuppProdController {
	@Autowired
	private SuppProdService suppProdService;
	@Autowired
	private PrivilegesCommon privilegesCommon;
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-yy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	/**
	 * 获取排产计划管理页
	 * @return
	 */
	@Log("获取排产计划列表信息")
	@RequestMapping("/getSuppProdIndex")
	public String getSuppProdIndex(){
		return "bam/suppProd/suppProdIndex";
	}
	/**
	 * 供应商查看排产计划列表
	 * @return
	 */
	@Log("供应商获取排产计划列表信息")
	@RequestMapping("/getSuppIndex")
	public String getSuppIndex(){
		return "bam/suppProd/suppIndex";
	}
	
	/**
	 * 获取排查的分配页面
	 * @param id
	 * @return
	 */
	@Log("供应商排产")
	@RequestMapping("/getSuppProdPlanDg")
	public String getSuppProdPlanDg(String id,String  planMonth, Model model,String status){
		SuppProd suppProd = suppProdService.getSuppProdById(id);
		model.addAttribute("suppProd", suppProd);
		model.addAttribute("planMonth", planMonth);
		model.addAttribute("status", status);
		return "bam/suppProd/suppProdPlanDg";
	}
	/**
	 * 内部人员查看排产计划
	 * @param id
	 * @param planMonth
	 * @param model
	 * @param status
	 * @return
	 */
	@Log("产看排产计划详情")
	@RequestMapping("/getViewProdPlanDg")
	public String getViewProdPlanDg(String id,String  planMonth, Model model,String status){
		SuppProd suppProd = suppProdService.getSuppProdById(id);
		model.addAttribute("suppProd", suppProd);
		model.addAttribute("planMonth", planMonth);
		model.addAttribute("status", status);
		return "bam/suppProd/viewProdPlanDg";
	}
	/**
	 * 分页获取备货计划
	 * @param planDesc
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppProdByPage")
	public LayuiPage<SuppProd> getSuppProdByPage(
			String planDesc,String status,Date startDate,
			Date endDate ,LayuiPage<SuppProd> page,String planMonth,
			String series,String mateDesc,String suppName,String invenPlanId){
		Map<String , Object> map=new HashMap<String,Object>();
		page.calculatePage();
		map.put("page", page);
		if(!StringUtils.isEmpty(planDesc)){
			map.put("planDesc", "%"+planDesc+"%");
		}
		map.put("status", status);
		if (startDate!=null) {
			map.put("startDate", startDate);
		}
		if (endDate!=null) {
			endDate = DateUtils.addDate(endDate, 1);
			map.put("endDate", endDate);
		}
		if(!StringUtils.isEmpty(planMonth)){
			map.put("planMonth", DateUtils.parse(planMonth, "yyyy-MM"));
		}
		if(!StringUtils.isEmpty(series)){
			map.put("series", "%"+series+"%");
		}
		if(!StringUtils.isEmpty(mateDesc)){
			map.put("mateDesc", "%"+mateDesc+"%");
		}
		if(!StringUtils.isEmpty(suppName)){
			map.put("suppName", "%"+suppName+"%");
		}
		map.put("invenPlanId", invenPlanId);
		List<String> suppCodes = privilegesCommon.getAllSupplierCode();
		map.put("suppCodes", suppCodes);
		LayuiPage<SuppProd> returnPage = suppProdService.getSuppProdByPage(map);
		return returnPage;
	}
	/**
	 * 保存多个备货计划
	 * @param suppProdJson
	 * @return
	 */
	@Log("分解备货计划")
	@ResponseBody
	@RequestMapping("/saveSuppProds")
	public RestCode  saveSuppProds(String suppProdJson,String planDetailId,String safeScale,BigDecimal prodPlan ){
		List<SuppProd> suppProds=JsonUtils.jsonToList(suppProdJson, SuppProd.class);
		suppProdService.saveSuppProds(suppProds,planDetailId,safeScale,prodPlan);
		return new RestCode();
	}
	/**
	 * 根据备货计划获取排产计划
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppProdByInvenId")
	public List<SuppProd> getSuppProdByInvenId(String id){
		List<SuppProd> list = suppProdService.getSuppProdByInvenId(id);
		return list;
	}
	/**
	 * 根基根据备货计划详情ID获取分解的排产计划
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppProdByMainId")
	public List<SuppProd> getSuppProdByMainId(String mainId){
		List<SuppProd> suppProdList = suppProdService.getSuppProdByMainId(mainId);
		return suppProdList;
	}
	/**
	 * 根基根据备货计划详情ID获取分解的排产计划
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppProdByPlanDetailId")
	public List<SuppProd> getSuppProdByPlanDetailId(String planDetailId){
		List<SuppProd> suppProdList = suppProdService.getSuppProdByPlanDetailId(planDetailId);
		return suppProdList;
	}
	
	/**
	 * 根据排产计划获取供应商每天的排产
	 * @param mainId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSuppProdPlanByMainId")
	public List<SuppProdPlan> getSuppProdPlanByMainId(String mainId){
		List<SuppProdPlan> plans = suppProdService.getSuppProdPlanByMainId(mainId);
		return plans;
	}
	/**
	 * 保存排产计划的排产详情
	 * @param planJson
	 * @return
	 */
	@Log("保存排产计划的排产详情")
	@ResponseBody
	@RequestMapping("/saveSuppProdPlan")
	public RestCode saveSuppProdPlan(String planJson,String mainId,String status,BigDecimal remainNum){
		List<SuppProdPlan> suppPlans = JsonUtils.jsonToList(planJson, SuppProdPlan.class);
		suppProdService.saveSuppProdPlan(suppPlans,mainId,status,remainNum);
		return new RestCode();
	}
	/**
	 * 更改排产计划的状态
	 * @param json
	 * @param status
	 * @return
	 */
	@Log("变更排产计划的状态")
	@ResponseBody
	@RequestMapping("/changeSuppProdStatus")
	public RestCode changeSuppProdStatus(String json,String status){
		List<String> ids = JsonUtils.jsonToList(json, String.class);
		suppProdService.changeSuppProdStatus(ids, status);
		return new RestCode();
	}
	/**
	 * 平均分配多个排产计划
	 * @param jsonIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/avgAllSuppProdPlan")
	public RestCode avgAllSuppProdPlan(String jsonIds){
		List<String> ids = JsonUtils.jsonToList(jsonIds, String.class);
		suppProdService.avgAllSuppProdPlan(ids);
		return new RestCode();
	}
}
