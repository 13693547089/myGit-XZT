package com.faujor.web.bam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.faujor.entity.bam.Quote;
import com.faujor.entity.bam.QuoteAttr;
import com.faujor.entity.bam.QuoteMate;
import com.faujor.entity.bam.QuotePrice;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.common.SysUserDO;
import com.faujor.entity.mdm.MateDO;
import com.faujor.entity.mdm.Material;
import com.faujor.entity.mdm.QualProc;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.entity.task.TaskDO;
import com.faujor.entity.task.TaskParamsDO;
import com.faujor.service.bam.QuoteService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.common.CodeService;
import com.faujor.service.mdm.MaterialService;
import com.faujor.service.mdm.QualPapersService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.service.task.TaskService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.PrivilegesCommon;
import com.faujor.utils.RestCode;
import com.faujor.utils.StringUtil;
import com.faujor.utils.UserCommon;

@Controller
@RequestMapping("/quote")
public class QuoteController {
	@Autowired
	private QuoteService quoteService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private QualSuppService qualSuppService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private QualPapersService qualPapersService;
	@Autowired
	private PrivilegesCommon privilegesCommon;
	@Autowired
	private TaskService taskService;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	
	/**
	 * 获取报价单列表页
	 * @param model
	 * @return
	 */
	@Log("获取报价单列信息")
	@RequestMapping("/getQuoteListPage")
	public String getQuoteListPage(Model model){
		SysUserDO user = UserCommon.getUser();
		List<Dic> quoteStatusList = basicService.findDicListByCategoryCode("QUOTESTATUS");
		model.addAttribute("quoteStatusList", quoteStatusList);
		model.addAttribute("userType", user.getUserType());
		return "bam/quote/quoteList";
	}
	
	/**
	 * 获取维护报价单的有效期
	 * @param model
	 * @return
	 */
	@RequestMapping("/getValidDateDg")
	public String getValidDateDg(Model model,String quoteCode ){
		Quote quote = quoteService.getQuoteByQuoteCode(quoteCode);
		model.addAttribute("quote", quote);
		return "bam/quote/validDateDg";
	}
	
	
	/**
	 * 根据物料Id获取物料信息
	 * @param mateId
	 * @return
	 */
	@RequestMapping("/getMateInfoPage")
	public String getMateInfoPage(String mateId,Model model){
		Material mate = materialService.queryOneMaterialByMateId(mateId);
		if(mate==null){
			mate=new Material();
		}
		model.addAttribute("mate", mate);
		return "bam/quote/basicInfo";
	}
	
	/**
	 * 获取报价单创建页面
	 * type 1创建 2查看  3编辑,4.采购员维护有效期
	 * @param model
	 * @return
	 */
	@Log("创建/编辑/查看/维护报检单有效期")
	@RequestMapping("/getAddQuotePage")
	public String getAddQuotePage(Model model,String type,String quoteCode){
		model.addAttribute("type", type);
		Quote quote=new Quote();
		TaskDO task = new TaskDO();
		if(type!=null && !"1".equals(type)){
			quote = quoteService.getQuoteByQuoteCode(quoteCode);
			task = taskService.getTask(quote.getId());
		}
		List<String> suppNos = privilegesCommon.getAllSupplierCode();
		List<QualSupp> suppList = qualSuppService.queryQualSuppBySapCodes(suppNos);
		model.addAttribute("suppList", suppList);
		List<Dic> quoteTypeList = basicService.findDicListByCategoryCode("QUOTETYPE");
		model.addAttribute("quoteTypeList", quoteTypeList);
		model.addAttribute("quote", quote);
		SysUserDO user = UserCommon.getUser();
		String userType = user.getUserType();
		model.addAttribute("userType", userType);
		model.addAttribute("task", task);
		return "bam/quote/addQuote";
	}
	/**
	 * 分页获取报价信息
	 * @param suppName
	 * @param quoteCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getQuoteByPage")
	public LayuiPage<Quote> getQuoteByPage(LayuiPage<Quote> page, String suppName,String quoteCode,String createTime,String statusJson){
		page.calculatePage();
		
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("page", page);
		
		List<String> status = new ArrayList<>();
		if(!StringUtils.isEmpty(statusJson)){
			status = JsonUtils.jsonToList(statusJson, String.class);
		}
		if(status!=null && status.size()>0){
			map.put("status", status);
		}
		if(!StringUtils.isEmpty(suppName)){
			map.put("suppName","%"+ suppName+"%");
		}
		if(!StringUtils.isEmpty(quoteCode)){
			map.put("quoteCode", "%"+quoteCode+"%");
		}
		if(!StringUtils.isEmpty(createTime)){
			//Date date = DateUtils.parse(createTime, "yyyy-MM-dd");
			map.put("createTime", createTime);
		}
		List<String> suppSapIds = privilegesCommon.getAllSupplierCode();
		map.put("suppSapIds", suppSapIds);
		map.put("createUser", UserCommon.getUser().getUsername());
		LayuiPage<Quote> mypage = quoteService.getQuoteByPage(map);
		return mypage;
	}
	/**
	 * 获取供应商的所有物料信息
	 * @param suppNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getMateBySuppNo")
	public List<MateDO> getMateBySuppId(String suppId){
		 List<MateDO> mates = materialService.queryAllMaterialOfSuppBySapId(suppId);
		return mates;
	}
	/**
	 * 根据供应商Id获取供应商的范围内信息
	 * @param suppId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryQualProcBySuppId")
	public List<QualProc> queryQualProcBySuppId(String suppId){
		List<QualProc> list = qualPapersService.queryQualProcBySapId(suppId);
		//把空的子范围过滤掉
		List<QualProc> list2= new ArrayList<QualProc>();
		for (QualProc qualProc : list) {
			String suppRange = qualProc.getSuppRange();
			String suppRangeDesc = qualProc.getSuppRangeDesc();
			
			if(!StringUtils.isEmpty(suppRange)){
				if(StringUtil.isNotNullOrEmpty(suppRangeDesc)){
					qualProc.setSuppRangeDesc(suppRange+"-"+suppRangeDesc);
				}
				list2.add(qualProc);
			}
		}
		return list2;
	}
	/**
	 * 根据报价单编号获取所有的物料和报价信息
	 * @param quoteCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getQuoteMateByQuoteCode")
	public List<QuoteMate> getQuoteMateByQuoteCode(String quoteCode){
		List<QuoteMate> mateList = quoteService.getQuoteMatesByQuoteCode(quoteCode);
		return mateList;
	}
	/**
	 * 根据物料Id去报价结构获取物料的报价信息
	 * @param mateId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getQuotePriceByMateId")
	public List<QuotePrice> getQuotePriceByMateId(String mateId){
		Map<String, Object> map=new HashMap<String ,Object>();
		map.put("mateId", mateId);
		List<QuotePrice> prices = quoteService.getQuotePriceByMateId(map);
		return prices;
	}
	/**
	 * 保存报价单信息
	 * @param quote
	 * @param mateJson
	 * @param type
	 * @return
	 */
	@Log("保存报价单")
	@ResponseBody
	@RequestMapping("/saveQuote")
	public RestCode saveQuote(Quote quote,String mateJson,String type,String attrJson){
		List<QuoteMate> quoteMates = JsonUtils.jsonToList(mateJson, QuoteMate.class);
		List<QuoteAttr> attrList = JsonUtils.jsonToList(attrJson, QuoteAttr.class);
		if(!StringUtils.isEmpty(type) && "1".equals(type)){
			String quoteCode = codeService.getCodeByCodeType("quoteNo");
			quote.setQuoteCode(quoteCode);
			quoteService.saveQuote(quote, quoteMates,attrList);
		}else{
			quoteService.updateQuote(quote, quoteMates,attrList);
		}
		return new RestCode().put("quote", quote);
	}
	/**
	 * 删除报价信息
	 * @param codeJson
	 * @return
	 */
	@Log("删除报价单")
	@ResponseBody
	@RequestMapping("/delQuotes")
	public RestCode delQuotes(String codeJson){
		List<String> quoteCodes = JsonUtils.jsonToList(codeJson, String.class);
		quoteService.delQuote(quoteCodes);
		return new RestCode();
	}
	/**
	 * 更新报价单状态
	 * @param status
	 * @param codeJson
	 * @return
	 */
	@Log("变更报价单状态")
	@ResponseBody
	@RequestMapping("/changeQuoteStatus")
	public RestCode changeQuoteStatus(String status,String codeJson){
		List<String> quoteCodes = JsonUtils.jsonToList(codeJson, String.class);
		quoteService.updateQuoteStatus(status, quoteCodes);
		return new RestCode();
	}
	/**
	 * 将报价单提交到OA
	 * @param jsonIds
	 * @return
	 */
	@Log("报价单提交到OA")
	@ResponseBody
	@RequestMapping("/submitQuoteToOA")
	public RestCode submitQuoteToOA(String jsonCodes){
		List<String> quoteCodes = JsonUtils.jsonToList(jsonCodes, String.class);
		RestCode rest = quoteService.submitQuoteToOA(quoteCodes);
		return  rest;
	}
	/**
	 * 更改报价单的有效期
	 * @param quoteCode
	 * @param validStart
	 * @param validEnd
	 * @return
	 */
	@Log("维护报价单的有效期")
	@ResponseBody
	@RequestMapping("/updateValidDate")
	public RestCode updateValidDate(String mateJson,String attrJson,String quoteCode){
		List<QuoteMate> mates = JsonUtils.jsonToList(mateJson, QuoteMate.class);
		List<QuoteAttr> attrs = JsonUtils.jsonToList(attrJson, QuoteAttr.class);
		quoteService.updateValidDate(mates);
		quoteService.updateQuoteFile(attrs, quoteCode);
		return new RestCode();
	}
	/**
	 * 初审之前校验有效期字段，检验采购员是否上传附件信息
	 * @param quoteJson
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validDate")
	public RestCode validDate(String quoteJson){
		List<String> quoteCodes = JsonUtils.jsonToList(quoteJson, String.class);
		RestCode restCode = quoteService.validDate(quoteCodes);
		return restCode;
	}
	
	/**
	 * 任务流程：跳转到报价单审核页面
	 * @param model
	 * @param sdata1
	 * @param taskName
	 * @param processCode
	 * @return
	 */
	@RequestMapping("/getQuoteAuditHtml")
	public String getQuoteAuditHtml(Model model,TaskParamsDO taskPDO){
		Quote quote = quoteService.getQuoteById(taskPDO.getSdata1());
		List<String> suppNos = privilegesCommon.getAllSupplierCode();
		List<QualSupp> suppList = qualSuppService.queryQualSuppBySapCodes(suppNos);
		model.addAttribute("suppList", suppList);
		List<Dic> quoteTypeList = basicService.findDicListByCategoryCode("QUOTETYPE");
		model.addAttribute("quoteTypeList", quoteTypeList);
		SysUserDO user = UserCommon.getUser();
		String userType = user.getUserType();
		model.addAttribute("userType", userType);
		model.addAttribute("quote", quote);
		model.addAttribute("taskPDO",taskPDO );
		model.addAttribute("type", "2");
		return "bam/quote/quoteAudit";
	}
	/**
	 * 获取一条数据的任务信息
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProcessCodeOfTask")
	public TaskDO getProcessCodeOfTask(String id){
		TaskDO task = taskService.getTask(id);
		return task;
	}
	/**
	 * 根据报价单编码获取附件列表
	 * @param quoteCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getQuoteAttrByQuoteCode")
	public List<QuoteAttr> getQuoteAttrByQuoteCode(String quoteCode){
		List<QuoteAttr> list = quoteService.getQuoteAttrByQuoteCode(quoteCode);
		return list;
	}
	/**
	 * 删除报价单附件
	 * @param docIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delFile")
	public RestCode delFile(String  docIds){
		List<String> list = JsonUtils.jsonToList(docIds, String.class);
		RestCode restCode = quoteService.deleteFile(list);
		return restCode;
	}
}
