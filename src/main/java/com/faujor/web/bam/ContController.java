package com.faujor.web.bam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.faujor.common.annotation.Log;
import com.faujor.entity.bam.Contract;
import com.faujor.entity.basic.Dic;
import com.faujor.entity.common.LayuiPage;
import com.faujor.entity.document.Document;
import com.faujor.entity.document.TempLog;
import com.faujor.entity.mdm.QualSupp;
import com.faujor.service.bam.ContService;
import com.faujor.service.basic.BasicService;
import com.faujor.service.mdm.QualSuppService;
import com.faujor.utils.DateUtils;
import com.faujor.utils.JsonUtils;
import com.faujor.utils.RestCode;

@Controller
@RequestMapping("/cont")
public class ContController {
	@Value("${file_upload_url}")
	private String url;
	@Value("${file_upload_username}")
	private String username;
	@Value("${file_upload_password}")
	private String password;
	@Autowired
	private ContService contService;
	@Autowired
	private BasicService basicService;
	@Autowired
	private QualSuppService qualSuppService;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	/**
	 * 获取合同管理页面
	 * @return
	 */
	@Log("获取合同列表信息")
	@RequestMapping("/getContListPage")
	public String getContListPage(Model model){
		List<Dic> contTypeList = basicService.findDicListByCategoryCode("CONTTYPE");
		model.addAttribute("contTypeList", contTypeList);
		return "bam/cont/contList";
	}
	/**
	 * 获取修改或编辑合同页面
	 * @param type
	 * 1新增 2编辑 3查看
	 * @return
	 */
	@Log("创建/编辑 /查看合同")
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAddContPage")
	public String getAddContPage(Model model,String type,String contId){
		Map<String, Object> params=new HashMap<String,Object>();
		Contract cont=null;
		if("1".equals(type)){
			 cont=new Contract();
		}else {
			params.put("id", contId);
			cont = contService.getCont(params);
		}
		List<Dic> contTypeList = basicService.findDicListByCategoryCode("CONTTYPE");List<Dic> firstPartList = basicService.findDicListByCategoryCode("FIRSTPART");
		model.addAttribute("firstPartList", firstPartList);
		List<QualSupp> suppList = (List<QualSupp>) qualSuppService.queryAllQualSupp().get("data");
		model.addAttribute("suppList", suppList);
		model.addAttribute("contTypeList", contTypeList);
		model.addAttribute("cont", cont);
		model.addAttribute("type", type);
		return "bam/cont/addCont";
	}
	/**
	 * 模板管理分页查询
	 * @param page
	 * @param tempName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getContByPage")
	public LayuiPage<Contract> getContTempByPage(LayuiPage<Contract> page,String suppName,String contName,String contType,String contStatus,Date startDate,Date endDate,Date vaildStart,Date vaildEnd){
		Map<String, Object> params=new HashMap<String,Object>();
		page.calculatePage();
		params.put("page", page);
		params.put("contType", contType);
		params.put("contStatus",contStatus);
		params.put("startDate", startDate);
		if(endDate!=null){
			endDate=DateUtils.addDate(endDate, 1);
		}
		params.put("endDate", endDate);
		params.put("vaildStart", vaildStart);
		params.put("vaildEnd", vaildEnd);
		if(!StringUtils.isEmpty(contName)){
			params.put("contName", "%"+contName+"%");
		}
		if(!StringUtils.isEmpty(suppName)){
			params.put("suppName", "%"+suppName+"%");
		}
		LayuiPage<Contract> contTempPage = contService.getContByPage(params);
		return contTempPage;
	}
	/**
	 * 保存合同信息
	 * @param temp
	 * @param jsonStr
	 * @return
	 */
	@Log("保存合同信息")
	@ResponseBody
	@RequestMapping("/saveCont")
	public RestCode saveCont(Contract cont,String jsonFile,String type){
		List<Document> docs = JsonUtils.jsonToList(jsonFile, Document.class);
		if("1".equals(type)){
			contService.saveCont(cont, docs);
		}else if("2".equals(type)){
			contService.updateCont(cont, docs);
		}
		return new RestCode().put("cont", cont);
	}
	/**
	 * 获取模板的日志信息
	 * @param tempNo
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getTempLogByContId")
	public List<TempLog> getTempLogByContId(String contId) {
		List<TempLog> logs = contService.getTempLogByContNo(contId);
		return logs;
	}
	/**
	 * 删除文件
	 * @param docId
	 * @return
	 */
	@Log("删除合同附件信息")
	@RequestMapping("/deleteFile")
	@ResponseBody
	public RestCode deleteFile(String jsonStr){
		List<String> docIds = JsonUtils.jsonToList(jsonStr, String.class);
		RestCode restCode = contService.deleteFile(docIds);
		return restCode;
	}
	/**
	 * 删除合同模板
	 * @param tempNos
	 * @return
	 */
	@Log("删除合同")
	@ResponseBody
	@RequestMapping("/deleteCont")
	public RestCode deleteCont(String contIds){
		List<String> list = JsonUtils.jsonToList(contIds, String.class);
		contService.deleteCont(list);
		return new RestCode();
	}
	/**
	 * 跟新合同模板的状态
	 * 已发布 与未发布的切换
	 * @param tempNos
	 * @param status
	 * @return
	 */
	@Log("变更合同状态")
	@ResponseBody
	@RequestMapping("/changeContStatus")
	public RestCode changeContStatus(String contIds,String status){
		List<String> list = JsonUtils.jsonToList(contIds, String.class);
		contService.changeContStatus(list, status);
		return new RestCode();
	}
	/**
	 * 判断某个合同编码是否存在
	 * @param id
	 * @param contCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkIsExist")
	public boolean checkIsExist(String id,String contCode){
		boolean flag = contService.checkIsExist(id, contCode);
		return flag;
	}
}
